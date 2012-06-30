package bode.moritz.stadtgefluester.task;

import java.io.IOException;

import org.json.JSONException;

import bode.moritz.stadtgefluester.StadtgefluesterApplication;
import bode.moritz.stadtgefluester.StadtgefluesterDetailActivity;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthToken;
import com.gmail.yuyang226.flickr.photos.comments.Comment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

@SuppressLint("ParserError")
public class CommentTask extends AsyncTask<OAuth, Void, Comment>{

	private StadtgefluesterDetailActivity activity;
	private ProgressDialog mProgressDialog;
	private StadtgefluesterApplication stadtgefluesterApplication;
	private String photoId;
	private String commentText;

	public CommentTask(String photoId,String commentText, StadtgefluesterDetailActivity activity) {
		this.photoId = photoId;
		this.commentText = commentText;
		this.activity = activity;
		this.stadtgefluesterApplication = (StadtgefluesterApplication) activity
				.getApplication();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = ProgressDialog.show(activity,
				"", "Upload Comment"); //$NON-NLS-1$ //$NON-NLS-2$
		mProgressDialog.setCanceledOnTouchOutside(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dlg) {
				CommentTask.this.cancel(true);
			}
		});
	}
	
	@Override
	protected Comment doInBackground(OAuth... params) {
		OAuthToken token = params[0].getToken();
		Flickr flickr = stadtgefluesterApplication.getFlickrAuthed(
				token.getOauthToken(), token.getOauthTokenSecret());
		try {
			flickr.getCommentsInterface().addComment(photoId, commentText);
			Comment comment = new Comment();
			//comment.setId(commentId);
			comment.setText(commentText);
			comment.setAuthorName(params[0].getUser().getUsername());
			return comment;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Comment result) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		this.activity.commentWasUploaded(result);
		super.onPostExecute(result);
	}

}
