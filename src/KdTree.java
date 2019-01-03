import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
        Node iter = root;
        while (true) {
            Comparator<Point2D> comparator = iter.level % 2 == 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;
            int cmp = comparator.compare(p, iter.point);
            if (cmp == 0 && p.equals(iter.point)) return;
            if (cmp < 0) {
                if (iter.left != null) {
                    iter = iter.left;
                    continue;
                } else {
                    iter.left = new Node(p);
                    iter.left.level = iter.level + 1;
                    size++;
                    break;
                }
            } else {
                if (iter.right != null) {
                    iter = iter.right;
                    continue;
                } else {
                    iter.right = new Node(p);
                    iter.right.level = iter.level + 1;
                    size++;
                    break;
                }
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        Node iter = root;

        while (iter != null) {
            Comparator<Point2D> comparator = iter.level % 2 == 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;
            int cmp = comparator.compare(p, iter.point);

            if (cmp == 0 && p.equals(iter.point)) {
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
        node.point.draw();
        draw(node.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        LinkedList<Point2D> list = new LinkedList<>();
        range(root, rect, list);
        return list;
    }

    private void range(Node node, RectHV rect, List<Point2D> accumulator) {
        if (node == null) return;

        Point2D point = node.point;
        if (rect.contains(point)) accumulator.add(point);

        if (node.level % 2 == 0) {
            if (point.x() <= rect.xmax() && point.x() >= rect.xmin()) {
                range(node.left, rect, accumulator);
                range(node.right, rect, accumulator);
            } else if (point.x() > rect.xmax()) {
                range(node.left, rect, accumulator);
            } else if (point.x() < rect.xmin()) {
                range(node.right, rect, accumulator);
            }
        } else {
            if (point.y() <= rect.ymax() && point.y() >= rect.ymin()) {
                range(node.left, rect, accumulator);
                range(node.right, rect, accumulator);
            } else if (point.y() > rect.ymax()) {
                range(node.left, rect, accumulator);
            } else if (point.y() < rect.ymin()) {
                range(node.right, rect, accumulator);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (isEmpty()) return null;


        Node nearest = nearest(root, p);
        return nearest != null ? nearest.point : null;
    }

    private Node nearest(Node node, Point2D p) {
        if (node == null) return null;

        Node nearest = node;


        Node rightNearest = nearest(node.right, p);
        if (rightNearest != null) {
            double rightDist = rightNearest.point.distanceSquaredTo(p);
            if (rightDist < nearest.point.distanceSquaredTo(p)) nearest = rightNearest;
        }

        Node leftNearest = nearest(node.left, p);
        if (leftNearest != null) {
            double leftDist = leftNearest.point.distanceSquaredTo(p);
            if (leftDist < nearest.point.distanceSquaredTo(p)) nearest = leftNearest;
        }

        return nearest;
    }

    private int compare(Point2D a, Point2D b, int level) {
        Comparator<Point2D> comparator = level % 2 == 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;
        return comparator.compare(a, b);
    }

    private static class Node {
        Point2D point;
        int level;
        Node left;
        Node right;

        Node(Point2D point) {
            this.point = point;
        }
    }
}
