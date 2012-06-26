package bode.moritz.stadtgefluester;

import com.gmail.yuyang226.flickr.photos.Photo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.widget.TextView;

public class StadtgefluesterDetailActivity extends Activity {
	private StadtgefluesterApplication stadtgefluesterapplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.detail_screen);
		String photoID = getIntent().getStringExtra(StadtgefluesterApplication.PHOTO_ID_STRING);
		stadtgefluesterapplication = (StadtgefluesterApplication) getApplication();
		Photo photo = stadtgefluesterapplication.getPhotoById(photoID);
		
		TextView textViewTitle = (TextView) findViewById(R.id.tv_detail_view_title);
		textViewTitle.setText(photo.getTitle());
	}
}
