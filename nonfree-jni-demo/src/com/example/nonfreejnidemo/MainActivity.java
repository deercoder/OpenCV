// A simple demo of JNI interface to implement SIFT detection for Android application using nonfree module in OpenCV4Android.
// Created by Guohui Wang 
// Email: robertwgh_at_gmail_com
// Data: 2/26/2014

package com.example.nonfreejnidemo;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtView = (TextView)findViewById(R.id.textView1);

		runBtn = (Button)findViewById(R.id.button_run_demo);
		runBtn.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Log.v("nonfree_jni_demo", "start runDemo");
				// Call the JNI interface
				NonfreeJNILib.runDemo();
		
				Toast.makeText(MainActivity.this, "Finished!", Toast.LENGTH_SHORT).show();
				txtView.setText("Finished! Please check /sdcard/nonfree for result image.");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	Button runBtn;
	TextView txtView;
}
