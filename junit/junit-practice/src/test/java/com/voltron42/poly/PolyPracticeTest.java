package com.voltron42.poly;

import org.junit.Test;

import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.junit.Assert.*;

public class PolyPracticeTest {

    private boolean pointInPoly(Point point, Polygon poly) {
        Point[] points = poly.getPoints().toArray(new Point[]{});
        int i, j, nvert = points.length;
        boolean c = false;

        for(i = 0, j = nvert - 1; i < nvert; j = i++) {
            if( ( (points[i].y >= point.y ) != (points[j].y >= point.y) ) &&
                    (point.x <= (points[j].x - points[i].x) * (point.y - points[i].y) / (points[j].y - points[i].y) + points[i].x)) {
                c = !c;
            }
        }
        return c;
    }

    @Test
    public void testPointInPoly() {
        Polygon square = new Polygon(point(0,10),
                point(10, 10),
                point(10,0),
                point(0,0));
        Polygon zigzag = new Polygon(point(0, 10),
                point(0, 20),
                point(15, 20),
                point(15, 5),
                point(25, 5),
                point(25, 0),
                point(10, 0),
                point(10, 10));
        assertTrue(pointInPoly(point(5,5), square));
        assertFalse(pointInPoly(point(15,5), square));
        assertFalse(pointInPoly(point(5, 5), zigzag));
        assertFalse(pointInPoly(point(20, 15), zigzag));
        assertTrue(pointInPoly(point(12, 10), zigzag));
    }

    @Test
    public void testBisect() {
        assertEquals(point(15, 15), bisect(point(10, 10), point(20, 20)));
        assertEquals(point(15, 15), bisect(point(10, 20), point(20, 10)));
        assertEquals(point(15, 30), bisect(point(10, 40), point(20, 20)));
    }

    @Test
    public void testRasterLine() {
        System.out.println(rasterLine(point(0, 0), point(10, 10)));
        System.out.println(rasterLine(point(0, 1), point(10, 11)));
        System.out.println(rasterLine(point(1, 0), point(11, 10)));
        System.out.println(rasterLine(point(0, 0), point(10, 20)));
        System.out.println(rasterLine(point(0, 0), point(10, 15)));
    }

    @Test
    public void testRasterPoly() {
        System.out.println(rasterPoly(new Polygon(point(0, 0), point(5, 5), point(0, 5))));
        System.out.println(rasterPoly(new Polygon(point(1, 1), point(5, 5), point(1, 5))));
    }

    private List<Point> rasterPoly(Polygon poly) {
        Set<Point> outSet = new HashSet<Point>();
        Point[] points = poly.getPoints().toArray(new Point[]{});
        Point first = points[0];
        Point min = first;
        Point max = first;
        for (int i = 1; i < points.length; i++) {
            min = point(min(min.x, points[i].x), min(min.y, points[i].y));
            max = point(max(max.x, points[i].x), max(max.y, points[i].y));
        }
        System.out.println("min, max: " + min + ", " + max);
        for (int x = min.x; x <= max.x; x++) {
            for (int y = min.y; y <= max.y; y++) {
                Point p = point(x,y);
                System.out.println("p: " + p);
                if (pointInPoly(p, poly)) {
                    System.out.println("add p: " + p);
                    outSet.add(p);
                }
            }
        }
        List<Point> out = new ArrayList<Point>(outSet);
        Collections.sort(out);
        return out;
    }

    private List<Point> rasterLine(Point a, Point b) {
        List<Point> out = new ArrayList<Point>();
        int minX = min(a.x, b.x);
        int minY = min(a.y, b.y);

        int lenX = abs(a.x - b.x) + 1;
        int lenY = abs(a.y - b.y) + 1;
        if (lenX > lenY) {
            for (int x = 0; x < lenX; x++) {
                out.add(point(minX + x, minY + (x * lenY) / lenX));
            }
        } else if (lenX < lenY) {
            for (int y = 0; y < lenY; y++) {
                out.add(point(minX + (y * lenX) / lenY, minY + y));
            }
        } else {
            for (int i = 0; i < lenX; i++) {
                out.add(point(minX + i, minY + i));
            }
        }
        return out;
    }

    private boolean adjacent(Point a, Point b) {
        return abs(a.x - b.x) <= 1 && abs(a.y - b.y) <= 1;
    }

    private Point point(int x, int y) {
        Point out = new Point();
        out.x = x;
        out.y = y;
        return out;
    }

    private class Point implements Comparable<Point> {
        public int x;
        public int y;

        public boolean equals(Object other) {
            if (!(other instanceof Point)) {
                return false;
            }
            Point p = (Point) other;
            return x == p.x && y == p.y;
        }

        public String toString() {
            return "(" + x + "," + y + ")";
        }

        public int compareTo(Point o) {
            int compare = x - o.x;
            if (compare != 0) return compare;
            return y - o.y;
        }
    }

    private Point bisect(Point a, Point b) {
        return point(min(a.x, b.x) + abs(a.x - b.x) / 2, min(a.y, b.y) + abs(a.y - b.y) / 2);
    }

    private class Polygon {
        private ArrayList<Point> points = new ArrayList<Point>();

        public Polygon(Point... points) {
            this.points.addAll(Arrays.asList(points));
        }

        public ArrayList<Point> getPoints() {
            return points;
        }

        public void setPoints(ArrayList<Point> points) {
            this.points = points;
        }
    }
}
