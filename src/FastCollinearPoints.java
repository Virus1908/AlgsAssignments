import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> segments;

    public FastCollinearPoints(Point[] givenPoints) {
        checkForNull(givenPoints);
        Point[] points = Arrays.copyOf(givenPoints, givenPoints.length);
        segments = new ArrayList<>();
        Arrays.sort(points);
        checkForDuplicates(points);
        Point[] aux;
        for (int i = 0; i < points.length - 3; i++) {
            Point p = points[i];
            aux = Arrays.copyOf(points, points.length);
            Arrays.sort(aux, i, points.length, p.slopeOrder());
            findSegments(aux, i + 1, p);
        }
    }

    private void findSegments(Point[] aux, int startIndex, Point p) {
        int sameSlopeIndex = startIndex;
        double currentSlope = p.slopeTo(aux[startIndex]);
        for (int j = startIndex + 1; j < aux.length; j++) {
            double newSlope = p.slopeTo(aux[j]);
            if (newSlope == currentSlope) {
                continue;
            }
            if (j - sameSlopeIndex > 2) {
                segments.add(new LineSegment(p, aux[j - 1]));
            }
            sameSlopeIndex = j;
            currentSlope = newSlope;
        }
        if (aux.length - sameSlopeIndex > 2) {
            segments.add(new LineSegment(p, aux[aux.length - 1]));
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
