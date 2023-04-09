public class T implements Comparable
{
    private Point point;
    private int orientation;

    public T(Point point, int orientation) {
        this.point = point;
        this.orientation = orientation;
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
