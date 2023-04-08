public record Point(int x, int y, int z) implements Comparable
{
    private static int HEIGHT = 5;
    private static final int WIDTH = 5;
    private static final int DEPTH = 5;

    public static void setHeight(int h){
        HEIGHT = h;
    }
    public static int getHeight(){
        return HEIGHT;
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
                case 1 -> new Point(x, y, DEPTH - z);                   //xy : 00
                case 2 -> new Point(x, HEIGHT - y, z);                  //xy : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                case 3 -> new Point(x, HEIGHT - y, DEPTH - z);          //00 : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                case 4 -> new Point(x, z, y);                           //xy : xxx
                case 5 -> new Point(x, z, HEIGHT - y);                  //00 : x,    yxz
                case 6 -> new Point(x, DEPTH - z, y);                   //00 : xxx
                case 7 -> new Point(x, DEPTH - z, HEIGHT - y);          //xy : x,    yxz
                case 8 -> new Point(WIDTH - x, y, z);                   //xy : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                case 9 -> new Point(WIDTH - x, y, DEPTH - z);           //00 : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                case 10-> new Point(WIDTH - x, HEIGHT - y, z);          //00 : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                case 11-> new Point(WIDTH - x, HEIGHT - y, DEPTH - z);//xy : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                case 12-> new Point(WIDTH - x, z, y);       //00 : xyy,  yzy,  zxy,  zyz,  zzx
                case 13-> new Point(WIDTH - x, z, HEIGHT - y);//xy : xzz,  yyx
                case 14-> new Point(WIDTH - x, DEPTH - z, y);       //xy : xyy,  yzy,  zxy,  zyz,  zzx
                case 15-> new Point(WIDTH - x, DEPTH - z, HEIGHT - y);//00 : xzz,  yyx
                case 16-> new Point(y, x, z);       //xy : xxz,  zyy
                case 17-> new Point(y, x, DEPTH - z);//00 : xxz,  zyy
                case 18-> new Point(y, WIDTH - x, z);       //00 : zzz
                case 19-> new Point(y, WIDTH - x, DEPTH - z);//xy : zzz
                case 20-> new Point(y, z, x);       //00 : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
                case 21-> new Point(y, z, WIDTH - x);//xy : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
                case 22-> new Point(y, DEPTH - z, x);       //xy : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
                case 23-> new Point(y, DEPTH - z, WIDTH - x);//00 : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
                case 24-> new Point(HEIGHT - y, x, z);       //00 : xzy,  z
                case 25-> new Point(HEIGHT - y, x, DEPTH - z);//xy : xzy,  z
                case 26-> new Point(HEIGHT - y, WIDTH - x, z);       //xy : xyx,  yxy,  yyz,  yzx,  zxx
                case 27-> new Point(HEIGHT - y, WIDTH - x, DEPTH - z);//00 : xyx,  yxy,  yyz,  yzx,  zxx
                case 28-> new Point(HEIGHT - y, z, x);       //xy : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
                case 29-> new Point(HEIGHT - y, z, WIDTH - x);//00 : xy,   xzyx, yxzy, yz,   zx,   zyxz
                case 30-> new Point(HEIGHT - y, DEPTH - z, x);       //00 : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
                case 31-> new Point(HEIGHT - y, DEPTH - z, WIDTH - x);//xy : xy,   xzyx, yxzy, yz,   zx,   zyxz
                case 32-> new Point(z, x, y);       //00 : xxxz, xzyy, yxxx, zy,   zzyx
                case 33-> new Point(z, x, HEIGHT - y);//xy : xxzy, xz,   yxzz, yyyx, zyyy
                case 34-> new Point(z, WIDTH - x, y);       //xy : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz
                case 35-> new Point(z, WIDTH - x, HEIGHT - y);//00 : xzzz, yx,   yyxz, zyxx, zzzy
                case 36-> new Point(z, y, x);       //xy : yyy
                case 37-> new Point(z, y, WIDTH - x);//00 : y,    zyx
                case 38-> new Point(z, HEIGHT - y, x);       //00 : yxx,  zzy
                case 39-> new Point(z, HEIGHT - y, WIDTH - x);//xy : xxy,  xyz,  xzx,  yzz,  zxz
                case 40-> new Point(DEPTH - z, x, y);       //xy : xxxz, xzyy, yxxx, zy,   zzyx
                case 41-> new Point(DEPTH - z, x, HEIGHT - y);//00 : xxzy, xz,   yxzz, yyyx, zyyy
                case 42-> new Point(DEPTH - z, WIDTH - x, y);       //xy : xzzz, yx,   yyxz, zyxx, zzzy
                case 43-> new Point(DEPTH - z, WIDTH - x, HEIGHT - y);//00 : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz
                case 44-> new Point(DEPTH - z, y, x);       //00 : yyy
                case 45-> new Point(DEPTH - z, y, WIDTH - x);//xy : y,    zyx
                case 46-> new Point(DEPTH - z, HEIGHT - y, x);       //xy : yxx,  zzy
                case 47-> new Point(DEPTH - z, HEIGHT - y, WIDTH - x);//00 : xxy,  xyz,  xzx,  yzz,  zxz
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
