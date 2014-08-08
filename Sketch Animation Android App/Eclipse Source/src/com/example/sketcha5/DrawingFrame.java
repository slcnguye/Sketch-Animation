package com.example.sketcha5;

import android.graphics.Point;

public class DrawingFrame {
	private boolean visible;
	private Point transition;

	public DrawingFrame() {
		visible = true;
		transition = new Point(0, 0);
	}
	
	public DrawingFrame(boolean visible, Point transition) {
		this.visible = visible;
		this.transition = transition;
	}

	public boolean isVisible() {
		return visible;
	}

	public Point getTransition() {
		return transition;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setTransition(Point transition) {
		this.transition = transition;
	}
}
