import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Drawing {
    private ArrayList<Point> points = new ArrayList<Point>();
    private ArrayList<DrawingFrame> frame = new ArrayList<DrawingFrame>();
    private Color color;
    private Stroke stroke;

    public Drawing(int numberOfFrames, Color selectedColor, Stroke selectedStroke) {
        for (int i = 0; i < numberOfFrames; i++) {
            frame.add(new DrawingFrame());
        }
        this.color = selectedColor;
        this.stroke = selectedStroke;
    }

    public void transitionPoints(int timeFrame, Point point1, Point point2) {
        Point currentPosition = frame.get(timeFrame).getTransition();
        for (int i = timeFrame; i < frame.size(); ++i) {
            frame.get(i).setTransition(addPoints(currentPosition, subPoints(point1, point2)));
        }
    }

    public void transitionFrame(Point point1, Point point2) {
        Point currentPosition = frame.get(0).getTransition();
        frame.get(0).setTransition(addPoints(currentPosition, subPoints(point1, point2)));
    }

    private Point addPoints(Point point1, Point point2) {
        Point p = new Point();
        p.setLocation(point2.getX() + point1.getX(), point2.getY() + point1.getY());
        return p;
    }

    private Point subPoints(Point point1, Point point2) {
        Point p = new Point();
        p.setLocation(point2.getX() - point1.getX(), point2.getY() - point1.getY());
        return p;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void erase(int currentTimeFrame) {
        for (int i = currentTimeFrame; i < frame.size(); i++) {
            frame.get(i).setVisible(false);
        }
    }

    public void redraw(int currentTimeFrame) {
        for (int i = currentTimeFrame; i < frame.size(); i++) {
            frame.get(i).setVisible(true);
        }
    }

    public boolean intersecting(Point p1, Point p2, int timeFrame) {
        if (p1 == p2) {
            Rectangle eraserArea = new Rectangle((int) p1.getX(),(int) p1.getY(),14,14);
            for (int i = 0; i < points.size()-1; i++) {
                Point start = addTransition(points.get(i), timeFrame);
                Point end = addTransition(points.get(i + 1), timeFrame);
                if (eraserArea.intersectsLine(start.getX(), start.getY(), end.getX(), end.getY())) {
                    return true;
                }
            }
        } else {
            Line2D.Double eraseLine = new Line2D.Double(p1, p2);
            for (int i = 0; i < points.size()-1; i++) {
                Point start = addTransition(points.get(i), timeFrame);
                Point end = addTransition(points.get(i+1), timeFrame);
                if (eraseLine.intersectsLine(start.getX(), start.getY(), end.getX(), end.getY())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Point addTransition(Point point, int timeFrame) {
        Point result = new Point();
        Point transitionDelta = frame.get(timeFrame).getTransition();
        result.setLocation(point.getX() + transitionDelta.getX(), point.getY() + transitionDelta.getY());
        return result;
    }

    public void setDrawnTimeFrame(int timeFrame) {
        for (int i = 0; i < timeFrame; i++) {
            frame.get(i).setVisible(false);
        }
    }

    //drawing

    public void draw(Graphics2D g2, int timeFrame) {
        if (!frame.get(timeFrame).isVisible()) {
            return;
        }
        g2.setColor(color);
        g2.setStroke(stroke);
        drawLine(g2, timeFrame);
    }

    public void drawSelected(Graphics2D g2) {
        g2.setColor(Color.gray);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        drawLine(g2, 0);
    }

    private void drawLine(Graphics2D g2, int timeFrame) {
        for (int i = 0; i < points.size()-1; i++) {
            Point start = addTransition(points.get(i), timeFrame);
            Point end = addTransition(points.get(i + 1), timeFrame);
            g2.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
        }
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public boolean isVisible(int currentTimeFrame) {
        return frame.get(currentTimeFrame).isVisible();
    }

    public ArrayList<Point> getTransitionedPoints(int currentTimeFrame) {
        ArrayList<Point> result = new ArrayList<Point>();
        for (Point p: points) {
            Point transition = frame.get(currentTimeFrame).getTransition();
            result.add(new Point((int) (p.getX() + transition.getX()),(int) (p.getY() + transition.getY())));
        }
        return result;
    }

    public void addTransitionFrame(int currentTimeFrame) {
        frame.add(new DrawingFrame());
        for (int i = frame.size()-1; i > currentTimeFrame; --i) {
            frame.set(i, frame.get(i-1));
        }
    }

    public void drawSelectedObject(Graphics2D g2, int timeFrame) {
        if (!frame.get(timeFrame).isVisible()) {
            return;
        }
        g2.setColor(Color.gray);
        g2.setStroke(stroke);
        drawLine(g2, timeFrame);
    }
}
