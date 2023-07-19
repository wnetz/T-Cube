import java.util.ArrayList;
import java.util.stream.Collectors;

public class SolutionState
{
    private static int HEIGHT = Main.HEIGHT + 1;
    private static final int WIDTH = Main.WIDTH + 1;
    private static final int DEPTH = Main.DEPTH + 1;

    private int[][][] cube;
    private ArrayList<Point> cubeFrontier, cubeExplored, lastFrontier, lastExplored;
    private ArrayList<Piece> piece_cube;
    public static ArrayList<ArrayList<Point>> ORIENTATIONS;
    public static int ORIENTATIONS_SIZE;//small optimization to reduce calls
    public SolutionState(int[][][] c)
    {
        cube = new int [WIDTH][HEIGHT][DEPTH];
        for(int x = 0; x < WIDTH; x++)
        {
            for(int y = 0; y < HEIGHT; y++)
            {
                System.arraycopy(c[x][y], 0, cube[x][y], 0, DEPTH);
            }
        }
        cubeFrontier = new ArrayList<>();
        cubeFrontier.add(new Point(0,0,0));
        cubeExplored = new ArrayList<>();
        lastFrontier = new ArrayList<>();
        lastExplored = new ArrayList<>();
        piece_cube = new ArrayList<>();
    }
    public SolutionState(ArrayList<Point> front, ArrayList<Point> explored, int[][][] c, ArrayList<Piece> tc)
    {
        cube = new int [6][c[0].length][6];
        for(int x = 0; x < c.length; x++)
        {
            for(int y = 0; y < c[x].length; y++)
            {
                System.arraycopy(c[x][y], 0, cube[x][y], 0, c[x][y].length);
            }
        }
        cubeFrontier = new ArrayList<>();
        cubeFrontier.addAll(front);
        cubeExplored = new ArrayList<>();
        cubeExplored.addAll(explored);
        lastFrontier = new ArrayList<>();
        lastExplored = new ArrayList<>();
        piece_cube = new ArrayList<>();
        piece_cube.addAll(tc);
    }

    /**
     * adds the next possible T if one exists and returns the orientation
     * @param point point to be checked
     * @param ori orientation to start checking at
     * @return Main.ORIENTATION_FAIL if no possible T was found otherwise returns the orientation of the added T
     */
    public int add_piece(Point point, int  ori)
    {
        int orientation = ori;
        ArrayList<Point> points;

        for(int i = orientation+1; i < ORIENTATIONS_SIZE; i++)
        {
            //get open piece
            points = isOpen(ORIENTATIONS.get(i),point);
            if(points != null)
            {
                for(Point p: points)
                {
                    //convert orientation to the base 12 to remove effective duplicates
                    cube[p.x()][p.y()][p.z()] = i%12;
                }

                updateExplored(points);
                updateFrontier(points);
                //TODO i believe this is faster for larger problems, but this needs to be checked
                if(problems())
                {
                    //revert updates
                    remove_piece(point,i);
                    cubeExplored.clear();
                    cubeExplored.addAll(lastExplored);
                    cubeFrontier.clear();
                    cubeFrontier.addAll(lastFrontier);
                }
                else
                {
                    orientation = i;
                    Point addPoint = Point.subtract(point, ORIENTATIONS.get(orientation).get(0));
                    Piece add_piece = new Piece(addPoint,i%12);
                    piece_cube.add(add_piece);
                    break;
                }
            }
        }
        //if no available orientation was found
        if(orientation == ori)
        {
            return Main.ORIENTATION_FAIL;
        }

        return orientation;
    }
    public void remove_piece(Point point, int orientation)
    {
        //cube[point.x()][point.y()][point.z()] = -1;
        piece_cube.remove(new Piece(point,orientation));
        for(int j = 0; j < ORIENTATIONS.get(orientation).size(); j++)
        {
            cube[point.x() + ORIENTATIONS.get(orientation).get(j).x()][point.y() + ORIENTATIONS.get(orientation).get(j).y()][point.z() + ORIENTATIONS.get(orientation).get(j).z()] = -1;
        }
    }

    /**
     * finds is a specific T fits at a specific point
     * @return points that the piece will contain
     */
    public ArrayList<Point> isOpen(ArrayList<Point> orientation, Point point)
    {
        ArrayList<Point> points = new ArrayList<>();
        int o = orientation.size();//small optimization to reduce calls
        for(int i = 0; i < o; i++)// loop on all points in T
        {
            //get point relative to cube instead of piece
            Point test = Point.add(orientation.get(i),point);
            if(test.x() < 0 || test.x() >= WIDTH
            || test.y() < 0 || test.y() >= HEIGHT
            || test.z() < 0 || test.z() >= DEPTH // if point is in cube
            || cube[test.x()][test.y()][test.z()] != -1)// if point is unoccupied
            {
                return null;
            }
            points.add(test);
        }
        return points;
    }

