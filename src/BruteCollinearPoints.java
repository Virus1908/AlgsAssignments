import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> segments;

    public BruteCollinearPoints(Point[] givenPoints) {
        checkForNull(givenPoints);
        Point[] points = Arrays.copyOf(givenPoints, givenPoints.length);
        segments = new ArrayList<>();
        Arrays.sort(points);
        checkForDuplicates(points);
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            for (int j = i + 1; j < points.length; j++) {
                Point q = points[j];
                double pq = p.slopeTo(q);
                for (int k = j + 1; k < points.length; k++) {
                    Point r = points[k];
                    double qr = q.slopeTo(r);
                    if (pq == qr) {
                        for (int si = k + 1; si < points.length; si++) {
                            Point s = points[si];
                            double rs = r.slopeTo(s);
                            if (qr == rs) {
                                segments.add(new LineSegment(p, s));
                            }
                        }
                    }
                }
            }
        }

    }

    private void checkForNull(Point[] p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        for (Point point : p) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void checkForDuplicates(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }
}
