package bode.moritz.stadtgefluester.task;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.slf4j.helpers.Util;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;
import bode.moritz.stadtgefluester.R;
import bode.moritz.stadtgefluester.StadtgefluesterApplication;
import bode.moritz.stadtgefluester.StadtgefluesterDetailActivity;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthToken;

@SuppressLint("ParserError")
public class FavoriteTask extends AsyncTask<OAuth, Void, Void>{

	private StadtgefluesterDetailActivity activity;
	private ProgressDialog mProgressDialog;
	private StadtgefluesterApplication stadtgefluesterApplication;
	private String photoId;

	public FavoriteTask(String photoId, StadtgefluesterDetailActivity activity) {
		this.photoId = photoId;
		this.activity = activity;
		this.stadtgefluesterApplication = (StadtgefluesterApplication) activity
				.getApplication();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = ProgressDialog.show(activity,
				"", "Mark as Favourite"); //$NON-NLS-1$ //$NON-NLS-2$
		mProgressDialog.setCanceledOnTouchOutside(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dlg) {
				FavoriteTask.this.cancel(true);
			}
		});
	}
	
	@Override
	protected Void doInBackground(OAuth... params) {
		OAuthToken token = params[0].getToken();
		Flickr flickr = stadtgefluesterApplication.getFlickrAuthed(
				token.getOauthToken(), token.getOauthTokenSecret());
		try {
			flickr.getFavoritesInterface().add(photoId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
@Override
protected void onPostExecute(Void result) {
	if (mProgressDialog != null) {
		mProgressDialog.dismiss();
	}
	//Toast.makeText(activity.getApplicationContext(),R.string.detail_screen_comment_favorite, 2000);
	super.onPostExecute(result);
}
}
