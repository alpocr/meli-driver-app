package com.melicdriver;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
		            Thread.sleep(2000);
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
				
				runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		                Intent theIntent = new Intent(SplashScreen.this, LoginActivity.class);
		                startActivity(theIntent);
		                finish();
		            }
		        });
				
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

}
