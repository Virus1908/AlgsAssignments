import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private static class Node {
        private final boolean isVertical;
        private final Point2D value;
        private Node left;
        private Node right;

        private Node(boolean isVertical, Point2D value) {
            this.isVertical = isVertical;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    public KdTree() {
        size = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(root, p, false);
    }

    private Node insert(Node x, Point2D p, boolean isPreviousVertical) {
        if (x == null) {
            size++;
            return new Node(!isPreviousVertical, p);
        }
        if (x.value.equals(p)) {
            return x;
        }
        int cmp;
        if (x.isVertical) {
            cmp = Double.compare(p.x(), x.value.x());
        } else {
            cmp = Double.compare(p.y(), x.value.y());
        }
        if (cmp < 0) x.left = insert(x.left, p, x.isVertical);
        else x.right = insert(x.right, p, x.isVertical);
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node containNode = contains(root, p);
        return containNode != null;
    }

    private Node contains(Node x, Point2D p) {
        if (x == null) {
            return null;
        }
        if (x.value.equals(p)) {
            return x;
        }
        int cmp;
        if (x.isVertical) {
            cmp = Double.compare(p.x(), x.value.x());
        } else {
            cmp = Double.compare(p.y(), x.value.y());
        }
        if (cmp < 0) return contains(x.left, p);
        else return contains(x.right, p);
    }

    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node node, RectHV prevRect) {
        if (node == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        node.value.draw();
        if (node.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.value.x(), prevRect.ymin(), node.value.x(), prevRect.ymax());
            draw(node.left, new RectHV(prevRect.xmin(), prevRect.ymin(), node.value.x(), prevRect.ymax()));
            draw(node.right, new RectHV(node.value.x(), prevRect.ymin(), prevRect.xmax(), prevRect.ymax()));
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(prevRect.xmin(), node.value.y(), prevRect.xmax(), node.value.y());
            draw(node.left, new RectHV(prevRect.xmin(), prevRect.ymin(), prevRect.xmax(), node.value.y()));
            draw(node.right, new RectHV(prevRect.xmin(), node.value.y(), prevRect.xmax(), prevRect.ymax()));
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> points = new ArrayList<>();
        range(root, rect, new RectHV(0, 0, 1, 1), points);
        return points;
    }

    private void range(Node node, RectHV rect, RectHV prevRect, ArrayList<Point2D> points) {
        if (node == null) {
            return;
        }
        if (!prevRect.intersects(rect)) {
            return;
        }
        if (rect.contains(node.value)) {
            points.add(node.value);
        }
        if (node.isVertical) {
            range(node.left, rect, new RectHV(prevRect.xmin(), prevRect.ymin(), node.value.x(), prevRect.ymax()), points);
            range(node.right, rect, new RectHV(node.value.x(), prevRect.ymin(), prevRect.xmax(), prevRect.ymax()), points);
        } else {
            range(node.left, rect, new RectHV(prevRect.xmin(), prevRect.ymin(), prevRect.xmax(), node.value.y()), points);
            range(node.right, rect, new RectHV(prevRect.xmin(), node.value.y(), prevRect.xmax(), prevRect.ymax()), points);
        }
    }

    private static class Bundle {
        private double distance;
        private Point2D point;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Bundle bundle = nearest(root, p, new RectHV(0, 0, 1, 1), null);
        return bundle != null ? bundle.point : null;
    }

    private Bundle nearest(Node node, Point2D p, RectHV prevRect, Bundle bundle) {
        if (node == null) {
            return bundle;
        }
        if (bundle == null) {
            bundle = new Bundle();
            bundle.point = node.value;
            bundle.distance = node.value.distanceSquaredTo(p);
        } else {
            if (node.value.distanceSquaredTo(p) < bundle.distance) {
                bundle.distance = node.value.distanceSquaredTo(p);
                bundle.point = node.value;
            }
        }
        RectHV leftRect;
        RectHV rightRect;
        int cmp;
        if (node.isVertical) {
            leftRect = new RectHV(prevRect.xmin(), prevRect.ymin(), node.value.x(), prevRect.ymax());
            rightRect = new RectHV(node.value.x(), prevRect.ymin(), prevRect.xmax(), prevRect.ymax());
            cmp = Double.compare(p.x(), node.value.x());
        } else {
            leftRect = new RectHV(prevRect.xmin(), prevRect.ymin(), prevRect.xmax(), node.value.y());
            rightRect = new RectHV(prevRect.xmin(), node.value.y(), prevRect.xmax(), prevRect.ymax());
            cmp = Double.compare(p.y(), node.value.y());
        }
        if (cmp < 0) {
            if (leftRect.distanceSquaredTo(p) < bundle.distance) {
                bundle = nearest(node.left, p, leftRect, bundle);
            }
            if (rightRect.distanceSquaredTo(p) < bundle.distance) {
                bundle = nearest(node.right, p, rightRect, bundle);
            }
        } else {
            if (rightRect.distanceSquaredTo(p) < bundle.distance) {
                bundle = nearest(node.right, p, rightRect, bundle);
            }
            if (leftRect.distanceSquaredTo(p) < bundle.distance) {
                bundle = nearest(node.left, p, leftRect, bundle);
            }
        }
        return bundle;
    }
}
