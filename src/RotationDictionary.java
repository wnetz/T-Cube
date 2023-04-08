import java.util.ArrayList;
import java.util.Arrays;

public class RotationDictionary {
    public static ArrayList<Integer[]> rotations = new ArrayList();
    public static Integer[] rot0 = new Integer[]{0 ,3 ,2, 1, 6, 5, 4, 7, 8, 9, 10,11};// x, y,-z : xy : 00
    public static Integer[] rot0 = new Integer[]{};
    public static Integer[] rot0 = new Integer[]{};
    public static Integer[] rot0 = new Integer[]{};
    public static Integer[] rot0 = new Integer[]{1 ,2 ,3 ,0 ,11,8 ,9 ,10,5 ,4 ,7 ,6 };// x, z,-y : 00 : x
    public static Integer[] rot0 = new Integer[]{3 ,0 ,1 ,2 ,9 ,8 ,11,10,5 ,6 ,7 ,4};// x,-z, y : 00 : xxx
    public static Integer[] rot0 = new Integer[]{3 ,2 ,1 ,0 ,8 ,11,10,9 ,4 ,7 ,6 ,5 };// x,-z,-y : xy : x,    yxz
    public static Integer[] rot0 = new Integer[]{0 ,1 ,2 ,3 ,6 ,5 ,4 ,7 ,10,9 ,8 ,11};//-x, y, z : xy : xxzz, xyyx, yy,   yzyx, zxyx, zyxy, zyyz, zyzx, zzxx
    public static Integer[] rot0 = new Integer[]{0 ,3 ,2 ,1 ,6 ,7 ,4 ,5 ,10,9 ,8 ,11};//-x, y,-z : 00 : yy
    public static Integer[] rot0 = new Integer[]{2 ,1 ,0 ,3 ,6 ,5 ,4 ,7 ,10,11,8 ,9 };//-x,-y, z : 00 : zz
    public static Integer[] rot0= new Integer[]{2 ,3 ,0 ,1 ,6 ,7 ,4 ,5 ,10,11,8 ,9 };//-x,-y,-z : xy : xxyy, xyzy, xzxy, xzyz, xzzx, yyxx, yzzy, zxzy, zz
    public static Integer[] rot0= new Integer[]{3 ,2 ,1 ,0 ,11,10,9 ,8 ,7 ,6 ,5 ,4 };//-x, z, y : 00 : xyy,  yzy,  zxy,  zyz,  zzx
    public static Integer[] rot0= new Integer[]{3 ,0 ,1 ,2 ,10,9 ,8 ,11,6 ,7 ,4 ,5 };//-x, z,-y : xy : xzz,  yyx
    public static Integer[] rot0= new Integer[]{1 ,2 ,3 ,0 ,10,11,8 ,9 ,6 ,5 ,4 ,7 };//-x,-z, y : xy : xyy,  yzy,  zxy,  zyz,  zzx
    public static Integer[] rot0= new Integer[]{3 ,2 ,1 ,0 ,10,11,8 ,9 ,6 ,7 ,4 ,5 };//-x,-z,-y : 00 : yyx    
    public static Integer[] rot0= new Integer[]{4 ,5 ,6 ,7 ,0 ,1 ,2 ,3 ,9 ,8 ,11,10};// y, x, z : xy : xxz,  zyy
    public static Integer[] rot0= new Integer[]{4 ,7 ,6 ,5 ,0 ,3 ,2 ,1 ,9 ,8 ,11,10};// y, x,-z : 00 : xxz,  zyy
    public static Integer[] rot0= new Integer[]{4 ,5 ,6 ,7 ,2 ,1 ,0 ,3 ,11 ,8 ,9,10};// y,-x, z : 00 : zzz
    public static Integer[] rot0= new Integer[]{4 ,7 ,6 ,5 ,2 ,3 ,0 ,1 ,11 ,8 ,9,10};// y,-x,-z : xy : zzz
    public static Integer[] rot0= new Integer[]{10,11,8 ,9 ,2 ,1 ,0 ,3 ,4 ,7 ,6 ,5 };// y, z, x : 00 : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
    public static Integer[] rot0= new Integer[]{10,9 ,8 ,11,1 ,0 ,3 ,2 ,5 ,6 ,7 ,4 };// y, z,-x : xy : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
    public static Integer[] rot0= new Integer[]{10,11,8 ,9 ,3 ,2 ,1 ,0 ,7 ,6 ,5 ,4 };// y,-z, x : xy : xyyy, yxxz, yzyy, zxyy, zyzy, zzxy, zzyz, zzzx
    public static Integer[] rot0= new Integer[]{10,11,8 ,9 ,1 ,2 ,3 ,0 ,5 ,6 ,7 ,4 };// y,-z,-x : 00 : xxxy, xxyz, xxzx, xyzz, xzxz, yzzz, zxzz, zyyx
    public static Integer[] rot0= new Integer[]{7 ,4 ,5 ,6 ,1 ,0 ,3 ,2 ,9 ,10,11,8 };//-y, x, z : 00 : z
    public static Integer[] rot0= new Integer[]{4 ,7 ,6 ,5 ,2 ,3 ,0 ,1 ,11,8 ,9 ,10};//-y, x,-z : xy : xzy,  z
    public static Integer[] rot0= new Integer[]{4 ,5 ,6 ,7 ,0 ,1 ,2 ,3 ,9 ,8 ,11,10};//-y,-x, z : xy : xyx,  yxy,  yyz,  yzx,  zxx
    public static Integer[] rot0= new Integer[]{5 ,6 ,7 ,4 ,3 ,0 ,1 ,2 ,9 ,8 ,11,10};//-y,-x,-z : 00 : yxy
    public static Integer[] rot0= new Integer[]{8 ,9 ,10,11,3 ,0 ,1 ,2 ,7 ,4 ,5 ,6 };//-y, z, x : xy : xyxx, xzzy, yxyx, yyxy, yyyz, yyzx, yzxx, zxxx
    public static Integer[] rot0= new Integer[]{8 ,11,10,9 ,2 ,3 ,0 ,1 ,6 ,5 ,4 ,7 };//-y, z,-x : 00 : zx
    public static Integer[] rot0= new Integer[]{8 ,9 ,10,11,0 ,1 ,2 ,3 ,4 ,5 ,6 ,7 };//-y,-z, x : 00 : yxyx
    public static Integer[] rot0= new Integer[]{8 ,11,10,9 ,1 ,2 ,3 ,0 ,5 ,4 ,7 ,6 };//-y,-z,-x : xy : xy,   xzyx, yxzy, yz,   zx,   zyxz
    public static Integer[] rot0= new Integer[]{5 ,6 ,7 ,4 ,11,10,9 ,8 ,2 ,1 ,0 ,3 };// z, x, y : 00 : yxxx
    public static Integer[] rot0= new Integer[]{7 ,6 ,5 ,4 ,11,10,9 ,8 ,2 ,3 ,0 ,1 };// z, x,-y : xy : xxzy, xz,   yxzz, yyyx, zyyy
    public static Integer[] rot0= new Integer[]{5 ,6 ,7 ,4 ,9 ,10,11,8 ,0 ,1 ,2 ,3 };// z,-x, y : xy : xxyx, xyxy, xyyz, xyzx, xzxx, yxyy, yyzy, yzxy, yzyz, yzzx, zxxy, zxyz, zxzx, zyzz, zzxz
    public static Integer[] rot0= new Integer[]{4 ,5 ,6 ,7 ,8 ,9 ,10,11,0 ,1 ,2 ,3 };// z,-x,-y : 00 : yx
    public static Integer[] rot0= new Integer[]{9 ,10,11,8 ,7 ,6 ,5 ,4 ,3 ,0 ,1 ,2 };// z, y, x : xy : yyy
    public static Integer[] rot0= new Integer[]{9 ,8 ,11,10,5 ,6 ,7 ,4 ,3 ,0 ,1 ,2 };// z, y,-x : 00 : y,    zyx
    public static Integer[] rot0= new Integer[]{11,8 ,9 ,10,5 ,4 ,7 ,6 ,1 ,2 ,3 ,0 };// z,-y, x : 00 : yxx
    public static Integer[] rot0= new Integer[]{11,10,9 ,8 ,5 ,6 ,7 ,4 ,1 ,2 ,3 ,0 };// z,-y,-x : xy : xxy,  xyz,  xzx,  yzz,  zxz
    public static Integer[] rot0= new Integer[]{5 ,4 ,7 ,6 ,11,8 ,9 ,10,2 ,1 ,0 ,3 };//-z, x, y : xy : xxxz, xzyy, yxxx, zy,   zzyx
    public static Integer[] rot0= new Integer[]{7 ,4 ,5 ,6 ,11,8 ,9 ,10,2 ,3 ,0 ,1 };//-z, x,-y : 00 : xxzy, xz,   yxzz, yyyx, zyyy
    public static Integer[] rot0= new Integer[]{5 ,4 ,7 ,6 ,9 ,8 ,11,10,0 ,1 ,2 ,3 };//-z,-x, y : 00 : yzzx
    public static Integer[] rot0= new Integer[]{7 ,4 ,5 ,6 ,9 ,8 ,11,10,0 ,3 ,2 ,1 };//-z,-x,-y : xy : xzzz, yx,   yyxz, zyxx, zzzy
    public static Integer[] rot0= new Integer[]{9 ,10,11,8 ,7 ,4 ,5 ,6 ,1 ,0 ,3 ,2 };//-z, y, x : 00 : yyy
    public static Integer[] rot0= new Integer[]{9 ,8 ,11,10,5 ,4 ,7 ,6 ,1 ,0 ,3 ,2 };//-z, y,-x : xy : y,    zyx
    public static Integer[] rot0= new Integer[]{11,8 ,9 ,10,7 ,4 ,5 ,6 ,3 ,2 ,1 ,0 };//-z, y,-x : xy : yxx,  zzy
    public static Integer[] rot0= new Integer[]{11,8 ,9 ,10,5 ,4 ,7 ,6 ,1 ,2 ,3 ,0 };//-z, y,-x : 00 : yzz

