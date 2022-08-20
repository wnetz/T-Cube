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
            //ArrayList<int[][][]> perms = getPerms(solutions.get(i));
            for(int r = 0; r < 5; r++)
            {
                cube = flipCube(solutions.get(i),r);
                int[][][] x1 = rotateCube(cube, 0);
                int[][][] x2 = rotateCube(x1, 0);
                int[][][] x3 = rotateCube(x2, 0);
                for (int j = i + 1; j < solutions.size(); j++)
                {
                    if (cubeCopy(cube, solutions.get(j))||cubeCopy(x1, solutions.get(j))
                    ||cubeCopy(x2, solutions.get(j))||cubeCopy(x3, solutions.get(j)))
                    {
                        solutions.remove(i);
                        removed++;
                        i--;
                        r=7;
                        break;
                    }
                }
            }
        }
        System.out.println("copies removed: " + removed);
        for(int i = 0; i < solutions.size(); i++)
            printCube(cube,solutionFile);
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
        for(int i = 0; i < 7*4; i++)
        {
            perms.add(new int[cube.length][cube[0].length][cube[0][0].length]);
        }
        for (int x = 0; x < cube.length; x++)
            for (int y = 0; y < cube[x].length; y++)
                for (int z = 0; z < cube[x][y].length; z++)
                {
                    perms.get(0)[x][y][z] =         cube[x]                         [y]                         [z];                         //00:1u
                  /*perms.get(0)[x][y][z] = switch (cube[x]                         [z]                         [-(y-cube[x].length+1)]){
                        case 0 -> 3;
                        case 1 -> 0;
                        case 2 -> 1;
                        case 3 -> 2;
                        case 4 -> 9;
                        case 5 -> 8;
                        case 6 -> 11;
                        case 7 -> 10;
                        case 8 -> 7;
                        case 9 -> 4;
                        case 10-> 5;
                        case 11-> 6;
                        default -> -1;
                    };                         //00:2u:x
                    perms.get(0)[x][y][z] = switch (cube[x]                         [-(y-cube[x].length+1)]                         [-(z-cube[x][y].length+1)]){
                        case 0 -> 2;
                        case 1 -> 3;
                        case 2 -> 0;
                        case 3 -> 1;
                        case 4 -> 4;
                        case 5 -> 7;
                        case 6 -> 6;
                        case 7 -> 5;
                        case 8 -> 10;
                        case 9 -> 9;
                        case 10-> 8;
                        case 11-> 11;
                        default -> -1;
                    };                         //00:6d:xx
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
                    };                         //00:5d:xxx*/
                    perms.get(1)[x][y][z] = switch (cube[z]                         [y]                         [-(x-cube.length+1)]){
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
                    };         //00:1l:y
                    perms.get(2)[x][y][z] = switch (cube[-(x-cube.length+1)]        [y]                         [-(z-cube[x][y].length+1)]){
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
                    };   //00:1d:yy
                    perms.get(3)[x][y][z] = switch (cube[-(z-cube[x][y].length+1)]  [y]                         [x]){
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
                    };                          //00:1r:yyy
                    /*perms.get(0)[x][y][z] = switch (cube[y]                         [-(x-cube.length+1)]                         [z]){
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
                    perms.get(0)[x][y][z] = switch (cube[-(x-cube.length+1)]                         [-(y-cube[x].length+1)]                         [z]){
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
                    };                         //00:6u:zz
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
                    5l = xyxx
                    6r = xyz
                    6l = zzy*/
                    perms.get(1)[x][y][z] = switch (cube[x]                 [y]                    [-(z-cube[x][y].length+1)])//xy
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
                    perms.get(1)[x][y][z] = switch (cube[x]                 [-(y-cube[x].length+1)][z])//xz
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
                    perms.get(1)[x][y][z] = switch (cube[-(x-cube.length+1)][y]                    [z])//yz
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
                    perms.get(1)[x][y][z] = switch (cube[x]                 [-(y-cube[x].length+1)][-(z-cube[x][y].length+1)])//xy,xz
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
