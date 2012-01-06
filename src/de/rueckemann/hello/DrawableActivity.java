package de.rueckemann.hello;

import android.app.Activity;
import android.os.Bundle;
import de.rueckemann.hello.DrawableViews.CustomDrawable;

public class DrawableActivity extends Activity {

	CustomDrawable mCustomDrawableView;

	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mCustomDrawableView = new CustomDrawable(this);
	    setContentView(mCustomDrawableView);
	}


}
