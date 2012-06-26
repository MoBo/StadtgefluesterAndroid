package bode.moritz.stadtgefluester.task;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import bode.moritz.stadtgefluester.StadtgefluesterApplication;
import bode.moritz.stadtgefluester.StadtgefluesterDetailActivity;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthToken;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.gmail.yuyang226.flickr.photos.PhotosInterface;
import com.gmail.yuyang226.flickr.photos.comments.Comment;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public class PhotoDetailsTask extends AsyncTask<OAuth, Void, Photo> {

	private Photo photo;
	private StadtgefluesterApplication application;
	private Activity activity;
	private ProgressDialog mProgressDialog;

	public PhotoDetailsTask(Photo p, Activity a) {
		this.photo = p;
		application = (StadtgefluesterApplication) a.getApplication();
		activity  = a;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = ProgressDialog.show(activity,
				"", "Load data for photo"); //$NON-NLS-1$ //$NON-NLS-2$
		mProgressDialog.setCanceledOnTouchOutside(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dlg) {
				PhotoDetailsTask.this.cancel(true);
			}
		});
	}
	
	@Override
	protected Photo doInBackground(OAuth... params) {
		OAuthToken token = params[0].getToken();
		Flickr flickr = application.getFlickrAuthed(token.getOauthToken(),
				token.getOauthTokenSecret());
		PhotosInterface photosInterface = flickr.getPhotosInterface();		
		try {
			
			Photo tmp = photosInterface.getInfo(photo.getId(), "");
			List<Comment> commentlist = flickr.getCommentsInterface().getList(tmp.getId(), null, null);			
			application.addCommentHash(photo.getId(), commentlist);
			application.replacePhotoInList(tmp);
			return tmp;
			
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return null;
	}
	
	
	@Override
	protected void onPostExecute(Photo result) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		Intent i = new Intent(activity, StadtgefluesterDetailActivity.class);
		i.putExtra(StadtgefluesterApplication.PHOTO_ID_STRING, result.getId());
		activity.startActivity(i);
	}

}
