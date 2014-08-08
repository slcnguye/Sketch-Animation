package com.example.sketcha5;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;

public class DrawView extends View {
    private Model model;
    private boolean draw = false;
    private Handler handler;
    private View target;
	private int frameRate;
    
    public DrawView(Context context) {
        super(context);
        handler = new Handler();
    }

    public void setModel(Model model) {
    	this.model = model;
    }
    
    @Override 
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (draw) {
        	int frame = model.getCurrentTimeFrame();
        	ArrayList<Drawing> drawings = model.getDrawings();
         	for (Drawing d : drawings) {
         		d.draw(canvas, frame);
         	}
         	
         	Paint p = new Paint();
         	p.setColor(Color.BLACK);
         	p.setTextSize(50f);
         	canvas.drawText(String.valueOf(frame) + "/" + (model.getTotalFrames()-1), 30, 50, p);
         	
         	model.incrementTimeFrame();
         	if (frame == model.getCurrentTimeFrame()) {
         		draw = false;
         		target.setEnabled(true);
         	}
         	handler.postDelayed(runnable, 500/frameRate);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
                invalidate();
        }
};
    
	public void animateSketch(View target, int frameRate) {
		draw = true;
		model.setCurrentTimeFrame(0);
		target.setEnabled(false);
		this.target = target;
		this.frameRate = frameRate;
		invalidate();
	}

	public void stopSketch() {
		draw = false;
		if (target != null) target.setEnabled(true);
	}
}
