package com.fontlose;

import com.fontlose.R;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReadAssets extends Activity {
    /** Called when the activity is first created. */
	String apkFilePath = "";   
	static {
		System.loadLibrary("zip");
		System.loadLibrary("readres");
	}
	
	
	public native void  readFromAssets(AssetManager ass,String filename);
	public native void  readFromAssetsLibzip(String ass,String filename);
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button bn=(Button)this.findViewById(R.id.button1);
         bn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				readFromAssets(getAssets(),"log.txt");
			}
		});
         
         bn=(Button)this.findViewById(R.id.button2);
         bn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		        readFromAssetsLibzip(getPackageResourcePath(),"assets/log.txt");
			}
		}); 
         
        
    }
	
	
	
	
}