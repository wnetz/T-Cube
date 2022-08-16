import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Solution
{
    private int cube [][][];
    private ArrayList<Point> cubeFrontier, cubeExplored;
    private FileWriter file;
    public final int orientations [][][] =
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
    public Solution(int  c[][][], FileWriter f)
    {
        file = f;
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
        cubeFrontier.add(new Point(0,0,0));
        cubeExplored = new ArrayList<>();
    }
    public Solution(ArrayList<Point> front, ArrayList<Point> expl, int  c[][][], FileWriter f)
    {
        file = f;
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
        for(Point point: front)
        {
            cubeFrontier.add(point);
        }
        cubeExplored = new ArrayList<>();
        for(Point point: expl)
        {
            cubeExplored.add(point);
        }
    }
    public int addT(Point point, int  ori) throws IOException
    {
        int orientation = ori;
        file.write("addT "+ point + "\n");
        for(int i = orientation+1; i < orientations.length; i++)
        {
            file.write("orientation " + i + "\n");
            if(isOpen(orientations[i],point))
            {
                file.write("in: " + i + "\n");
                cube[point.getX()][point.getY()][point.getZ()] = i;
                file.write("{"+point.getX() + "," + point.getY() + "," + point.getZ()+"} = "+i + "\n");
                for(int j = 0; j < 3; j++)
                {
                    cube[point.getX() + orientations[i][j][0]][point.getY() + orientations[i][j][1]][point.getZ() + orientations[i][j][2]] = i;
                    file.write("{"+point.getX() + orientations[i][j][0]+","+point.getY() + orientations[i][j][1]+","+point.getZ() + orientations[i][j][2]+"} = "+i + "\n");
                }
                orientation = i;
                i = orientations.length;
            }
        }
        if(orientation == ori)
        {
            return -2;
        }
        file.write("orientation " + orientation + "\n");
        return orientation;
    }
    public boolean isOpen(int orientation[][], Point point) throws IOException
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
                    file.write("colision at {" + orientation[i][0] + point.getX() + ", " + orientation[i][1] + point.getY() + ", " + orientation[i][2] + point.getZ() + "}\n");
                    open = false;
                }
            }
            else
            {
                file.write("out at {" + orientation[i][0] + point.getX() + ", " + orientation[i][1] + point.getY() + ", " + orientation[i][2] + point.getZ() + "}\n");
            }
        }
        return (open && in);
    }
    public void update(int orientation) throws IOException
    {
        //get all new explored points
        ArrayList<Point> newExplored = new ArrayList<>();
        newExplored.add(cubeFrontier.get(0));
        for(int i = 0; i < orientations[orientation].length; i++)
        {
            newExplored.add(new Point(newExplored.get(0).getX() + orientations[orientation][i][0],
                    newExplored.get(0).getY() + orientations[orientation][i][1],
                    newExplored.get(0).getZ() + orientations[orientation][i][2]));
        }
        file.write("newExplored :");
        for(Point point :newExplored)
        {
            file.write( "{"+point.getX()+","+point.getY()+","+point.getZ()+"}");
        }
        file.write("\n");

        updateExplored(newExplored);
        updateFrontier(newExplored);
    }
    public void updateExplored (ArrayList<Point> newExplored) throws IOException
    {
        //add newExplored points to explored
        int index = 0;
        if(cubeExplored.size() == 0)
        {
            file.write("0 cubeExplored\n");
            cubeExplored.addAll(newExplored);
        }
        else
        {
            for(Point newPoint : newExplored)
            {
                boolean in = false;
                for(Point point : cubeExplored)
                {
                    in = in || newPoint.equals(point);
                }
                if(!in)
                {
                    cubeExplored.add(newPoint);
                    index --;
                }
                index ++;
            }
        }
        file.write("explored + newExplored:");
        for(Point point :cubeExplored)
        {
            file.write(point.toString());
        }
        file.write("\n");
    }
    public void updateFrontier (ArrayList<Point> newExplored) throws IOException
    {
        //remove newExplored points from frontier
        for(int i = 0; i < cubeFrontier.size(); i++)
        {
            boolean in = false;
            for(Point point : newExplored)
            {
                if(cubeFrontier.get(i) == point)
                {
                    in = in || cubeFrontier.get(i).equals(point);
                }
            }
            if(in)
            {
                cubeFrontier.remove(i);
                i --;
            }
        }

        file.write("frontier -  newExplored:" + "\n");
        for(Point point :cubeFrontier)
        {
            file.write(point.toString());
        }
        file.write("\n");

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

        file.write("newFrontier:");
        for(Point point :newFrontier)
        {
            file.write(point.toString());
        }
        file.write("\n");

        //remove explored points from newFrontier
        for(int i = 0; i < newFrontier.size(); i++)
        {
            boolean in = false;
            for(Point point : cubeExplored)
            {
                in = in || newFrontier.get(i).equals(point);
            }
            if(in)
            {
                newFrontier.remove(i);
                i --;
            }
        }

        file.write("newFrontier - explored:");
        for(Point point :newFrontier)
        {
            file.write(point.toString());
        }
        file.write("\n");

        //add newFrontier points to frontier
        if(cubeFrontier.size() == 0)
        {
            file.write( "0 cubeFrontier" + "\n");
            cubeFrontier.addAll(newFrontier);
        }
        else
        {
            for(Point newPoint : newFrontier)
            {
                boolean in = false;
                for(Point point : cubeFrontier)
                {
                    in = in || newPoint.equals(point);
                }
                if(!in)
                {
                    cubeFrontier.add(newPoint);
                }
            }
        }
        file.write("frontier + newFrontier:");
        for(Point point :cubeFrontier)
        {
            file.write("{"+point.getX()+","+point.getY()+","+point.getZ()+"}");
        }
        file.write("\n");
    }
    public Point getNext()
    {
        return cubeFrontier.size() > 0 ? cubeFrontier.get(0) : null;
    }
    public int[][][] getCube() {
        return cube;
    }
}
