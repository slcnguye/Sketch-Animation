package com.example.sketcha5;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Drawing {
    private ArrayList<Point> points = new ArrayList<Point>();
    private ArrayList<DrawingFrame> frame = new ArrayList<DrawingFrame>();
    private int color;
    private float strokeSize;

    public ArrayList<DrawingFrame> getFrame() {
        return frame;
    }

    public void setFrame(ArrayList<DrawingFrame> newFrame) {
    	this.frame = newFrame;
    }
    
    public int getColor() {
        return color;
    }

    public float getStrokeSize() {
        return strokeSize;
    }

    public Drawing(int selectedColor,float size) {
        this.color  = selectedColor;
        this.strokeSize = size;
    }
    
    public Drawing(int numberOfFrames, int selectedColor, float size) {
        for (int i = 0; i < numberOfFrames; i++) {
            frame.add(new DrawingFrame());
        }
        this.color = selectedColor;
        this.strokeSize = size;
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
        p.set(point2.x + point1.x, point2.y + point1.y);
        return p;
    }

    private Point subPoints(Point point1, Point point2) {
        Point p = new Point();
        p.set(point2.x - point1.x, point2.y - point1.y);
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

    private Point addTransition(Point point, int timeFrame) {
        Point result = new Point();
        Point transitionDelta = frame.get(timeFrame).getTransition();
        result.set(point.x + transitionDelta.x, point.y + transitionDelta.y);
        return result;
    }

    public void setDrawnTimeFrame(int timeFrame) {
        for (int i = 0; i < timeFrame; i++) {
            frame.get(i).setVisible(false);
        }
    }

    //drawing

    public void draw(Canvas canvas, int timeFrame) {
        if (!frame.get(timeFrame).isVisible()) {
        	return;
        }
        Paint p = new Paint();
        p.setColor(color);
        p.setStrokeWidth(strokeSize);
        drawLine(canvas, p, timeFrame);
    }

    private void drawLine(Canvas canvas, Paint p, int timeFrame) {
        for (int i = 0; i < points.size()-1; i++) {
            Point start = addTransition(points.get(i), timeFrame);
            Point end = addTransition(points.get(i + 1), timeFrame);
            canvas.drawLine(start.x, start.y, end.x, end.y, p);
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
            result.add(new Point((int) (p.x + transition.x), (int) (p.y + transition.y)));
        }
        return result;
    }

    public void addTransitionFrame(int currentTimeFrame) {
        frame.add(new DrawingFrame());
        for (int i = frame.size()-1; i > currentTimeFrame; --i) {
            frame.set(i, frame.get(i-1));
        }
    }

	public void setPoints(ArrayList<Point> points2) {
		points = points2;		
	}
}
