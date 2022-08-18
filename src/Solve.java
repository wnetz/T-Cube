import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Solve
{
    public final int PRINT_TO;
    public final int HEIGHT = 2;

    FileWriter solutionFile;
    FileWriter file;
    int fail = 0;
    long time;

    static ArrayList<int[][][]> solutions;
    
    public Solve(int p, FileWriter f) throws IOException
    {
        solutionFile = new FileWriter("solutions.txt");
        PRINT_TO = p;
        file = f;
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

        System.out.println("converting ");
        for(int i = 0; i < solutions.size(); i++)
            for (int x = 0; x < 6; x++)
                for (int y = 0; y < HEIGHT; y++)
                    for (int z = 0; z < 6; z++)
                        solutions.get(i)[x][y][z] = solutions.get(i)[x][y][z]%12;

        System.out.println("checking copies");
        int removed = 0;
        for(int i = 0; i < solutions.size(); i++)
        {
            int[][][] x1 = rotateCube(solutions.get(i), "x");
            int[][][] x2 = rotateCube(x1, "x");
            int[][][] x3 = rotateCube(x2, "x");
            for (int j = i + 1; j < solutions.size(); j++)
            {
                if (cubeCopy(solutions.get(i), solutions.get(j)))
                {
                    //printCube(solutions.get(i));
                    solutions.remove(i);
                    removed++;
                    i--;
                    break;
                }
                else if(cubeCopy(x1, solutions.get(j)))
                {
                    //System.out.println("x1");
                    //printCube(x1);
                    solutions.remove(i);
                    removed++;
                    i--;
                    break;
                }
                else if(cubeCopy(x2, solutions.get(j)))
                {
                    //System.out.println("x2");
                    //printCube(x2);
                    solutions.remove(i);
                    removed++;
                    i--;
                    break;
                }
                else if(cubeCopy(x3, solutions.get(j)))
                {
                    //System.out.println("x3");
                    //printCube(x3);
                    solutions.remove(i);
                    removed++;
                    i--;
                    break;
                }
            }
        }
        System.out.println(removed);
        for(int i = 0; i < solutions.size(); i++)
            printCube(solutions.get(i),solutionFile);
        solutionFile.close();
    }



    public  void printCube(int[][][] cube)
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
            System.out.print(layer + "\n");
        }
        System.out.print("########################################################" + "\n\n");
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
            file.write("########################################################" + "\n\n");
        else
            System.out.print("########################################################" + "\n\n");
    }
    public  boolean cubeCopy(int[][][] c1, int[][][] c2)
    {
        boolean same = true;
        for (int x = 0; x < 6 && same; x++)
            for (int y = 0; y < HEIGHT && same; y++)
                for (int z = 0; z < 6 && same; z++)
                    if (c1[x][y][z] != c2[x][y][z])
                        same = false;
        return same;
    }
    public  int[][][] rotateCube(int[][][] cube, String axis)
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
                                case "x" -> switch (cube[z][y][-x+cube.length-1])
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


    public  Point recursiveSolve(SolutionState solution, int depth) throws IOException
    {
        int rotation = 0;

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
                        /*if(result!=null)
                        {
                            //return until problem solved
                            if(!solution.isOpen(result))
                            {
                                return result;
                            }
                            else
                            {
                                printCube(solution.getCube(),file);
                                if(++fail%500000 == 0)
                                System.out.println(fail/500000+ " "+ (System.nanoTime()-time)/60000000000.0 );
                            }
                        }
                        else
                        {
                            //Main.print("out depth: " + depth + "\n");
                        }*/
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
                    return point;
                }
            }
        }
        return null;
    }
}
