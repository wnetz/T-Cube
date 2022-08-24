import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Solve
{
    public final int PRINT_TO;
    public final int HEIGHT =2;
    ArrayList<int[][][]> deadEnd;
    FileWriter solutionFile;
    FileWriter file;
    long time;

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
        System.out.println("dead:" + deadEnd.size());
        solutionFile.close();
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
        System.out.print("########################################################\n\n");
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
            file.write("########################################################\n\n");
        else
            System.out.print("########################################################\n\n");
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
        for(int i = 0; i < 56; i++)
        {
            perms.add(new int[cube.length][cube[0].length][cube[0][0].length]);
        }
        for (int x = 0; x < cube.length; x++)
            for (int y = 0; y < cube[x].length; y++)
                for (int z = 0; z < cube[x][y].length; z++)
                {
                    perms.get(0)[x][y][z] =         cube[x]                       [y]                       [z];                             //00 : 00
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
                    };   //00 : x
                    perms.get(0)[x][y][z] = switch (cube[x]                       [-(y-cube[x].length+1)]   [-(z-cube[x][y].length+1)]){
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
                    };//00 : xx
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
                    perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]   [-(x-cube.length+1)]){
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
                    };      //00 : xxy
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
                    };//00 : xxz
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
                    };      //00 : xy
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
                    };//00 : xyx
                    perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [z]                       [y]){
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
                    };                       //00 : xyy
                    perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]   [-(x-cube.length+1)]){
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
                    };      //00 : xyz
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
                    };   //00 : xz
                    perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]   [-(x-cube.length+1)]){
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
                    };      //00 : xzx
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
                    };                       //00 : xzy
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
                    };   //00 : xzz
                    perms.get(0)[x][y][z] = switch (cube[z]                       [y]                       [-(x-cube.length+1)]){
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
                    };      //00 : y
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
                    };   //00 : yx
                    perms.get(0)[x][y][z] = switch (cube[z]                       [-(y-cube[x].length+1)]   [x]){
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
                    };                       //00 : yxx
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
                    };//00 : yxy
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
                    };   //00 : yxz
                    perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [y]                       [-(z-cube[x][y].length+1)]){
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
                    };//00 : yy
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
                    };   //00 : yyx
                    perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][y]                       [x]){
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
                    };//00 : yyz
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
                    };      //00 : yz
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
                    };//00 : yzx
                    perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [z]                       [y]){
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
                    };                       //00 : yzy
                    perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]   [-(x-cube.length+1)]){
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
                    };      //00 : yzz
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
                    };//00 : z
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
                    };      //00 : zx
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
                    };//00 : zxx
                    perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [z]                       [y]){
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
                    };                       //00 : zxy
                    perms.get(0)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)]   [-(x-cube.length+1)]){
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
                    };      //00 : zxz
                    perms.get(0)[x][y][z] = switch (cube[z]                       [x]                       [z]){
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
                    };                       //00 : zy
                    perms.get(0)[x][y][z] = switch (cube[z]                       [y]                       [-(x-cube.length+1)]){
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
                    };      //00 : zyx
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
                    };//00 : zyy
                    perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]      [z]                       [y]){
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
                    };//00 : zyz
                    //00 : zz
                    //00 : zzx
                    //00 : zzy
                    //00 : zzz

                    /*//xy : 00
                    //xy : x
                    //xy : xx
                    //xy : xxx
                    //xy : xxy
                    //xy : xxz
                    //xy : xy
                    //xy : xyx
                    //xy : xyy
                    //xy : xyz
                    //xy : xz
                    //xy : xzx
                    //xy : xzy
                    //xy : xzz
                    //xy : y
                    //xy : yx
                    //xy : yxx
                    //xy : yxy
                    //xy : yxz
                    //xy : yy
                    //xy : yyx
                    //xy : yyy
                    //xy : yyz
                    //xy : yz
                    //xy : yzx
                    //xy : yzy
                    //xy : yzz
                    //xy : z
                    //xy : zx
                    //xy : zxx
                    //xy : zxy
                    //xy : zxz
                    //xy : zy
                    //xy : zyx
                    //xy : zyy
                    //xy : zyz
                    //xy : zz
                    //xy : zzx
                    //xy : zzy
                    //xy : zzz*/

                    /*//xz : 00
                    //xz : x
                    //xz : xx
                    //xz : xxx
                    //xz : xxy
                    //xz : xxz
                    //xz : xy
                    //xz : xyx
                    //xz : xyy
                    //xz : xyz
                    //xz : xz
                    //xz : xzx
                    //xz : xzy
                    //xz : xzz
                    //xz : y
                    //xz : yx
                    //xz : yxx
                    //xz : yxy
                    //xz : yxz
                    //xz : yy
                    //xz : yyx
                    //xz : yyy
                    //xz : yyz
                    //xz : yz
                    //xz : yzx
                    //xz : yzy
                    //xz : yzz
                    //xz : z
                    //xz : zx
                    //xz : zxx
                    //xz : zxy
                    //xz : zxz
                    //xz : zy
                    //xz : zyx
                    //xz : zyy
                    //xz : zyz
                    //xz : zz
                    //xz : zzx
                    //xz : zzy
                    //xz : zzz*/

                    /*//yz : 00
                    //yz : x
                    //yz : xx
                    //yz : xxx
                    //yz : xxy
                    //yz : xxz
                    //yz : xy
                    //yz : xyx
                    //yz : xyy
                    //yz : xyz
                    //yz : xz
                    //yz : xzx
                    //yz : xzy
                    //yz : xzz
                    //yz : y
                    //yz : yx
                    //yz : yxx
                    //yz : yxy
                    //yz : yxz
                    //yz : yy
                    //yz : yyx
                    //yz : yyy
                    //yz : yyz
                    //yz : yz
                    //yz : yzx
                    //yz : yzy
                    //yz : yzz
                    //yz : z
                    //yz : zx
                    //yz : zxx
                    //yz : zxy
                    //yz : zxz
                    //yz : zy
                    //yz : zyx
                    //yz : zyy
                    //yz : zyz
                    //yz : zz
                    //yz : zzx
                    //yz : zzy
                    //yz : zzz*/

                    /*//xy,xz : 00
                    //xy,xz : x
                    //xy,xz : xx
                    //xy,xz : xxx
                    //xy,xz : xxy
                    //xy,xz : xxz
                    //xy,xz : xy
                    //xy,xz : xyx
                    //xy,xz : xyy
                    //xy,xz : xyz
                    //xy,xz : xz
                    //xy,xz : xzx
                    //xy,xz : xzy
                    //xy,xz : xzz
                    //xy,xz : y
                    //xy,xz : yx
                    //xy,xz : yxx
                    //xy,xz : yxy
                    //xy,xz : yxz
                    //xy,xz : yy
                    //xy,xz : yyx
                    //xy,xz : yyy
                    //xy,xz : yyz
                    //xy,xz : yz
                    //xy,xz : yzx
                    //xy,xz : yzy
                    //xy,xz : yzz
                    //xy,xz : z
                    //xy,xz : zx
                    //xy,xz : zxx
                    //xy,xz : zxy
                    //xy,xz : zxz
                    //xy,xz : zy
                    //xy,xz : zyx
                    //xy,xz : zyy
                    //xy,xz : zyz
                    //xy,xz : zz
                    //xy,xz : zzx
                    //xy,xz : zzy
                    //xy,xz : zzz*/

                    /*//xz,yz : 00
                    //xz,yz : x
                    //xz,yz : xx
                    //xz,yz : xxx
                    //xz,yz : xxy
                    //xz,yz : xxz
                    //xz,yz : xy
                    //xz,yz : xyx
                    //xz,yz : xyy
                    //xz,yz : xyz
                    //xz,yz : xz
                    //xz,yz : xzx
                    //xz,yz : xzy
                    //xz,yz : xzz
                    //xz,yz : y
                    //xz,yz : yx
                    //xz,yz : yxx
                    //xz,yz : yxy
                    //xz,yz : yxz
                    //xz,yz : yy
                    //xz,yz : yyx
                    //xz,yz : yyy
                    //xz,yz : yyz
                    //xz,yz : yz
                    //xz,yz : yzx
                    //xz,yz : yzy
                    //xz,yz : yzz
                    //xz,yz : z
                    //xz,yz : zx
                    //xz,yz : zxx
                    //xz,yz : zxy
                    //xz,yz : zxz
                    //xz,yz : zy
                    //xz,yz : zyx
                    //xz,yz : zyy
                    //xz,yz : zyz
                    //xz,yz : zz
                    //xz,yz : zzx
                    //xz,yz : zzy
                    //xz,yz : zzz*/

                    /*//yz,xy : 00
                    //yz,xy : x
                    //yz,xy : xx
                    //yz,xy : xxx
                    //yz,xy : xxy
                    //yz,xy : xxz
                    //yz,xy : xy
                    //yz,xy : xyx
                    //yz,xy : xyy
                    //yz,xy : xyz
                    //yz,xy : xz
                    //yz,xy : xzx
                    //yz,xy : xzy
                    //yz,xy : xzz
                    //yz,xy : y
                    //yz,xy : yx
                    //yz,xy : yxx
                    //yz,xy : yxy
                    //yz,xy : yxz
                    //yz,xy : yy
                    //yz,xy : yyx
                    //yz,xy : yyy
                    //yz,xy : yyz
                    //yz,xy : yz
                    //yz,xy : yzx
                    //yz,xy : yzy
                    //yz,xy : yzz
                    //yz,xy : z
                    //yz,xy : zx
                    //yz,xy : zxx
                    //yz,xy : zxy
                    //yz,xy : zxz
                    //yz,xy : zy
                    //yz,xy : zyx
                    //yz,xy : zyy
                    //yz,xy : zyz
                    //yz,xy : zz
                    //yz,xy : zzx
                    //yz,xy : zzy
                    //yz,xy : zzz*/

                    /*//xy,xz,yz : 00
                    //xy,xz,yz : x
                    //xy,xz,yz : xx
                    //xy,xz,yz : xxx
                    //xy,xz,yz : xxy
                    //xy,xz,yz : xxz
                    //xy,xz,yz : xy
                    //xy,xz,yz : xyx
                    //xy,xz,yz : xyy
                    //xy,xz,yz : xyz
                    //xy,xz,yz : xz
                    //xy,xz,yz : xzx
                    //xy,xz,yz : xzy
                    //xy,xz,yz : xzz
                    //xy,xz,yz : y
                    //xy,xz,yz : yx
                    //xy,xz,yz : yxx
                    //xy,xz,yz : yxy
                    //xy,xz,yz : yxz
                    //xy,xz,yz : yy
                    //xy,xz,yz : yyx
                    //xy,xz,yz : yyy
                    //xy,xz,yz : yyz
                    //xy,xz,yz : yz
                    //xy,xz,yz : yzx
                    //xy,xz,yz : yzy
                    //xy,xz,yz : yzz
                    //xy,xz,yz : z
                    //xy,xz,yz : zx
                    //xy,xz,yz : zxx
                    //xy,xz,yz : zxy
                    //xy,xz,yz : zxz
                    //xy,xz,yz : zy
                    //xy,xz,yz : zyx
                    //xy,xz,yz : zyy
                    //xy,xz,yz : zyz
                    //xy,xz,yz : zz
                    //xy,xz,yz : zzx
                    //xy,xz,yz : zzy
                    //xy,xz,yz : zzz*/
                    perms.get(48)[x][y][z] = switch (cube[x][-(y-cube[x].length+1)][-(z-cube[x][y].length+1)]){
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
                    };//00:xx
                    perms.get(54)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)][-(x-cube.length+1)]){
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
                    };//00:6r:xxy

                    perms.get(41)[x][y][z] =switch (cube[-(x-cube.length+1)][y][-(z-cube[x][y].length+1)]){
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
                    };//00:yy
                    perms.get(35)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][y][x]){
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
                    };//00:yyy
                    perms.get(37)[x][y][z] =switch (cube[-(x-cube.length+1)][-(y-cube[x].length+1)][z]){
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
                    };//00:6u:zz
                    perms.get(28)[x][y][z] = switch (cube[z][-(y-cube[x].length+1)][x]){
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
                    };//00:6l:zzy

                    perms.get(3)[x][y][z] = switch (cube[x][y][-(z-cube[x][y].length+1)]){
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
                    };//xy:00

                    perms.get(1)[x][y][z] = switch (cube[-(x-cube.length+1)][y][z]){
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
                    };//yz:00
                    perms.get(2)[x][y][z] = switch (cube[x][-(y-cube[x].length+1)][z]){
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
                    };//xz:00
                    perms.get(4)[x][y][z] = switch (cube[-(x-cube.length+1)][-(y-cube[x].length+1)][z]){
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
                    };//xz,yz:00
                    perms.get(5)[x][y][z] = switch (cube[-(x-cube.length+1)][y][-(z-cube[x][y].length+1)]){
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
                    };//yz,xy:00
                    perms.get(6)[x][y][z] = switch (cube[x][-(y-cube[x].length+1)][-(z-cube[x][y].length+1)]){
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
                    };//xy,xz:00


                    perms.get(10)[x][y][z] =switch (cube[z][-(y-cube[x].length+1)][x]){
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
                    };//xy,xz:1l:y
                    perms.get(11)[x][y][z] =switch (cube[-(z-cube[x][y].length+1)][y][x]){
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
                    };//yz,xy:1l:y
                    perms.get(12)[x][y][z] =switch (cube[z][-(y-cube[x].length+1)][-(x-cube.length+1)]){
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
                    };//xz:1l:y
                    perms.get(13)[x][y][z] =switch (cube[-(z-cube[x][y].length+1)][y][-(x-cube.length+1)]){
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
                    };//yz:1l:y
                    perms.get(14)[x][y][z] =switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)][x]){
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
                    };//xz:1r:yyy
                    perms.get(15)[x][y][z] =switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)][-(x-cube.length+1)]){
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
                    };//xz,yz:1l:y


                    /*perms.get(16)[x][y][z] = switch (cube[x][y][z]){
                        case 0 -> 0;
                        case 1 -> 1;
                        case 2 -> 2;
                        case 3 -> 3;
                        case 4 -> 4;
                        case 5 -> 5;
                        case 6 -> 6;
                        case 7 -> 7;
                        case 8 -> 8;
                        case 9 -> 9;
                        case 10-> 10;
                        case 11-> 11;
                        default -> -1;
                    };//yz,xy:1d:yy
                    perms.get(17)[x][y][z] = switch (cube[x][y][z]){
                        case 0 -> 0;
                        case 1 -> 1;
                        case 2 -> 2;
                        case 3 -> 3;
                        case 4 -> 4;
                        case 5 -> 5;
                        case 6 -> 6;
                        case 7 -> 7;
                        case 8 -> 8;
                        case 9 -> 9;
                        case 10-> 10;
                        case 11-> 11;
                        default -> -1;
                    };//xz,yz:6u:zz
                    perms.get(18)[x][y][z] = switch (cube[x][y][z]){
                        case 0 -> 0;
                        case 1 -> 1;
                        case 2 -> 2;
                        case 3 -> 3;
                        case 4 -> 4;
                        case 5 -> 5;
                        case 6 -> 6;
                        case 7 -> 7;
                        case 8 -> 8;
                        case 9 -> 9;
                        case 10-> 10;
                        case 11-> 11;
                        default -> -1;
                    };//xy,xz:6d:xx
                    perms.get(19)[x][y][z] = switch (cube[z][y][x]){
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
                    };//yz:1r:yyy
                    perms.get(20)[x][y][z] = switch (cube[z][y][x]){
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
                    };//xz:6l:zzy
                    perms.get(21)[x][y][z] = switch (cube[-(x-cube.length+1)][y][z]){
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
                    };//xy:1d:yy
                    perms.get(22)[x][y][z] = switch (cube[-(x-cube.length+1)][y][z]){
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
                    };//xz:6u:zz
                    perms.get(23)[x][y][z] = switch (cube[z][y][-(x-cube.length+1)]){
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
                    };//xy,xz:6l:zzy
                    perms.get(24)[x][y][z] = switch (cube[z][y][-(x-cube.length+1)]){
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
                    };//xz,yz:6r:xxy
                    perms.get(25)[x][y][z] = switch (cube[z][y][-(x-cube.length+1)]){
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
                    };//yz,xy:1r:yyy
                    perms.get(26)[x][y][z] = switch (cube[x][-(y-cube[x].length+1)][z]){
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
                    };//yz:6u:zz
                    perms.get(27)[x][y][z] = switch (cube[x][-(y-cube[x].length+1)][z]){
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
                    };//xy:6d:xx
                    perms.get(29)[x][y][z] = switch (cube[z][-(y-cube[x].length+1)][x]){
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
                    };//yz,xy:6r:xxy
                    perms.get(30)[x][y][z] = switch (cube[z][-(y-cube[x].length+1)][x]){
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
                    };//xz,yz:1r:yyy
                    perms.get(31)[x][y][z] = switch (cube[x][y][-(z-cube[x][y].length+1)]){
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
                    };//yz:1d:yy
                    perms.get(32)[x][y][z] = switch (cube[x][y][-(z-cube[x][y].length+1)]){
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
                    };//xz:6d:xx
                    perms.get(33)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][y][x]){
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
                    };//xz,yz:6l:zzy
                    perms.get(34)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][y][x]){
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
                    };//xy,xz:6r:xxy
                    perms.get(36)[x][y][z] = switch (cube[-(x-cube.length+1)][-(y-cube[x].length+1)][z]){
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
                    };//xy,xz:1d:yy
                    perms.get(38)[x][y][z] = switch (cube[-(x-cube.length+1)][-(y-cube[x].length+1)][z]){
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
                    };//yz,xy:6d:xx
                    perms.get(39)[x][y][z] = switch (cube[z][-(y-cube[x].length+1)][-(x-cube.length+1)]){
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
                    };//xy:6l:zzy
                    perms.get(40)[x][y][z] = switch (cube[z][-(y-cube[x].length+1)][-(x-cube.length+1)]){
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
                    };//yz:6r:xxy
                    perms.get(42)[x][y][z] = switch (cube[-(x-cube.length+1)][y][-(z-cube[x][y].length+1)]){
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
                    };//xy,xz:6u:zz
                    perms.get(43)[x][y][z] = switch (cube[-(x-cube.length+1)][y][-(z-cube[x][y].length+1)]){
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
                    };//xz,yz:6d:xx
                    perms.get(44)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][y][-(x-cube.length+1)]){
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
                    };//xy:1r:yyy
                    perms.get(45)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][y][-(x-cube.length+1)]){
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
                    };//xz:6r:xxy
                    perms.get(46)[x][y][z] = switch (cube[x][-(y-cube[x].length+1)][-(z-cube[x][y].length+1)]){
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
                    };//xz,yz:1d:yy
                    perms.get(47)[x][y][z] = switch (cube[x][-(y-cube[x].length+1)][-(z-cube[x][y].length+1)]){
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
                    };//yz,xy:6u:zz
                    perms.get(49)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)][x]){
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
                    };//yz:6l:zzy
                    perms.get(50)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)][x]){
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
                    };//xy:6r:xxy
                    perms.get(51)[x][y][z] = switch (cube[-(x-cube.length+1)][-(y-cube[x].length+1)][-(z-cube[x][y].length+1)]){
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
                    };//xy:6u:zz
                    perms.get(52)[x][y][z] = switch (cube[-(x-cube.length+1)][-(y-cube[x].length+1)][-(z-cube[x][y].length+1)]){
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
                    };//yz:6d:xx
                    perms.get(53)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)][-(x-cube.length+1)]){
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
                    };//yz,xy:6l:zzy
                    perms.get(55)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)][-(y-cube[x].length+1)][-(x-cube.length+1)]){
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
                    };//xy,xz:1r:yyy
                    */
                    /*perms.get(0)[x][y][z] = switch (cube[x]                         [z]                         [-(y-cube[x].length+1)]){
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
                    };                         //00:2u:x
                    perms.get(0)[x][y][z] = switch (cube[x]                         [-(z-cube[x][y].length+1)]                         [y]){
                        case 0 -> 1;
                        case 1 -> 2;
                        case 2 -> 3;
                        case 3 -> 0;
                        case 4 -> 9;
                        case 5 -> 10;
                        case 6 -> 11;
                        case 7 -> 8;
                        case 8 -> 5;
                        case 9 -> 4;
                        case 10-> 7;
                        case 11-> 6;
                        default -> -1;
                    };                         //00:5d:xxx

                    perms.get(0)[x][y][z] = switch (cube[y]                         [-(x-cube.length+1)]                         [z]){
                        case 0 -> 4;
                        case 1 -> 5;
                        case 2 -> 6;
                        case 3 -> 7;
                        case 4 -> 2;
                        case 5 -> 1;
                        case 6 -> 0;
                        case 7 -> 3;
                        case 8 -> 9;
                        case 9 -> 10;
                        case 10-> 11;
                        case 11-> 8;
                        default -> -1;
                    };                         //00:3l:z
                    perms.get(0)[x][y][z] = switch (cube[-(y-cube[x].length+1)]                         [x]                         [z]){
                        case 0 -> 6;
                        case 1 -> 5;
                        case 2 -> 4;
                        case 3 -> 7;
                        case 4 -> 0;
                        case 5 -> 1;
                        case 6 -> 2;
                        case 7 -> 3;
                        case 8 -> 11;
                        case 9 -> 8;
                        case 10-> 9;
                        case 11-> 10;
                        default -> -1;
                    };                         //00:4r:zzz*/

                    /*
                    2r = xyyy
                    2d = xyy
                    2l = xy
                    3u = yyyx
                    3r = xxz
                    3d = xxxz
                    4u = zzzy
                    4d = xyxy
                    4l = xyx
                    5u = yyx
                    5r = xxxy
                    5l = xyxx*/


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
                //if a T fits
                if (orientation != -2)
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
                        if(solutions.size()%1000 ==0)
                        {

                            System.out.println("solutions " + solutions.size() + " " + (System.nanoTime()-time)/60000000000.0);
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
        if(deadEnd.size() == 0)
        {
            deadEnd.add(cube);
        }
        else
        {
            boolean contained = false;
            for (int d = 0; d < deadEnd.size(); d++)
            {

                boolean deadContains = true;
                for (int x = 0; x < 6 && deadContains; x++)
                    for (int y = 0; y < HEIGHT && deadContains; y++)
                        for (int z = 0; z < 6; z++)
                        {
                            //if(deadEnd.get(d)[x][y][z] == cube[x][y][z] || deadEnd.get(d)[x][y][z] != -1) cube is in dead
                            if (deadEnd.get(d)[x][y][z] != cube[x][y][z] && cube[x][y][z] != -1)// cube is not in dead
                            {
                                deadContains = false;
                                break;
                            }
                        }
                if (deadContains)
                {
                    contained = true;
                    if (!added)
                    {
                        deadEnd.set(d, cube);
                        added = true;
                    }
                    else
                    {
                        deadEnd.remove(d);
                        d--;
                    }
                }
            }
            if(!contained)
            {
                deadEnd.add(cube);
            }
        }
    }
}
