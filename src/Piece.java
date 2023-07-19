import java.util.ArrayList;
import java.util.Hashtable;

public class Piece implements Comparable
{
    private ArrayList<Point> base;// only used in program initialization
    private Point point;
    private int orientation;
    public static  Hashtable<Point,ArrayList<Integer>> rotations;
    public static Object[] rotationKeys;

    /**
     * used exclusively for use in Main finding all rotations of the base piece
     * @param points set of x,y,z points that compose base piece
     */
    public Piece(int[][] points)
    {
        base = new ArrayList<>();
        base.add(new Point(0,0,0));
        for (int[] point:points)
        {
            base.add(new Point(point[0],point[1],point[2]));
        }
    }
    public Piece(Point point, int orientation) {
        this.point = point;
        this.orientation = orientation;
    }
    /**
     * used exclusively for use in Main finding all rotations of the base piece
     * @return all points the piece inhabits
     */
    public ArrayList<Point> get_array()
    {
        return base;
    }
    public Point get_point() {
        return point;
    }
    public int get_orientation() {
        return orientation;
    }

    public Piece rotate(int rotation)
    {
        Point rot = (Point)rotationKeys[rotation];// this piece of shit was nearly 60% of the run time
        int ori = rotations.get(rot).get(orientation);
        Point rot2 = Point.subtract(SolutionState.ORIENTATIONS.get(orientation).get(2),point).rotate(rot);
        rot = point.rotate(rot);
        //gets the point closer to origin because base orientations are defined by point closest to origin
        rot = rot.compareTo(rot2) > 0 ? rot2:rot;
        return new Piece(rot,ori);
    }

    @Override
    public int compareTo(Object obj) {
        return obj instanceof Piece? point.compareTo(((Piece)obj).get_point()):0;
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Piece && ((Piece)obj).get_point().equals(point) && ((Piece)obj).get_orientation()==orientation;
    }
    @Override
    public String toString() {
        return point.toString() + ":" + orientation;
    }

}
