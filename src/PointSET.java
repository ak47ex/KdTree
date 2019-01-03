import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        Iterable<Point2D> subset = set.subSet(new Point2D(rect.xmin(), rect.ymin()), true, new Point2D(rect.xmax(), rect.ymax()), true);

        Point2D bot = new Point2D(rect.xmin(), rect.ymin());
        Point2D top = new Point2D(rect.xmax(), rect.ymax());
        List<Point2D> list = new LinkedList<>();
        for (Point2D p : subset) {
            if (Point2D.X_ORDER.compare(p, bot) > -1 && Point2D.X_ORDER.compare(p, top) < 1) list.add(p);
        }

        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        Point2D floor = set.floor(p);
        Point2D ceil = set.ceiling(p);
        if (floor == null) return ceil;
        if (ceil == null) return floor;

        return p.distanceTo(floor) < p.distanceTo(ceil) ? floor : ceil;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

}
