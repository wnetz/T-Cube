import java.io.Serializable;

public record Point(int x, int y, int z) implements Comparable, Serializable
{
    private static int HEIGHT = Main.HEIGHT;
    private static final int WIDTH = Main.WIDTH;
    private static final int DEPTH = Main.DEPTH;

    /**
     *
     * @param point1
     * @param point2
     * @return point1 - point2
     */
    public static Point subtract(Point point1, Point point2)
    {
        return new Point(point1.x() - point2.x(),point1.y() - point2.y(),point1.z() - point2.z());
    }
    /**
     *
     * @param point1
     * @param point2
     * @return point1 + point2
     */
    public static Point add(Point point1, Point point2)
    {
        return new Point(point1.x() + point2.x(),point1.y() + point2.y(),point1.z() + point2.z());
    }

    public Point rotate(Point rotation)
    {
        int rotx = switch (rotation.x()){
            case 1 -> x;
            case 2 -> y;
            case 3 -> z;
            case -1 -> WIDTH - x;
            case -2 -> HEIGHT - y;
            case -3 -> DEPTH - z;
            default -> throw new IllegalStateException("Unexpected value: " + rotation.x());
        };
        int roty = switch (rotation.y()){
            case 1 -> x;
            case 2 -> y;
            case 3 -> z;
            case -1 -> WIDTH - x;
            case -2 -> HEIGHT - y;
            case -3 -> DEPTH - z;
            default -> throw new IllegalStateException("Unexpected value: " + rotation.y());
        };
        int rotz = switch (rotation.z()){
            case 1 -> x;
            case 2 -> y;
            case 3 -> z;
            case -1 -> WIDTH - x;
            case -2 -> HEIGHT - y;
            case -3 -> DEPTH - z;
            default -> throw new IllegalStateException("Unexpected value: " + rotation.z());
        };
        return new Point(rotx,roty,rotz);
    }
    public Point rotate_about_origin(int rotation)
    {
        return switch (rotation){
            //x axis
            case 0 -> new Point(x,-z,y);
            //y axis
            case 1 -> new Point(z,y,-x);
            //z axis
            case 2 -> new Point(-y,x,z);
            //-x axis
            case 3 -> new Point(x,z,-y);
            //-y axis
            case 4 -> new Point(-z,y,x);
            //-z axis
            case 5 -> new Point(y,-x,z);
            default -> new Point(x,y,z);
        };
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Point && ((Point) obj).x == x && ((Point) obj).y == y && ((Point) obj).z == z;
    }

    @Override
    public int compareTo(Object obj) {
        if (obj instanceof Point)
            if(((Point) obj).x != x )
                return x-((Point) obj).x;
            else if(((Point) obj).y != y )
                return y-((Point) obj).y;
            else if(((Point) obj).z != z )
                return z-((Point) obj).z;
            else
                return 0;
        return 0;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }


}
