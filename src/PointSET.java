import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> set;

    public PointSET() {
        set = new TreeSet<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return set.contains(p);
    }

    public void draw() {
        for (Point2D point2D : set) {
            point2D.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> points = new ArrayList<>();
        for (Point2D point2D : set) {
            if (rect.contains(point2D)) {
                points.add(point2D);
            }
        }
        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        double nearestDistance = 0;
        Point2D nearestPoint = null;
        for (Point2D point2D : set) {
            if (nearestPoint == null || p.distanceSquaredTo(point2D) < nearestDistance) {
                nearestPoint = point2D;
                nearestDistance = p.distanceSquaredTo(point2D);
            }
        }
        return nearestPoint;
    }
}
