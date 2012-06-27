package bode.moritz.stadtgefluester;

import java.util.ArrayList;
import java.util.List;

import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.comments.Comment;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StadtgefluesterDetailActivity extends Activity {
	private StadtgefluesterApplication stadtgefluesterapplication;
	private String photoID = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.detail_screen);
		stadtgefluesterapplication = (StadtgefluesterApplication) getApplication();
		this.photoID = getIntent().getStringExtra(
				StadtgefluesterApplication.PHOTO_ID_STRING);
		setViewContent();
	}

	private void setViewContent() {
		setTitle();
		populateListViewCOntent();
	}
	
	private void setTitle() {		
		Photo photo = stadtgefluesterapplication.getPhotoById(photoID);
		TextView textViewTitle = (TextView) findViewById(R.id.tv_detail_view_title);
		textViewTitle.setText(photo.getTitle());
	}

	private void populateListViewCOntent() {
		/** Filthy Stuff **/
		 ArrayList<Comment> content = (ArrayList<Comment>) stadtgefluesterapplication.getComments(photoID);		
		/** Filthy Stuff End **/

		ListView listview = (ListView) findViewById(R.id.lv_comments);
		listview.setAdapter(new CommentAdapter(this, R.layout.listviewlayout,
				 content));
		
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
					tt.setText(o.getAuthor() + ": ");
				}
				if (bt != null) {
					bt.setText(o.getText() + ": ");
				}
			}
			return v;
		}
	}

	
	}
