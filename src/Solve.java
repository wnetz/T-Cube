import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solve
{
    public final int PRINT_TO;
    public final int HEIGHT = 4;
    ArrayList<int[][][]> deadEnd;
    FileWriter solutionFile;
    FileWriter file;
    long time;
    int dead = 0;
    int kill = 0;
    static ArrayList<int[][][]> solutions;
    
    public Solve(int p, FileWriter f) throws IOException
    {
        solutionFile = new FileWriter("solutions.txt");
        PRINT_TO = p;
        file = f;
        deadEnd = new ArrayList<>();
        solutions = new ArrayList<>();
        int[][][] cube = new int [6][HEIGHT][6];
        for(int x = 0; x < 6; x++)
            for(int y = 0; y < HEIGHT; y++)
                for(int z = 0; z < 6; z++)
                    cube[x][y][z] = -1;

        System.out.println("start");
        time = System.nanoTime();
        recursiveSolve(new SolutionState(cube),0);
        System.out.println((System.nanoTime()-time)/1000000000.0 );

        /*System.out.println("converting ");
        for (int[][][] solution : solutions)
            for (int x = 0; x < 6; x++)
                for (int y = 0; y < HEIGHT; y++)
                    for (int z = 0; z < 6; z++)
                        solution[x][y][z] = solution[x][y][z] % 12;*/


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
                        removed++;
                        i--;
                        c = perms.size();
                        break;
                    }
                }
            }
        }
        System.out.println("copies removed: " + removed);
        for(int i = 0; i < solutions.size(); i++)
            printCube(solutions.get(i),solutionFile);
        System.out.println("killed: " + kill);
        System.out.println("dead: " + deadEnd.size() + " " + dead);
        solutionFile.close();
    }

    public ArrayList<int[][][]> readSolutions()
    {
        ArrayList<int[][][]> solutions = new ArrayList<>();
        return solutions;
    }
    public  void printCube(int[][][] cube)
    {
        for (int i = 0; i < HEIGHT; i++)
        {
            StringBuilder layer = new StringBuilder();
            for (int j = 0; j < 6; j++)
            {
                for (int k = 0; k < 6; k++)
                    if (cube[k][i][j] != -1)
                    {
                        if (cube[k][i][j] < 10)
                            layer.append("0").append(cube[k][i][j]).append(", ");
                        else
                            layer.append(cube[k][i][j]).append(", ");
                    }
                    else
                        layer.append(cube[k][i][j]).append(", ");

                layer.append("\n");
            }
            System.out.print(layer + "\n");
        }
        System.out.print("########################################################\n");
    }
    public  void printCube(int[][][] cube, FileWriter file) throws IOException
    {
        for (int i = 0; i < HEIGHT; i++)
        {
            StringBuilder layer = new StringBuilder();
            boolean allEmpty = true;
            for (int j = 0; j < 6; j++)
            {
                for (int k = 0; k < 6; k++)
                    if (cube[k][i][j] != -1)
                    {
                        allEmpty = false;
                        if (cube[k][i][j] < 10)
                            layer.append("0").append(cube[k][i][j]).append(", ");
                        else
                            layer.append(cube[k][i][j]).append(", ");
                    }
                    else
                        layer.append(cube[k][i][j]).append(", ");

                layer.append("\n");
            }
            if (!allEmpty && PRINT_TO != 0)
                file.write(layer + "\n");
            else if(PRINT_TO == 0)
                System.out.print(layer + "\n");
        }
        if (PRINT_TO != 0)
            file.write("########################################################\n");
        else
            System.out.print("########################################################\n");
    }
    public  boolean cubeCopy(int[][][] c1, int[][][] c2)
    {
        boolean same = true;
        for (int x = 0; x < 6 && same; x++)
            for (int y = 0; y < HEIGHT && same; y++)
                for (int z = 0; z < 6; z++)
                    if (c1[x][y][z] != c2[x][y][z])
                    {
                        same = false;
                        break;
                    }
        return same;
    }
    /*
    1u = 0
    1r = yyy
    1d = yy,xxzz,xyyx
    1l = y,zyx
    2u = x,yxz
    2r = xyyy,yxxz,zzzx
    2d = xyy,zyz,zzx
    2l = xy,yz,zx
    3u = xz,xxzy,yyyx
    3r = xxz,zyy
    3d = zy,yxxx,xxxz
    3l = z,xzy
    4u = yx,xzzz,zzzy
    4r = zzz
    4d = xyxy,xyyz,xxyx
    4l = xyx,yxy,yyz
    5u = xzz,yyx
    5r = xxxy,xxyz,xxzx
    5d = xxx
    5l = xyxx,xzzy,yyyz
    6u = zz,xxyy,xzzx
    6r = xxy,xyz,xzx
    6d = xx,yxxy,xyxz
    6l = yxx,zzy
    1u = 0
    1r = yyy
    1d = yy
    1l = y
    2u = x
    2r = xyyy
    2d = xyy
    2l = xy
    3u = yyyx
    3r = xxz
    3d = xxxz
    3l = z
    4u = zzzy
    4r = zzz
    4d = xyxy
    4l = xyx
    5u = yyx
    5r = xxxy
    5d = xxx
    5l = xyxx
    6u = zz
    6r = xyz
    6d = xx
    6l = zzy
     */
    public  int[][][] rotateCube(int[][][] cube, int axis)
    {
        int[][][] c = new int[cube.length][][];
        for(int x = 0; x < cube.length; x++)
        {
            c[x] = new int [cube[x].length][];
            for(int y = 0; y < cube[x].length; y++)
            {
                c[x][y] = new int [cube[x][y].length];
                for(int z = 0; z < cube[x][y].length; z++)
                {
                    c[x][y][z] = switch (axis)
                            {
                                case 0 -> switch (cube[z][y][-(x-cube.length+1)])//y
                                        {
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
                                        };
                                default -> -1;
                            };
                }
            }
        }
        return c;
    }
    public  int[][][] flipCube(int[][][] cube, int axis)
    {
        int[][][] c = new int[cube.length][][];
        for(int x = 0; x < cube.length; x++)
        {
            c[x] = new int [cube[x].length][];
            for(int y = 0; y < cube[x].length; y++)
            {
                c[x][y] = new int [cube[x][y].length];
                for(int z = 0; z < cube[x][y].length; z++)
                {
                    c[x][y][z] = switch (axis)
                            {
                                case 0 -> switch (cube[x][y][-(z-cube[x][y].length+1)])//xy
                                        {
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
                                        };
                                case 1 -> switch (cube[x][-(y-cube[x].length+1)][z])//xz
                                        {
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
                                        };
                                case 2 -> switch (cube[-(x-cube.length+1)][y][z])//yz
                                        {
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
                                        };
                                case 3 -> switch (cube[x][-(y-cube[x].length+1)][-(z-cube[x][y].length+1)])//xy,xz
                                        {
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
                                        };
                                /*case 4 -> switch (cube[-(x-cube.length+1)][-(y-cube[x].length+1)][z])//xz,yz
                                        {
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
                                        };
                                case 5 -> switch (cube[-(x-cube.length+1)][y][-(z-cube[x][y].length+1)])//yz,xy
                                        {
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
                                        };*/
                                default -> cube[x][y][z];
                            };
                }
            }
        }
        return c;
    }
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


    public  Point recursiveSolve(SolutionState solution, int depth)
    {
        ArrayList<Point> oldCubeFrontier = solution.getCubeFrontier();
        ArrayList<Point> oldCubeExplored = solution.getCubeExplored();

        //Main.print("depth: " + depth + "\n");
        //try every point in the frontier
        for(Point point : oldCubeFrontier)
        {
            //Main.print("rotation: " + rotation++ + "\n");

            int orientation = -1;
            //try every orientation on point
            while (orientation != -2)
            {
                orientation = solution.addT(point, orientation);

                //Main.print("orientation: " + orientation + "\n");
                //if already explored
                if(orientation != -2 && deadContains(solution.getCube()))
                {
                    solution.removeT(point, orientation);
                    solution.setCubeFrontier(oldCubeFrontier);
                    solution.setCubeExplored(oldCubeExplored);
                }
                //if a T fits
                else if (orientation != -2 )
                {
                    //printCube(solution.getCube(),file);

                    if (solution.getCubeFrontier().size() > 0)
                    {
                        Point result = recursiveSolve(new SolutionState(solution.getCubeFrontier(), solution.getCubeExplored(), solution.getCube()), depth + 1);

                        solution.removeT(point, orientation);
                        solution.setCubeFrontier(oldCubeFrontier);
                        solution.setCubeExplored(oldCubeExplored);
                    }
                    else
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
                    addDead(solution.getCube());
                    return point;
                }
            }
        }
        return null;
    }
    public void addDead(int[][][] cube)
    {
        boolean added = false;
        boolean empty = true;
        if(deadEnd.size() == 0)
        {
            deadEnd.add(cube);
        }
        else
        {
            boolean contained = false;
            for (int d = 0; d < deadEnd.size(); d++)
            {
                ArrayList<Boolean> deadContains = new ArrayList<>();
                for(int i = 0; i < 16; i++)
                {
                    deadContains.add(true);
                }
                contained = true;
                for (int x = 0; x < 6 && contained; x++)
                    for (int y = 0; y < HEIGHT && contained; y++)
                        for (int z = 0; z < 6 && contained; z++)
                        {
                            //if(deadEnd.get(d)[x][y][z] == cube[x][y][z] || deadEnd.get(d)[x][y][z] != -1) cube is in dead
                            if(cube[x][y][z] != -1)
                            {
                                empty = false;
                                if (deadEnd.get(d)[x][y][z] != cube[x][y][z])// cube is not in dead
                                    deadContains.set(0, false);
                                if (!switch (cube[x]                           [y]                           [-(z - cube[x][y].length + 1)]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 11;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })//xy : 00
                                    deadContains.set(1, false);
                                if (!switch (cube[x]                           [-(y - cube[x].length + 1)]   [z]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 9;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })                           //xy : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                                    deadContains.set(2, false);
                                if (!switch (cube[x]                           [-(y - cube[x].length + 1)]   [-(z - cube[x][y].length + 1)]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 9;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })//00 : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                                    deadContains.set(3, false);
                                /*perms.get(0)[x][y][z] = switch (cube[x]                       [z]                       [y]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 10-> 6;
                                    case 11-> 7;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //xy : xxx
                                perms.get(0)[x][y][z] = switch (cube[x]                       [z]                       [-(y-cube[x].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 10-> 6;
                                    case 11-> 5;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };   //00 : x,    yxz
                                perms.get(0)[x][y][z] = switch (cube[x]                       [-(z-cube[x][y].length+1)][y]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 10-> 6;
                                    case 11-> 7;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //00 : xxx
                                perms.get(0)[x][y][z] = switch (cube[x]                       [-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 10-> 6;
                                    case 11-> 5;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };   //xy : x,    yxz*/
                                if (!switch (cube[-(x - cube.length + 1)]      [y]                           [z]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 11;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })                           //xy : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                                    deadContains.set(4, false);
                                if (!switch (cube[-(x - cube.length + 1)]      [y]                           [-(z - cube[x][y].length + 1)]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 11;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })//00 : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                                    deadContains.set(5, false);
                                if (!switch (cube[-(x - cube.length + 1)]      [-(y - cube[x].length + 1)]   [z]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 9;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })                           //00 : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                                    deadContains.set(6, false);
                                if (!switch (cube[-(x - cube.length + 1)]      [-(y - cube[x].length + 1)]   [-(z - cube[x][y].length + 1)]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 9;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })//xy : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                                    deadContains.set(7, false);
                                /*perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [z]                       [y]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 10-> 4;
                                    case 11-> 7;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //00 : xyy,  yzy,  zxy,  zyz,  zzx
                                perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [z]                       [-(y-cube[x].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 10-> 4;
                                    case 11-> 5;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };   //xy : xzz,  yyx
                                perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [-(z-cube[x][y].length+1)][y]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 10-> 4;
                                    case 11-> 7;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //xy : xyy,  yzy,  zxy,  zyz,  zzx
                                perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 10-> 4;
                                    case 11-> 5;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };   //00 : xzz,  yyx
                                perms.get(0)[x][y][z] = switch (cube[y]                       [x]                       [z]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 10-> 9;
                                    case 11-> 8;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //xy : xxz,  zyy
                                perms.get(0)[x][y][z] = switch (cube[y]                       [x]                       [-(z-cube[x][y].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 10-> 9;
                                    case 11-> 8;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };//00 : xxz,  zyy
                                perms.get(0)[x][y][z] = switch (cube[y]                       [-(x-cube.length+1)]      [z]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 10-> 11;
                                    case 11-> 8;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //00 : zzz
                                perms.get(0)[x][y][z] = switch (cube[y]                       [-(x-cube.length+1)]      [-(z-cube[x][y].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 10-> 11;
                                    case 11-> 8;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };//xy : zzz
                                perms.get(0)[x][y][z] = switch (cube[y]                       [z]                       [x]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 10-> 5;
                                    case 11-> 4;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //00 : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
                                perms.get(0)[x][y][z] = switch (cube[y]                       [z]                       [-(x-cube.length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 10-> 7;
                                    case 11-> 4;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };      //xy : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
                                perms.get(0)[x][y][z] = switch (cube[y]                       [-(z-cube[x][y].length+1)][x]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 10-> 5;
                                    case 11-> 4;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //xy : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
                                perms.get(0)[x][y][z] = switch (cube[y]                       [-(z-cube[x][y].length+1)][-(x-cube.length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 10-> 7;
                                    case 11-> 4;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };      //00 : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
                                perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [x]                       [z]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 10-> 9;
                                    case 11-> 10;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //00 : xzy,  z
                                perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [x]                       [-(z-cube[x][y].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 10-> 9;
                                    case 11-> 10;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };//xy : xzy,  z
                                perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(x-cube.length+1)]      [z]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 10-> 11;
                                    case 11-> 10;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //xy : xyx,  yxy,  yyz,  yzx,  zxx
                                perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(x-cube.length+1)]      [-(z-cube[x][y].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 10-> 11;
                                    case 11-> 10;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };//00 : xyx,  yxy,  yyz,  yzx,  zxx
                                perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [z]                       [x]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 10-> 5;
                                    case 11-> 6;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //xy : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
                                perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [z]                       [-(x-cube.length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 10-> 7;
                                    case 11-> 6;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };      //00 : xy,   xzyx, yxzy, yz,   zx,   zyxz
                                perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(z-cube[x][y].length+1)][x]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 10-> 5;
                                    case 11-> 6;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //00 : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
                                perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(z-cube[x][y].length+1)][-(x-cube.length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 10-> 7;
                                    case 11-> 6;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };      //xy : xy,   xzyx, yxzy, yz,   zx,   zyxz
                                perms.get(0)[x][y][z] = switch (cube[z]                       [x]                       [y]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 10-> 0;
                                    case 11-> 3;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //00 : xxxz, xzyy, yxxx, zy,   zzyx
                                perms.get(0)[x][y][z] = switch (cube[z]                       [x]                       [-(y-cube[x].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 10-> 0;
                                    case 11-> 1;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };   //xy : xxzy, xz,   yxzz, yyyx, zyyy
                                perms.get(0)[x][y][z] = switch (cube[z]                       [-(x-cube.length+1)]      [y]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 10-> 2;
                                    case 11-> 3;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //xy : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz
                                perms.get(0)[x][y][z] = switch (cube[z]                       [-(x-cube.length+1)]      [-(y-cube[x].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 10-> 2;
                                    case 11-> 1;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };   //00 : xzzz, yx,   yyxz, zyxx, zzzy*/
                                if (!switch (cube[z]                           [y]                           [x]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 2;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })                           //xy : yyy
                                    deadContains.set(8, false);
                                if (!switch (cube[z]                           [y]                           [-(x - cube.length + 1)]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 2;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })      //00 : y,    zyx
                                    deadContains.set(9, false);
                                if (!switch (cube[z]                           [-(y - cube[x].length + 1)]   [x]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 0;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })                           //00 : yxx,  zzy
                                    deadContains.set(10, false);
                                if (!switch (cube[z]                           [-(y - cube[x].length + 1)]   [-(x - cube.length + 1)]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 0;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })      //xy : xxy,  xyz,  xzx,  yzz,  zxz
                                    deadContains.set(11, false);
                                /*perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][x]                       [y]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 10-> 0;
                                    case 11-> 3;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //xy : xxxz, xzyy, yxxx, zy,   zzyx
                                perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][x]                       [-(y-cube[x].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 10-> 0;
                                    case 11-> 1;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };   //00 : xxzy, xz,   yxzz, yyyx, zyyy
                                perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(x-cube.length+1)]      [-(y-cube[x].length+1)]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 10-> 2;
                                    case 11-> 1;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };   //xy : xzzz, yx,   yyxz, zyxx, zzzy
                                perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(x-cube.length+1)]      [y]){
                                    case 0 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 10-> 2;
                                    case 11-> 3;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                };                       //00 : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz*/
                                if (!switch (cube[-(z - cube[x][y].length + 1)][y]                           [x]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 2;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })                           //00 : yyy
                                    deadContains.set(12, false);
                                if (!switch (cube[-(z - cube[x][y].length + 1)][y]                           [-(x - cube.length + 1)]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 0;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 2;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })      //xy : y,    zyx
                                    deadContains.set(13, false);
                                if (!switch (cube[-(z - cube[x][y].length + 1)][-(y - cube[x].length + 1)]   [x]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 0;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })                           //xy : yxx,  zzy
                                    deadContains.set(14, false);
                                if (!switch (cube[-(z - cube[x][y].length + 1)][-(y - cube[x].length + 1)]   [-(x - cube.length + 1)]) {
                                    case 0 -> deadEnd.get(d)[x][y][z] == 11;
                                    case 1 -> deadEnd.get(d)[x][y][z] == 8;
                                    case 2 -> deadEnd.get(d)[x][y][z] == 9;
                                    case 3 -> deadEnd.get(d)[x][y][z] == 10;
                                    case 4 -> deadEnd.get(d)[x][y][z] == 5;
                                    case 5 -> deadEnd.get(d)[x][y][z] == 4;
                                    case 6 -> deadEnd.get(d)[x][y][z] == 7;
                                    case 7 -> deadEnd.get(d)[x][y][z] == 6;
                                    case 8 -> deadEnd.get(d)[x][y][z] == 1;
                                    case 9 -> deadEnd.get(d)[x][y][z] == 2;
                                    case 10 -> deadEnd.get(d)[x][y][z] == 3;
                                    case 11 -> deadEnd.get(d)[x][y][z] == 0;
                                    default -> deadEnd.get(d)[x][y][z] == -1;
                                })      //00 : xxy,  xyz,  xzx,  yzz,  zxz
                                    deadContains.set(15, false);
                                contained = deadContains.contains(true);
                            }
                        }
                if (contained && !empty)
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
            if(!contained && !empty)
            {
                deadEnd.add(cube);
                dead++;
            }
        }
    }
    public boolean deadContains(int[][][] cube)
    {
        ArrayList<Boolean> contains = new ArrayList<>();
        boolean contained = false;
        for (int[][][] d : deadEnd)
        {
            contains.clear();
            for(int i = 0; i < 16; i++)
            {
                contains.add(true);
            }
            contained = true;
            for (int x = 0; x < 6 && contained; x++)
                for (int y = 0; y < HEIGHT && contained; y++)
                    for (int z = 0; z < 6 && contained; z++)
                    {
                        //if(deadEnd.get(d)[x][y][z] == cube[x][y][z] || deadEnd.get(d)[x][y][z] != -1) cube is in dead
                        if(d[x][y][z] != -1)
                        {
                            if (d[x][y][z] != cube[x][y][z])// cube is not in dead
                                contains.set(0, false);
                            if (!switch (cube[x]                           [y]                           [-(z - cube[x][y].length + 1)]) {
                                case 0 -> d[x][y][z] == 0;
                                case 1 -> d[x][y][z] == 3;
                                case 2 -> d[x][y][z] == 2;
                                case 3 -> d[x][y][z] == 1;
                                case 4 -> d[x][y][z] == 4;
                                case 5 -> d[x][y][z] == 7;
                                case 6 -> d[x][y][z] == 6;
                                case 7 -> d[x][y][z] == 5;
                                case 8 -> d[x][y][z] == 8;
                                case 9 -> d[x][y][z] == 9;
                                case 10 -> d[x][y][z] == 10;
                                case 11 -> d[x][y][z] == 11;
                                default -> d[x][y][z] == -1;
                            })//xy : 00
                                contains.set(1, false);
                            if (!switch (cube[x]                           [-(y - cube[x].length + 1)]   [z]) {
                                case 0 -> d[x][y][z] == 2;
                                case 1 -> d[x][y][z] == 1;
                                case 2 -> d[x][y][z] == 0;
                                case 3 -> d[x][y][z] == 3;
                                case 4 -> d[x][y][z] == 4;
                                case 5 -> d[x][y][z] == 5;
                                case 6 -> d[x][y][z] == 6;
                                case 7 -> d[x][y][z] == 7;
                                case 8 -> d[x][y][z] == 8;
                                case 9 -> d[x][y][z] == 11;
                                case 10 -> d[x][y][z] == 10;
                                case 11 -> d[x][y][z] == 9;
                                default -> d[x][y][z] == -1;
                            })                           //xy : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                                contains.set(2, false);
                            if (!switch (cube[x]                           [-(y - cube[x].length + 1)]   [-(z - cube[x][y].length + 1)]) {
                                case 0 -> d[x][y][z] == 2;
                                case 1 -> d[x][y][z] == 3;
                                case 2 -> d[x][y][z] == 0;
                                case 3 -> d[x][y][z] == 1;
                                case 4 -> d[x][y][z] == 4;
                                case 5 -> d[x][y][z] == 7;
                                case 6 -> d[x][y][z] == 6;
                                case 7 -> d[x][y][z] == 5;
                                case 8 -> d[x][y][z] == 8;
                                case 9 -> d[x][y][z] == 11;
                                case 10 -> d[x][y][z] == 10;
                                case 11 -> d[x][y][z] == 9;
                                default -> d[x][y][z] == -1;
                            })//00 : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                                contains.set(3, false);
                            /*perms.get(0)[x][y][z] = switch (cube[x]                       [z]                       [y]){
                                case 0 -> d[x][y][z] == 1;
                                case 1 -> d[x][y][z] == 0;
                                case 2 -> d[x][y][z] == 3;
                                case 3 -> d[x][y][z] == 2;
                                case 4 -> d[x][y][z] == 8;
                                case 5 -> d[x][y][z] == 9;
                                case 6 -> d[x][y][z] == 10;
                                case 7 -> d[x][y][z] == 11;
                                case 8 -> d[x][y][z] == 4;
                                case 9 -> d[x][y][z] == 5;
                                case 10-> 6;
                                case 11-> 7;
                                default -> d[x][y][z] == -1;
                            };                       //xy : xxx
                            perms.get(0)[x][y][z] = switch (cube[x]                       [z]                       [-(y-cube[x].length+1)]){
                                case 0 -> d[x][y][z] == 3;
                                case 1 -> d[x][y][z] == 0;
                                case 2 -> d[x][y][z] == 1;
                                case 3 -> d[x][y][z] == 2;
                                case 4 -> d[x][y][z] == 8;
                                case 5 -> d[x][y][z] == 9;
                                case 6 -> d[x][y][z] == 10;
                                case 7 -> d[x][y][z] == 11;
                                case 8 -> d[x][y][z] == 4;
                                case 9 -> d[x][y][z] == 7;
                                case 10-> 6;
                                case 11-> 5;
                                default -> d[x][y][z] == -1;
                            };   //00 : x,    yxz
                            perms.get(0)[x][y][z] = switch (cube[x]                       [-(z-cube[x][y].length+1)][y]){
                                case 0 -> d[x][y][z] == 1;
                                case 1 -> d[x][y][z] == 2;
                                case 2 -> d[x][y][z] == 3;
                                case 3 -> d[x][y][z] == 0;
                                case 4 -> d[x][y][z] == 8;
                                case 5 -> d[x][y][z] == 11;
                                case 6 -> d[x][y][z] == 10;
                                case 7 -> d[x][y][z] == 9;
                                case 8 -> d[x][y][z] == 4;
                                case 9 -> d[x][y][z] == 5;
                                case 10-> 6;
                                case 11-> 7;
                                default -> d[x][y][z] == -1;
                            };                       //00 : xxx
                            perms.get(0)[x][y][z] = switch (cube[x]                       [-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]){
                                case 0 -> d[x][y][z] == 3;
                                case 1 -> d[x][y][z] == 2;
                                case 2 -> d[x][y][z] == 1;
                                case 3 -> d[x][y][z] == 0;
                                case 4 -> d[x][y][z] == 8;
                                case 5 -> d[x][y][z] == 11;
                                case 6 -> d[x][y][z] == 10;
                                case 7 -> d[x][y][z] == 9;
                                case 8 -> d[x][y][z] == 4;
                                case 9 -> d[x][y][z] == 7;
                                case 10-> 6;
                                case 11-> 5;
                                default -> d[x][y][z] == -1;
                            };   //xy : x,    yxz*/
                            if (!switch (cube[-(x - cube.length + 1)]      [y]                           [z]) {
                                case 0 -> d[x][y][z] == 0;
                                case 1 -> d[x][y][z] == 1;
                                case 2 -> d[x][y][z] == 2;
                                case 3 -> d[x][y][z] == 3;
                                case 4 -> d[x][y][z] == 6;
                                case 5 -> d[x][y][z] == 5;
                                case 6 -> d[x][y][z] == 4;
                                case 7 -> d[x][y][z] == 7;
                                case 8 -> d[x][y][z] == 10;
                                case 9 -> d[x][y][z] == 9;
                                case 10 -> d[x][y][z] == 8;
                                case 11 -> d[x][y][z] == 11;
                                default -> d[x][y][z] == -1;
                            })                           //xy : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                                contains.set(4, false);
                            if (!switch (cube[-(x - cube.length + 1)]      [y]                           [-(z - cube[x][y].length + 1)]) {
                                case 0 -> d[x][y][z] == 0;
                                case 1 -> d[x][y][z] == 3;
                                case 2 -> d[x][y][z] == 2;
                                case 3 -> d[x][y][z] == 1;
                                case 4 -> d[x][y][z] == 6;
                                case 5 -> d[x][y][z] == 7;
                                case 6 -> d[x][y][z] == 4;
                                case 7 -> d[x][y][z] == 5;
                                case 8 -> d[x][y][z] == 10;
                                case 9 -> d[x][y][z] == 9;
                                case 10 -> d[x][y][z] == 8;
                                case 11 -> d[x][y][z] == 11;
                                default -> d[x][y][z] == -1;
                            })//00 : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                                contains.set(5, false);
                            if (!switch (cube[-(x - cube.length + 1)]      [-(y - cube[x].length + 1)]   [z]) {
                                case 0 -> d[x][y][z] == 2;
                                case 1 -> d[x][y][z] == 1;
                                case 2 -> d[x][y][z] == 0;
                                case 3 -> d[x][y][z] == 3;
                                case 4 -> d[x][y][z] == 6;
                                case 5 -> d[x][y][z] == 5;
                                case 6 -> d[x][y][z] == 4;
                                case 7 -> d[x][y][z] == 7;
                                case 8 -> d[x][y][z] == 10;
                                case 9 -> d[x][y][z] == 11;
                                case 10 -> d[x][y][z] == 8;
                                case 11 -> d[x][y][z] == 9;
                                default -> d[x][y][z] == -1;
                            })                           //00 : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                                contains.set(6, false);
                            if (!switch (cube[-(x - cube.length + 1)]      [-(y - cube[x].length + 1)]   [-(z - cube[x][y].length + 1)]) {
                                case 0 -> d[x][y][z] == 2;
                                case 1 -> d[x][y][z] == 3;
                                case 2 -> d[x][y][z] == 0;
                                case 3 -> d[x][y][z] == 1;
                                case 4 -> d[x][y][z] == 6;
                                case 5 -> d[x][y][z] == 7;
                                case 6 -> d[x][y][z] == 4;
                                case 7 -> d[x][y][z] == 5;
                                case 8 -> d[x][y][z] == 10;
                                case 9 -> d[x][y][z] == 11;
                                case 10 -> d[x][y][z] == 8;
                                case 11 -> d[x][y][z] == 9;
                                default -> d[x][y][z] == -1;
                            })//xy : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                                contains.set(7, false);
                            /*perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [z]                       [y]){
                                case 0 -> d[x][y][z] == 1;
                                case 1 -> d[x][y][z] == 0;
                                case 2 -> d[x][y][z] == 3;
                                case 3 -> d[x][y][z] == 2;
                                case 4 -> d[x][y][z] == 10;
                                case 5 -> d[x][y][z] == 9;
                                case 6 -> d[x][y][z] == 8;
                                case 7 -> d[x][y][z] == 11;
                                case 8 -> d[x][y][z] == 6;
                                case 9 -> d[x][y][z] == 5;
                                case 10-> 4;
                                case 11-> 7;
                                default -> d[x][y][z] == -1;
                            };                       //00 : xyy,  yzy,  zxy,  zyz,  zzx
                            perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [z]                       [-(y-cube[x].length+1)]){
                                case 0 -> d[x][y][z] == 3;
                                case 1 -> d[x][y][z] == 0;
                                case 2 -> d[x][y][z] == 1;
                                case 3 -> d[x][y][z] == 2;
                                case 4 -> d[x][y][z] == 10;
                                case 5 -> d[x][y][z] == 9;
                                case 6 -> d[x][y][z] == 8;
                                case 7 -> d[x][y][z] == 11;
                                case 8 -> d[x][y][z] == 6;
                                case 9 -> d[x][y][z] == 7;
                                case 10-> 4;
                                case 11-> 5;
                                default -> d[x][y][z] == -1;
                            };   //xy : xzz,  yyx
                            perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [-(z-cube[x][y].length+1)][y]){
                                case 0 -> d[x][y][z] == 1;
                                case 1 -> d[x][y][z] == 2;
                                case 2 -> d[x][y][z] == 3;
                                case 3 -> d[x][y][z] == 0;
                                case 4 -> d[x][y][z] == 10;
                                case 5 -> d[x][y][z] == 11;
                                case 6 -> d[x][y][z] == 8;
                                case 7 -> d[x][y][z] == 9;
                                case 8 -> d[x][y][z] == 6;
                                case 9 -> d[x][y][z] == 5;
                                case 10-> 4;
                                case 11-> 7;
                                default -> d[x][y][z] == -1;
                            };                       //xy : xyy,  yzy,  zxy,  zyz,  zzx
                            perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]){
                                case 0 -> d[x][y][z] == 3;
                                case 1 -> d[x][y][z] == 2;
                                case 2 -> d[x][y][z] == 1;
                                case 3 -> d[x][y][z] == 0;
                                case 4 -> d[x][y][z] == 10;
                                case 5 -> d[x][y][z] == 11;
                                case 6 -> d[x][y][z] == 8;
                                case 7 -> d[x][y][z] == 9;
                                case 8 -> d[x][y][z] == 6;
                                case 9 -> d[x][y][z] == 7;
                                case 10-> 4;
                                case 11-> 5;
                                default -> d[x][y][z] == -1;
                            };   //00 : xzz,  yyx
                            perms.get(0)[x][y][z] = switch (cube[y]                       [x]                       [z]){
                                case 0 -> d[x][y][z] == 6;
                                case 1 -> d[x][y][z] == 5;
                                case 2 -> d[x][y][z] == 4;
                                case 3 -> d[x][y][z] == 7;
                                case 4 -> d[x][y][z] == 2;
                                case 5 -> d[x][y][z] == 1;
                                case 6 -> d[x][y][z] == 0;
                                case 7 -> d[x][y][z] == 3;
                                case 8 -> d[x][y][z] == 11;
                                case 9 -> d[x][y][z] == 10;
                                case 10-> 9;
                                case 11-> 8;
                                default -> d[x][y][z] == -1;
                            };                       //xy : xxz,  zyy
                            perms.get(0)[x][y][z] = switch (cube[y]                       [x]                       [-(z-cube[x][y].length+1)]){
                                case 0 -> d[x][y][z] == 6;
                                case 1 -> d[x][y][z] == 7;
                                case 2 -> d[x][y][z] == 4;
                                case 3 -> d[x][y][z] == 5;
                                case 4 -> d[x][y][z] == 2;
                                case 5 -> d[x][y][z] == 3;
                                case 6 -> d[x][y][z] == 0;
                                case 7 -> d[x][y][z] == 1;
                                case 8 -> d[x][y][z] == 11;
                                case 9 -> d[x][y][z] == 10;
                                case 10-> 9;
                                case 11-> 8;
                                default -> d[x][y][z] == -1;
                            };//00 : xxz,  zyy
                            perms.get(0)[x][y][z] = switch (cube[y]                       [-(x-cube.length+1)]      [z]){
                                case 0 -> d[x][y][z] == 6;
                                case 1 -> d[x][y][z] == 5;
                                case 2 -> d[x][y][z] == 4;
                                case 3 -> d[x][y][z] == 7;
                                case 4 -> d[x][y][z] == 0;
                                case 5 -> d[x][y][z] == 1;
                                case 6 -> d[x][y][z] == 2;
                                case 7 -> d[x][y][z] == 3;
                                case 8 -> d[x][y][z] == 9;
                                case 9 -> d[x][y][z] == 10;
                                case 10-> 11;
                                case 11-> 8;
                                default -> d[x][y][z] == -1;
                            };                       //00 : zzz
                            perms.get(0)[x][y][z] = switch (cube[y]                       [-(x-cube.length+1)]      [-(z-cube[x][y].length+1)]){
                                case 0 -> d[x][y][z] == 6;
                                case 1 -> d[x][y][z] == 7;
                                case 2 -> d[x][y][z] == 4;
                                case 3 -> d[x][y][z] == 5;
                                case 4 -> d[x][y][z] == 0;
                                case 5 -> d[x][y][z] == 3;
                                case 6 -> d[x][y][z] == 2;
                                case 7 -> d[x][y][z] == 1;
                                case 8 -> d[x][y][z] == 9;
                                case 9 -> d[x][y][z] == 10;
                                case 10-> 11;
                                case 11-> 8;
                                default -> d[x][y][z] == -1;
                            };//xy : zzz
                            perms.get(0)[x][y][z] = switch (cube[y]                       [z]                       [x]){
                                case 0 -> d[x][y][z] == 10;
                                case 1 -> d[x][y][z] == 9;
                                case 2 -> d[x][y][z] == 8;
                                case 3 -> d[x][y][z] == 11;
                                case 4 -> d[x][y][z] == 3;
                                case 5 -> d[x][y][z] == 0;
                                case 6 -> d[x][y][z] == 1;
                                case 7 -> d[x][y][z] == 2;
                                case 8 -> d[x][y][z] == 7;
                                case 9 -> d[x][y][z] == 6;
                                case 10-> 5;
                                case 11-> 4;
                                default -> d[x][y][z] == -1;
                            };                       //00 : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
                            perms.get(0)[x][y][z] = switch (cube[y]                       [z]                       [-(x-cube.length+1)]){
                                case 0 -> d[x][y][z] == 10;
                                case 1 -> d[x][y][z] == 9;
                                case 2 -> d[x][y][z] == 8;
                                case 3 -> d[x][y][z] == 11;
                                case 4 -> d[x][y][z] == 1;
                                case 5 -> d[x][y][z] == 0;
                                case 6 -> d[x][y][z] == 3;
                                case 7 -> d[x][y][z] == 2;
                                case 8 -> d[x][y][z] == 5;
                                case 9 -> d[x][y][z] == 6;
                                case 10-> 7;
                                case 11-> 4;
                                default -> d[x][y][z] == -1;
                            };      //xy : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
                            perms.get(0)[x][y][z] = switch (cube[y]                       [-(z-cube[x][y].length+1)][x]){
                                case 0 -> d[x][y][z] == 10;
                                case 1 -> d[x][y][z] == 11;
                                case 2 -> d[x][y][z] == 8;
                                case 3 -> d[x][y][z] == 9;
                                case 4 -> d[x][y][z] == 3;
                                case 5 -> d[x][y][z] == 2;
                                case 6 -> d[x][y][z] == 1;
                                case 7 -> d[x][y][z] == 0;
                                case 8 -> d[x][y][z] == 7;
                                case 9 -> d[x][y][z] == 6;
                                case 10-> 5;
                                case 11-> 4;
                                default -> d[x][y][z] == -1;
                            };                       //xy : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
                            perms.get(0)[x][y][z] = switch (cube[y]                       [-(z-cube[x][y].length+1)][-(x-cube.length+1)]){
                                case 0 -> d[x][y][z] == 10;
                                case 1 -> d[x][y][z] == 11;
                                case 2 -> d[x][y][z] == 8;
                                case 3 -> d[x][y][z] == 9;
                                case 4 -> d[x][y][z] == 1;
                                case 5 -> d[x][y][z] == 2;
                                case 6 -> d[x][y][z] == 3;
                                case 7 -> d[x][y][z] == 0;
                                case 8 -> d[x][y][z] == 5;
                                case 9 -> d[x][y][z] == 6;
                                case 10-> 7;
                                case 11-> 4;
                                default -> d[x][y][z] == -1;
                            };      //00 : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
                            perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [x]                       [z]){
                                case 0 -> d[x][y][z] == 4;
                                case 1 -> d[x][y][z] == 5;
                                case 2 -> d[x][y][z] == 6;
                                case 3 -> d[x][y][z] == 7;
                                case 4 -> d[x][y][z] == 2;
                                case 5 -> d[x][y][z] == 1;
                                case 6 -> d[x][y][z] == 0;
                                case 7 -> d[x][y][z] == 3;
                                case 8 -> d[x][y][z] == 11;
                                case 9 -> d[x][y][z] == 8;
                                case 10-> 9;
                                case 11-> 10;
                                default -> d[x][y][z] == -1;
                            };                       //00 : xzy,  z
                            perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [x]                       [-(z-cube[x][y].length+1)]){
                                case 0 -> d[x][y][z] == 4;
                                case 1 -> d[x][y][z] == 7;
                                case 2 -> d[x][y][z] == 6;
                                case 3 -> d[x][y][z] == 5;
                                case 4 -> d[x][y][z] == 2;
                                case 5 -> d[x][y][z] == 3;
                                case 6 -> d[x][y][z] == 0;
                                case 7 -> d[x][y][z] == 1;
                                case 8 -> d[x][y][z] == 11;
                                case 9 -> d[x][y][z] == 8;
                                case 10-> 9;
                                case 11-> 10;
                                default -> d[x][y][z] == -1;
                            };//xy : xzy,  z
                            perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(x-cube.length+1)]      [z]){
                                case 0 -> d[x][y][z] == 4;
                                case 1 -> d[x][y][z] == 5;
                                case 2 -> d[x][y][z] == 6;
                                case 3 -> d[x][y][z] == 7;
                                case 4 -> d[x][y][z] == 0;
                                case 5 -> d[x][y][z] == 1;
                                case 6 -> d[x][y][z] == 2;
                                case 7 -> d[x][y][z] == 3;
                                case 8 -> d[x][y][z] == 9;
                                case 9 -> d[x][y][z] == 8;
                                case 10-> 11;
                                case 11-> 10;
                                default -> d[x][y][z] == -1;
                            };                       //xy : xyx,  yxy,  yyz,  yzx,  zxx
                            perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(x-cube.length+1)]      [-(z-cube[x][y].length+1)]){
                                case 0 -> d[x][y][z] == 4;
                                case 1 -> d[x][y][z] == 7;
                                case 2 -> d[x][y][z] == 6;
                                case 3 -> d[x][y][z] == 5;
                                case 4 -> d[x][y][z] == 0;
                                case 5 -> d[x][y][z] == 3;
                                case 6 -> d[x][y][z] == 2;
                                case 7 -> d[x][y][z] == 1;
                                case 8 -> d[x][y][z] == 9;
                                case 9 -> d[x][y][z] == 8;
                                case 10-> 11;
                                case 11-> 10;
                                default -> d[x][y][z] == -1;
                            };//00 : xyx,  yxy,  yyz,  yzx,  zxx
                            perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [z]                       [x]){
                                case 0 -> d[x][y][z] == 8;
                                case 1 -> d[x][y][z] == 9;
                                case 2 -> d[x][y][z] == 10;
                                case 3 -> d[x][y][z] == 11;
                                case 4 -> d[x][y][z] == 3;
                                case 5 -> d[x][y][z] == 0;
                                case 6 -> d[x][y][z] == 1;
                                case 7 -> d[x][y][z] == 2;
                                case 8 -> d[x][y][z] == 7;
                                case 9 -> d[x][y][z] == 4;
                                case 10-> 5;
                                case 11-> 6;
                                default -> d[x][y][z] == -1;
                            };                       //xy : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
                            perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [z]                       [-(x-cube.length+1)]){
                                case 0 -> d[x][y][z] == 8;
                                case 1 -> d[x][y][z] == 9;
                                case 2 -> d[x][y][z] == 10;
                                case 3 -> d[x][y][z] == 11;
                                case 4 -> d[x][y][z] == 1;
                                case 5 -> d[x][y][z] == 0;
                                case 6 -> d[x][y][z] == 3;
                                case 7 -> d[x][y][z] == 2;
                                case 8 -> d[x][y][z] == 5;
                                case 9 -> d[x][y][z] == 4;
                                case 10-> 7;
                                case 11-> 6;
                                default -> d[x][y][z] == -1;
                            };      //00 : xy,   xzyx, yxzy, yz,   zx,   zyxz
                            perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(z-cube[x][y].length+1)][x]){
                                case 0 -> d[x][y][z] == 8;
                                case 1 -> d[x][y][z] == 11;
                                case 2 -> d[x][y][z] == 10;
                                case 3 -> d[x][y][z] == 9;
                                case 4 -> d[x][y][z] == 3;
                                case 5 -> d[x][y][z] == 2;
                                case 6 -> d[x][y][z] == 1;
                                case 7 -> d[x][y][z] == 0;
                                case 8 -> d[x][y][z] == 7;
                                case 9 -> d[x][y][z] == 4;
                                case 10-> 5;
                                case 11-> 6;
                                default -> d[x][y][z] == -1;
                            };                       //00 : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
                            perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]   [-(z-cube[x][y].length+1)][-(x-cube.length+1)]){
                                case 0 -> d[x][y][z] == 8;
                                case 1 -> d[x][y][z] == 11;
                                case 2 -> d[x][y][z] == 10;
                                case 3 -> d[x][y][z] == 9;
                                case 4 -> d[x][y][z] == 1;
                                case 5 -> d[x][y][z] == 2;
                                case 6 -> d[x][y][z] == 3;
                                case 7 -> d[x][y][z] == 0;
                                case 8 -> d[x][y][z] == 5;
                                case 9 -> d[x][y][z] == 4;
                                case 10-> 7;
                                case 11-> 6;
                                default -> d[x][y][z] == -1;
                            };      //xy : xy,   xzyx, yxzy, yz,   zx,   zyxz
                            perms.get(0)[x][y][z] = switch (cube[z]                       [x]                       [y]){
                                case 0 -> d[x][y][z] == 5;
                                case 1 -> d[x][y][z] == 6;
                                case 2 -> d[x][y][z] == 7;
                                case 3 -> d[x][y][z] == 4;
                                case 4 -> d[x][y][z] == 11;
                                case 5 -> d[x][y][z] == 10;
                                case 6 -> d[x][y][z] == 9;
                                case 7 -> d[x][y][z] == 8;
                                case 8 -> d[x][y][z] == 2;
                                case 9 -> d[x][y][z] == 1;
                                case 10-> 0;
                                case 11-> 3;
                                default -> d[x][y][z] == -1;
                            };                       //00 : xxxz, xzyy, yxxx, zy,   zzyx
                            perms.get(0)[x][y][z] = switch (cube[z]                       [x]                       [-(y-cube[x].length+1)]){
                                case 0 -> d[x][y][z] == 7;
                                case 1 -> d[x][y][z] == 6;
                                case 2 -> d[x][y][z] == 5;
                                case 3 -> d[x][y][z] == 4;
                                case 4 -> d[x][y][z] == 11;
                                case 5 -> d[x][y][z] == 10;
                                case 6 -> d[x][y][z] == 9;
                                case 7 -> d[x][y][z] == 8;
                                case 8 -> d[x][y][z] == 2;
                                case 9 -> d[x][y][z] == 3;
                                case 10-> 0;
                                case 11-> 1;
                                default -> d[x][y][z] == -1;
                            };   //xy : xxzy, xz,   yxzz, yyyx, zyyy
                            perms.get(0)[x][y][z] = switch (cube[z]                       [-(x-cube.length+1)]      [y]){
                                case 0 -> d[x][y][z] == 5;
                                case 1 -> d[x][y][z] == 6;
                                case 2 -> d[x][y][z] == 7;
                                case 3 -> d[x][y][z] == 4;
                                case 4 -> d[x][y][z] == 9;
                                case 5 -> d[x][y][z] == 10;
                                case 6 -> d[x][y][z] == 11;
                                case 7 -> d[x][y][z] == 8;
                                case 8 -> d[x][y][z] == 0;
                                case 9 -> d[x][y][z] == 1;
                                case 10-> 2;
                                case 11-> 3;
                                default -> d[x][y][z] == -1;
                            };                       //xy : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz
                            perms.get(0)[x][y][z] = switch (cube[z]                       [-(x-cube.length+1)]      [-(y-cube[x].length+1)]){
                                case 0 -> d[x][y][z] == 7;
                                case 1 -> d[x][y][z] == 6;
                                case 2 -> d[x][y][z] == 5;
                                case 3 -> d[x][y][z] == 4;
                                case 4 -> d[x][y][z] == 9;
                                case 5 -> d[x][y][z] == 10;
                                case 6 -> d[x][y][z] == 11;
                                case 7 -> d[x][y][z] == 8;
                                case 8 -> d[x][y][z] == 0;
                                case 9 -> d[x][y][z] == 3;
                                case 10-> 2;
                                case 11-> 1;
                                default -> d[x][y][z] == -1;
                            };   //00 : xzzz, yx,   yyxz, zyxx, zzzy*/
                            if (!switch (cube[z]                           [y]                           [x]) {
                                case 0 -> d[x][y][z] == 9;
                                case 1 -> d[x][y][z] == 10;
                                case 2 -> d[x][y][z] == 11;
                                case 3 -> d[x][y][z] == 8;
                                case 4 -> d[x][y][z] == 7;
                                case 5 -> d[x][y][z] == 6;
                                case 6 -> d[x][y][z] == 5;
                                case 7 -> d[x][y][z] == 4;
                                case 8 -> d[x][y][z] == 3;
                                case 9 -> d[x][y][z] == 0;
                                case 10 -> d[x][y][z] == 1;
                                case 11 -> d[x][y][z] == 2;
                                default -> d[x][y][z] == -1;
                            })                           //xy : yyy
                                contains.set(8, false);
                            if (!switch (cube[z]                           [y]                           [-(x - cube.length + 1)]) {
                                case 0 -> d[x][y][z] == 9;
                                case 1 -> d[x][y][z] == 10;
                                case 2 -> d[x][y][z] == 11;
                                case 3 -> d[x][y][z] == 8;
                                case 4 -> d[x][y][z] == 5;
                                case 5 -> d[x][y][z] == 6;
                                case 6 -> d[x][y][z] == 7;
                                case 7 -> d[x][y][z] == 4;
                                case 8 -> d[x][y][z] == 1;
                                case 9 -> d[x][y][z] == 0;
                                case 10 -> d[x][y][z] == 3;
                                case 11 -> d[x][y][z] == 2;
                                default -> d[x][y][z] == -1;
                            })      //00 : y,    zyx
                                contains.set(9, false);
                            if (!switch (cube[z]                           [-(y - cube[x].length + 1)]   [x]) {
                                case 0 -> d[x][y][z] == 11;
                                case 1 -> d[x][y][z] == 10;
                                case 2 -> d[x][y][z] == 9;
                                case 3 -> d[x][y][z] == 8;
                                case 4 -> d[x][y][z] == 7;
                                case 5 -> d[x][y][z] == 6;
                                case 6 -> d[x][y][z] == 5;
                                case 7 -> d[x][y][z] == 4;
                                case 8 -> d[x][y][z] == 3;
                                case 9 -> d[x][y][z] == 2;
                                case 10 -> d[x][y][z] == 1;
                                case 11 -> d[x][y][z] == 0;
                                default -> d[x][y][z] == -1;
                            })                           //00 : yxx,  zzy
                                contains.set(10, false);
                            if (!switch (cube[z]                           [-(y - cube[x].length + 1)]   [-(x - cube.length + 1)]) {
                                case 0 -> d[x][y][z] == 11;
                                case 1 -> d[x][y][z] == 10;
                                case 2 -> d[x][y][z] == 9;
                                case 3 -> d[x][y][z] == 8;
                                case 4 -> d[x][y][z] == 5;
                                case 5 -> d[x][y][z] == 6;
                                case 6 -> d[x][y][z] == 7;
                                case 7 -> d[x][y][z] == 4;
                                case 8 -> d[x][y][z] == 1;
                                case 9 -> d[x][y][z] == 2;
                                case 10 -> d[x][y][z] == 3;
                                case 11 -> d[x][y][z] == 0;
                                default -> d[x][y][z] == -1;
                            })      //xy : xxy,  xyz,  xzx,  yzz,  zxz
                                contains.set(11, false);
                            /*perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][x]                       [y]){
                                case 0 -> d[x][y][z] == 5;
                                case 1 -> d[x][y][z] == 4;
                                case 2 -> d[x][y][z] == 7;
                                case 3 -> d[x][y][z] == 6;
                                case 4 -> d[x][y][z] == 11;
                                case 5 -> d[x][y][z] == 8;
                                case 6 -> d[x][y][z] == 9;
                                case 7 -> d[x][y][z] == 10;
                                case 8 -> d[x][y][z] == 2;
                                case 9 -> d[x][y][z] == 1;
                                case 10-> 0;
                                case 11-> 3;
                                default -> d[x][y][z] == -1;
                            };                       //xy : xxxz, xzyy, yxxx, zy,   zzyx
                            perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][x]                       [-(y-cube[x].length+1)]){
                                case 0 -> d[x][y][z] == 7;
                                case 1 -> d[x][y][z] == 4;
                                case 2 -> d[x][y][z] == 5;
                                case 3 -> d[x][y][z] == 6;
                                case 4 -> d[x][y][z] == 11;
                                case 5 -> d[x][y][z] == 8;
                                case 6 -> d[x][y][z] == 9;
                                case 7 -> d[x][y][z] == 10;
                                case 8 -> d[x][y][z] == 2;
                                case 9 -> d[x][y][z] == 3;
                                case 10-> 0;
                                case 11-> 1;
                                default -> d[x][y][z] == -1;
                            };   //00 : xxzy, xz,   yxzz, yyyx, zyyy
                            perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(x-cube.length+1)]      [-(y-cube[x].length+1)]){
                                case 0 -> d[x][y][z] == 7;
                                case 1 -> d[x][y][z] == 4;
                                case 2 -> d[x][y][z] == 5;
                                case 3 -> d[x][y][z] == 6;
                                case 4 -> d[x][y][z] == 9;
                                case 5 -> d[x][y][z] == 8;
                                case 6 -> d[x][y][z] == 11;
                                case 7 -> d[x][y][z] == 10;
                                case 8 -> d[x][y][z] == 0;
                                case 9 -> d[x][y][z] == 3;
                                case 10-> 2;
                                case 11-> 1;
                                default -> d[x][y][z] == -1;
                            };   //xy : xzzz, yx,   yyxz, zyxx, zzzy
                            perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(x-cube.length+1)]      [y]){
                                case 0 -> d[x][y][z] == 5;
                                case 1 -> d[x][y][z] == 4;
                                case 2 -> d[x][y][z] == 7;
                                case 3 -> d[x][y][z] == 6;
                                case 4 -> d[x][y][z] == 9;
                                case 5 -> d[x][y][z] == 8;
                                case 6 -> d[x][y][z] == 11;
                                case 7 -> d[x][y][z] == 10;
                                case 8 -> d[x][y][z] == 0;
                                case 9 -> d[x][y][z] == 1;
                                case 10-> 2;
                                case 11-> 3;
                                default -> d[x][y][z] == -1;
                            };                       //00 : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz*/
                            if (!switch (cube[-(z - cube[x][y].length + 1)][y]                           [x]) {
                                case 0 -> d[x][y][z] == 9;
                                case 1 -> d[x][y][z] == 8;
                                case 2 -> d[x][y][z] == 11;
                                case 3 -> d[x][y][z] == 10;
                                case 4 -> d[x][y][z] == 7;
                                case 5 -> d[x][y][z] == 4;
                                case 6 -> d[x][y][z] == 5;
                                case 7 -> d[x][y][z] == 6;
                                case 8 -> d[x][y][z] == 3;
                                case 9 -> d[x][y][z] == 0;
                                case 10 -> d[x][y][z] == 1;
                                case 11 -> d[x][y][z] == 2;
                                default -> d[x][y][z] == -1;
                            })                           //00 : yyy
                                contains.set(12, false);
                            if (!switch (cube[-(z - cube[x][y].length + 1)][y]                           [-(x - cube.length + 1)]) {
                                case 0 -> d[x][y][z] == 9;
                                case 1 -> d[x][y][z] == 8;
                                case 2 -> d[x][y][z] == 11;
                                case 3 -> d[x][y][z] == 10;
                                case 4 -> d[x][y][z] == 5;
                                case 5 -> d[x][y][z] == 4;
                                case 6 -> d[x][y][z] == 7;
                                case 7 -> d[x][y][z] == 6;
                                case 8 -> d[x][y][z] == 1;
                                case 9 -> d[x][y][z] == 0;
                                case 10 -> d[x][y][z] == 3;
                                case 11 -> d[x][y][z] == 2;
                                default -> d[x][y][z] == -1;
                            })      //xy : y,    zyx
                                contains.set(13, false);
                            if (!switch (cube[-(z - cube[x][y].length + 1)][-(y - cube[x].length + 1)]   [x]) {
                                case 0 -> d[x][y][z] == 11;
                                case 1 -> d[x][y][z] == 8;
                                case 2 -> d[x][y][z] == 9;
                                case 3 -> d[x][y][z] == 10;
                                case 4 -> d[x][y][z] == 7;
                                case 5 -> d[x][y][z] == 4;
                                case 6 -> d[x][y][z] == 5;
                                case 7 -> d[x][y][z] == 6;
                                case 8 -> d[x][y][z] == 3;
                                case 9 -> d[x][y][z] == 2;
                                case 10 -> d[x][y][z] == 1;
                                case 11 -> d[x][y][z] == 0;
                                default -> d[x][y][z] == -1;
                            })                           //xy : yxx,  zzy
                                contains.set(14, false);
                            if (!switch (cube[-(z - cube[x][y].length + 1)][-(y - cube[x].length + 1)]   [-(x - cube.length + 1)]) {
                                case 0 -> d[x][y][z] == 11;
                                case 1 -> d[x][y][z] == 8;
                                case 2 -> d[x][y][z] == 9;
                                case 3 -> d[x][y][z] == 10;
                                case 4 -> d[x][y][z] == 5;
                                case 5 -> d[x][y][z] == 4;
                                case 6 -> d[x][y][z] == 7;
                                case 7 -> d[x][y][z] == 6;
                                case 8 -> d[x][y][z] == 1;
                                case 9 -> d[x][y][z] == 2;
                                case 10 -> d[x][y][z] == 3;
                                case 11 -> d[x][y][z] == 0;
                                default -> d[x][y][z] == -1;
                            })      //00 : xxy,  xyz,  xzx,  yzz,  zxz
                                contains.set(15, false);
                            contained = contains.contains(true);
                        }
                    }
            if(contained)
            {
                kill++;
                break;
            }
        }
        return contained;
    }
}
