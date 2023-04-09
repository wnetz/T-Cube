import java.util.ArrayList;
import java.util.Hashtable;

public class Piece implements Comparable
{
    private ArrayList<Point> base;
    private Point point;
    private int orientation;
    private Hashtable<Point,ArrayList<Integer>> rotations;

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

    public Piece set_point(Point point) {
        return new Piece(point,orientation);
    }
    public Piece set_orientation(int orientation) {
        return new Piece(point,orientation);
    }

    public Piece rotate(int rotation)
    {
        Point rot = (Point)rotations.keySet().toArray()[rotation];
        point = point.rotate(rot);
        return new Piece(point,rotations.get(rotations).get(orientation));
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
