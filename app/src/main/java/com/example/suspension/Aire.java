package com.example.suspension;

public class Aire {

    Point[] points;
    int SuspensionLevel;
    int id;


    public Aire(int id, Point[] points, int level){
        if(points.length < 3){
            throw new RuntimeException("The number of points must be >= 3 to define an area.");
        }
        this.points = points;
        this.id = id;
        this.SuspensionLevel = level;
    }

    public boolean contains(Point p){
        int compteur = 0;
        for (int i = 0; i < points.length; i++) {
            Point p1 = points[i];
            Point p2 = points[(i + 1) % points.length];
            if (onSegment(p, p1, p2)) {
                return true;
            }
            if (p.y > Math.min(p1.y, p2.y) && p.y <= Math.max(p1.y, p2.y)) {
                if (p.x <= Math.max(p1.x, p2.x)) {
                    double xIntersection = (p.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x;
                    if (p1.x == p2.x || p.x <= xIntersection) {
                        compteur++;
                    }
                }
            }
        }
        return compteur % 2 != 0;
    }

    public static boolean onSegment(Point point, Point p1, Point p2) {
        double val = (point.y - p1.y) * (p2.x - point.x) - (point.x - p1.x) * (p2.y - point.y);
        if (val != 0) {
            return false; // Les points ne sont pas alignés
        }

        // Vérifier si le point est entre les deux autres points
        return (point.x <= Math.max(p1.x, p2.x) && point.x >= Math.min(p1.x, p2.x) &&
                point.y <= Math.max(p1.y, p2.y) && point.y >= Math.min(p1.y, p2.y));
    }
}