    public final int[][][] orientations =
    {
            //             x1:0                               x2:1                               x3:2                               x4:3
            {{ 1, 0, 0},{ 2, 0, 0},{ 1, 1, 0}},{{ 1, 0, 0},{ 2, 0, 0},{ 1, 0, 1}},{{ 1, 0, 0},{ 2, 0, 0},{ 1,-1, 0}},{{ 1, 0, 0},{ 2, 0, 0},{ 1, 0,-1}},
            //             y1:4                               y2:5                               y3:6                               y4:7
            {{ 0, 1, 0},{ 0, 2, 0},{ 0, 1, 1}},{{ 0, 1, 0},{ 0, 2, 0},{ 1, 1, 0}},{{ 0, 1, 0},{ 0, 2, 0},{ 0, 1,-1}},{{ 0, 1, 0},{ 0, 2, 0},{-1, 1, 0}},
            //             z1:8                               z2:9                               z3:10                              z4:11
            {{ 0, 0, 1},{ 0, 0, 2},{ 1, 0, 1}},{{ 0, 0, 1},{ 0, 0, 2},{ 0, 1, 1}},{{ 0, 0, 1},{ 0, 0, 2},{-1, 0, 1}},{{ 0, 0, 1},{ 0, 0, 2},{ 0,-1, 1}},
            //            xh1:12                             xh2:13                             xh3:14                             xh4:15
            {{-1,-1, 0},{ 0,-1, 0},{ 1,-1, 0}},{{-1, 0,-1},{ 0, 0,-1},{ 1, 0,-1}},{{-1, 1, 0},{ 0, 1, 0},{ 1, 1, 0}},{{-1, 0, 1},{ 0, 0, 1},{ 1, 0, 1}},
            //            yh1:16                             yh2:17                             yh3:18                             yh4:19
            {{ 0,-1,-1},{ 0, 0,-1},{ 0, 1,-1}},{{-1,-1, 0},{-1, 0, 0},{-1, 1, 0}},{{ 0,-1, 1},{ 0, 0, 1},{ 0, 1, 1}},{{ 1,-1, 0},{ 1, 0, 0},{ 1, 1, 0}},
            //            zh1:20                             zh2:21                             zh3:22                             zh4:23
            {{-1, 0,-1},{-1, 0, 0},{-1, 0, 1}},{{ 0,-1,-1},{ 0,-1, 0},{ 0,-1, 1}},{{ 1, 0,-1},{ 1, 0, 0},{ 1, 0, 1}},{{0 , 1,-1},{ 0, 1, 0},{ 0, 1, 1}},
            //            x12:24                             x22:25                             x32:26                             x42:27
            {{-1, 0, 0},{-2, 0, 0},{-1, 1, 0}},{{-1, 0, 0},{-2, 0, 0},{-1, 0, 1}},{{-1, 0, 0},{-2, 0, 0},{-1,-1, 0}},{{-1, 0, 0},{-2, 0, 0},{-1, 0,-1}},
            //            y12:28                             y22:29                             y32:30                             y42:31
            {{ 0,-1, 0},{ 0,-2, 0},{ 0,-1, 1}},{{ 0,-1, 0},{ 0,-2, 0},{ 1,-1, 0}},{{ 0,-1, 0},{ 0,-2, 0},{ 0,-1,-1}},{{ 0,-1, 0},{ 0,-2, 0},{-1,-1, 0}},
            //            z12:32                             z22:33                             z32:34                             z42:35
            {{ 0, 0,-1},{ 0, 0,-2},{ 1, 0,-1}},{{ 0, 0,-1},{ 0, 0,-2},{ 0, 1,-1}},{{ 0, 0,-1},{ 0, 0,-2},{-1, 0,-1}},{{ 0, 0,-1},{ 0, 0,-2},{ 0,-1,-1}},
            //            xc1:36                             xc2:37                             xc3:38                             xc4:39
            {{-1, 0, 0},{ 0, 1, 0},{ 1, 0, 0}},{{-1, 0, 0},{ 0, 0, 1},{ 1, 0, 0}},{{-1, 0, 0},{ 0,-1, 0},{ 1, 0, 0}},{{-1, 0, 0},{ 0, 0,-1},{ 1, 0, 0}},
            //            yc1:40                             yc2:41                             yc3:42                             yc4:43
            {{ 0,-1, 0},{ 0, 0, 1},{ 0, 1, 0}},{{ 0,-1, 0},{ 1, 0, 0},{ 0, 1, 0}},{{ 0,-1, 0},{ 0, 0,-1},{ 0, 1, 0}},{{ 0,-1, 0},{-1, 0, 0},{ 0, 1, 0}},
            //            zc1:44                             zc2:45                             zc3:46                             zc4:47
            {{ 0, 0,-1},{ 1, 0, 0},{ 0, 0, 1}},{{ 0, 0,-1},{ 0, 1, 0},{ 0, 0, 1}},{{ 0, 0,-1},{-1, 0, 0},{ 0, 0, 1}},{{ 0, 0,-1},{ 0,-1, 0},{ 0, 0, 1}},
    };
    /*
    1u: 5d 1l 3l
    1r: 4d 1u 5l
    1d: 2d 1r 4l
    1l: 3d 1d 2l
    2u: 1u 2l 3u
    2r: 4r 2u 1r
    2d: 6u 2r 4d
    2l: 3l 2d 6r
    3u: 1r 3l 5u
    3r: 2r 3u 1d
    3d: 6l 3r 2d
    3l: 5l 3d 6u
    4u: 1l 4l 2u
    4r: 5r 4u 1u
    4d: 6r 4r 5d
    4l: 2l 4d 6d
    5u: 1d 5l 4u
    5r: 3r 5u 1l
    5d: 6d 5r 3d
    5l: 4l 5d 6l
    6u: 5u 6l 4r
    6r: 3u 6u 5r
    6d: 2u 6r 3r
    6l: 4u 6d 2r

    1r: yyy
    1d: yy
    1l: y
    2u: xxx
    2r: yxyx
    2d: yyx
    2l: yz
    3u: yzzx
    3r: yxy
    3d: yx
    3l: z
    4u: yxxx
    4r: zzz
    4d: yyyx
    4l: yyz
    5u: xyy
    5r: xy
    5d: x
    5l: zx
    6u: zz
    6r: yzz
    6d: xx
    6l: yxx
     */
}
