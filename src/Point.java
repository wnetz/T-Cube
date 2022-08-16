public class Point
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
    public String toString() {
        return "{" + x + "," + y+ "," + z +"}";
    }
}
