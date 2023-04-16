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
        ROTATIONS = Rotations.read_qube_rotations(Main.CUBE_ROTATION_FILE).size();
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
                        //System.out.println(c);
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
        System.out.println("dead: " + dead_cubes.size() + " " + dead);
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
        for(int i = 0; i < ROTATIONS; i++)
        {
            perms.add(new int[Main.WIDTH+1][Main.HEIGHT+1][Main.DEPTH+1]);
            Point rotation = (Point)Piece.rotations.keySet().toArray()[i];
            for (int x = 0; x <= Main.WIDTH; x++)
                for (int y = 0; y <= Main.HEIGHT; y++)
                    for (int z = 0; z <= Main.DEPTH; z++) {
                        Point rotated = new Point(x, y, z).rotate(rotation);
                        if(rotated.x()>=0 && rotated.x()<=Main.WIDTH && rotated.y()>=0 && rotated.y()<=Main.HEIGHT && rotated.z()>=0 && rotated.z()<=Main.DEPTH)
                            perms.get(perms.size()-1)[rotated.x()][rotated.y()][rotated.z()] = Piece.rotations.get(rotation).get(cube[x][y][z]);
                        else
                        {
                            perms.remove(perms.size()-1);
                            z = Main.DEPTH;
                            y = Main.HEIGHT;
                            x = Main.WIDTH;
                        }
                    }
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
            while (orientation != Main.ORIENTATION_FAIL)
            {
                orientation = solution.add_piece(point, orientation);
                //if piece added and solution already explored
                if(orientation != Main.ORIENTATION_FAIL && is_explored(solution.getTCube()))
                {
                    solution.remove_piece(point, orientation);
                    //TODO check if last's in solutionState can be used instead
                    solution.setCubeFrontier(oldCubeFrontier);
                    solution.setCubeExplored(oldCubeExplored);
                }
                //if a piece was added
                else if (orientation != Main.ORIENTATION_FAIL )
                {
                    //if there is still space
                    if (solution.getCubeFrontier().size() > 0)
                    {
                        //start next set
                        recursiveSolve(new SolutionState(solution.getCubeFrontier(), solution.getCubeExplored(), solution.getCube(), solution.getTCube()), depth + 1);
                        int size = solution.getTCube().size();
                        double v = Math.log(size) / Math.log(2);
                        if(size == 1 || (int)(Math.ceil(v)) == (int)(Math.floor(v)))
                        {
                            ArrayList<Piece> copy = new ArrayList<>();
                            copy.addAll(solution.getTCube());
                            addDead(copy);
                        }
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
                    addDead(solution.getTCube());
                    return;
                }
            }
        }
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
        for(int i = 0; i< ROTATIONS;i++)
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
