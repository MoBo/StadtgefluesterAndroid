package bode.moritz.stadtgefluester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import org.json.JSONException;

import bode.moritz.stadtgefluester.task.GetOAuthTokenTask;
import bode.moritz.stadtgefluester.task.OAuthTask;
import bode.moritz.stadtgefluester.task.PhotoSearchTask;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthToken;
import com.gmail.yuyang226.flickr.people.User;
import com.gmail.yuyang226.flickr.photos.GeoData;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.gmail.yuyang226.flickr.photos.SearchParameters;
import com.gmail.yuyang226.flickr.tags.Cluster;
import com.gmail.yuyang226.flickr.tags.ClusterList;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts.Photos;
import android.widget.Toast;

public class SplashscreenActivity extends Activity {
	private StadtgefluesterApplication stadtgefluesterApplication;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		this.stadtgefluesterApplication = (StadtgefluesterApplication) getApplication();
		// new LoadDataTask().execute(null);
		OAuth oauth = this.stadtgefluesterApplication.getOAuthToken();
		if (oauth == null || oauth.getUser() == null) {
			OAuthTask task = new OAuthTask(this);
			task.execute();
		} else {
			load(oauth);
		}
	}

	private void load(OAuth oauth) {
		new PhotoSearchTask(this).execute(oauth);
	}

	@Override
	public void onResume() {
		super.onResume();
		Intent intent = getIntent();
		String scheme = intent.getScheme();
		OAuth savedToken = stadtgefluesterApplication.getOAuthToken();
		if (StadtgefluesterApplication.OAUTH_CALLBACK_SCHEME.equals(scheme)
				&& (savedToken == null || savedToken.getUser() == null)) {
			Uri uri = intent.getData();
			String query = uri.getQuery();
			//logger.debug("Returned Query: {}", query); //$NON-NLS-1$
			String[] data = query.split("&"); //$NON-NLS-1$
			if (data != null && data.length == 2) {
				String oauthToken = data[0].substring(data[0].indexOf("=") + 1); //$NON-NLS-1$
				String oauthVerifier = data[1]
						.substring(data[1].indexOf("=") + 1); //$NON-NLS-1$
				// logger.debug(
				//		"OAuth Token: {}; OAuth Verifier: {}", oauthToken, oauthVerifier); //$NON-NLS-1$

				OAuth oauth = stadtgefluesterApplication.getOAuthToken();
				if (oauth != null && oauth.getToken() != null
						&& oauth.getToken().getOauthTokenSecret() != null) {
					GetOAuthTokenTask task = new GetOAuthTokenTask(this);
					task.execute(oauthToken, oauth.getToken()
							.getOauthTokenSecret(), oauthVerifier);
				}
			}
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// this is very important, otherwise you would get a null Scheme in the
		// onResume later on.
		setIntent(intent);
	}

	//
	// private class LoadDataTask extends AsyncTask<Void, Void, Void> {
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	// Flickr flickr = new Flickr("fb39d904688ecbe4d5f4a173a959a657",
	// "a3107c0ffa286691");
	// new Fli
	// flickr
	// try {
	// GeoData geoData = new GeoData("8.845833", "53.091111", "6");
	// PhotoList photoList =
	// flickr.getPhotosInterface().getGeoInterface().photosForLocation(geoData,
	// new HashSet<String>(), 10, 1);
	// for (Photo photo : photoList) {
	// photo.getTitle();
	// }
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (FlickrException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// Intent intent = new Intent(getApplicationContext(),
	// StadtgefluesterMapActivity.class);
	// startActivity(intent);
	// super.onPostExecute(result);
	// }
	//

	public void onOAuthDone(OAuth result) {
		if (result == null) {
			Toast.makeText(this, "Authorization failed", //$NON-NLS-1$
					Toast.LENGTH_LONG).show();
		} else {
			User user = result.getUser();
			OAuthToken token = result.getToken();
			if (user == null || user.getId() == null || token == null
					|| token.getOauthToken() == null
					|| token.getOauthTokenSecret() == null) {
				Toast.makeText(this, "Authorization failed", //$NON-NLS-1$
						Toast.LENGTH_LONG).show();
				return;
			}
			String message = String
					.format(Locale.US,
							"Authorization Succeed: user=%s, userId=%s, oauthToken=%s, tokenSecret=%s", //$NON-NLS-1$
							user.getUsername(), user.getId(),
							token.getOauthToken(), token.getOauthTokenSecret());
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			stadtgefluesterApplication.saveOAuthToken(user.getUsername(),
					user.getId(), token.getOauthToken(),
					token.getOauthTokenSecret());
			load(result);
		}
	}

}