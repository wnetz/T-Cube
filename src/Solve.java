import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Solve
{
    public final int PRINT_TO;
    public final int ROTATIONS;
    private static int HEIGHT = Main.HEIGHT+1;
    private static final int WIDTH = Main.WIDTH+1;
    private static final int DEPTH = Main.DEPTH+1;
    ArrayList<int[][][]> deadEnd;
    ArrayList<ArrayList<Piece>> dead_cubes;
    ArrayList<int[][][]> knownSolutions;
    FileWriter solutionFile;
    FileWriter file;
    long time;
    int dead = 0;
    int kill = 0;
    static ArrayList<int[][][]> solutions;
    
    public Solve(int p, FileWriter f) throws IOException
    {
        ROTATIONS = Rotations.read_qube_rotations(Main.QUBE_ROTATION_FILE).size();
        solutionFile = new FileWriter("solutions.txt");
        PRINT_TO = p;
        file = f;
        deadEnd = new ArrayList<>();
        dead_cubes = new ArrayList<>();
        solutions = new ArrayList<>();
        //initialize to empty
        int[][][] cube = new int [WIDTH][HEIGHT][DEPTH];
        for(int x = 0; x < WIDTH; x++)
            for(int y = 0; y < HEIGHT; y++)
                for(int z = 0; z < DEPTH; z++)
                    cube[x][y][z] = -1;
        //knownSolutions = readSolutions();
        System.out.println("start");
        time = System.nanoTime();
        recursiveSolve(new SolutionState(cube),0);
        System.out.println((System.nanoTime()-time)/1000000000.0 );

        System.out.println("checking copies");
        int removed = 0;
        for(int i = 0; i < solutions.size(); i++) //need to do xy,xz,yz reflections
        {
            ArrayList<int[][][]> perms = getPerms(solutions.get(i));
            for(int c = 0; c < perms.size();c++)
            {
                for (int j = i + 1; j < solutions.size(); j++)
                {
                    if (cubeCopy(perms.get(c), solutions.get(j)))
                    {
                        solutions.remove(i);
                        System.out.println(c);
                        removed++;
                        i--;
                        c = perms.size();
                        break;
                    }
                }
            }
        }
        System.out.println("copies removed: " + removed);
        for (int[][][] solution : solutions)
            printCube(solution, solutionFile);
        System.out.println("killed: " + kill);
        System.out.println("dead: " + deadEnd.size() + " " + dead);
        solutionFile.close();
    }

    /***
     *
     * @return all known solutions
     */
    public ArrayList<int[][][]> readSolutions()
    {
        ArrayList<int[][][]> solutions = new ArrayList<>();
        List<String> lines;
        try
        {
            lines = Files.readAllLines(Paths.get("h2.txt"), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        int row = 0;
        int height = 0;
        int[][][] current = null;
        for(String line: lines)
        {
            if(line.contains("#"))
            {
                solutions.add(current);
                row = 0;
                height = 0;
            }
            else
            {
                if (row == 0)
                {
                    current = new int[WIDTH][HEIGHT][DEPTH];
                }
                else if(row == 6)
                {
                    row = 0;
                    height++;
                }
                ArrayList<Integer> xs = (ArrayList<Integer>) Arrays.stream(line.split(",")).map(x -> {
                    if(x.contains(" "))
                    {
                        x = x.substring(1);
                    }
                    if(x.equals(""))
                    {
                        return -1;
                    }
                    return Integer.valueOf(x);
                }).collect(Collectors.toList());
                for(int x = 0; x < xs.size()-1;x++)
                {
                    current[x][height][row] = xs.get(x);
                }
                row++;
            }
        }
        return solutions;
    }

    /***
     *
     * @param cube to be printed to file
     */
    public  void printCube(int[][][] cube, FileWriter file) throws IOException
    {
        for (int y = 0; y < HEIGHT; y++)
        {
            StringBuilder layer = new StringBuilder();
            boolean allEmpty = true;
            for (int z = 0; z < DEPTH; z++)
            {
                for (int x = 0; x < WIDTH; x++)
                    if (cube[x][y][z] != -1)
                    {
                        allEmpty = false;
                        if (cube[x][y][z] < 10)
                            layer.append("0").append(cube[x][y][z]).append(", ");
                        else
                            layer.append(cube[x][y][z]).append(", ");
                    }
                    else
                        layer.append(cube[x][y][z]).append(", ");

                layer.append("\n");
            }
            if (!allEmpty && PRINT_TO != 0)
                file.write(""+ layer);
            else if(!allEmpty &&PRINT_TO == 0)
                System.out.print(layer + "\n");
        }
        if (PRINT_TO != 0)
            file.write("########################################################\n");
        else
            System.out.print("########################################################\n");
    }

    /***
     *
     * @return checks if two cubes are an exact copy
     */
    public  boolean cubeCopy(int[][][] c1, int[][][] c2)
    {
        boolean same = true;
        for (int x = 0; x < WIDTH && same; x++)
            for (int y = 0; y < HEIGHT && same; y++)
                for (int z = 0; z < DEPTH; z++)
                    if (c1[x][y][z] != c2[x][y][z])
                    {
                        same = false;
                        break;
                    }
        return same;
    }

    /***
     *
     * @return all reflections and rotations of cube
     */
    public  ArrayList<int[][][]> getPerms(int[][][] cube)
    {
        ArrayList<int[][][]> perms = new ArrayList<>();
        for(int i = 0; i < 16; i++)
        {
            perms.add(new int[cube.length][cube[0].length][cube[0][0].length]);
        }
        for (int x = 0; x < cube.length; x++)
            for (int y = 0; y < cube[x].length; y++)
                for (int z = 0; z < cube[x][y].length; z++)
                {
                    perms.get(0)[x][y][z] =         cube[x]                       [y]                       [z];                             //00 : 00
                    perms.get(1)[x][y][z] = switch (cube[x]                       [y]                       [-(z-cube[x][y].length+1)]){
                        case 0 -> 0;
                        case 1 -> 3;
                        case 2 -> 2;
                        case 3 -> 1;
                        case 4 -> 4;
                        case 5 -> 7;
                        case 6 -> 6;
                        case 7 -> 5;
                        case 8 -> 8;
                        case 9 -> 9;
                        case 10-> 10;
                        case 11-> 11;
                        default -> -1;
                    };//xy : 00
                    perms.get(2)[x][y][z] = switch (cube[x]                       [-(y-cube[x].length+1)]   [z]){
                        case 0 -> 2;
                        case 1 -> 1;
                        case 2 -> 0;
                        case 3 -> 3;
                        case 4 -> 4;
                        case 5 -> 5;
                        case 6 -> 6;
                        case 7 -> 7;
                        case 8 -> 8;
                        case 9 -> 11;
                        case 10-> 10;
                        case 11-> 9;
                        default -> -1;
                    };                       //xy : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                    perms.get(3)[x][y][z] = switch (cube[x]                       [-(y-cube[x].length+1)]   [-(z-cube[x][y].length+1)]){
                        case 0 -> 2;
                        case 1 -> 3;
                        case 2 -> 0;
                        case 3 -> 1;
                        case 4 -> 4;
                        case 5 -> 7;
                        case 6 -> 6;
                        case 7 -> 5;
                        case 8 -> 8;
                        case 9 -> 11;
                        case 10-> 10;
                        case 11-> 9;
                        default -> -1;
                    };//00 : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                    /*perms.get(0)[x][y][z] = switch (cube[x]                       [z]                       [y]){
                        case 0 -> 1;
                        case 1 -> 0;
                        case 2 -> 3;
                        case 3 -> 2;
                        case 4 -> 8;
                        case 5 -> 9;
                        case 6 -> 10;
                        case 7 -> 11;
                        case 8 -> 4;
                        case 9 -> 5;
                        case 10-> 6;
                        case 11-> 7;
                        default -> -1;
                    };                       //xy : xxx
                    perms.get(0)[x][y][z] = switch (cube[x]                       [z]                       [-(y-cube[x].length+1)]){
                        case 0 -> 3;
                        case 1 -> 0;
                        case 2 -> 1;
                        case 3 -> 2;
                        case 4 -> 8;
                        case 5 -> 9;
                        case 6 -> 10;
                        case 7 -> 11;
                        case 8 -> 4;
                        case 9 -> 7;
                        case 10-> 6;
                        case 11-> 5;
                        default -> -1;
                    };   //00 : x,    yxz
                    perms.get(0)[x][y][z] = switch (cube[x]                       [-(z-cube[x][y].length+1)][y]){
                        case 0 -> 1;
                        case 1 -> 2;
                        case 2 -> 3;
                        case 3 -> 0;
                        case 4 -> 8;
                        case 5 -> 11;
                        case 6 -> 10;
                        case 7 -> 9;
                        case 8 -> 4;
                        case 9 -> 5;
                        case 10-> 6;
                        case 11-> 7;
                        default -> -1;
                    };                       //00 : xxx
                    perms.get(0)[x][y][z] = switch (cube[x]                       [-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]){
                        case 0 -> 3;
                        case 1 -> 2;
                        case 2 -> 1;
                        case 3 -> 0;
                        case 4 -> 8;
                        case 5 -> 11;
                        case 6 -> 10;
                        case 7 -> 9;
                        case 8 -> 4;
                        case 9 -> 7;
                        case 10-> 6;
                        case 11-> 5;
                        default -> -1;
                    };   //xy : x,    yxz*/
                    perms.get(4)[x][y][z] = switch (cube[-(x-cube.length+1)]      [y]                       [z]){
                        case 0 -> 0;
                        case 1 -> 1;
                        case 2 -> 2;
                        case 3 -> 3;
                        case 4 -> 6;
                        case 5 -> 5;
                        case 6 -> 4;
                        case 7 -> 7;
                        case 8 -> 10;
                        case 9 -> 9;
                        case 10-> 8;
                        case 11-> 11;
                        default -> -1;
                    };                       //xy : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                    perms.get(5)[x][y][z] = switch (cube[-(x-cube.length+1)]      [y]                       [-(z-cube[x][y].length+1)]){
                        case 0 -> 0;
                        case 1 -> 3;
                        case 2 -> 2;
                        case 3 -> 1;
                        case 4 -> 6;
                        case 5 -> 7;
                        case 6 -> 4;
                        case 7 -> 5;
                        case 8 -> 10;
                        case 9 -> 9;
                        case 10-> 8;
                        case 11-> 11;
                        default -> -1;
                    };//00 : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                    perms.get(6)[x][y][z] = switch (cube[-(x-cube.length+1)]      [-(y-cube[x].length+1)]   [z]){
                        case 0 -> 2;
                        case 1 -> 1;
                        case 2 -> 0;
                        case 3 -> 3;
                        case 4 -> 6;
                        case 5 -> 5;
                        case 6 -> 4;
                        case 7 -> 7;
                        case 8 -> 10;
                        case 9 -> 11;
                        case 10-> 8;
                        case 11-> 9;
                        default -> -1;
                    };                       //00 : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                    perms.get(7)[x][y][z] = switch (cube[-(x-cube.length+1)]      [-(y-cube[x].length+1)]   [-(z-cube[x][y].length+1)]){
                        case 0 -> 2;
                        case 1 -> 3;
                        case 2 -> 0;
                        case 3 -> 1;
                        case 4 -> 6;
                        case 5 -> 7;
                        case 6 -> 4;
                        case 7 -> 5;
                        case 8 -> 10;
                        case 9 -> 11;
                        case 10-> 8;
                        case 11-> 9;
                        default -> -1;
                    };//xy : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                    /*perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [z]                       [y]){
                        case 0 -> 1;
                        case 1 -> 0;
                        case 2 -> 3;
                        case 3 -> 2;
                        case 4 -> 10;
                        case 5 -> 9;
                        case 6 -> 8;
                        case 7 -> 11;
                        case 8 -> 6;
                        case 9 -> 5;
                        case 10-> 4;
                        case 11-> 7;
                        default -> -1;
                    };                       //00 : xyy,  yzy,  zxy,  zyz,  zzx
                    perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [z]                       [-(y-cube[x].length+1)]){
                        case 0 -> 3;
                        case 1 -> 0;
                        case 2 -> 1;
                        case 3 -> 2;
                        case 4 -> 10;
                        case 5 -> 9;
                        case 6 -> 8;
                        case 7 -> 11;
                        case 8 -> 6;
                        case 9 -> 7;
                        case 10-> 4;
                        case 11-> 5;
                        default -> -1;
                    };   //xy : xzz,  yyx
                    perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [-(z-cube[x][y].length+1)][y]){
                        case 0 -> 1;
                        case 1 -> 2;
                        case 2 -> 3;
                        case 3 -> 0;
                        case 4 -> 10;
                        case 5 -> 11;
                        case 6 -> 8;
                        case 7 -> 9;
                        case 8 -> 6;
                        case 9 -> 5;
                        case 10-> 4;
                        case 11-> 7;
                        default -> -1;
                    };                       //xy : xyy,  yzy,  zxy,  zyz,  zzx
                    perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]){
                        case 0 -> 3;
                        case 1 -> 2;
                        case 2 -> 1;
                        case 3 -> 0;
                        case 4 -> 10;
                        case 5 -> 11;
                        case 6 -> 8;
                        case 7 -> 9;
                        case 8 -> 6;
                        case 9 -> 7;
                        case 10-> 4;
                        case 11-> 5;
                        default -> -1;
                    };   //00 : xzz,  yyx
                    perms.get(0)[x][y][z] = switch (cube[y]                       [x]                       [z]){
                        case 0 -> 6;
                        case 1 -> 5;
                        case 2 -> 4;
                        case 3 -> 7;
                        case 4 -> 2;
                        case 5 -> 1;
                        case 6 -> 0;
                        case 7 -> 3;
                        case 8 -> 11;
                        case 9 -> 10;
                        case 10-> 9;
                        case 11-> 8;
                        default -> -1;
                    };                       //xy : xxz,  zyy
                    perms.get(0)[x][y][z] = switch (cube[y]                       [x]                       [-(z-cube[x][y].length+1)]){
                        case 0 -> 6;
                        case 1 -> 7;
                        case 2 -> 4;
                        case 3 -> 5;
                        case 4 -> 2;
                        case 5 -> 3;
                        case 6 -> 0;
                        case 7 -> 1;
                        case 8 -> 11;
                        case 9 -> 10;
                        case 10-> 9;
                        case 11-> 8;
                        default -> -1;
                    };//00 : xxz,  zyy
                    perms.get(0)[x][y][z] = switch (cube[y]                       [-(x-cube.length+1)]      [z]){
                        case 0 -> 6;
                        case 1 -> 5;
                        case 2 -> 4;
                        case 3 -> 7;
                        case 4 -> 0;
                        case 5 -> 1;
                        case 6 -> 2;
                        case 7 -> 3;
                        case 8 -> 9;
                        case 9 -> 10;
                        case 10-> 11;
                        case 11-> 8;
                        default -> -1;
                    };                       //00 : zzz
                    perms.get(0)[x][y][z] = switch (cube[y]                       [-(x-cube.length+1)]      [-(z-cube[x][y].length+1)]){
                        case 0 -> 6;
                        case 1 -> 7;
                        case 2 -> 4;
                        case 3 -> 5;
                        case 4 -> 0;
                        case 5 -> 3;
                        case 6 -> 2;
                        case 7 -> 1;
                        case 8 -> 9;
                        case 9 -> 10;
                        case 10-> 11;
                        case 11-> 8;
                        default -> -1;
                    };//xy : zzz
                    perms.get(0)[x][y][z] = switch (cube[y]                       [z]                       [x]){
                        case 0 -> 10;
                        case 1 -> 9;
                        case 2 -> 8;
                        case 3 -> 11;
                        case 4 -> 3;
                        case 5 -> 0;
                        case 6 -> 1;
                        case 7 -> 2;
                        case 8 -> 7;
                        case 9 -> 6;
                        case 10-> 5;
                        case 11-> 4;
                        default -> -1;
                    };                       //00 : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
                    perms.get(0)[x][y][z] = switch (cube[y]                       [z]                       [-(x-cube.length+1)]){
                        case 0 -> 10;
                        case 1 -> 9;
                        case 2 -> 8;
                        case 3 -> 11;
                        case 4 -> 1;
                        case 5 -> 0;
                        case 6 -> 3;
                        case 7 -> 2;
                        case 8 -> 5;
                        case 9 -> 6;
                        case 10-> 7;
                        case 11-> 4;
                        default -> -1;
                    };      //xy : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
                    perms.get(0)[x][y][z] = switch (cube[y]                       [-(z-cube[x][y].length+1)][x]){
                        case 0 -> 10;
                        case 1 -> 11;
                        case 2 -> 8;
                        case 3 -> 9;
                        case 4 -> 3;
                        case 5 -> 2;
                        case 6 -> 1;
                        case 7 -> 0;
                        case 8 -> 7;
                        case 9 -> 6;
                        case 10-> 5;
                        case 11-> 4;
                        default -> -1;
                    };                       //xy : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
                    perms.get(0)[x][y][z] = switch (cube[y]                       [-(z-cube[x][y].length+1)][-(x-cube.length+1)]){
                        case 0 -> 10;
                        case 1 -> 11;
                        case 2 -> 8;
                        case 3 -> 9;
                        case 4 -> 1;
                        case 5 -> 2;
                        case 6 -> 3;
                        case 7 -> 0;
                        case 8 -> 5;
                        case 9 -> 6;
                        case 10-> 7;
                        case 11-> 4;
                        default -> -1;
                    };      //00 : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
                    perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [x]                       [z]){
                        case 0 -> 4;
                        case 1 -> 5;
                        case 2 -> 6;
                        case 3 -> 7;
                        case 4 -> 2;
                        case 5 -> 1;
                        case 6 -> 0;
                        case 7 -> 3;
                        case 8 -> 11;
                        case 9 -> 8;
                        case 10-> 9;
                        case 11-> 10;
                        default -> -1;
                    };                       //00 : xzy,  z
                    perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [x]                       [-(z-cube[x][y].length+1)]){
                        case 0 -> 4;
                        case 1 -> 7;
                        case 2 -> 6;
                        case 3 -> 5;
                        case 4 -> 2;
                        case 5 -> 3;
                        case 6 -> 0;
                        case 7 -> 1;
                        case 8 -> 11;
                        case 9 -> 8;
                        case 10-> 9;
                        case 11-> 10;
                        default -> -1;
                    };//xy : xzy,  z
                    perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(x-cube.length+1)]      [z]){
                        case 0 -> 4;
                        case 1 -> 5;
                        case 2 -> 6;
                        case 3 -> 7;
                        case 4 -> 0;
                        case 5 -> 1;
                        case 6 -> 2;
                        case 7 -> 3;
                        case 8 -> 9;
                        case 9 -> 8;
                        case 10-> 11;
                        case 11-> 10;
                        default -> -1;
                    };                       //xy : xyx,  yxy,  yyz,  yzx,  zxx
                    perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(x-cube.length+1)]      [-(z-cube[x][y].length+1)]){
                        case 0 -> 4;
                        case 1 -> 7;
                        case 2 -> 6;
                        case 3 -> 5;
                        case 4 -> 0;
                        case 5 -> 3;
                        case 6 -> 2;
                        case 7 -> 1;
                        case 8 -> 9;
                        case 9 -> 8;
                        case 10-> 11;
                        case 11-> 10;
                        default -> -1;
                    };//00 : xyx,  yxy,  yyz,  yzx,  zxx
                    perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [z]                       [x]){
                        case 0 -> 8;
                        case 1 -> 9;
                        case 2 -> 10;
                        case 3 -> 11;
                        case 4 -> 3;
                        case 5 -> 0;
                        case 6 -> 1;
                        case 7 -> 2;
                        case 8 -> 7;
                        case 9 -> 4;
                        case 10-> 5;
                        case 11-> 6;
                        default -> -1;
                    };                       //xy : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
                    perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [z]                       [-(x-cube.length+1)]){
                        case 0 -> 8;
                        case 1 -> 9;
                        case 2 -> 10;
                        case 3 -> 11;
                        case 4 -> 1;
                        case 5 -> 0;
                        case 6 -> 3;
                        case 7 -> 2;
                        case 8 -> 5;
                        case 9 -> 4;
                        case 10-> 7;
                        case 11-> 6;
                        default -> -1;
                    };      //00 : xy,   xzyx, yxzy, yz,   zx,   zyxz
                    perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(z-cube[x][y].length+1)][x]){
                        case 0 -> 8;
                        case 1 -> 11;
                        case 2 -> 10;
                        case 3 -> 9;
                        case 4 -> 3;
                        case 5 -> 2;
                        case 6 -> 1;
                        case 7 -> 0;
                        case 8 -> 7;
                        case 9 -> 4;
                        case 10-> 5;
                        case 11-> 6;
                        default -> -1;
                    };                       //00 : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
                    perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(z-cube[x][y].length+1)][-(x-cube.length+1)]){
                        case 0 -> 8;
                        case 1 -> 11;
                        case 2 -> 10;
                        case 3 -> 9;
                        case 4 -> 1;
                        case 5 -> 2;
                        case 6 -> 3;
                        case 7 -> 0;
                        case 8 -> 5;
                        case 9 -> 4;
                        case 10-> 7;
                        case 11-> 6;
                        default -> -1;
                    };      //xy : xy,   xzyx, yxzy, yz,   zx,   zyxz
                    perms.get(0)[x][y][z] = switch (cube[z]                       [x]                       [y]){
                        case 0 -> 5;
                        case 1 -> 6;
                        case 2 -> 7;
                        case 3 -> 4;
                        case 4 -> 11;
                        case 5 -> 10;
                        case 6 -> 9;
                        case 7 -> 8;
                        case 8 -> 2;
                        case 9 -> 1;
                        case 10-> 0;
                        case 11-> 3;
                        default -> -1;
                    };                       //00 : xxxz, xzyy, yxxx, zy,   zzyx
                    perms.get(0)[x][y][z] = switch (cube[z]                       [x]                       [-(y-cube[x].length+1)]){
                        case 0 -> 7;
                        case 1 -> 6;
                        case 2 -> 5;
                        case 3 -> 4;
                        case 4 -> 11;
                        case 5 -> 10;
                        case 6 -> 9;
                        case 7 -> 8;
                        case 8 -> 2;
                        case 9 -> 3;
                        case 10-> 0;
                        case 11-> 1;
                        default -> -1;
                    };   //xy : xxzy, xz,   yxzz, yyyx, zyyy
                    perms.get(0)[x][y][z] = switch (cube[z]                       [-(x-cube.length+1)]      [y]){
                        case 0 -> 5;
                        case 1 -> 6;
                        case 2 -> 7;
                        case 3 -> 4;
                        case 4 -> 9;
                        case 5 -> 10;
                        case 6 -> 11;
                        case 7 -> 8;
                        case 8 -> 0;
                        case 9 -> 1;
                        case 10-> 2;
                        case 11-> 3;
                        default -> -1;
                    };                       //xy : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz
                    perms.get(0)[x][y][z] = switch (cube[z]                       [-(x-cube.length+1)]      [-(y-cube[x].length+1)]){
                        case 0 -> 7;
                        case 1 -> 6;
                        case 2 -> 5;
                        case 3 -> 4;
                        case 4 -> 9;
                        case 5 -> 10;
                        case 6 -> 11;
                        case 7 -> 8;
                        case 8 -> 0;
                        case 9 -> 3;
                        case 10-> 2;
                        case 11-> 1;
                        default -> -1;
                    };   //00 : xzzz, yx,   yyxz, zyxx, zzzy*/
                    perms.get(8)[x][y][z] = switch (cube[z]                       [y]                       [x]){
                        case 0 -> 9;
                        case 1 -> 10;
                        case 2 -> 11;
                        case 3 -> 8;
                        case 4 -> 7;
                        case 5 -> 6;
                        case 6 -> 5;
                        case 7 -> 4;
                        case 8 -> 3;
                        case 9 -> 0;
                        case 10-> 1;
                        case 11-> 2;
                        default -> -1;
                    };                       //xy : yyy
                    perms.get(9)[x][y][z] = switch (cube[z]                       [y]                       [-(x-cube.length+1)]){
                        case 0 -> 9;
                        case 1 -> 10;
                        case 2 -> 11;
                        case 3 -> 8;
                        case 4 -> 5;
                        case 5 -> 6;
                        case 6 -> 7;
                        case 7 -> 4;
                        case 8 -> 1;
                        case 9 -> 0;
                        case 10-> 3;
                        case 11-> 2;
                        default -> -1;
                    };      //00 : y,    zyx
                    perms.get(10)[x][y][z] =switch (cube[z]                       [-(y-cube[x].length+1)]   [x]){
                        case 0 -> 11;
                        case 1 -> 10;
                        case 2 -> 9;
                        case 3 -> 8;
                        case 4 -> 7;
                        case 5 -> 6;
                        case 6 -> 5;
                        case 7 -> 4;
                        case 8 -> 3;
                        case 9 -> 2;
                        case 10-> 1;
                        case 11-> 0;
                        default -> -1;
                    };                       //00 : yxx,  zzy
                    perms.get(11)[x][y][z] =switch (cube[z]                       [-(y-cube[x].length+1)]   [-(x-cube.length+1)]){
                        case 0 -> 11;
                        case 1 -> 10;
                        case 2 -> 9;
                        case 3 -> 8;
                        case 4 -> 5;
                        case 5 -> 6;
                        case 6 -> 7;
                        case 7 -> 4;
                        case 8 -> 1;
                        case 9 -> 2;
                        case 10-> 3;
                        case 11-> 0;
                        default -> -1;
                    };      //xy : xxy,  xyz,  xzx,  yzz,  zxz
                    /*perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][x]                       [y]){
                        case 0 -> 5;
                        case 1 -> 4;
                        case 2 -> 7;
                        case 3 -> 6;
                        case 4 -> 11;
                        case 5 -> 8;
                        case 6 -> 9;
                        case 7 -> 10;
                        case 8 -> 2;
                        case 9 -> 1;
                        case 10-> 0;
                        case 11-> 3;
                        default -> -1;
                    };                       //xy : xxxz, xzyy, yxxx, zy,   zzyx
                    perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][x]                       [-(y-cube[x].length+1)]){
                        case 0 -> 7;
                        case 1 -> 4;
                        case 2 -> 5;
                        case 3 -> 6;
                        case 4 -> 11;
                        case 5 -> 8;
                        case 6 -> 9;
                        case 7 -> 10;
                        case 8 -> 2;
                        case 9 -> 3;
                        case 10-> 0;
                        case 11-> 1;
                        default -> -1;
                    };   //00 : xxzy, xz,   yxzz, yyyx, zyyy
                    perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(x-cube.length+1)]      [-(y-cube[x].length+1)]){
                        case 0 -> 7;
                        case 1 -> 4;
                        case 2 -> 5;
                        case 3 -> 6;
                        case 4 -> 9;
                        case 5 -> 8;
                        case 6 -> 11;
                        case 7 -> 10;
                        case 8 -> 0;
                        case 9 -> 3;
                        case 10-> 2;
                        case 11-> 1;
                        default -> -1;
                    };   //xy : xzzz, yx,   yyxz, zyxx, zzzy
                    perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(x-cube.length+1)]      [y]){
                        case 0 -> 5;
                        case 1 -> 4;
                        case 2 -> 7;
                        case 3 -> 6;
                        case 4 -> 9;
                        case 5 -> 8;
                        case 6 -> 11;
                        case 7 -> 10;
                        case 8 -> 0;
                        case 9 -> 1;
                        case 10-> 2;
                        case 11-> 3;
                        default -> -1;
                    };                       //00 : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz*/
                    perms.get(12)[x][y][z] =switch (cube[-(z-cube[x][y].length+1)][y]                       [x]){
                        case 0 -> 9;
                        case 1 -> 8;
                        case 2 -> 11;
                        case 3 -> 10;
                        case 4 -> 7;
                        case 5 -> 4;
                        case 6 -> 5;
                        case 7 -> 6;
                        case 8 -> 3;
                        case 9 -> 0;
                        case 10-> 1;
                        case 11-> 2;
                        default -> -1;
                    };                       //00 : yyy
                    perms.get(13)[x][y][z] =switch (cube[-(z-cube[x][y].length+1)][y]                       [-(x-cube.length+1)]){
                        case 0 -> 9;
                        case 1 -> 8;
                        case 2 -> 11;
                        case 3 -> 10;
                        case 4 -> 5;
                        case 5 -> 4;
                        case 6 -> 7;
                        case 7 -> 6;
                        case 8 -> 1;
                        case 9 -> 0;
                        case 10-> 3;
                        case 11-> 2;
                        default -> -1;
                    };      //xy : y,    zyx
                    perms.get(14)[x][y][z] =switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]   [x]){
                        case 0 -> 11;
                        case 1 -> 8;
                        case 2 -> 9;
                        case 3 -> 10;
                        case 4 -> 7;
                        case 5 -> 4;
                        case 6 -> 5;
                        case 7 -> 6;
                        case 8 -> 3;
                        case 9 -> 2;
                        case 10-> 1;
                        case 11-> 0;
                        default -> -1;
                    };                       //xy : yxx,  zzy
                    perms.get(15)[x][y][z] =switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]   [-(x-cube.length+1)]){
                        case 0 -> 11;
                        case 1 -> 8;
                        case 2 -> 9;
                        case 3 -> 10;
                        case 4 -> 5;
                        case 5 -> 4;
                        case 6 -> 7;
                        case 7 -> 6;
                        case 8 -> 1;
                        case 9 -> 2;
                        case 10-> 3;
                        case 11-> 0;
                        default -> -1;
                    };      //00 : xxy,  xyz,  xzx,  yzz,  zxz

                }
        return perms;
    }


    public void recursiveSolve(SolutionState solution, int depth)
    {
        //used to avoid computation on backtrack
        ArrayList<Point> oldCubeFrontier = new ArrayList<>(solution.getCubeFrontier());
        ArrayList<Point> oldCubeExplored = new ArrayList<>(solution.getCubeExplored());

        //try every point in the frontier
        for(Point point : oldCubeFrontier)
        {
            int orientation = -1;
            //try every orientation on point
            while (orientation != -2)
            {
                orientation = solution.add_piece(point, orientation);

                //if piece added and solution already explored
                if(orientation != -2 && is_explored(solution.getTCube()))
                {
                    solution.remove_piece(point, orientation);
                    //TODO check if last's in solutionState can be used instead
                    solution.setCubeFrontier(oldCubeFrontier);
                    solution.setCubeExplored(oldCubeExplored);
                }
                //if a piece was added
                else if (orientation != -2 )
                {
                    //if there is still space
                    if (solution.getCubeFrontier().size() > 0)
                    {
                        //start next set
                        recursiveSolve(new SolutionState(solution.getCubeFrontier(), solution.getCubeExplored(), solution.getCube(), solution.getTCube()), depth + 1);
                        //after done with lower levels pop last added to try next possible orientation
                        solution.remove_piece(point, orientation);
                        //TODO check if last's in solutionState can be used instead
                        solution.setCubeFrontier(oldCubeFrontier);
                        solution.setCubeExplored(oldCubeExplored);
                    }
                    else//if cube is full
                    {
                        solutions.add(solution.getCube());
                        if(solutions.size()%100 == 0)
                        {
                            System.out.println("solutions " + solutions.size() + " " + (System.nanoTime() - time) / 60000000000.0);
                        }
                        //printCube(solution.getCube(),solutionFile);
                    }
                }
                else
                {
                    //addDead(solution.getCube());
                    addDead(solution.getTCube());
                    return;
                }
            }
        }
    }

    /***
     *
     * @param cube is either added or replaces other fully explored cubes
     */
    public void addDead(int[][][] cube)
    {
        boolean added = false;
        boolean[] contain_empty = new boolean[]{false,true};
        if(deadEnd.size() == 0)
        {
            deadEnd.add(cube);
        }
        else
        {
            for (int d = 0; d < deadEnd.size(); d++)
            {
                contain_empty = contains(deadEnd.get(d),cube);
                if (contain_empty[0] && ! contain_empty[1])
                {
                    if (!added)
                    {
                        deadEnd.set(d, cube);
                        dead++;
                        added = true;
                    }
                    else
                    {
                        deadEnd.remove(d);
                        d--;
                    }
                }
            }
            if(!contain_empty[0] && ! contain_empty[1])
            {
                deadEnd.add(cube);
                dead++;
            }
        }
    }
    public boolean is_explored(int[][][] cube)
    {
        boolean contained = false;
        for (int[][][] d : deadEnd)
        {
            contained = contains(cube,d)[0];
            if(contained)
            {
                kill++;
                break;
            }
        }
        /*if(!contained)
        {
            for (int[][][] s : knownSolutions)
            {
                contained = contains(cube,s)[0];
                if(contained)
                {
                    kill++;
                    System.out.println("Known");
                    break;
                }
            }
        }*/
        return contained;
    }
    /***
     *
     * @param large larger cube
     * @param small small cube
     * @return returns true if small contains any permutations of large
     */
    public boolean[] contains(int[][][] large, int[][][] small)
    {
        ArrayList<Boolean> contains = new ArrayList<>();
        for(int i = 0; i < 16; i++)
        {
            contains.add(true);
        }
        boolean contained = true;
        boolean empty = true;
        for (int x = 0; x < WIDTH && contained; x++)
            for (int y = 0; y < HEIGHT+1 && contained; y++)
                for (int z = 0; z < DEPTH && contained; z++)
                {
                    if(large[x][y][z] != -1)
                    {
                        empty = false;
                        if (large[x][y][z] != small[x][y][z])
                            contains.set(0, false);
                        if (!switch (small[x]                           [y]                           [-(z - small[x][y].length + 1)]) {
                            case 0 -> large[x][y][z] == 0;
                            case 1 -> large[x][y][z] == 3;
                            case 2 -> large[x][y][z] == 2;
                            case 3 -> large[x][y][z] == 1;
                            case 4 -> large[x][y][z] == 4;
                            case 5 -> large[x][y][z] == 7;
                            case 6 -> large[x][y][z] == 6;
                            case 7 -> large[x][y][z] == 5;
                            case 8 -> large[x][y][z] == 8;
                            case 9 -> large[x][y][z] == 9;
                            case 10 -> large[x][y][z] == 10;
                            case 11 -> large[x][y][z] == 11;
                            default -> large[x][y][z] == -1;
                        })//xy : 00
                            contains.set(1, false);
                        if (!switch (small[x]                           [-(y - small[x].length + 1)]   [z]) {
                            case 0 -> large[x][y][z] == 2;
                            case 1 -> large[x][y][z] == 1;
                            case 2 -> large[x][y][z] == 0;
                            case 3 -> large[x][y][z] == 3;
                            case 4 -> large[x][y][z] == 4;
                            case 5 -> large[x][y][z] == 5;
                            case 6 -> large[x][y][z] == 6;
                            case 7 -> large[x][y][z] == 7;
                            case 8 -> large[x][y][z] == 8;
                            case 9 -> large[x][y][z] == 11;
                            case 10 -> large[x][y][z] == 10;
                            case 11 -> large[x][y][z] == 9;
                            default -> large[x][y][z] == -1;
                        })                           //xy : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                            contains.set(2, false);
                        if (!switch (small[x]                           [-(y - small[x].length + 1)]   [-(z - small[x][y].length + 1)]) {
                            case 0 -> large[x][y][z] == 2;
                            case 1 -> large[x][y][z] == 3;
                            case 2 -> large[x][y][z] == 0;
                            case 3 -> large[x][y][z] == 1;
                            case 4 -> large[x][y][z] == 4;
                            case 5 -> large[x][y][z] == 7;
                            case 6 -> large[x][y][z] == 6;
                            case 7 -> large[x][y][z] == 5;
                            case 8 -> large[x][y][z] == 8;
                            case 9 -> large[x][y][z] == 11;
                            case 10 -> large[x][y][z] == 10;
                            case 11 -> large[x][y][z] == 9;
                            default -> large[x][y][z] == -1;
                        })//00 : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                            contains.set(3, false);
                            /*perms.get(0)[x][y][z] = switch (small[x]                       [z]                       [y]){
                                case 0 -> large[x][y][z] == 1;
                                case 1 -> large[x][y][z] == 0;
                                case 2 -> large[x][y][z] == 3;
                                case 3 -> large[x][y][z] == 2;
                                case 4 -> large[x][y][z] == 8;
                                case 5 -> large[x][y][z] == 9;
                                case 6 -> large[x][y][z] == 10;
                                case 7 -> large[x][y][z] == 11;
                                case 8 -> large[x][y][z] == 4;
                                case 9 -> large[x][y][z] == 5;
                                case 10-> 6;
                                case 11-> 7;
                                default -> large[x][y][z] == -1;
                            };                       //xy : xxx
                            perms.get(0)[x][y][z] = switch (small[x]                       [z]                       [-(y-small[x].length+1)]){
                                case 0 -> large[x][y][z] == 3;
                                case 1 -> large[x][y][z] == 0;
                                case 2 -> large[x][y][z] == 1;
                                case 3 -> large[x][y][z] == 2;
                                case 4 -> large[x][y][z] == 8;
                                case 5 -> large[x][y][z] == 9;
                                case 6 -> large[x][y][z] == 10;
                                case 7 -> large[x][y][z] == 11;
                                case 8 -> large[x][y][z] == 4;
                                case 9 -> large[x][y][z] == 7;
                                case 10-> 6;
                                case 11-> 5;
                                default -> large[x][y][z] == -1;
                            };   //00 : x,    yxz
                            perms.get(0)[x][y][z] = switch (small[x]                       [-(z-small[x][y].length+1)][y]){
                                case 0 -> large[x][y][z] == 1;
                                case 1 -> large[x][y][z] == 2;
                                case 2 -> large[x][y][z] == 3;
                                case 3 -> large[x][y][z] == 0;
                                case 4 -> large[x][y][z] == 8;
                                case 5 -> large[x][y][z] == 11;
                                case 6 -> large[x][y][z] == 10;
                                case 7 -> large[x][y][z] == 9;
                                case 8 -> large[x][y][z] == 4;
                                case 9 -> large[x][y][z] == 5;
                                case 10-> 6;
                                case 11-> 7;
                                default -> large[x][y][z] == -1;
                            };                       //00 : xxx
                            perms.get(0)[x][y][z] = switch (small[x]                       [-(z-small[x][y].length+1)][-(y-small[x].length+1)]){
                                case 0 -> large[x][y][z] == 3;
                                case 1 -> large[x][y][z] == 2;
                                case 2 -> large[x][y][z] == 1;
                                case 3 -> large[x][y][z] == 0;
                                case 4 -> large[x][y][z] == 8;
                                case 5 -> large[x][y][z] == 11;
                                case 6 -> large[x][y][z] == 10;
                                case 7 -> large[x][y][z] == 9;
                                case 8 -> large[x][y][z] == 4;
                                case 9 -> large[x][y][z] == 7;
                                case 10-> 6;
                                case 11-> 5;
                                default -> large[x][y][z] == -1;
                            };   //xy : x,    yxz*/
                        if (!switch (small[-(x - small.length + 1)]      [y]                           [z]) {
                            case 0 -> large[x][y][z] == 0;
                            case 1 -> large[x][y][z] == 1;
                            case 2 -> large[x][y][z] == 2;
                            case 3 -> large[x][y][z] == 3;
                            case 4 -> large[x][y][z] == 6;
                            case 5 -> large[x][y][z] == 5;
                            case 6 -> large[x][y][z] == 4;
                            case 7 -> large[x][y][z] == 7;
                            case 8 -> large[x][y][z] == 10;
                            case 9 -> large[x][y][z] == 9;
                            case 10 -> large[x][y][z] == 8;
                            case 11 -> large[x][y][z] == 11;
                            default -> large[x][y][z] == -1;
                        })                           //xy : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                            contains.set(4, false);
                        if (!switch (small[-(x - small.length + 1)]      [y]                           [-(z - small[x][y].length + 1)]) {
                            case 0 -> large[x][y][z] == 0;
                            case 1 -> large[x][y][z] == 3;
                            case 2 -> large[x][y][z] == 2;
                            case 3 -> large[x][y][z] == 1;
                            case 4 -> large[x][y][z] == 6;
                            case 5 -> large[x][y][z] == 7;
                            case 6 -> large[x][y][z] == 4;
                            case 7 -> large[x][y][z] == 5;
                            case 8 -> large[x][y][z] == 10;
                            case 9 -> large[x][y][z] == 9;
                            case 10 -> large[x][y][z] == 8;
                            case 11 -> large[x][y][z] == 11;
                            default -> large[x][y][z] == -1;
                        })//00 : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                            contains.set(5, false);
                        if (!switch (small[-(x - small.length + 1)]      [-(y - small[x].length + 1)]   [z]) {
                            case 0 -> large[x][y][z] == 2;
                            case 1 -> large[x][y][z] == 1;
                            case 2 -> large[x][y][z] == 0;
                            case 3 -> large[x][y][z] == 3;
                            case 4 -> large[x][y][z] == 6;
                            case 5 -> large[x][y][z] == 5;
                            case 6 -> large[x][y][z] == 4;
                            case 7 -> large[x][y][z] == 7;
                            case 8 -> large[x][y][z] == 10;
                            case 9 -> large[x][y][z] == 11;
                            case 10 -> large[x][y][z] == 8;
                            case 11 -> large[x][y][z] == 9;
                            default -> large[x][y][z] == -1;
                        })                           //00 : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                            contains.set(6, false);
                        if (!switch (small[-(x - small.length + 1)]      [-(y - small[x].length + 1)]   [-(z - small[x][y].length + 1)]) {
                            case 0 -> large[x][y][z] == 2;
                            case 1 -> large[x][y][z] == 3;
                            case 2 -> large[x][y][z] == 0;
                            case 3 -> large[x][y][z] == 1;
                            case 4 -> large[x][y][z] == 6;
                            case 5 -> large[x][y][z] == 7;
                            case 6 -> large[x][y][z] == 4;
                            case 7 -> large[x][y][z] == 5;
                            case 8 -> large[x][y][z] == 10;
                            case 9 -> large[x][y][z] == 11;
                            case 10 -> large[x][y][z] == 8;
                            case 11 -> large[x][y][z] == 9;
                            default -> large[x][y][z] == -1;
                        })//xy : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                            contains.set(7, false);
                            /*perms.get(0)[x][y][z] = switch (small[-(x-small.length+1)]      [z]                       [y]){
                                case 0 -> large[x][y][z] == 1;
                                case 1 -> large[x][y][z] == 0;
                                case 2 -> large[x][y][z] == 3;
                                case 3 -> large[x][y][z] == 2;
                                case 4 -> large[x][y][z] == 10;
                                case 5 -> large[x][y][z] == 9;
                                case 6 -> large[x][y][z] == 8;
                                case 7 -> large[x][y][z] == 11;
                                case 8 -> large[x][y][z] == 6;
                                case 9 -> large[x][y][z] == 5;
                                case 10-> 4;
                                case 11-> 7;
                                default -> large[x][y][z] == -1;
                            };                       //00 : xyy,  yzy,  zxy,  zyz,  zzx
                            perms.get(0)[x][y][z] = switch (small[-(x-small.length+1)]      [z]                       [-(y-small[x].length+1)]){
                                case 0 -> large[x][y][z] == 3;
                                case 1 -> large[x][y][z] == 0;
                                case 2 -> large[x][y][z] == 1;
                                case 3 -> large[x][y][z] == 2;
                                case 4 -> large[x][y][z] == 10;
                                case 5 -> large[x][y][z] == 9;
                                case 6 -> large[x][y][z] == 8;
                                case 7 -> large[x][y][z] == 11;
                                case 8 -> large[x][y][z] == 6;
                                case 9 -> large[x][y][z] == 7;
                                case 10-> 4;
                                case 11-> 5;
                                default -> large[x][y][z] == -1;
                            };   //xy : xzz,  yyx
                            perms.get(0)[x][y][z] = switch (small[-(x-small.length+1)]      [-(z-small[x][y].length+1)][y]){
                                case 0 -> large[x][y][z] == 1;
                                case 1 -> large[x][y][z] == 2;
                                case 2 -> large[x][y][z] == 3;
                                case 3 -> large[x][y][z] == 0;
                                case 4 -> large[x][y][z] == 10;
                                case 5 -> large[x][y][z] == 11;
                                case 6 -> large[x][y][z] == 8;
                                case 7 -> large[x][y][z] == 9;
                                case 8 -> large[x][y][z] == 6;
                                case 9 -> large[x][y][z] == 5;
                                case 10-> 4;
                                case 11-> 7;
                                default -> large[x][y][z] == -1;
                            };                       //xy : xyy,  yzy,  zxy,  zyz,  zzx
                            perms.get(0)[x][y][z] = switch (small[-(x-small.length+1)]      [-(z-small[x][y].length+1)][-(y-small[x].length+1)]){
                                case 0 -> large[x][y][z] == 3;
                                case 1 -> large[x][y][z] == 2;
                                case 2 -> large[x][y][z] == 1;
                                case 3 -> large[x][y][z] == 0;
                                case 4 -> large[x][y][z] == 10;
                                case 5 -> large[x][y][z] == 11;
                                case 6 -> large[x][y][z] == 8;
                                case 7 -> large[x][y][z] == 9;
                                case 8 -> large[x][y][z] == 6;
                                case 9 -> large[x][y][z] == 7;
                                case 10-> 4;
                                case 11-> 5;
                                default -> large[x][y][z] == -1;
                            };   //00 : xzz,  yyx
                            perms.get(0)[x][y][z] = switch (small[y]                       [x]                       [z]){
                                case 0 -> large[x][y][z] == 6;
                                case 1 -> large[x][y][z] == 5;
                                case 2 -> large[x][y][z] == 4;
                                case 3 -> large[x][y][z] == 7;
                                case 4 -> large[x][y][z] == 2;
                                case 5 -> large[x][y][z] == 1;
                                case 6 -> large[x][y][z] == 0;
                                case 7 -> large[x][y][z] == 3;
                                case 8 -> large[x][y][z] == 11;
                                case 9 -> large[x][y][z] == 10;
                                case 10-> 9;
                                case 11-> 8;
                                default -> large[x][y][z] == -1;
                            };                       //xy : xxz,  zyy
                            perms.get(0)[x][y][z] = switch (small[y]                       [x]                       [-(z-small[x][y].length+1)]){
                                case 0 -> large[x][y][z] == 6;
                                case 1 -> large[x][y][z] == 7;
                                case 2 -> large[x][y][z] == 4;
                                case 3 -> large[x][y][z] == 5;
                                case 4 -> large[x][y][z] == 2;
                                case 5 -> large[x][y][z] == 3;
                                case 6 -> large[x][y][z] == 0;
                                case 7 -> large[x][y][z] == 1;
                                case 8 -> large[x][y][z] == 11;
                                case 9 -> large[x][y][z] == 10;
                                case 10-> 9;
                                case 11-> 8;
                                default -> large[x][y][z] == -1;
                            };//00 : xxz,  zyy
                            perms.get(0)[x][y][z] = switch (small[y]                       [-(x-small.length+1)]      [z]){
                                case 0 -> large[x][y][z] == 6;
                                case 1 -> large[x][y][z] == 5;
                                case 2 -> large[x][y][z] == 4;
                                case 3 -> large[x][y][z] == 7;
                                case 4 -> large[x][y][z] == 0;
                                case 5 -> large[x][y][z] == 1;
                                case 6 -> large[x][y][z] == 2;
                                case 7 -> large[x][y][z] == 3;
                                case 8 -> large[x][y][z] == 9;
                                case 9 -> large[x][y][z] == 10;
                                case 10-> 11;
                                case 11-> 8;
                                default -> large[x][y][z] == -1;
                            };                       //00 : zzz
                            perms.get(0)[x][y][z] = switch (small[y]                       [-(x-small.length+1)]      [-(z-small[x][y].length+1)]){
                                case 0 -> large[x][y][z] == 6;
                                case 1 -> large[x][y][z] == 7;
                                case 2 -> large[x][y][z] == 4;
                                case 3 -> large[x][y][z] == 5;
                                case 4 -> large[x][y][z] == 0;
                                case 5 -> large[x][y][z] == 3;
                                case 6 -> large[x][y][z] == 2;
                                case 7 -> large[x][y][z] == 1;
                                case 8 -> large[x][y][z] == 9;
                                case 9 -> large[x][y][z] == 10;
                                case 10-> 11;
                                case 11-> 8;
                                default -> large[x][y][z] == -1;
                            };//xy : zzz
                            perms.get(0)[x][y][z] = switch (small[y]                       [z]                       [x]){
                                case 0 -> large[x][y][z] == 10;
                                case 1 -> large[x][y][z] == 9;
                                case 2 -> large[x][y][z] == 8;
                                case 3 -> large[x][y][z] == 11;
                                case 4 -> large[x][y][z] == 3;
                                case 5 -> large[x][y][z] == 0;
                                case 6 -> large[x][y][z] == 1;
                                case 7 -> large[x][y][z] == 2;
                                case 8 -> large[x][y][z] == 7;
                                case 9 -> large[x][y][z] == 6;
                                case 10-> 5;
                                case 11-> 4;
                                default -> large[x][y][z] == -1;
                            };                       //00 : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
                            perms.get(0)[x][y][z] = switch (small[y]                       [z]                       [-(x-small.length+1)]){
                                case 0 -> large[x][y][z] == 10;
                                case 1 -> large[x][y][z] == 9;
                                case 2 -> large[x][y][z] == 8;
                                case 3 -> large[x][y][z] == 11;
                                case 4 -> large[x][y][z] == 1;
                                case 5 -> large[x][y][z] == 0;
                                case 6 -> large[x][y][z] == 3;
                                case 7 -> large[x][y][z] == 2;
                                case 8 -> large[x][y][z] == 5;
                                case 9 -> large[x][y][z] == 6;
                                case 10-> 7;
                                case 11-> 4;
                                default -> large[x][y][z] == -1;
                            };      //xy : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
                            perms.get(0)[x][y][z] = switch (small[y]                       [-(z-small[x][y].length+1)][x]){
                                case 0 -> large[x][y][z] == 10;
                                case 1 -> large[x][y][z] == 11;
                                case 2 -> large[x][y][z] == 8;
                                case 3 -> large[x][y][z] == 9;
                                case 4 -> large[x][y][z] == 3;
                                case 5 -> large[x][y][z] == 2;
                                case 6 -> large[x][y][z] == 1;
                                case 7 -> large[x][y][z] == 0;
                                case 8 -> large[x][y][z] == 7;
                                case 9 -> large[x][y][z] == 6;
                                case 10-> 5;
                                case 11-> 4;
                                default -> large[x][y][z] == -1;
                            };                       //xy : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
                            perms.get(0)[x][y][z] = switch (small[y]                       [-(z-small[x][y].length+1)][-(x-small.length+1)]){
                                case 0 -> large[x][y][z] == 10;
                                case 1 -> large[x][y][z] == 11;
                                case 2 -> large[x][y][z] == 8;
                                case 3 -> large[x][y][z] == 9;
                                case 4 -> large[x][y][z] == 1;
                                case 5 -> large[x][y][z] == 2;
                                case 6 -> large[x][y][z] == 3;
                                case 7 -> large[x][y][z] == 0;
                                case 8 -> large[x][y][z] == 5;
                                case 9 -> large[x][y][z] == 6;
                                case 10-> 7;
                                case 11-> 4;
                                default -> large[x][y][z] == -1;
                            };      //00 : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
                            perms.get(0)[x][y][z] = switch (small[-(y-small[x].length+1)]   [x]                       [z]){
                                case 0 -> large[x][y][z] == 4;
                                case 1 -> large[x][y][z] == 5;
                                case 2 -> large[x][y][z] == 6;
                                case 3 -> large[x][y][z] == 7;
                                case 4 -> large[x][y][z] == 2;
                                case 5 -> large[x][y][z] == 1;
                                case 6 -> large[x][y][z] == 0;
                                case 7 -> large[x][y][z] == 3;
                                case 8 -> large[x][y][z] == 11;
                                case 9 -> large[x][y][z] == 8;
                                case 10-> 9;
                                case 11-> 10;
                                default -> large[x][y][z] == -1;
                            };                       //00 : xzy,  z
                            perms.get(0)[x][y][z] = switch (small[-(y-small[x].length+1)]   [x]                       [-(z-small[x][y].length+1)]){
                                case 0 -> large[x][y][z] == 4;
                                case 1 -> large[x][y][z] == 7;
                                case 2 -> large[x][y][z] == 6;
                                case 3 -> large[x][y][z] == 5;
                                case 4 -> large[x][y][z] == 2;
                                case 5 -> large[x][y][z] == 3;
                                case 6 -> large[x][y][z] == 0;
                                case 7 -> large[x][y][z] == 1;
                                case 8 -> large[x][y][z] == 11;
                                case 9 -> large[x][y][z] == 8;
                                case 10-> 9;
                                case 11-> 10;
                                default -> large[x][y][z] == -1;
                            };//xy : xzy,  z
                            perms.get(0)[x][y][z] = switch (small[-(y-small[x].length+1)]   [-(x-small.length+1)]      [z]){
                                case 0 -> large[x][y][z] == 4;
                                case 1 -> large[x][y][z] == 5;
                                case 2 -> large[x][y][z] == 6;
                                case 3 -> large[x][y][z] == 7;
                                case 4 -> large[x][y][z] == 0;
                                case 5 -> large[x][y][z] == 1;
                                case 6 -> large[x][y][z] == 2;
                                case 7 -> large[x][y][z] == 3;
                                case 8 -> large[x][y][z] == 9;
                                case 9 -> large[x][y][z] == 8;
                                case 10-> 11;
                                case 11-> 10;
                                default -> large[x][y][z] == -1;
                            };                       //xy : xyx,  yxy,  yyz,  yzx,  zxx
                            perms.get(0)[x][y][z] = switch (small[-(y-small[x].length+1)]   [-(x-small.length+1)]      [-(z-small[x][y].length+1)]){
                                case 0 -> large[x][y][z] == 4;
                                case 1 -> large[x][y][z] == 7;
                                case 2 -> large[x][y][z] == 6;
                                case 3 -> large[x][y][z] == 5;
                                case 4 -> large[x][y][z] == 0;
                                case 5 -> large[x][y][z] == 3;
                                case 6 -> large[x][y][z] == 2;
                                case 7 -> large[x][y][z] == 1;
                                case 8 -> large[x][y][z] == 9;
                                case 9 -> large[x][y][z] == 8;
                                case 10-> 11;
                                case 11-> 10;
                                default -> large[x][y][z] == -1;
                            };//00 : xyx,  yxy,  yyz,  yzx,  zxx
                            perms.get(0)[x][y][z] = switch (small[-(y-small[x].length+1)]   [z]                       [x]){
                                case 0 -> large[x][y][z] == 8;
                                case 1 -> large[x][y][z] == 9;
                                case 2 -> large[x][y][z] == 10;
                                case 3 -> large[x][y][z] == 11;
                                case 4 -> large[x][y][z] == 3;
                                case 5 -> large[x][y][z] == 0;
                                case 6 -> large[x][y][z] == 1;
                                case 7 -> large[x][y][z] == 2;
                                case 8 -> large[x][y][z] == 7;
                                case 9 -> large[x][y][z] == 4;
                                case 10-> 5;
                                case 11-> 6;
                                default -> large[x][y][z] == -1;
                            };                       //xy : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
                            perms.get(0)[x][y][z] = switch (small[-(y-small[x].length+1)]   [z]                       [-(x-small.length+1)]){
                                case 0 -> large[x][y][z] == 8;
                                case 1 -> large[x][y][z] == 9;
                                case 2 -> large[x][y][z] == 10;
                                case 3 -> large[x][y][z] == 11;
                                case 4 -> large[x][y][z] == 1;
                                case 5 -> large[x][y][z] == 0;
                                case 6 -> large[x][y][z] == 3;
                                case 7 -> large[x][y][z] == 2;
                                case 8 -> large[x][y][z] == 5;
                                case 9 -> large[x][y][z] == 4;
                                case 10-> 7;
                                case 11-> 6;
                                default -> large[x][y][z] == -1;
                            };      //00 : xy,   xzyx, yxzy, yz,   zx,   zyxz
                            perms.get(0)[x][y][z] = switch (small[-(y-small[x].length+1)]   [-(z-small[x][y].length+1)][x]){
                                case 0 -> large[x][y][z] == 8;
                                case 1 -> large[x][y][z] == 11;
                                case 2 -> large[x][y][z] == 10;
                                case 3 -> large[x][y][z] == 9;
                                case 4 -> large[x][y][z] == 3;
                                case 5 -> large[x][y][z] == 2;
                                case 6 -> large[x][y][z] == 1;
                                case 7 -> large[x][y][z] == 0;
                                case 8 -> large[x][y][z] == 7;
                                case 9 -> large[x][y][z] == 4;
                                case 10-> 5;
                                case 11-> 6;
                                default -> large[x][y][z] == -1;
                            };                       //00 : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
                            perms.get(0)[x][y][z] = switch (small[-(y-small[x].length+1)]   [-(z-small[x][y].length+1)][-(x-small.length+1)]){
                                case 0 -> large[x][y][z] == 8;
                                case 1 -> large[x][y][z] == 11;
                                case 2 -> large[x][y][z] == 10;
                                case 3 -> large[x][y][z] == 9;
                                case 4 -> large[x][y][z] == 1;
                                case 5 -> large[x][y][z] == 2;
                                case 6 -> large[x][y][z] == 3;
                                case 7 -> large[x][y][z] == 0;
                                case 8 -> large[x][y][z] == 5;
                                case 9 -> large[x][y][z] == 4;
                                case 10-> 7;
                                case 11-> 6;
                                default -> large[x][y][z] == -1;
                            };      //xy : xy,   xzyx, yxzy, yz,   zx,   zyxz
                            perms.get(0)[x][y][z] = switch (small[z]                       [x]                       [y]){
                                case 0 -> large[x][y][z] == 5;
                                case 1 -> large[x][y][z] == 6;
                                case 2 -> large[x][y][z] == 7;
                                case 3 -> large[x][y][z] == 4;
                                case 4 -> large[x][y][z] == 11;
                                case 5 -> large[x][y][z] == 10;
                                case 6 -> large[x][y][z] == 9;
                                case 7 -> large[x][y][z] == 8;
                                case 8 -> large[x][y][z] == 2;
                                case 9 -> large[x][y][z] == 1;
                                case 10-> 0;
                                case 11-> 3;
                                default -> large[x][y][z] == -1;
                            };                       //00 : xxxz, xzyy, yxxx, zy,   zzyx
                            perms.get(0)[x][y][z] = switch (small[z]                       [x]                       [-(y-small[x].length+1)]){
                                case 0 -> large[x][y][z] == 7;
                                case 1 -> large[x][y][z] == 6;
                                case 2 -> large[x][y][z] == 5;
                                case 3 -> large[x][y][z] == 4;
                                case 4 -> large[x][y][z] == 11;
                                case 5 -> large[x][y][z] == 10;
                                case 6 -> large[x][y][z] == 9;
                                case 7 -> large[x][y][z] == 8;
                                case 8 -> large[x][y][z] == 2;
                                case 9 -> large[x][y][z] == 3;
                                case 10-> 0;
                                case 11-> 1;
                                default -> large[x][y][z] == -1;
                            };   //xy : xxzy, xz,   yxzz, yyyx, zyyy
                            perms.get(0)[x][y][z] = switch (small[z]                       [-(x-small.length+1)]      [y]){
                                case 0 -> large[x][y][z] == 5;
                                case 1 -> large[x][y][z] == 6;
                                case 2 -> large[x][y][z] == 7;
                                case 3 -> large[x][y][z] == 4;
                                case 4 -> large[x][y][z] == 9;
                                case 5 -> large[x][y][z] == 10;
                                case 6 -> large[x][y][z] == 11;
                                case 7 -> large[x][y][z] == 8;
                                case 8 -> large[x][y][z] == 0;
                                case 9 -> large[x][y][z] == 1;
                                case 10-> 2;
                                case 11-> 3;
                                default -> large[x][y][z] == -1;
                            };                       //xy : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz
                            perms.get(0)[x][y][z] = switch (small[z]                       [-(x-small.length+1)]      [-(y-small[x].length+1)]){
                                case 0 -> large[x][y][z] == 7;
                                case 1 -> large[x][y][z] == 6;
                                case 2 -> large[x][y][z] == 5;
                                case 3 -> large[x][y][z] == 4;
                                case 4 -> large[x][y][z] == 9;
                                case 5 -> large[x][y][z] == 10;
                                case 6 -> large[x][y][z] == 11;
                                case 7 -> large[x][y][z] == 8;
                                case 8 -> large[x][y][z] == 0;
                                case 9 -> large[x][y][z] == 3;
                                case 10-> 2;
                                case 11-> 1;
                                default -> large[x][y][z] == -1;
                            };   //00 : xzzz, yx,   yyxz, zyxx, zzzy*/
                        if (!switch (small[z]                           [y]                           [x]) {
                            case 0 -> large[x][y][z] == 9;
                            case 1 -> large[x][y][z] == 10;
                            case 2 -> large[x][y][z] == 11;
                            case 3 -> large[x][y][z] == 8;
                            case 4 -> large[x][y][z] == 7;
                            case 5 -> large[x][y][z] == 6;
                            case 6 -> large[x][y][z] == 5;
                            case 7 -> large[x][y][z] == 4;
                            case 8 -> large[x][y][z] == 3;
                            case 9 -> large[x][y][z] == 0;
                            case 10 -> large[x][y][z] == 1;
                            case 11 -> large[x][y][z] == 2;
                            default -> large[x][y][z] == -1;
                        })                           //xy : yyy
                            contains.set(8, false);
                        if (!switch (small[z]                           [y]                           [-(x - small.length + 1)]) {
                            case 0 -> large[x][y][z] == 9;
                            case 1 -> large[x][y][z] == 10;
                            case 2 -> large[x][y][z] == 11;
                            case 3 -> large[x][y][z] == 8;
                            case 4 -> large[x][y][z] == 5;
                            case 5 -> large[x][y][z] == 6;
                            case 6 -> large[x][y][z] == 7;
                            case 7 -> large[x][y][z] == 4;
                            case 8 -> large[x][y][z] == 1;
                            case 9 -> large[x][y][z] == 0;
                            case 10 -> large[x][y][z] == 3;
                            case 11 -> large[x][y][z] == 2;
                            default -> large[x][y][z] == -1;
                        })      //00 : y,    zyx
                            contains.set(9, false);
                        if (!switch (small[z]                           [-(y - small[x].length + 1)]   [x]) {
                            case 0 -> large[x][y][z] == 11;
                            case 1 -> large[x][y][z] == 10;
                            case 2 -> large[x][y][z] == 9;
                            case 3 -> large[x][y][z] == 8;
                            case 4 -> large[x][y][z] == 7;
                            case 5 -> large[x][y][z] == 6;
                            case 6 -> large[x][y][z] == 5;
                            case 7 -> large[x][y][z] == 4;
                            case 8 -> large[x][y][z] == 3;
                            case 9 -> large[x][y][z] == 2;
                            case 10 -> large[x][y][z] == 1;
                            case 11 -> large[x][y][z] == 0;
                            default -> large[x][y][z] == -1;
                        })                           //00 : yxx,  zzy
                            contains.set(10, false);
                        if (!switch (small[z]                           [-(y - small[x].length + 1)]   [-(x - small.length + 1)]) {
                            case 0 -> large[x][y][z] == 11;
                            case 1 -> large[x][y][z] == 10;
                            case 2 -> large[x][y][z] == 9;
                            case 3 -> large[x][y][z] == 8;
                            case 4 -> large[x][y][z] == 5;
                            case 5 -> large[x][y][z] == 6;
                            case 6 -> large[x][y][z] == 7;
                            case 7 -> large[x][y][z] == 4;
                            case 8 -> large[x][y][z] == 1;
                            case 9 -> large[x][y][z] == 2;
                            case 10 -> large[x][y][z] == 3;
                            case 11 -> large[x][y][z] == 0;
                            default -> large[x][y][z] == -1;
                        })      //xy : xxy,  xyz,  xzx,  yzz,  zxz
                            contains.set(11, false);
                            /*perms.get(0)[x][y][z] = switch (small[-(z-small[x][y].length+1)][x]                       [y]){
                                case 0 -> large[x][y][z] == 5;
                                case 1 -> large[x][y][z] == 4;
                                case 2 -> large[x][y][z] == 7;
                                case 3 -> large[x][y][z] == 6;
                                case 4 -> large[x][y][z] == 11;
                                case 5 -> large[x][y][z] == 8;
                                case 6 -> large[x][y][z] == 9;
                                case 7 -> large[x][y][z] == 10;
                                case 8 -> large[x][y][z] == 2;
                                case 9 -> large[x][y][z] == 1;
                                case 10-> 0;
                                case 11-> 3;
                                default -> large[x][y][z] == -1;
                            };                       //xy : xxxz, xzyy, yxxx, zy,   zzyx
                            perms.get(0)[x][y][z] = switch (small[-(z-small[x][y].length+1)][x]                       [-(y-small[x].length+1)]){
                                case 0 -> large[x][y][z] == 7;
                                case 1 -> large[x][y][z] == 4;
                                case 2 -> large[x][y][z] == 5;
                                case 3 -> large[x][y][z] == 6;
                                case 4 -> large[x][y][z] == 11;
                                case 5 -> large[x][y][z] == 8;
                                case 6 -> large[x][y][z] == 9;
                                case 7 -> large[x][y][z] == 10;
                                case 8 -> large[x][y][z] == 2;
                                case 9 -> large[x][y][z] == 3;
                                case 10-> 0;
                                case 11-> 1;
                                default -> large[x][y][z] == -1;
                            };   //00 : xxzy, xz,   yxzz, yyyx, zyyy
                            perms.get(0)[x][y][z] = switch (small[-(z-small[x][y].length+1)][-(x-small.length+1)]      [-(y-small[x].length+1)]){
                                case 0 -> large[x][y][z] == 7;
                                case 1 -> large[x][y][z] == 4;
                                case 2 -> large[x][y][z] == 5;
                                case 3 -> large[x][y][z] == 6;
                                case 4 -> large[x][y][z] == 9;
                                case 5 -> large[x][y][z] == 8;
                                case 6 -> large[x][y][z] == 11;
                                case 7 -> large[x][y][z] == 10;
                                case 8 -> large[x][y][z] == 0;
                                case 9 -> large[x][y][z] == 3;
                                case 10-> 2;
                                case 11-> 1;
                                default -> large[x][y][z] == -1;
                            };   //xy : xzzz, yx,   yyxz, zyxx, zzzy
                            perms.get(0)[x][y][z] = switch (small[-(z-small[x][y].length+1)][-(x-small.length+1)]      [y]){
                                case 0 -> large[x][y][z] == 5;
                                case 1 -> large[x][y][z] == 4;
                                case 2 -> large[x][y][z] == 7;
                                case 3 -> large[x][y][z] == 6;
                                case 4 -> large[x][y][z] == 9;
                                case 5 -> large[x][y][z] == 8;
                                case 6 -> large[x][y][z] == 11;
                                case 7 -> large[x][y][z] == 10;
                                case 8 -> large[x][y][z] == 0;
                                case 9 -> large[x][y][z] == 1;
                                case 10-> 2;
                                case 11-> 3;
                                default -> large[x][y][z] == -1;
                            };                       //00 : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz*/
                        if (!switch (small[-(z - small[x][y].length + 1)][y]                           [x]) {
                            case 0 -> large[x][y][z] == 9;
                            case 1 -> large[x][y][z] == 8;
                            case 2 -> large[x][y][z] == 11;
                            case 3 -> large[x][y][z] == 10;
                            case 4 -> large[x][y][z] == 7;
                            case 5 -> large[x][y][z] == 4;
                            case 6 -> large[x][y][z] == 5;
                            case 7 -> large[x][y][z] == 6;
                            case 8 -> large[x][y][z] == 3;
                            case 9 -> large[x][y][z] == 0;
                            case 10 -> large[x][y][z] == 1;
                            case 11 -> large[x][y][z] == 2;
                            default -> large[x][y][z] == -1;
                        })                           //00 : yyy
                            contains.set(12, false);
                        if (!switch (small[-(z - small[x][y].length + 1)][y]                           [-(x - small.length + 1)]) {
                            case 0 -> large[x][y][z] == 9;
                            case 1 -> large[x][y][z] == 8;
                            case 2 -> large[x][y][z] == 11;
                            case 3 -> large[x][y][z] == 10;
                            case 4 -> large[x][y][z] == 5;
                            case 5 -> large[x][y][z] == 4;
                            case 6 -> large[x][y][z] == 7;
                            case 7 -> large[x][y][z] == 6;
                            case 8 -> large[x][y][z] == 1;
                            case 9 -> large[x][y][z] == 0;
                            case 10 -> large[x][y][z] == 3;
                            case 11 -> large[x][y][z] == 2;
                            default -> large[x][y][z] == -1;
                        })      //xy : y,    zyx
                            contains.set(13, false);
                        if (!switch (small[-(z - small[x][y].length + 1)][-(y - small[x].length + 1)]   [x]) {
                            case 0 -> large[x][y][z] == 11;
                            case 1 -> large[x][y][z] == 8;
                            case 2 -> large[x][y][z] == 9;
                            case 3 -> large[x][y][z] == 10;
                            case 4 -> large[x][y][z] == 7;
                            case 5 -> large[x][y][z] == 4;
                            case 6 -> large[x][y][z] == 5;
                            case 7 -> large[x][y][z] == 6;
                            case 8 -> large[x][y][z] == 3;
                            case 9 -> large[x][y][z] == 2;
                            case 10 -> large[x][y][z] == 1;
                            case 11 -> large[x][y][z] == 0;
                            default -> large[x][y][z] == -1;
                        })                           //xy : yxx,  zzy
                            contains.set(14, false);
                        if (!switch (small[-(z - small[x][y].length + 1)][-(y - small[x].length + 1)]   [-(x - small.length + 1)]) {
                            case 0 -> large[x][y][z] == 11;
                            case 1 -> large[x][y][z] == 8;
                            case 2 -> large[x][y][z] == 9;
                            case 3 -> large[x][y][z] == 10;
                            case 4 -> large[x][y][z] == 5;
                            case 5 -> large[x][y][z] == 4;
                            case 6 -> large[x][y][z] == 7;
                            case 7 -> large[x][y][z] == 6;
                            case 8 -> large[x][y][z] == 1;
                            case 9 -> large[x][y][z] == 2;
                            case 10 -> large[x][y][z] == 3;
                            case 11 -> large[x][y][z] == 0;
                            default -> large[x][y][z] == -1;
                        })      //00 : xxy,  xyz,  xzx,  yzz,  zxz
                            contains.set(15, false);
                        contained = contains.contains(true);
                    }
                }
        return new boolean[]{contained,empty};
    }

    public void addDead(ArrayList<Piece> cube)
    {
        boolean added = false;
        boolean contains = false;
        boolean contained = false;
        if(dead_cubes.size() == 0 && cube.size()>0)
        {
            dead_cubes.add(cube);
        }
        else if(cube.size()>0)
        {
            for (int d = 0; d < dead_cubes.size(); d++)
            {
                contains = contains(dead_cubes.get(d),cube);
                if (contains)
                {
                    contained = true;
                    if (!added)
                    {
                        dead_cubes.set(d, cube);
                        dead++;
                        added = true;
                    }
                    else
                    {
                        dead_cubes.remove(d);
                        d--;
                    }
                }
            }
            if(!contains)
            {
                dead_cubes.add(cube);
                dead++;
            }
        }
    }

    /**
     * finds if cube is a part of an explored set
     * @param cube
     * @return
     */
    public boolean is_explored(ArrayList<Piece> cube)
    {
        boolean contained = false;
        for (ArrayList<Piece> dead : dead_cubes)
        {
            contained = contains(cube,dead);
            if(contained)
            {
                kill++;
                break;
            }
        }
        return contained;
    }

    /**
     * finds if one cube contains another
     * @param large container
     * @param small contained
     * @return true if small is completely contained in large false otherwise
     */
    public boolean contains(ArrayList<Piece> large,ArrayList<Piece> small)
    {
        if(small.size() >= large.size())//if small has more pieces it cannot be contained
            return false;
        //TODO intuition says there is an optimisation here
        //check all rotations
        for(int i = 0; i<= ROTATIONS;i++)
        {
            boolean contains = true;
            for (Piece piece : small)
            {
                //this only works if all points are unique
                if (!large.contains(piece.rotate(i)))
                {
                    contains = false;
                    break;
                }
            }
            if(contains)
            {
                return true;
            }
        }
        return false;
    }
}
