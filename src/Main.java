import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class Main
{
    public static final int PRINT_TO = 1;
    public static final int HEIGHT = 2;

    static FileWriter file;
    static FileWriter solutionFile;
    public static void main(String[] args) throws IOException 
    {
        int[][][] cube = new int [6][HEIGHT][6];
        file = new FileWriter("output2.txt");
        solutionFile = new FileWriter("solutions.txt");
        System.out.println("start");
        for(int x = 0; x < 6; x++)
            for(int y = 0; y < HEIGHT; y++)
                for(int z = 0; z < 6; z++)
                    cube[x][y][z] = -1;
        long time = System.nanoTime();
        recursiveSolve(new Solution(cube),0);
        System.out.println((System.nanoTime()-time)/1000000000.0 );
        file.close();
        solutionFile.close();
    }

    public static void print(String string) throws IOException
    {
        switch (PRINT_TO)
        {
            case 0 -> System.out.print(string);
            case 1 -> file.write(string);
            default -> {}
        }
    }

    public static void printCube(int[][][] cube, FileWriter file) throws IOException
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


    public static Point recursiveSolve(Solution solution, int depth) throws IOException
    {
        int rotation = 0;

        ArrayList<Point> oldCubeFrontier = solution.getCubeFrontier();
        ArrayList<Point> oldCubeExplored = solution.getCubeExplored();

        print("depth: " + depth + "\n");
        //try every point in the frontier
        for(Point point : oldCubeFrontier)
        {
            print("rotation: " + rotation++ + "\n");

            int orientation = -1;
            //try every orientation on point
            while (orientation != -2)
            {
                orientation = solution.addT(point, orientation);

                print("orientation: " + orientation + "\n");
                //if a T fits
                if (orientation != -2)
                {
                    solution.update(point,orientation);
                    printCube(solution.getCube(),file);

                    if (solution.getCubeFrontier().size() > 0)
                    {
                        Point result = recursiveSolve(new Solution(solution.getCubeFrontier(), solution.getCubeExplored(), solution.getCube()), depth + 1);

                        solution.removeT(point, orientation);
                        solution.setCubeFrontier(oldCubeFrontier);
                        solution.setCubeExplored(oldCubeExplored);
                        if(result!=null)
                        {
                            int temp = solution.addT(result, -1);
                            //return until problem solved
                            if(temp  == -2)
                            {
                                return result;
                            }
                            else
                            {
                                solution.removeT(result, temp);
                                solution.setCubeFrontier(oldCubeFrontier);
                                solution.setCubeExplored(oldCubeExplored);
                                printCube(solution.getCube(),file);
                            }
                        }
                        else
                        {
                            print("out depth: " + depth + "\n");
                        }
                    }
                    else
                    {
                        printCube(solution.getCube(),solutionFile);
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
