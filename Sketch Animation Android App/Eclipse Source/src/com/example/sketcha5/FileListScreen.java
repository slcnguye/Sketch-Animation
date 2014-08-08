package com.example.sketcha5;

import java.util.ArrayList;
import java.util.Arrays;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FileListScreen extends Activity{
	private ListView fileList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filelist_screen);
		
		Bundle data = getIntent().getExtras();
		String[] files = data.getStringArray("fileNames");
		displayFileList(files);	
	}

	private void displayFileList(String[] files) {
		fileList = (ListView) findViewById(R.id.fileList);
		ArrayList<String> fileLists = new ArrayList<String>(Arrays.asList(files));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, fileLists);
		fileList.setAdapter(adapter);
		
		fileList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String fileName = (String) parent.getItemAtPosition(position);
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_OK, resultIntent);
				resultIntent.putExtra("fileToLoad", fileName);
				finish();
			}
		});
		
	}
	
	public void backToMainScreens(View target) {
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();
	}
}