    /**
     * finds if a given point has any possible piece's
     * @return true if there is an available piece
     */
    public boolean isOpen(Point point)
    {
        for (ArrayList<Point> orientation : ORIENTATIONS)
        {
            if (isOpen(orientation, point) != null)
            {
                return true;
            }
        }
        return false;
    }
    public void updateExplored (ArrayList<Point> newExplored)
    {
        //deep copy
        lastExplored.clear();
        lastExplored.addAll(cubeExplored);
        //add newExplored points to explored
        cubeExplored.addAll(newExplored);
        //TODO i think this can go
        cubeExplored = (ArrayList<Point>) cubeExplored.stream().distinct().collect(Collectors.toList());
        /*Main.print("explored + newExplored:");
        for(Point point :cubeExplored)
        {
            Main.print(point.toString());
        }
        Main.print("\n");*/
    }
    public void updateFrontier (ArrayList<Point> newExplored)
    {
        //deep copy
        lastFrontier.clear();
        lastFrontier.addAll(cubeFrontier);

        //get all possible new frontier points in range
        ArrayList<Point> newFrontier = new ArrayList<>();
        for(Point point : newExplored)
        {
            //if in cube add point
            if(point.x() + 1 >= 0 && point.x() + 1 < WIDTH)
            {
                newFrontier.add(new Point(point.x() + 1, point.y(), point.z()));
            }
            if(point.x() - 1 >= 0 && point.x() - 1 < WIDTH)
            {
                newFrontier.add(new Point(point.x() - 1, point.y(), point.z()));
            }
            if(point.y() + 1 >= 0 && point.y() + 1 < HEIGHT)
            {
                newFrontier.add(new Point(point.x(), point.y() + 1, point.z()));
            }
            if(point.y() - 1 >= 0 && point.y() - 1 < HEIGHT)
            {
                newFrontier.add(new Point(point.x(), point.y() - 1, point.z()));
            }
            if(point.z() + 1 >= 0 && point.z() + 1 < DEPTH)
            {
                newFrontier.add(new Point(point.x(), point.y(), point.z() + 1));
            }
            if(point.z() - 1 >= 0 && point.z() - 1 < DEPTH)
            {
                newFrontier.add(new Point(point.x(), point.y(), point.z() - 1));
            }
        }
        newFrontier = (ArrayList<Point>) newFrontier.stream().distinct().collect(Collectors.toList());

        //TODO i think this can just be newExplored
        //remove all explored points
        newFrontier.removeAll(cubeExplored);
        //add newFrontier points to frontier
        cubeFrontier.addAll(newFrontier);
        //TODO should not have to do this twice
        cubeFrontier.removeAll(cubeExplored);
        //TODO test wether sort is faster or not
        //cubeFrontier = (ArrayList<Point>) cubeFrontier.stream().distinct().collect(Collectors.toList());
        cubeFrontier = (ArrayList<Point>) cubeFrontier.stream().distinct().sorted().collect(Collectors.toList());
    }

    /**
     * checks to see if the addition of a T has left a frontier point with no possible T's
     * @return false if all frontier points have a plausible T
     */
    public boolean problems()
    {
        for(Point point: cubeFrontier)
        {
            if(!isOpen(point))
            {
                //Main.print("Problem at " + point + "\n");
                return true;
            }
        }
        return false;
    }

    public int[][][] getCube()
    {
        int[][][] c = new int [cube.length][cube[0].length][cube[0][0].length];
        for(int x = 0; x < cube.length; x++)
        {
            for(int y = 0; y < cube[x].length; y++)
            {
                System.arraycopy(cube[x][y],0,c[x][y],0,cube[x][y].length);
            }
        }
        return c;
    }
    public ArrayList<Piece> getTCube()
    {
        return piece_cube;
    }
    public ArrayList<Point> getCubeFrontier()
    {
        return (ArrayList<Point>) cubeFrontier.stream().map(p -> new Point(p.x(), p.y(), p.z())).collect(Collectors.toList());
    }
    public ArrayList<Point> getCubeExplored()
    {
        return (ArrayList<Point>) cubeExplored.stream().map(p -> new Point(p.x(), p.y(), p.z())).collect(Collectors.toList());
    }

    public void setCubeExplored(ArrayList<Point> cubeExplored)
    {
        this.cubeExplored.clear();
        this.cubeExplored.addAll(cubeExplored);
    }
    public void setCubeFrontier(ArrayList<Point> cubeFrontier)
    {
        this.cubeFrontier.clear();
        this.cubeFrontier.addAll(cubeFrontier);
    }


}
