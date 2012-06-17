package bode.moritz.stadtgefluester;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class SplashscreenActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        new LoadDataTask().execute(null);
    }
    
    private class LoadDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// do nothing
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(getApplicationContext(), StadtgefluesterMapActivity.class);
			startActivity(intent);
			super.onPostExecute(result);
		}
    	
    }
}