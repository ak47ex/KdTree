import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class KdTree {

    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (root == null) {
            root = new Node(p);
            size++;
            return;
        }
        int level = 0;
        Node iter = root;
        while (true) {
            Comparator<Point2D> comparator = level % 2 == 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;
            int cmp = comparator.compare(p, iter.point);
            level++;
            if (cmp < 0) {
                if (iter.left != null) {
                    iter = iter.left;
                    continue;
                } else {
                    iter.left = new Node(p);
                    size++;
                    break;
                }
            } else if (cmp > 0){
                if (iter.right != null) {
                    iter = iter.right;
                    continue;
                } else {
                    iter.right = new Node(p);
                    size++;
                    break;
                }
            }
            break;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        Node iter = root;

        int level = 0;
        while (iter != null) {
            int cmp = compare(p, iter.point, level++);

            if (cmp == 0) {
                return true;
            }

            if (cmp < 0) {
                iter = iter.left;
            } else {
                iter = iter.right;
            }
        }

        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;

        draw(node.left);
        StdDraw.point(node.point.x(), node.point.y());
        draw(node.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        LinkedList<Point2D> list = new LinkedList<>();

        return Collections.emptyList();
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (isEmpty()) return null;

        Node iter = root;

        int level = 0;
        while (iter != null) {
            Comparator<Point2D> comparator = level % 2 == 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;
            int cmp = comparator.compare(p, iter.point);
            level++;

            if (cmp == 0) return iter.point;

            if (cmp < 0) {
                if (iter.left == null) return iter.point;
                double dst1 = p.distanceTo(iter.point);
                double dst2 = p.distanceTo(iter.left.point);

                if (dst1 < dst2) return iter.point;
                iter = iter.left;
            } else {
                if (iter.right == null) return iter.point;

                double dst1 = p.distanceTo(iter.point);
                double dst2 = p.distanceTo(iter.right.point);

                if (dst1 < dst2) return iter.point;
                iter = iter.right;
            }
        }

        return null;
    }

    private int compare(Point2D a, Point2D b, int level) {
        Comparator<Point2D> comparator = level % 2 == 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;
        return comparator.compare(a, b);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        kdtree.insert(new Point2D(0.5, 0.4));
        kdtree.insert(new Point2D(0.3, 0.3));
        kdtree.insert(new Point2D(0.2, 0.2));
        kdtree.insert(new Point2D(0.1, 0.1));

        System.out.println(kdtree.nearest(new Point2D(0.35, 0.4)));
    }

    private static class Node {
        Point2D point;
        RectHV rect;
        Node left;
        Node right;

        Node(Point2D point) {
            this.point = point;
        }
    }
}
