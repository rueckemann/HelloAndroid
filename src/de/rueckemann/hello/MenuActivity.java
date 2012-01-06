package de.rueckemann.hello;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MenuActivity extends Activity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.mainmenu, menu);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.components:
            openComponents();
            return true;
        case R.id.drawable:
            openDrawable();
            return true;
        case R.id.imageview:
            openImageView();
            return true;    
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	private void openImageView() {
		Intent intent = new Intent(this, ImageViewActivity.class);
		startActivity(intent);				
	}

	private void openComponents() {
		Intent intent = new Intent(this, ComponentsActivity.class);
		startActivity(intent);		
	}

	private void openDrawable() {
		Intent intent = new Intent(this, DrawableActivity.class);
		startActivity(intent);				
	}
}
