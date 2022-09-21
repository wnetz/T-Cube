public class T implements Comparable
{
    private Point point;
    private int orientation;

    public T(Point point, int orientation) {
        this.point = point;
        this.orientation = orientation;
    }
    public T rotate(int rot)
    {
        int ori = orientation;
        if(Point.getHeight()==5)
                ori = switch(rot) {
            case 1 -> switch (orientation) {
                    case 0 -> 0;
                    case 1 -> 3;
                    case 2 -> 2;
                    case 3 -> 1;
                    case 4 -> 4;
                    case 5 -> 7;
                    case 6 -> 6;
                    case 7 -> 5;
                    case 8 -> 8;
                    case 9 -> 9;
                    case 10 -> 10;
                    case 11 -> 11;
                    default -> -1;
                };//xy : 00;
            case 2 -> switch (orientation) {
                    case 0 -> 2;
                    case 1 -> 1;
                    case 2 -> 0;
                    case 3 -> 3;
                    case 4 -> 4;
                    case 5 -> 5;
                    case 6 -> 6;
                    case 7 -> 7;
                    case 8 -> 8;
                    case 9 -> 11;
                    case 10 -> 10;
                    case 11 -> 9;
                    default -> -1;
                };//xy : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
            case 3 -> switch (orientation) {
                    case 0 -> 2;
                    case 1 -> 3;
                    case 2 -> 0;
                    case 3 -> 1;
                    case 4 -> 4;
                    case 5 -> 7;
                    case 6 -> 6;
                    case 7 -> 5;
                    case 8 -> 8;
                    case 9 -> 11;
                    case 10 -> 10;
                    case 11 -> 9;
                    default -> -1;
                };//00 : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
            case 4 -> switch (orientation) {
                    case 0 -> 1;
                    case 1 -> 0;
                    case 2 -> 3;
                    case 3 -> 2;
                    case 4 -> 8;
                    case 5 -> 9;
                    case 6 -> 10;
                    case 7 -> 11;
                    case 8 -> 4;
                    case 9 -> 5;
                    case 10-> 6;
                    case 11-> 7;
                    default -> -1;
                };//xy : xxx
            case 5 -> switch (orientation) {
                    case 0 -> 3;
                    case 1 -> 0;
                    case 2 -> 1;
                    case 3 -> 2;
                    case 4 -> 8;
                    case 5 -> 9;
                    case 6 -> 10;
                    case 7 -> 11;
                    case 8 -> 4;
                    case 9 -> 7;
                    case 10-> 6;
                    case 11-> 5;
                    default -> -1;
                };//00 : x,    yxz
            case 6 -> switch (orientation) {
                    case 0 -> 1;
                    case 1 -> 2;
                    case 2 -> 3;
                    case 3 -> 0;
                    case 4 -> 8;
                    case 5 -> 11;
                    case 6 -> 10;
                    case 7 -> 9;
                    case 8 -> 4;
                    case 9 -> 5;
                    case 10-> 6;
                    case 11-> 7;
                    default -> -1;
                };//00 : xxx
            case 7 -> switch (orientation) {
                    case 0 -> 3;
                    case 1 -> 2;
                    case 2 -> 1;
                    case 3 -> 0;
                    case 4 -> 8;
                    case 5 -> 11;
                    case 6 -> 10;
                    case 7 -> 9;
                    case 8 -> 4;
                    case 9 -> 7;
                    case 10-> 6;
                    case 11-> 5;
                    default -> -1;
                };//xy : x,    yxz
            case 8 -> switch (orientation) {
                    case 0 -> 0;
                    case 1 -> 1;
                    case 2 -> 2;
                    case 3 -> 3;
                    case 4 -> 6;
                    case 5 -> 5;
                    case 6 -> 4;
                    case 7 -> 7;
                    case 8 -> 10;
                    case 9 -> 9;
                    case 10 -> 8;
                    case 11 -> 11;
                    default -> -1;
                };//xy : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
            case 9 -> switch (orientation) {
                    case 0 -> 0;
                    case 1 -> 3;
                    case 2 -> 2;
                    case 3 -> 1;
                    case 4 -> 6;
                    case 5 -> 7;
                    case 6 -> 4;
                    case 7 -> 5;
                    case 8 -> 10;
                    case 9 -> 9;
                    case 10 -> 8;
                    case 11 -> 11;
                    default -> -1;
                };//00 : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
            case 10-> switch (orientation) {
                    case 0 -> 2;
                    case 1 -> 1;
                    case 2 -> 0;
                    case 3 -> 3;
                    case 4 -> 6;
                    case 5 -> 5;
                    case 6 -> 4;
                    case 7 -> 7;
                    case 8 -> 10;
                    case 9 -> 11;
                    case 10 -> 8;
                    case 11 -> 9;
                    default -> -1;
                };//00 : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
            case 11-> switch (orientation) {
                    case 0 -> 2;
                    case 1 -> 3;
                    case 2 -> 0;
                    case 3 -> 1;
                    case 4 -> 6;
                    case 5 -> 7;
                    case 6 -> 4;
                    case 7 -> 5;
                    case 8 -> 10;
                    case 9 -> 11;
                    case 10 -> 8;
                    case 11 -> 9;
                    default -> -1;
                };//xy : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
            case 12-> switch (orientation) {
                    case 0 -> 1;
                    case 1 -> 0;
                    case 2 -> 3;
                    case 3 -> 2;
                    case 4 -> 10;
                    case 5 -> 9;
                    case 6 -> 8;
                    case 7 -> 11;
                    case 8 -> 6;
                    case 9 -> 5;
                    case 10-> 4;
                    case 11-> 7;
                    default -> -1;
                };//00 : xyy,  yzy,  zxy,  zyz,  zzx
            case 13-> switch (orientation) {
                    case 0 -> 3;
                    case 1 -> 0;
                    case 2 -> 1;
                    case 3 -> 2;
                    case 4 -> 10;
                    case 5 -> 9;
                    case 6 -> 8;
                    case 7 -> 11;
                    case 8 -> 6;
                    case 9 -> 7;
                    case 10-> 4;
                    case 11-> 5;
                    default -> -1;
                };//xy : xzz,  yyx
            case 14-> switch (orientation) {
                    case 0 -> 1;
                    case 1 -> 2;
                    case 2 -> 3;
                    case 3 -> 0;
                    case 4 -> 10;
                    case 5 -> 11;
                    case 6 -> 8;
                    case 7 -> 9;
                    case 8 -> 6;
                    case 9 -> 5;
                    case 10-> 4;
                    case 11-> 7;
                    default -> -1;
                };//xy : xyy,  yzy,  zxy,  zyz,  zzx
            case 15-> switch (orientation) {
                    case 0 -> 3;
                    case 1 -> 2;
                    case 2 -> 1;
                    case 3 -> 0;
                    case 4 -> 10;
                    case 5 -> 11;
                    case 6 -> 8;
                    case 7 -> 9;
                    case 8 -> 6;
                    case 9 -> 7;
                    case 10-> 4;
                    case 11-> 5;
                    default -> -1;
                };//00 : xzz,  yyx
            case 16-> switch (orientation) {
                    case 0 -> 6;
                    case 1 -> 5;
                    case 2 -> 4;
                    case 3 -> 7;
                    case 4 -> 2;
                    case 5 -> 1;
                    case 6 -> 0;
                    case 7 -> 3;
                    case 8 -> 11;
                    case 9 -> 10;
                    case 10-> 9;
                    case 11-> 8;
                    default -> -1;
                };//xy : xxz,  zyy
            case 17-> switch (orientation) {
                    case 0 -> 6;
                    case 1 -> 7;
                    case 2 -> 4;
                    case 3 -> 5;
                    case 4 -> 2;
                    case 5 -> 3;
                    case 6 -> 0;
                    case 7 -> 1;
                    case 8 -> 11;
                    case 9 -> 10;
                    case 10-> 9;
                    case 11-> 8;
                    default -> -1;
                };//00 : xxz,  zyy
            case 18-> switch (orientation) {
                    case 0 -> 6;
                    case 1 -> 5;
                    case 2 -> 4;
                    case 3 -> 7;
                    case 4 -> 0;
                    case 5 -> 1;
                    case 6 -> 2;
                    case 7 -> 3;
                    case 8 -> 9;
                    case 9 -> 10;
                    case 10-> 11;
                    case 11-> 8;
                    default -> -1;
                };//00 : zzz
            case 19-> switch (orientation) {
                    case 0 -> 6;
                    case 1 -> 7;
                    case 2 -> 4;
                    case 3 -> 5;
                    case 4 -> 0;
                    case 5 -> 3;
                    case 6 -> 2;
                    case 7 -> 1;
                    case 8 -> 9;
                    case 9 -> 10;
                    case 10-> 11;
                    case 11-> 8;
                    default -> -1;
                };//xy : zzz
            case 20-> switch (orientation) {
                    case 0 -> 10;
                    case 1 -> 9;
                    case 2 -> 8;
                    case 3 -> 11;
                    case 4 -> 3;
                    case 5 -> 0;
                    case 6 -> 1;
                    case 7 -> 2;
                    case 8 -> 7;
                    case 9 -> 6;
                    case 10-> 5;
                    case 11-> 4;
                    default -> -1;
                };//00 : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
            case 21-> switch (orientation) {
                    case 0 -> 10;
                    case 1 -> 9;
                    case 2 -> 8;
                    case 3 -> 11;
                    case 4 -> 1;
                    case 5 -> 0;
                    case 6 -> 3;
                    case 7 -> 2;
                    case 8 -> 5;
                    case 9 -> 6;
                    case 10-> 7;
                    case 11-> 4;
                    default -> -1;
                };//xy : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
            case 22-> switch (orientation) {
                    case 0 -> 10;
                    case 1 -> 11;
                    case 2 -> 8;
                    case 3 -> 9;
                    case 4 -> 3;
                    case 5 -> 2;
                    case 6 -> 1;
                    case 7 -> 0;
                    case 8 -> 7;
                    case 9 -> 6;
                    case 10-> 5;
                    case 11-> 4;
                    default -> -1;
                };//xy : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
            case 23-> switch (orientation) {
                    case 0 -> 10;
                    case 1 -> 11;
                    case 2 -> 8;
                    case 3 -> 9;
                    case 4 -> 1;
                    case 5 -> 2;
                    case 6 -> 3;
                    case 7 -> 0;
                    case 8 -> 5;
                    case 9 -> 6;
                    case 10-> 7;
                    case 11-> 4;
                    default -> -1;
                };//00 : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
            case 24-> switch (orientation) {
                    case 0 -> 4;
                    case 1 -> 5;
                    case 2 -> 6;
                    case 3 -> 7;
                    case 4 -> 2;
                    case 5 -> 1;
                    case 6 -> 0;
                    case 7 -> 3;
                    case 8 -> 11;
                    case 9 -> 8;
                    case 10-> 9;
                    case 11-> 10;
                    default -> -1;
                };//00 : xzy,  z
            case 25-> switch (orientation) {
                    case 0 -> 4;
                    case 1 -> 7;
                    case 2 -> 6;
                    case 3 -> 5;
                    case 4 -> 2;
                    case 5 -> 3;
                    case 6 -> 0;
                    case 7 -> 1;
                    case 8 -> 11;
                    case 9 -> 8;
                    case 10-> 9;
                    case 11-> 10;
                    default -> -1;
                };//xy : xzy,  z
            case 26-> switch (orientation) {
                    case 0 -> 4;
                    case 1 -> 5;
                    case 2 -> 6;
                    case 3 -> 7;
                    case 4 -> 0;
                    case 5 -> 1;
                    case 6 -> 2;
                    case 7 -> 3;
                    case 8 -> 9;
                    case 9 -> 8;
                    case 10-> 11;
                    case 11-> 10;
                    default -> -1;
                };//xy : xyx,  yxy,  yyz,  yzx,  zxx
            case 27-> switch (orientation) {
                    case 0 -> 4;
                    case 1 -> 7;
                    case 2 -> 6;
                    case 3 -> 5;
                    case 4 -> 0;
                    case 5 -> 3;
                    case 6 -> 2;
                    case 7 -> 1;
                    case 8 -> 9;
                    case 9 -> 8;
                    case 10-> 11;
                    case 11-> 10;
                    default -> -1;
                };//00 : xyx,  yxy,  yyz,  yzx,  zxx
            case 28-> switch (orientation) {
                    case 0 -> 8;
                    case 1 -> 9;
                    case 2 -> 10;
                    case 3 -> 11;
                    case 4 -> 3;
                    case 5 -> 0;
                    case 6 -> 1;
                    case 7 -> 2;
                    case 8 -> 7;
                    case 9 -> 4;
                    case 10-> 5;
                    case 11-> 6;
                    default -> -1;
                };//xy : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
            case 29-> switch (orientation) {
                    case 0 -> 8;
                    case 1 -> 9;
                    case 2 -> 10;
                    case 3 -> 11;
                    case 4 -> 1;
                    case 5 -> 0;
                    case 6 -> 3;
                    case 7 -> 2;
                    case 8 -> 5;
                    case 9 -> 4;
                    case 10-> 7;
                    case 11-> 6;
                    default -> -1;
                };//00 : xy,   xzyx, yxzy, yz,   zx,   zyxz
            case 30-> switch (orientation) {
                    case 0 -> 8;
                    case 1 -> 11;
                    case 2 -> 10;
                    case 3 -> 9;
                    case 4 -> 3;
                    case 5 -> 2;
                    case 6 -> 1;
                    case 7 -> 0;
                    case 8 -> 7;
                    case 9 -> 4;
                    case 10-> 5;
                    case 11-> 6;
                    default -> -1;
                };//00 : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
            case 31-> switch (orientation) {
                    case 0 -> 8;
                    case 1 -> 11;
                    case 2 -> 10;
                    case 3 -> 9;
                    case 4 -> 1;
                    case 5 -> 2;
                    case 6 -> 3;
                    case 7 -> 0;
                    case 8 -> 5;
                    case 9 -> 4;
                    case 10-> 7;
                    case 11-> 6;
                    default -> -1;
                };//xy : xy,   xzyx, yxzy, yz,   zx,   zyxz
            case 32-> switch (orientation) {
                    case 0 -> 5;
                    case 1 -> 6;
                    case 2 -> 7;
                    case 3 -> 4;
                    case 4 -> 11;
                    case 5 -> 10;
                    case 6 -> 9;
                    case 7 -> 8;
                    case 8 -> 2;
                    case 9 -> 1;
                    case 10-> 0;
                    case 11-> 3;
                    default -> -1;
                };//00 : xxxz, xzyy, yxxx, zy,   zzyx
            case 33-> switch (orientation) {
                    case 0 -> 7;
                    case 1 -> 6;
                    case 2 -> 5;
                    case 3 -> 4;
                    case 4 -> 11;
                    case 5 -> 10;
                    case 6 -> 9;
                    case 7 -> 8;
                    case 8 -> 2;
                    case 9 -> 3;
                    case 10-> 0;
                    case 11-> 1;
                    default -> -1;
                };//xy : xxzy, xz,   yxzz, yyyx, zyyy
            case 34-> switch (orientation) {
                case 0 -> 5;
                case 1 -> 6;
                case 2 -> 7;
                case 3 -> 4;
                case 4 -> 9;
                case 5 -> 10;
                case 6 -> 11;
                case 7 -> 8;
                case 8 -> 0;
                case 9 -> 1;
                case 10-> 2;
                case 11-> 3;
                default -> -1;
            };//xy : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz
            case 35-> switch (orientation) {
                    case 0 -> 7;
                    case 1 -> 6;
                    case 2 -> 5;
                    case 3 -> 4;
                    case 4 -> 9;
                    case 5 -> 10;
                    case 6 -> 11;
                    case 7 -> 8;
                    case 8 -> 0;
                    case 9 -> 3;
                    case 10-> 2;
                    case 11-> 1;
                    default -> -1;
                };//00 : xzzz, yx,   yyxz, zyxx, zzzy
            case 36-> switch (orientation) {
                    case 0 -> 9;
                    case 1 -> 10;
                    case 2 -> 11;
                    case 3 -> 8;
                    case 4 -> 7;
                    case 5 -> 6;
                    case 6 -> 5;
                    case 7 -> 4;
                    case 8 -> 3;
                    case 9 -> 0;
                    case 10 -> 1;
                    case 11 -> 2;
                    default -> -1;
                };//xy : yyy
            case 37-> switch (orientation) {
                    case 0 -> 9;
                    case 1 -> 10;
                    case 2 -> 11;
                    case 3 -> 8;
                    case 4 -> 5;
                    case 5 -> 6;
                    case 6 -> 7;
                    case 7 -> 4;
                    case 8 -> 1;
                    case 9 -> 0;
                    case 10 -> 3;
                    case 11 -> 2;
                    default -> -1;
                };//00 : y,    zyx
            case 38-> switch (orientation) {
                    case 0 -> 11;
                    case 1 -> 10;
                    case 2 -> 9;
                    case 3 -> 8;
                    case 4 -> 7;
                    case 5 -> 6;
                    case 6 -> 5;
                    case 7 -> 4;
                    case 8 -> 3;
                    case 9 -> 2;
                    case 10 -> 1;
                    case 11 -> 0;
                    default -> -1;
                };//00 : yxx,  zzy
            case 39-> switch (orientation) {
                    case 0 -> 11;
                    case 1 -> 10;
                    case 2 -> 9;
                    case 3 -> 8;
                    case 4 -> 5;
                    case 5 -> 6;
                    case 6 -> 7;
                    case 7 -> 4;
                    case 8 -> 1;
                    case 9 -> 2;
                    case 10 -> 3;
                    case 11 -> 0;
                    default -> -1;
                };//xy : xxy,  xyz,  xzx,  yzz,  zxz
            case 40-> switch (orientation) {
                    case 0 -> 5;
                    case 1 -> 4;
                    case 2 -> 7;
                    case 3 -> 6;
                    case 4 -> 11;
                    case 5 -> 8;
                    case 6 -> 9;
                    case 7 -> 10;
                    case 8 -> 2;
                    case 9 -> 1;
                    case 10-> 0;
                    case 11-> 3;
                    default -> -1;
                };//xy : xxxz, xzyy, yxxx, zy,   zzyx
            case 41-> switch (orientation) {
                    case 0 -> 7;
                    case 1 -> 4;
                    case 2 -> 5;
                    case 3 -> 6;
                    case 4 -> 11;
                    case 5 -> 8;
                    case 6 -> 9;
                    case 7 -> 10;
                    case 8 -> 2;
                    case 9 -> 3;
                    case 10-> 0;
                    case 11-> 1;
                    default -> -1;
                };//00 : xxzy, xz,   yxzz, yyyx, zyyy
            case 42-> switch (orientation) {
                    case 0 -> 7;
                    case 1 -> 4;
                    case 2 -> 5;
                    case 3 -> 6;
                    case 4 -> 9;
                    case 5 -> 8;
                    case 6 -> 11;
                    case 7 -> 10;
                    case 8 -> 0;
                    case 9 -> 3;
                    case 10-> 2;
                    case 11-> 1;
                    default -> -1;
                };//xy : xzzz, yx,   yyxz, zyxx, zzzy
            case 43-> switch (orientation) {
                    case 0 -> 5;
                    case 1 -> 4;
                    case 2 -> 7;
                    case 3 -> 6;
                    case 4 -> 9;
                    case 5 -> 8;
                    case 6 -> 11;
                    case 7 -> 10;
                    case 8 -> 0;
                    case 9 -> 1;
                    case 10-> 2;
                    case 11-> 3;
                    default -> -1;
                };//00 : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz
            case 44-> switch (orientation) {
                    case 0 -> 9;
                    case 1 -> 8;
                    case 2 -> 11;
                    case 3 -> 10;
                    case 4 -> 7;
                    case 5 -> 4;
                    case 6 -> 5;
                    case 7 -> 6;
                    case 8 -> 3;
                    case 9 -> 0;
                    case 10 -> 1;
                    case 11 -> 2;
                    default -> -1;
                };//00 : yyy
            case 45-> switch (orientation) {
                    case 0 -> 9;
                    case 1 -> 8;
                    case 2 -> 11;
                    case 3 -> 10;
                    case 4 -> 5;
                    case 5 -> 4;
                    case 6 -> 7;
                    case 7 -> 6;
                    case 8 -> 1;
                    case 9 -> 0;
                    case 10 -> 3;
                    case 11 -> 2;
                    default -> -1;
                };//xy : y,    zyx
            case 46-> switch (orientation) {
                    case 0 -> 11;
                    case 1 -> 8;
                    case 2 -> 9;
                    case 3 -> 10;
                    case 4 -> 7;
                    case 5 -> 4;
                    case 6 -> 5;
                    case 7 -> 6;
                    case 8 -> 3;
                    case 9 -> 2;
                    case 10 -> 1;
                    case 11 -> 0;
                    default -> -1;
                };//xy : yxx,  zzy
            case 47-> switch (orientation) {
                    case 0 -> 11;
                    case 1 -> 8;
                    case 2 -> 9;
                    case 3 -> 10;
                    case 4 -> 5;
                    case 5 -> 4;
                    case 6 -> 7;
                    case 7 -> 6;
                    case 8 -> 1;
                    case 9 -> 2;
                    case 10 -> 3;
                    case 11 -> 0;
                    default -> -1;
                };//00 : xxy,  xyz,  xzx,  yzz,  zxz
            default -> orientation;
        };
        else
                ori = switch(rot) {
                        case 1 -> switch (orientation) {
                                case 0 -> 0;
                                case 1 -> 3;
                                case 2 -> 2;
                                case 3 -> 1;
                                case 4 -> 4;
                                case 5 -> 7;
                                case 6 -> 6;
                                case 7 -> 5;
                                case 8 -> 8;
                                case 9 -> 9;
                                case 10 -> 10;
                                case 11 -> 11;
                                default -> -1;
                        };//xy : 00;
                        case 2 -> switch (orientation) {
                                case 0 -> 2;
                                case 1 -> 1;
                                case 2 -> 0;
                                case 3 -> 3;
                                case 4 -> 4;
                                case 5 -> 5;
                                case 6 -> 6;
                                case 7 -> 7;
                                case 8 -> 8;
                                case 9 -> 11;
                                case 10 -> 10;
                                case 11 -> 9;
                                default -> -1;
                        };//xy : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                        case 3 -> switch (orientation) {
                                case 0 -> 2;
                                case 1 -> 3;
                                case 2 -> 0;
                                case 3 -> 1;
                                case 4 -> 4;
                                case 5 -> 7;
                                case 6 -> 6;
                                case 7 -> 5;
                                case 8 -> 8;
                                case 9 -> 11;
                                case 10 -> 10;
                                case 11 -> 9;
                                default -> -1;
                        };//00 : xx,   xyxz, yxxy, yxyz, yxzx, yyzz, yzxz, zxxz, zzyy
                        case 4 -> switch (orientation) {
                                case 0 -> 0;
                                case 1 -> 1;
                                case 2 -> 2;
                                case 3 -> 3;
                                case 4 -> 6;
                                case 5 -> 5;
                                case 6 -> 4;
                                case 7 -> 7;
                                case 8 -> 10;
                                case 9 -> 9;
                                case 10 -> 8;
                                case 11 -> 11;
                                default -> -1;
                        };//xy : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                        case 5 -> switch (orientation) {
                                case 0 -> 0;
                                case 1 -> 3;
                                case 2 -> 2;
                                case 3 -> 1;
                                case 4 -> 6;
                                case 5 -> 7;
                                case 6 -> 4;
                                case 7 -> 5;
                                case 8 -> 10;
                                case 9 -> 9;
                                case 10 -> 8;
                                case 11 -> 11;
                                default -> -1;
                        };//00 : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
                        case 6 -> switch (orientation) {
                                case 0 -> 2;
                                case 1 -> 1;
                                case 2 -> 0;
                                case 3 -> 3;
                                case 4 -> 6;
                                case 5 -> 5;
                                case 6 -> 4;
                                case 7 -> 7;
                                case 8 -> 10;
                                case 9 -> 11;
                                case 10 -> 8;
                                case 11 -> 9;
                                default -> -1;
                        };//00 : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                        case 7 -> switch (orientation) {
                                case 0 -> 2;
                                case 1 -> 3;
                                case 2 -> 0;
                                case 3 -> 1;
                                case 4 -> 6;
                                case 5 -> 7;
                                case 6 -> 4;
                                case 7 -> 5;
                                case 8 -> 10;
                                case 9 -> 11;
                                case 10 -> 8;
                                case 11 -> 9;
                                default -> -1;
                        };//xy : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
                        case 8 -> switch (orientation) {
                                case 0 -> 9;
                                case 1 -> 10;
                                case 2 -> 11;
                                case 3 -> 8;
                                case 4 -> 7;
                                case 5 -> 6;
                                case 6 -> 5;
                                case 7 -> 4;
                                case 8 -> 3;
                                case 9 -> 0;
                                case 10 -> 1;
                                case 11 -> 2;
                                default -> -1;
                        };//xy : yyy
                        case 9 -> switch (orientation) {
                                case 0 -> 9;
                                case 1 -> 10;
                                case 2 -> 11;
                                case 3 -> 8;
                                case 4 -> 5;
                                case 5 -> 6;
                                case 6 -> 7;
                                case 7 -> 4;
                                case 8 -> 1;
                                case 9 -> 0;
                                case 10 -> 3;
                                case 11 -> 2;
                                default -> -1;
                        };//00 : y,    zyx
                        case 10-> switch (orientation) {
                                case 0 -> 11;
                                case 1 -> 10;
                                case 2 -> 9;
                                case 3 -> 8;
                                case 4 -> 7;
                                case 5 -> 6;
                                case 6 -> 5;
                                case 7 -> 4;
                                case 8 -> 3;
                                case 9 -> 2;
                                case 10 -> 1;
                                case 11 -> 0;
                                default -> -1;
                        };//00 : yxx,  zzy
                        case 11-> switch (orientation) {
                                case 0 -> 11;
                                case 1 -> 10;
                                case 2 -> 9;
                                case 3 -> 8;
                                case 4 -> 5;
                                case 5 -> 6;
                                case 6 -> 7;
                                case 7 -> 4;
                                case 8 -> 1;
                                case 9 -> 2;
                                case 10 -> 3;
                                case 11 -> 0;
                                default -> -1;
                        };//xy : xxy,  xyz,  xzx,  yzz,  zxz
                        case 12-> switch (orientation) {
                                case 0 -> 9;
                                case 1 -> 8;
                                case 2 -> 11;
                                case 3 -> 10;
                                case 4 -> 7;
                                case 5 -> 4;
                                case 6 -> 5;
                                case 7 -> 6;
                                case 8 -> 3;
                                case 9 -> 0;
                                case 10 -> 1;
                                case 11 -> 2;
                                default -> -1;
                        };//00 : yyy
                        case 13-> switch (orientation) {
                                case 0 -> 9;
                                case 1 -> 8;
                                case 2 -> 11;
                                case 3 -> 10;
                                case 4 -> 5;
                                case 5 -> 4;
                                case 6 -> 7;
                                case 7 -> 6;
                                case 8 -> 1;
                                case 9 -> 0;
                                case 10 -> 3;
                                case 11 -> 2;
                                default -> -1;
                        };//xy : y,    zyx
                        case 14-> switch (orientation) {
                                case 0 -> 11;
                                case 1 -> 8;
                                case 2 -> 9;
                                case 3 -> 10;
                                case 4 -> 7;
                                case 5 -> 4;
                                case 6 -> 5;
                                case 7 -> 6;
                                case 8 -> 3;
                                case 9 -> 2;
                                case 10 -> 1;
                                case 11 -> 0;
                                default -> -1;
                        };//xy : yxx,  zzy
                        case 15-> switch (orientation) {
                                case 0 -> 11;
                                case 1 -> 8;
                                case 2 -> 9;
                                case 3 -> 10;
                                case 4 -> 5;
                                case 5 -> 4;
                                case 6 -> 7;
                                case 7 -> 6;
                                case 8 -> 1;
                                case 9 -> 2;
                                case 10 -> 3;
                                case 11 -> 0;
                                default -> -1;
                        };//00 : xxy,  xyz,  xzx,  yzz,  zxz
                        default -> orientation;
                };
        Point newPoint = point.rotate(rot);
        newPoint = switch (rot)
        {
                case 1, 12 -> switch (ori)
                {
                        case 8,9,10,11 -> new Point(newPoint.x(),newPoint.y(),newPoint.z()-2);
                        default -> newPoint;
                };
                case 2, 15 -> switch (ori)
                {
                        case 4,5,6,7 -> new Point(newPoint.x(),newPoint.y()-2,newPoint.z());
                        default -> newPoint;
                };
                case 3, 14 -> switch (ori)
                {
                        case 4,5,6,7 -> new Point(newPoint.x(),newPoint.y()-2,newPoint.z());
                        case 8,9,10,11 -> new Point(newPoint.x(),newPoint.y(),newPoint.z()-2);
                        default -> newPoint;
                };
                case 4, 9 -> switch (ori)
                {
                        case 0,1,2,3 -> new Point(newPoint.x()-2,newPoint.y(),newPoint.z());
                        default -> newPoint;
                };
                case 5, 8 -> switch (ori)
                {
                        case 0,1,2,3 -> new Point(newPoint.x()-2,newPoint.y(),newPoint.z());
                        case 8,9,10,11 -> new Point(newPoint.x(),newPoint.y(),newPoint.z()-2);
                        default -> newPoint;
                };
                case 6, 11 -> switch (ori)
                {
                        case 0,1,2,3 -> new Point(newPoint.x()-2,newPoint.y(),newPoint.z());
                        case 4,5,6,7 -> new Point(newPoint.x(),newPoint.y()-2,newPoint.z());
                        default -> newPoint;
                };
                case 7, 10 -> switch (ori)
                {
                        case 0,1,2,3 -> new Point(newPoint.x()-2,newPoint.y(),newPoint.z());
                        case 4,5,6,7 -> new Point(newPoint.x(),newPoint.y()-2,newPoint.z());
                        case 8,9,10,11 -> new Point(newPoint.x(),newPoint.y(),newPoint.z()-2);
                        default -> newPoint;
                };
                case 13-> switch (ori)
                {
                        default -> newPoint;
                };
                default -> newPoint;
        };
        return new T(newPoint,ori);
    }

    public Point getPoint() {
        return point;
    }
    public int getOrientation() {
        return orientation;
    }

    public T setPoint(Point point) {
        return new T(point,orientation);
    }
    public T setOrientation(int orientation) {
        return new T(point,orientation);
    }

    @Override
    public int compareTo(Object obj) {
        return obj instanceof T? point.compareTo(((T)obj).getPoint()):0;
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof T && ((T)obj).getPoint().equals(point) && ((T)obj).getOrientation()==orientation;
    }
    @Override
    public String toString() {
            return point.toString() + ":" + orientation;
    }
}
