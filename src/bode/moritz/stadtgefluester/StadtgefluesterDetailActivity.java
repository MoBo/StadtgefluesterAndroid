package bode.moritz.stadtgefluester;


import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import bode.moritz.stadtgefluester.task.imagedownloadtask.ImageDownloadTask;
import bode.moritz.stadtgefluester.task.imagedownloadtask.ImageUtils.DownloadedDrawable;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.comments.Comment;

import de.unibremen.util.Util;

@SuppressLint({ "ParserError", "ParserError", "ParserError" })
public class StadtgefluesterDetailActivity extends Activity {
	private StadtgefluesterApplication stadtgefluesterapplication;
	private String photoID = "";
	private Photo photo = null;
	private ImageButton shareButton;
	private Builder builder;
	private AlertDialog alertDialog;
	private ImageButton commentButton;
	private View alertLayout;
	private ArrayList<Comment> comments;
	private ListView listview;
	private CommentAdapter commentAdapter;
	private ImageButton favoriteButton;
	private Activity activity = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.detail_screen);
		
		
		stadtgefluesterapplication = (StadtgefluesterApplication) getApplication();
		this.photoID = getIntent().getStringExtra(
				StadtgefluesterApplication.PHOTO_ID_STRING);
		photo = stadtgefluesterapplication.getPhotoById(photoID);		
		initViews();
		setViewContent();
	}

	@SuppressLint("ParserError")
	private void initViews() {
		this.shareButton = (ImageButton) findViewById(R.id.ib_detail_screen_share);
		this.shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.putExtra(Intent.EXTRA_TEXT, photo.getUrl());
				sharingIntent.setType("text/plain");
				startActivity(Intent.createChooser(sharingIntent, getString(R.string.detail_screen_comment_comment_text)));
			}
		});
		
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		alertLayout = inflater.inflate(R.layout.comment_dialog,
		                               (ViewGroup) findViewById(R.id.layout_root));

		builder = new AlertDialog.Builder(this);
		builder.setView(alertLayout);
		alertDialog = builder.create();
		
		TextView text = (TextView) alertLayout.findViewById(R.id.tv_comment_dialog_title);
		text.setText(getString(R.string.detail_screen_comment_title));
		
		Button cancelButton = (Button) alertLayout.findViewById(R.id.btn_detail_screen_cancel);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		
		final EditText editText = (EditText) alertLayout.findViewById(R.id.et_detail_screen_comment_text);
		
		Button commentDialogButton = (Button) alertLayout.findViewById(R.id.btn_detail_screen_comment);
		commentDialogButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String commentText = editText.getEditableText().toString();
				if(commentText!=null&&!"".equals(commentText)){
					OAuth oAuth = stadtgefluesterapplication.getOAuthToken();
//					Flickr flickr = stadtgefluesterapplication.getFlickrAuthed(oAuth.getToken().getOauthToken(), oAuth.getToken().getOauthTokenSecret());
//					String commentId = null;
//					try {
						//commentId = flickr.getCommentsInterface().addComment(photo.getId(),commentText);
						Comment comment = new Comment();
						//comment.setId(commentId);
						comment.setText(commentText);
						comment.setAuthorName(oAuth.getUser().getUsername());
						comments.add(comment);
						commentAdapter.notifyDataSetChanged();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (FlickrException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
				}
				alertDialog.dismiss();
			}
		});
		
//		ImageView image = (ImageView) layout.findViewById(R.id.image);
//		image.setImageResource(R.drawable.android);

		
		
		commentButton = (ImageButton) findViewById(R.id.ib_detail_screen_comment);
		commentButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.show();
			}
		});
		
		
		favoriteButton = (ImageButton) findViewById(R.id.ib_detail_screen_star);
		favoriteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.makeToast(getString(R.string.detail_screen_comment_favorite), activity);
			}
		});
		
		
		
	}

	private void downloadImage() {
		ImageView image=(ImageView) findViewById(R.id.iv_detail_view_top);                
        if (image != null) {
        	ImageDownloadTask task = new ImageDownloadTask(image);
            Drawable drawable = new DownloadedDrawable(task);
            image.setImageDrawable(drawable);
            task.execute(photo.getMediumUrl());
            
        }
	}

	private void setViewContent() {
		downloadImage();
		setTitle();
		populateListViewCOntent();
	}
	
	private void setTitle() {		
		TextView textViewTitle = (TextView) findViewById(R.id.tv_detail_view_title);
		textViewTitle.setText(photo.getTitle());
	}

	private void populateListViewCOntent() {
		/** Filthy Stuff **/
		 comments = (ArrayList<Comment>) stadtgefluesterapplication.getComments(photoID);		
		/** Filthy Stuff End **/

		listview = (ListView) findViewById(R.id.lv_comments);
		commentAdapter = new CommentAdapter(this, R.layout.listviewlayout,
				 comments);
		listview.setAdapter(commentAdapter);
		
	}
	
	private class CommentAdapter extends ArrayAdapter<Comment> {

		private ArrayList<Comment> items;

		public CommentAdapter(Context context, int textViewResourceId,
				ArrayList<Comment> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.listviewlayout, null);
			}
			Comment o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.tv_comment_author);
				TextView bt = (TextView) v.findViewById(R.id.tv_comment_text);
				if (tt != null) {
					tt.setText(o.getAuthorName() + ": ");
				}
				if (bt != null) {
					bt.setText(o.getText());
				}
			}
			return v;
		}
	}

	
	}
