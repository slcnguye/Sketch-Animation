package com.example.sketcha5;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ConfigScreen extends Activity{
	private SeekBar frameRateAdjuster;
	private TextView frameRate;
	private SeekBar redAdjuster;
	private SeekBar greenAdjuster;
	private SeekBar blueAdjuster;
	private TextView backgroundColour;
	private int color;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_screen);
		
		Bundle data = getIntent().getExtras();
		int colorValue = data.getInt("backgroundColor");
		int frameRate = data.getInt("frameRate");
		initializeFrameRateSeekBar(frameRate);
		initializeColorSeekBars(colorValue);
	}

	private void initializeColorSeekBars(int colorValue) {
		backgroundColour = (TextView) findViewById(R.id.backgroundColour);
		redAdjuster = (SeekBar) findViewById(R.id.redAdjuster);
		greenAdjuster = (SeekBar) findViewById(R.id.greenAdjuster);
		blueAdjuster = (SeekBar) findViewById(R.id.blueAdjuster);
		redAdjuster.setMax(254);
		greenAdjuster.setMax(254);
		blueAdjuster.setMax(254);
		redAdjuster.setProgress(Color.red(colorValue)-1);
		greenAdjuster.setProgress(Color.green(colorValue)-1);
		blueAdjuster.setProgress(Color.blue(colorValue)-1);
		updateBackgroundColor();
		
		OnSeekBarChangeListener colorAdjuster = new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				updateBackgroundColor();
			}
		};
		
		redAdjuster.setOnSeekBarChangeListener(colorAdjuster);
		greenAdjuster.setOnSeekBarChangeListener(colorAdjuster);
		blueAdjuster.setOnSeekBarChangeListener(colorAdjuster);
	}

	private void initializeFrameRateSeekBar(int frameRate2) {
		frameRateAdjuster = (SeekBar) findViewById(R.id.frameRateAdjuster);
		frameRateAdjuster.setMax(99);
		frameRateAdjuster.setProgress(frameRate2-1);
		frameRate = (TextView) findViewById(R.id.frameRate);
		updateText();
		
		frameRateAdjuster.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				updateText();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {				
			}
		});
	}

	private void updateBackgroundColor() {
		color = Color.rgb(redAdjuster.getProgress()+1, greenAdjuster.getProgress()+1, blueAdjuster.getProgress()+1);
		ColorDrawable background = new ColorDrawable(color);
		background.setBounds(0, 0, 150, 50);
		backgroundColour.setCompoundDrawables(null, null, background, null);
	}
	
	private void updateText() {
		frameRate.setText("Frame Rate : " + (frameRateAdjuster.getProgress()+1) + " FPS");
	}
	
	public void backToMainScreen(View target) {
		Intent resultIntent = new Intent();
		resultIntent.putExtra("frameRate", frameRateAdjuster.getProgress()+1);
		resultIntent.putExtra("backgroundColor", color);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
