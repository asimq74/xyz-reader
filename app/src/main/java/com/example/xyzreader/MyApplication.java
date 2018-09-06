package com.example.xyzreader;

import android.app.Application;
import android.content.Context;

import com.example.xyzreader.dagger.ApplicationComponent;
import com.example.xyzreader.dagger.ApplicationModule;
import com.example.xyzreader.dagger.DaggerApplicationComponent;


/**
 * Base Application Class
 */

public class MyApplication extends Application {

	public static MyApplication get(Context context) {
		return (MyApplication) context.getApplicationContext();
	}

	private ApplicationComponent applicationComponent;

	public ApplicationComponent getApplicationComponent() {
		return applicationComponent;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		applicationComponent = DaggerApplicationComponent
				.builder()
				.applicationModule(new ApplicationModule(this))
				.build();
		applicationComponent.inject(this);


	}

}
