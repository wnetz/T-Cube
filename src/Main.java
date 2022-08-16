import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static final int HEIGHT = 2;
    public static final int orientations [][][] =
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

    static FileWriter file;
    public static void main(String[] args) throws IOException 
    {
        int cube [][][] = new int [6][HEIGHT][6];
        ArrayList<ArrayList<Integer>> cubeFrontier = new ArrayList<>();
        cubeFrontier.add(new ArrayList<> (List.of(new Integer[]{0, 0, 0})));
        ArrayList<ArrayList<Integer>> cubeExplored = new ArrayList<>();
        file = new FileWriter("output2.txt");
        System.out.println("start");
        for(int x = 0; x < 6; x++)
        {
            for(int y = 0; y < HEIGHT; y++)
            {
                for(int z = 0; z < 6; z++)
                {
                    cube[x][y][z] = -1;
                }
            }
        }

        recursiveSolve(new Solution(cube,file),0);
        String layer = "";
        boolean allEmpty = true;
        for(int i = 0; i < HEIGHT; i++)
        {
            layer = "";
            allEmpty = true;
            for(int j = 0; j < 6; j++)
            {
                for(int k = 0; k < 6; k++)
                {
                    if(cube[k][i][j] != -1)
                    {
                        allEmpty = false;
                        if(cube[k][i][j] < 10)
                        {
                            layer = layer + "0" + cube[k][i][j] + ", ";
                        }
                        else
                        {
                            layer = layer + cube[k][i][j] + ", ";
                        }
                    }
                    else
                    {
                        layer = layer + cube[k][i][j] + ", ";
                    }
                }
                layer = layer + "\n";
            }
            file.write(layer + "\n");
        }
        file.close();
    }


    public static void removeT(int cube[][][], ArrayList<Integer> point,int orientation) throws IOException
    {
        file.write("removeT "+ point.get(0) + " " + point.get(1) + " " + point.get(2) + " orientation " + orientation + "\n");
        cube[point.get(0)][point.get(1)][point.get(2)] = -1;
        file.write("{"+point.get(0)+","+point.get(1)+","+point.get(2)+"} = "+-1 + "\n");
        for(int j = 0; j < 3; j++)
        {
            cube[point.get(0) + orientations[orientation][j][0]][point.get(1) + orientations[orientation][j][1]][point.get(2) + orientations[orientation][j][2]] = -1;
            file.write("{"+point.get(0) + orientations[orientation][j][0]+","+point.get(1) + orientations[orientation][j][1]+","+point.get(2) + orientations[orientation][j][2]+"} = "+-1 + "\n");
        }
    }


    public static int recursiveSolve(Solution solution, int d) throws IOException
    {
        int depth = d;
        int orientation = -1;

        file.write("depth: " + depth + "\n");
        while (orientation != -2)
        {
            Point point = solution.getNext();
            orientation = solution.addT(point,orientation);
            file.write( "orientation: " + orientation + "\n");
            //cout + "depth: " + depth + " orientation: " + orientation + "\n";
            if(orientation != -2)
            {
                ArrayList<ArrayList<Integer>> oldCubeFrontier = cubeFrontier;
                ArrayList<ArrayList<Integer>> oldCubeExplored = cubeExplored;
                solution.update(orientation);
                int cube [][][] = solution.getCube();
                for(int i = 0; i < HEIGHT; i++)
                {
                    String layer = "";
                    boolean allEmpty = true;
                    for(int j = 0; j < 6; j++)
                    {
                        for(int k = 0; k < 6; k++)
                        {
                            if(cube[k][i][j] != -1)
                            {
                                allEmpty = false;
                                if(cube[k][i][j] < 10)
                                {
                                    layer = layer + "0" + cube[k][i][j] + ", ";
                                }
                            else
                                {
                                    layer = layer + cube[k][i][j] + ", ";
                                }
                            }
                        else
                            {
                                layer = layer + cube[k][i][j] + ", ";
                            }
                        }
                        layer = layer + "\n";
                    }
                    if(!allEmpty)
                    {
                        file.write(layer + "\n");
                    }
                }
                if(cubeFrontier.size()>0)
                {
                    int result = recursiveSolve(cube,cubeFrontier,cubeExplored,depth + 1);
                    file.write( "depth: " + depth + " result: " + result + "\n");
                    //cout + "depth: " + depth + " result: " + result + "\n";
                    if(result == 0)
                    {
                        return 0;
                    }
                    else
                    {
                        removeT(cube,point,orientation);
                        cubeFrontier = oldCubeFrontier;
                        cubeExplored = oldCubeExplored;
                    }
                }
                else
                {
                    return 0;
                }
            f = cubeFrontier;
            e = cubeExplored;
            }
            else
            {
                //file.write( + "erase {" + cubeFrontier.get(0).get(0) + ", " + cubeFrontier.get(0).get(1) + ", " + cubeFrontier.get(0).get(2) + "}" + "\n";
                //cubeFrontier.erase(cubeFrontier.begin());
                return -2;
            }
        }
        return -2;
    }
}
