/**
 * 
 */
package bode.moritz.stadtgefluester.task;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import bode.moritz.stadtgefluester.StadtgefluesterApplication;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.auth.Permission;
import com.gmail.yuyang226.flickr.oauth.OAuthToken;

/**
 * Represents the task to start the oauth process.
 * 
 * @author yayu(yuyang226@gmail.com)
 * 
 */
public class OAuthTask extends AsyncTask<Void, Integer, String> {

	private static final Logger logger = LoggerFactory
			.getLogger(OAuthTask.class);
	private static final Uri OAUTH_CALLBACK_URI = Uri.parse(StadtgefluesterApplication.OAUTH_CALLBACK_SCHEME
			+ "://oauth"); //$NON-NLS-1$


	/**
	 * The progress dialog before going to the browser.
	 */
	private ProgressDialog mProgressDialog;
	private Activity activity;
	private StadtgefluesterApplication stadtgefluesterApplication;

	/**
	 * Constructor.
	 * 
	 * @param context
	 */
	public OAuthTask(Activity activity) {
		super();
		this.activity = activity;
		this.stadtgefluesterApplication = (StadtgefluesterApplication) activity.getApplication();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = ProgressDialog.show(activity,
				"", "Generating the authorization request..."); //$NON-NLS-1$ //$NON-NLS-2$
		mProgressDialog.setCanceledOnTouchOutside(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dlg) {
				OAuthTask.this.cancel(true);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Void... params) {
		try {
			Flickr f = stadtgefluesterApplication.getFlickr();
			OAuthToken oauthToken = f.getOAuthInterface().getRequestToken(
					OAUTH_CALLBACK_URI.toString());
			saveTokenSecrent(oauthToken.getOauthTokenSecret());
			URL oauthUrl = f.getOAuthInterface().buildAuthenticationUrl(
					Permission.WRITE, oauthToken);
			return oauthUrl.toString();
		} catch (Exception e) {
			logger.error("Error to oauth", e); //$NON-NLS-1$
			return "error:" + e.getMessage(); //$NON-NLS-1$
		}
	}

	/**
	 * Saves the oauth token secrent.
	 * 
	 * @param tokenSecret
	 */
	private void saveTokenSecrent(String tokenSecret) {
		logger.debug("request token: " + tokenSecret); //$NON-NLS-1$
		stadtgefluesterApplication.saveOAuthToken(null, null, null, tokenSecret);
		logger.debug("oauth token secret saved: {}", tokenSecret); //$NON-NLS-1$
	}

	@Override
	protected void onPostExecute(String result) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		if (result != null && !result.startsWith("error") ) { //$NON-NLS-1$
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
					.parse(result)));
		} else {
			Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
		}
	}

}
