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

    public Point setY(int y)
    {
        return new Point(x, y, z);
    }

    public Point setX(int x)
    {
        return new Point(x, y, z);
    }

    public Point setZ(int z)
    {
        return new Point(x, y, z);
    }

    public Point rotate(int rot){
        if(HEIGHT == WIDTH)
        {
            return switch (rot) {
                case 1 -> new Point(x, y, DEPTH - z);
                case 2 -> new Point(x, HEIGHT - y, z);
                case 3 -> new Point(x, HEIGHT - y, DEPTH - z);
                case 4 -> new Point(x, z, y);
                case 5 -> new Point(x, z, HEIGHT - y);
                case 6 -> new Point(x, DEPTH - z, y);
                case 7 -> new Point(x, DEPTH - z, HEIGHT - y);
                case 8 -> new Point(WIDTH - x, y, z);
                case 9 -> new Point(WIDTH - x, y, DEPTH - z);
                case 10-> new Point(WIDTH - x, HEIGHT - y, z);
                case 11-> new Point(WIDTH - x, HEIGHT - y, DEPTH - z);
                case 12-> new Point(WIDTH - x, z, y);
                case 13-> new Point(WIDTH - x, z, HEIGHT - y);
                case 14-> new Point(WIDTH - x, DEPTH - z, y);
                case 15-> new Point(WIDTH - x, DEPTH - z, HEIGHT - y);
                case 16-> new Point(y, x, z);
                case 17-> new Point(y, x, DEPTH - z);
                case 18-> new Point(y, WIDTH - x, z);
                case 19-> new Point(y, WIDTH - x, DEPTH - z);
                case 20-> new Point(y, z, x);
                case 21-> new Point(y, z, WIDTH - x);
                case 22-> new Point(y, DEPTH - z, x);
                case 23-> new Point(y, DEPTH - z, WIDTH - x);
                case 24-> new Point(HEIGHT - y, x, z);
                case 25-> new Point(HEIGHT - y, x, DEPTH - z);
                case 26-> new Point(HEIGHT - y, WIDTH - x, z);
                case 27-> new Point(HEIGHT - y, WIDTH - x, DEPTH - z);
                case 28-> new Point(HEIGHT - y, z, x);
                case 29-> new Point(HEIGHT - y, z, WIDTH - x);
                case 30-> new Point(HEIGHT - y, DEPTH - z, x);
                case 31-> new Point(HEIGHT - y, DEPTH - z, WIDTH - x);
                case 32-> new Point(z, x, y);
                case 33-> new Point(z, x, HEIGHT - y);
                case 34-> new Point(z, WIDTH - x, y);
                case 35-> new Point(z, WIDTH - x, HEIGHT - y);
                case 36-> new Point(z, y, x);
                case 37-> new Point(z, y, WIDTH - x);
                case 38-> new Point(z, HEIGHT - y, x);
                case 39-> new Point(z, HEIGHT - y, WIDTH - x);
                case 40-> new Point(DEPTH - z, x, y);
                case 41-> new Point(DEPTH - z, x, HEIGHT - y);
                case 42-> new Point(DEPTH - z, WIDTH - x, y);
                case 43-> new Point(DEPTH - z, WIDTH - x, HEIGHT - y);
                case 44-> new Point(DEPTH - z, y, x);
                case 45-> new Point(DEPTH - z, y, WIDTH - x);
                case 46-> new Point(DEPTH - z, HEIGHT - y, x);
                case 47-> new Point(DEPTH - z, HEIGHT - y, WIDTH - x);
                default-> new Point(x, y, z);
            };
        }
        else
        {
            return switch (rot) {
                case 1 -> new Point(x, y, DEPTH - z);//xy : 00
                case 2 -> new Point(x, HEIGHT - y, z);       //xy : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                case 3 -> new Point(x, HEIGHT - y, DEPTH - z);//00 : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                case 4 -> new Point(WIDTH - x, y, z);       //xy : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                case 5 -> new Point(WIDTH - x, y, DEPTH - z);//00 : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                case 6 -> new Point(WIDTH - x, HEIGHT - y, z);       //00 : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                case 7 -> new Point(WIDTH - x, HEIGHT - y, DEPTH - z);//xy : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                case 8 -> new Point(z, y, x);       //xy : yyy
                case 9 -> new Point(z, y, WIDTH - x);//00 : y,    zyx
                case 10-> new Point(z, HEIGHT - y, x);       //00 : yxx,  zzy
                case 11-> new Point(z, HEIGHT - y, WIDTH - x);//xy : xxy,  xyz,  xzx,  yzz,  zxz
                case 12-> new Point(DEPTH - z, y, x);       //00 : yyy
                case 13-> new Point(DEPTH - z, y, WIDTH - x);//xy : y,    zyx
                case 14-> new Point(DEPTH - z, HEIGHT - y, x);       //xy : yxx,  zzy
                case 15-> new Point(DEPTH - z, HEIGHT - y, WIDTH - x);//00 : xxy,  xyz,  xzx,  yzz,  zxz
                default-> new Point(x, y, z);
            };
        }
    }
    public Point rotate(Point rotation)
    {
        int x = switch (rotation.x()){
            case 1 -> this.x();
            case 2 -> this.y();
            case 3 -> this.z();
            case -1 -> WIDTH - this.x();
            case -2 -> HEIGHT - this.y();
            case -3 -> DEPTH - this.z();
            default -> throw new IllegalStateException("Unexpected value: " + rotation.x());
        };
        int y = switch (rotation.x()){
            case 1 -> this.x();
            case 2 -> this.y();
            case 3 -> this.z();
            case -1 -> WIDTH - this.x();
            case -2 -> HEIGHT - this.y();
            case -3 -> DEPTH - this.z();
            default -> throw new IllegalStateException("Unexpected value: " + rotation.x());
        };
        int z = switch (rotation.x()){
            case 1 -> this.x();
            case 2 -> this.y();
            case 3 -> this.z();
            case -1 -> WIDTH - this.x();
            case -2 -> HEIGHT - this.y();
            case -3 -> DEPTH - this.z();
            default -> throw new IllegalStateException("Unexpected value: " + rotation.x());
        };
        return new Point(x,y,z);
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
