import java.util.ArrayList;
import java.util.stream.Collectors;

public class SolutionState
{
    private int[][][] cube;
    private ArrayList<Point> cubeFrontier, cubeExplored, lastFrontier, lastExplored;
    public final int[][][] orientations =
            {
                    //             x1:0                               x2:1                               x3:2                               x4:3
                    {{ 1, 0, 0},{ 2, 0, 0},{ 1, 1, 0}},{{ 1, 0, 0},{ 2, 0, 0},{ 1, 0, 1}},{{ 1, 0, 0},{ 2, 0, 0},{ 1,-1, 0}},{{ 1, 0, 0},{ 2, 0, 0},{ 1, 0,-1}},
                    //             y1:4                               y2:5                               y3:6                               y4:7
                    {{ 0, 1, 0},{ 0, 2, 0},{ 1, 1, 0}},{{ 0, 1, 0},{ 0, 2, 0},{ 0, 1, 1}},{{ 0, 1, 0},{ 0, 2, 0},{-1, 1, 0}},{{ 0, 1, 0},{ 0, 2, 0},{0 , 1,-1}},
                    //             z1:8                               z2:9                               z3:10                              z4:11
                    {{ 0, 0, 1},{ 0, 0, 2},{ 1, 0, 1}},{{ 0, 0, 1},{ 0, 0, 2},{ 0, 1, 1}},{{ 0, 0, 1},{ 0, 0, 2},{-1, 0, 1}},{{ 0, 0, 1},{ 0, 0, 2},{ 0,-1, 1}},
                    //            xh1:12                             xh2:13                             xh3:14                             xh4:15
                    {{-1,-1, 0},{ 0,-1, 0},{ 1,-1, 0}},{{-1, 0,-1},{ 0, 0,-1},{ 1, 0,-1}},{{-1, 1, 0},{ 0, 1, 0},{ 1, 1, 0}},{{-1, 0, 1},{ 0, 0, 1},{ 1, 0, 1}},
                    //            yh1:16                             yh2:17                             yh3:18                             yh4:19
                    {{-1,-1, 0},{-1, 0, 0},{-1, 1, 0}},{{ 0,-1,-1},{ 0, 0,-1},{0 , 1,-1}},{{ 1,-1, 0},{ 1, 0, 0},{ 1, 1, 0}},{{ 0,-1, 1},{ 0, 0, 1},{ 0, 1, 1}},
                    //            zh1:20                             zh2:21                             zh3:22                             zh4:23
                    {{-1, 0,-1},{-1, 0, 0},{-1, 0, 1}},{{ 0,-1,-1},{ 0,-1, 0},{ 0,-1, 1}},{{ 1, 0,-1},{ 1, 0, 0},{ 1, 0, 1}},{{0 , 1,-1},{ 0, 1, 0},{ 0, 1, 1}},
                    //            x12:24                             x22:25                             x32:26                             x42:27
                    {{-1, 0, 0},{-2, 0, 0},{-1, 1, 0}},{{-1, 0, 0},{-2, 0, 0},{-1, 0, 1}},{{-1, 0, 0},{-2, 0, 0},{-1,-1, 0}},{{-1, 0, 0},{-2, 0, 0},{-1, 0,-1}},
                    //            y12:28                             y22:29                             y32:30                             y42:31
                    {{ 0,-1, 0},{ 0,-2, 0},{ 1,-1, 0}},{{ 0,-1, 0},{ 0,-2, 0},{ 0,-1, 1}},{{ 0,-1, 0},{ 0,-2, 0},{-1,-1, 0}},{{ 0,-1, 0},{ 0,-2, 0},{ 0,-1,-1}},
                    //            z12:32                             z22:33                             z32:34                             z42:35
                    {{ 0, 0,-1},{ 0, 0,-2},{ 1, 0,-1}},{{ 0, 0,-1},{ 0, 0,-2},{ 0, 1,-1}},{{ 0, 0,-1},{ 0, 0,-2},{-1, 0,-1}},{{ 0, 0,-1},{ 0, 0,-2},{ 0,-1,-1}},
                    //            xc1:36                             xc2:37                             xc3:38                             xc4:39
                    {{-1, 0, 0},{ 0, 1, 0},{ 1, 0, 0}},{{-1, 0, 0},{ 0, 0, 1},{ 1, 0, 0}},{{-1, 0, 0},{ 0,-1, 0},{ 1, 0, 0}},{{-1, 0, 0},{ 0, 0,-1},{ 1, 0, 0}},
                    //            yc1:40                             yc2:41                             yc3:42                             yc4:43
                    {{ 0,-1, 0},{ 1, 0, 0},{ 0, 1, 0}},{{ 0,-1, 0},{ 0, 0, 1},{ 0, 1, 0}},{{ 0,-1, 0},{-1, 0, 0},{ 0, 1, 0}},{{ 0,-1, 0},{ 0, 0,-1},{ 0, 1, 0}},
                    //            zc1:44                             zc2:45                             zc3:46                             zc4:47
                    {{ 0, 0,-1},{ 1, 0, 0},{ 0, 0, 1}},{{ 0, 0,-1},{ 0, 1, 0},{ 0, 0, 1}},{{ 0, 0,-1},{-1, 0, 0},{ 0, 0, 1}},{{ 0, 0,-1},{ 0,-1, 0},{ 0, 0, 1}},
            };
    public SolutionState(int[][][] c)
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
        cubeFrontier.add(new Point(0,0,0));
        cubeExplored = new ArrayList<>();
        lastFrontier = new ArrayList<>();
        lastExplored = new ArrayList<>();
    }
    public SolutionState(ArrayList<Point> front, ArrayList<Point> explored, int[][][] c)
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
    }
    public int addT(Point point, int  ori)
    {
        int orientation = ori;
        ArrayList<Point> points;
        //Main.print("addT "+ point + "\n");
        for(int i = orientation+1; i < orientations.length; i++)
        {
            ////Main.print("try orientation: " + i + "\n");
            points = isOpen(orientations[i],point);
            if(points != null)
            {
                //Main.print("in: " + i + "\n");
                for(Point p: points)
                {
                    cube[p.x()][p.y()][p.z()] = i;
                    //Main.print("{"+p.getX() + "," + p.getY() + "," + p.getZ()+"} = "+i + "\n");
                }

                //Main.print("found orientation: " + i + "\n");
                updateExplored(points);
                updateFrontier(points);
                if(problems())
                {
                    removeT(points);
                    cubeExplored.clear();
                    cubeExplored.addAll(lastExplored);
                    cubeFrontier.clear();
                    cubeFrontier.addAll(lastFrontier);
                }
                else
                {
                    orientation = i;
                    break;
                }
            }
        }
        if(orientation == ori)
        {
            return -2;
        }

        return orientation;
    }
    public void removeT(Point point,int orientation)
    {
        //Main.print("removeT "+ point + " orientation " + orientation + "\n");
        cube[point.x()][point.y()][point.z()] = -1;
        //Main.print(point + " = "+-1 + "\n");
        for(int j = 0; j < 3; j++)
        {
            cube[point.x() + orientations[orientation][j][0]][point.y() + orientations[orientation][j][1]][point.z() + orientations[orientation][j][2]] = -1;
            //Main.print("{"+(point.getX() + orientations[orientation][j][0])+","+(point.getY() + orientations[orientation][j][1])+","+(point.getZ() + orientations[orientation][j][2])+"} = "+-1 + "\n");
        }
    }
    public void removeT(ArrayList<Point> points)
    {
        //Main.print("removeT "+ points + "\n");
        for(Point point: points)
        {
            cube[point.x()][point.y()][point.z()] = -1;
            //Main.print(point + " = "+-1 + "\n");
        }
    }
    public ArrayList<Point> isOpen(int[][] orientation, Point point)
    {
        ArrayList<Point> points = new ArrayList<>();
        points.add(point);
        for(int i = 0; i < 3; i++)
        {
            if(orientation[i][0] + point.x() < 0 || orientation[i][0] + point.x() >= 6
            || orientation[i][1] + point.y() < 0 || orientation[i][1] + point.y() >= cube[0].length
            || orientation[i][2] + point.z() < 0 || orientation[i][2] + point.z() >= 6
            || cube[orientation[i][0] + point.x()][orientation[i][1] + point.y()][orientation[i][2] + point.z()] != -1)
            {
                return null;
            }
            points.add(new Point(orientation[i][0] + point.x(),orientation[i][1] + point.y(),orientation[i][2] + point.z()));
        }
        return points;
    }
    public boolean isOpen(Point point)
    {
        for (int[][] orientation : orientations)
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
        //add newExplored points to explored
        lastExplored.clear();
        lastExplored.addAll(cubeExplored);
        cubeExplored.addAll(newExplored);
        cubeExplored = (ArrayList<Point>) cubeExplored.stream().distinct().collect(Collectors.toList());
        /*Main.print("explored + newExplored:");
        for(Point point :cubeExplored)
        {
            Main.print(point.toString());
        }
        Main.print("\n");*/
    }
    public ArrayList<Point> updateFrontier (ArrayList<Point> newExplored)
    {
        lastFrontier.clear();
        lastFrontier.addAll(cubeFrontier);
        //remove newExplored points from frontier
        /*cubeFrontier.removeAll(newExplored);

        //Main.print("frontier -  newExplored:" + "\n");
        for(Point point :cubeFrontier)
        {
            //Main.print(point.toString());
        }
        //Main.print("\n");*/

        //get all possible new frontier points in range
        ArrayList<Point> newFrontier = new ArrayList<>();
        for(Point point : newExplored)
        {
            //if in range add point
            if(point.x() + 1 >= 0 && point.x() + 1 < 6)
            {
                newFrontier.add(new Point(point.x() + 1, point.y(), point.z()));
            }
            if(point.x() - 1 >= 0 && point.x() - 1 < 6)
            {
                newFrontier.add(new Point(point.x() - 1, point.y(), point.z()));
            }
            if(point.y() + 1 >= 0 && point.y() + 1 < cube[0].length)
            {
                newFrontier.add(new Point(point.x(), point.y() + 1, point.z()));
            }
            if(point.y() - 1 >= 0 && point.y() - 1 < cube[0].length)
            {
                newFrontier.add(new Point(point.x(), point.y() - 1, point.z()));
            }
            if(point.z() + 1 >= 0 && point.z() + 1 < 6)
            {
                newFrontier.add(new Point(point.x(), point.y(), point.z() + 1));
            }
            if(point.z() - 1 >= 0 && point.z() - 1 < 6)
            {
                newFrontier.add(new Point(point.x(), point.y(), point.z() - 1));
            }
        }
        newFrontier = (ArrayList<Point>) newFrontier.stream().distinct().collect(Collectors.toList());
        /*//Main.print("newFrontier:");
        for(Point point :newFrontier)
        {
            //Main.print(point.toString());
        }
        //Main.print("\n");*/

        //remove explored points from newFrontier

        /*//Main.print("newFrontier - explored:");
        for(Point point :newFrontier)
        {
            //Main.print(point.toString());
        }
        //Main.print("\n");*/

        //add newFrontier points to frontier
        newFrontier.removeAll(cubeExplored);
        cubeFrontier.addAll(newFrontier);
        cubeFrontier.removeAll(cubeExplored);
        //cubeFrontier = (ArrayList<Point>) cubeFrontier.stream().distinct().collect(Collectors.toList());
        cubeFrontier = (ArrayList<Point>) cubeFrontier.stream().distinct().sorted().collect(Collectors.toList());
        /*Main.print("frontier + newFrontier:");
        for(Point point :cubeFrontier)
        {
            Main.print(point.toString());
        }
        Main.print("\n");*/
        return newFrontier;
    }
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
