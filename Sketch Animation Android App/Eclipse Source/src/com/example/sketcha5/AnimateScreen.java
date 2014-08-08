package com.example.sketcha5;

import java.io.File;
import java.io.FilenameFilter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AnimateScreen extends Activity {
	private FrameLayout container;
	private Model model;
	private TextView fileName;
	private int frameRate;
	private DrawView drawView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate_screen);  
        
        fileName = (TextView) findViewById(R.id.fileName);
        container = (FrameLayout)findViewById(R.id.sc);
        drawView = new DrawView(this);
        container.addView(drawView);
        frameRate = 30;
    }
    
    public void playAnimation(View target){
    	drawView.animateSketch(target, frameRate);
    }
    
    public void loadAnimation(View target){
       	drawView.stopSketch();
    	
    	File sdCard = Environment.getExternalStorageDirectory();
    	FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".xml");
			}
    		
    	};
    	String[] files = sdCard.list(filter);
    	
    	Intent intent = new Intent(AnimateScreen.this, FileListScreen.class);
    	intent.putExtra("fileNames", files);
    	startActivityForResult(intent, 2);
    }
    
    public void configAnimation(View target) {
       	drawView.stopSketch();
    	Intent intent = new Intent(AnimateScreen.this, ConfigScreen.class);
    	ColorDrawable colorDrawable = (ColorDrawable) container.getBackground();
    	intent.putExtra("backgroundColor", colorDrawable.getColor());
    	intent.putExtra("frameRate", frameRate);
    	startActivityForResult(intent, 1);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	switch(requestCode) {
	    	case (1) : {
	    		if (resultCode == Activity.RESULT_OK) {
	    			int backgroundColor = intent.getIntExtra("backgroundColor", 0);
	    			container.setBackgroundColor(backgroundColor);
	    			frameRate = intent.getIntExtra("frameRate", 30);
	    			//settings for frameRate
	    		}
	    		break;
	    	}
	    	case (2) : {
	    		if (resultCode == Activity.RESULT_OK) {
	    			String fileToLoad = intent.getStringExtra("fileToLoad");
	    			File file = new File(Environment.getExternalStorageDirectory(),fileToLoad);
	    			fileName.setText(fileToLoad);
					model = ParseXml.parseXmlToModel(file);
					Button playButton = (Button) findViewById(R.id.play);
					playButton.setEnabled(true);
					drawView.setModel(model);
	    		}
	    		break;
	    	}
     	}
    }
    
}
