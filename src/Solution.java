import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Solution
{
    private int[][][] cube;
    private ArrayList<Point> cubeFrontier, cubeExplored;
    public final int[][][] orientations =
            {
                    //          x1                        x2                        x3                         x4
                    {{1,0,0},{2,0,0},{1,1,0}},{{1,0,0},{2,0,0},{1,0,1}},{{1,0,0},{2,0,0},{1,-1,0}},{{1,0,0},{2,0,0},{1,0,-1}},
                    //          y1                        y2                        y3                         y4
                    {{0,1,0},{0,2,0},{1,1,0}},{{0,1,0},{0,2,0},{0,1,1}},{{0,1,0},{0,2,0},{-1,1,0}},{{0,1,0},{0,2,0},{0,1,-1}},
                    //          z1                        z2                        z3                         z4
                    {{0,0,1},{0,0,2},{1,0,1}},{{0,0,1},{0,0,2},{0,1,1}},{{0,0,1},{0,0,2},{-1,0,1}},{{0,0,1},{0,0,2},{0,-1,1}},
                    //          xc1                       xc2                       xc3                        xc4
                    {{-1,-1,0},{0,-1,0},{1,-1,0}},{{-1,0,-1},{0,0,-1},{1,0,-1}},{{-1,1,0},{0,1,0},{1,1,0}},{{-1,0,1},{0,0,1},{1,0,1}},
                    //          yc1                       yc2                       yc3                        yc4
                    {{-1,-1,0},{-1,0,0},{-1,1,0}},{{0,-1,-1},{0,0,-1},{0,1,-1}},{{1,-1,0},{1,0,0},{1,1,0}},{{0,-1,1},{0,0,1},{0,1,1}},
                    //          zc1                       zc2                       zc3                        zc4
                    {{-1,0,-1},{-1,0,0},{-1,0,1}},{{0,-1,-1},{0,-1,0},{0,-1,1}},{{1,0,-1},{1,0,0},{1,0,1}},{{0,1,-1},{0,1,0},{0,1,1}}
            };
    public Solution(int[][][] c)
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
    }
    public Solution(ArrayList<Point> front, ArrayList<Point> expl, int[][][] c)
    {
        cube = new int [6][c[0].length][6];
        for(int x = 0; x < c.length; x++)
        {
            for(int y = 0; y < c[x].length; y++)
            {
                for(int z = 0; z < c[x][y].length; z++)
                {
                    cube[x][y][z] = c[x][y][z];
                }
            }
        }
        cubeFrontier = new ArrayList<>();
        cubeFrontier.addAll(front);
        cubeExplored = new ArrayList<>();
        cubeExplored.addAll(expl);
    }
    public int addT(Point point, int  ori) throws IOException
    {
        int orientation = ori;
        Main.print("addT "+ point + "\n");
        for(int i = orientation+1; i < orientations.length; i++)
        {
            //Main.print("try orientation: " + i + "\n");
            if(isOpen(orientations[i],point))
            {
                Main.print("in: " + i + "\n");
                cube[point.getX()][point.getY()][point.getZ()] = i;
                Main.print("{"+point.getX() + "," + point.getY() + "," + point.getZ()+"} = "+i + "\n");
                for(int j = 0; j < 3; j++)
                {
                    cube[point.getX() + orientations[i][j][0]][point.getY() + orientations[i][j][1]][point.getZ() + orientations[i][j][2]] = i;
                    Main.print("{"+(point.getX() + orientations[i][j][0])+","+(point.getY() + orientations[i][j][1])+","+(point.getZ() + orientations[i][j][2])+"} = "+i + "\n");
                }
                orientation = i;
                break;
            }
        }
        if(orientation == ori)
        {
            return -2;
        }
        Main.print("found orientation: " + orientation + "\n");
        return orientation;
    }
    public void removeT(Point point,int orientation) throws IOException
    {
        Main.print("removeT "+ point + " orientation " + orientation + "\n");
        cube[point.getX()][point.getY()][point.getZ()] = -1;
        Main.print(point + " = "+-1 + "\n");
        for(int j = 0; j < 3; j++)
        {
            cube[point.getX() + orientations[orientation][j][0]][point.getY() + orientations[orientation][j][1]][point.getZ() + orientations[orientation][j][2]] = -1;
            Main.print("{"+(point.getX() + orientations[orientation][j][0])+","+(point.getY() + orientations[orientation][j][1])+","+(point.getZ() + orientations[orientation][j][2])+"} = "+-1 + "\n");
        }
    }
    public boolean isOpen(int[][] orientation, Point point) throws IOException
    {
        boolean in = true;
        boolean open = true;
        for(int i = 0; i < 3; i++)
        {
            if(orientation[i][0] + point.getX() < 0 || orientation[i][0] + point.getX() >= 6)
            {
                in = false;
            }
            else if(orientation[i][1] + point.getY() < 0 || orientation[i][1] + point.getY() >= cube[0].length)
            {
                in = false;
            }
            else if(orientation[i][2] + point.getZ() < 0 || orientation[i][2] + point.getZ() >= 6)
            {
                in = false;
            }
            if(in)
            {
                if(cube[orientation[i][0] + point.getX()][orientation[i][1] + point.getY()][orientation[i][2] + point.getZ()] != -1)
                {
                    //Main.print("colision at {" + (orientation[i][0] + point.getX()) + ", " + (orientation[i][1] + point.getY()) + ", " + (orientation[i][2] + point.getZ()) + "}\n");
                    open = false;
                    break;
                }
            }
            else
            {
                //Main.print("out at {" + (orientation[i][0] + point.getX()) + ", " + (orientation[i][1] + point.getY()) + ", " + (orientation[i][2] + point.getZ() )+ "}\n");

                break;
            }
        }
        return (open && in);
    }
    public void update(Point point, int orientation) throws IOException
    {
        //get all new explored points
        ArrayList<Point> newExplored = new ArrayList<>();
        newExplored.add(point);
        for(int i = 0; i < orientations[orientation].length; i++)
        {
            newExplored.add(new Point(point.getX() + orientations[orientation][i][0],
                    point.getY() + orientations[orientation][i][1],
                    point.getZ() + orientations[orientation][i][2]));
        }
        Main.print("newExplored :");
        for(Point p :newExplored)
        {
            Main.print(p.toString());
        }
        Main.print("\n");

        updateExplored(newExplored);
        updateFrontier(newExplored);
    }
    public void updateExplored (ArrayList<Point> newExplored) throws IOException
    {
        //add newExplored points to explored

        cubeExplored.addAll(newExplored);
        cubeExplored = (ArrayList<Point>) cubeExplored.stream().distinct().collect(Collectors.toList());
        Main.print("explored + newExplored:");
        for(Point point :cubeExplored)
        {
            Main.print(point.toString());
        }
        Main.print("\n");
    }
    public void updateFrontier (ArrayList<Point> newExplored) throws IOException
    {
        //remove newExplored points from frontier
        cubeFrontier.removeAll(newExplored);

        Main.print("frontier -  newExplored:" + "\n");
        for(Point point :cubeFrontier)
        {
            Main.print(point.toString());
        }
        Main.print("\n");

        //get all possible new frontier points in range
        ArrayList<Point> newFrontier = new ArrayList<>();
        for(Point point : newExplored)
        {
            //if in range add point
            if(point.getX() + 1 >= 0 && point.getX() + 1 < 6)
            {
                newFrontier.add(new Point(point.getX() + 1, point.getY(), point.getZ()));
            }
            if(point.getX() - 1 >= 0 && point.getX() - 1 < 6)
            {
                newFrontier.add(new Point(point.getX() - 1, point.getY(), point.getZ()));
            }
            if(point.getY() + 1 >= 0 && point.getY() + 1 < cube[0].length)
            {
                newFrontier.add(new Point(point.getX(), point.getY() + 1, point.getZ()));
            }
            if(point.getY() - 1 >= 0 && point.getY() - 1 < cube[0].length)
            {
                newFrontier.add(new Point(point.getX(), point.getY() - 1, point.getZ()));
            }
            if(point.getZ() + 1 >= 0 && point.getZ() + 1 < 6)
            {
                newFrontier.add(new Point(point.getX(), point.getY(), point.getZ() + 1));
            }
            if(point.getZ() - 1 >= 0 && point.getZ() - 1 < 6)
            {
                newFrontier.add(new Point(point.getX(), point.getY(), point.getZ() - 1));
            }
        }
        newFrontier = (ArrayList<Point>) newFrontier.stream().distinct().collect(Collectors.toList());
        Main.print("newFrontier:");
        for(Point point :newFrontier)
        {
            Main.print(point.toString());
        }
        Main.print("\n");

        //remove explored points from newFrontier
        newFrontier.removeAll(cubeExplored);

        Main.print("newFrontier - explored:");
        for(Point point :newFrontier)
        {
            Main.print(point.toString());
        }
        Main.print("\n");

        //add newFrontier points to frontier
        cubeFrontier.addAll(newFrontier);
        //cubeFrontier = (ArrayList<Point>) cubeFrontier.stream().distinct().collect(Collectors.toList());
        cubeFrontier = (ArrayList<Point>) cubeFrontier.stream().distinct().sorted().collect(Collectors.toList());
        Main.print("frontier + newFrontier:");
        for(Point point :cubeFrontier)
        {
            Main.print(point.toString());
        }
        Main.print("\n");
    }


    public int[][][] getCube() {
        return cube;
    }
    public ArrayList<Point> getCubeFrontier()
    {
        return (ArrayList<Point>) cubeFrontier.stream().map(p -> new Point(p.getX(), p.getY(), p.getZ())).collect(Collectors.toList());
    }
    public ArrayList<Point> getCubeExplored()
    {
        return (ArrayList<Point>) cubeExplored.stream().map(p -> new Point(p.getX(), p.getY(), p.getZ())).collect(Collectors.toList());
    }

    public void setCubeExplored(ArrayList<Point> cubeExplored)
    {
        this.cubeExplored = (ArrayList<Point>) cubeExplored.stream().map(p -> new Point(p.getX(), p.getY(), p.getZ())).collect(Collectors.toList());
    }
    public void setCubeFrontier(ArrayList<Point> cubeFrontier)
    {
        this.cubeFrontier = (ArrayList<Point>) cubeFrontier.stream().map(p -> new Point(p.getX(), p.getY(), p.getZ())).collect(Collectors.toList());
    }

    
}
