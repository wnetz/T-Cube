public class Point implements Comparable
{
    private final int x,y,z;

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }
    public Point setY(int y) {
        return new Point(x,y,z);
    }
    public int getZ() {
        return z;
    }

    public Point setX(int x) {
        return new Point(x,y,z);
    }
    public int getY() {
        return y;
    }
    public Point setZ(int z) {
        return new Point(x,y,z);
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Point && ((Point) obj).x == x && ((Point) obj).y == y && ((Point) obj).z == z;
    }
    @Override
    public int compareTo(Object obj) {
        if(obj instanceof Point)
            return x + y + z - ((Point) obj).x - ((Point) obj).y - ((Point) obj).z;
            /*if(((Point) obj).x != x )
                return x-((Point) obj).x;
            else if(((Point) obj).y != y )
                return y-((Point) obj).y;
            else if(((Point) obj).z != z )
                return z-((Point) obj).z;
            else
                return 0;*/
        return 0;
    }

    @Override
    public String toString() {
        return "{" + x + "," + y+ "," + z +"}";
    }


}
