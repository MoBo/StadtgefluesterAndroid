/**
 * 
 */
package bode.moritz.stadtgefluester.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Application;
import android.os.AsyncTask;
import bode.moritz.stadtgefluester.SplashscreenActivity;
import bode.moritz.stadtgefluester.StadtgefluesterApplication;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthInterface;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class GetOAuthTokenTask extends AsyncTask<String, Integer, OAuth> {

	private SplashscreenActivity activity;
	private StadtgefluesterApplication stadtgefluesterApplication;

	public GetOAuthTokenTask(SplashscreenActivity context) {
		this.activity = context;
		this.stadtgefluesterApplication = (StadtgefluesterApplication) activity.getApplication();
	}

	@Override
	protected OAuth doInBackground(String... params) {
		String oauthToken = params[0];
		String oauthTokenSecret = params[1];
		String verifier = params[2];

		Flickr f = stadtgefluesterApplication.getFlickr();
		OAuthInterface oauthApi = f.getOAuthInterface();
		try {
			return oauthApi.getAccessToken(oauthToken, oauthTokenSecret,
					verifier);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	protected void onPostExecute(OAuth result) {
		if (activity != null) {
			activity.onOAuthDone(result);
		}
	}


}
