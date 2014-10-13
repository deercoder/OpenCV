// A simple demo of JNI interface to implement SIFT detection for Android application using nonfree module in OpenCV4Android.
// Created by Guohui Wang 
// Email: robertwgh_at_gmail_com
// Data: 2/26/2014

package com.example.nonfreejnidemo;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int REQUEST_EX = 1;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtView = (TextView)findViewById(R.id.textView1);
		mContext  = getApplicationContext(); 

		runBtn = (Button)findViewById(R.id.button_run_demo);
		runBtn.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Log.v("nonfree_jni_demo", "start runDemo");
				
				Intent intent = new Intent();
				intent.putExtra("explorer_title",
						getString(R.string.dialog_read_from_dir));
				intent.setDataAndType(Uri.fromFile(new File("/sdcard")), "*/*");
				intent.setClass(MainActivity.this, ExDialog.class);
				startActivityForResult(intent, REQUEST_EX);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		final String path;
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_EX) {
				//Uri uri = intent.getData();
				path = intent.getStringExtra("test"); /// get absolute path from transferring...
				TextView text = (TextView) findViewById(R.id.button_run_demo);
				text.setText("select: "+ path);
				//getRealPathFromURI (uri);

			    /* 线程体是Clock对象本身，线程名字为"Clock" */
		        Thread processThread = new Thread(new Runnable() {
		            @Override
		            public void run() {
		            	Looper.prepare();
		            	// Call the JNI interface
						NonfreeJNILib.runDemo(path);
		                }
		        });
		        processThread.start();
			}
		}
	}
	
	private String getRealPathFromURI(Uri contentUri) {
	    String[] proj = { MediaStore.Images.Media.DATA };
	    CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
	    Cursor cursor = loader.loadInBackground();
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	Button runBtn;
	TextView txtView;
}
