package bode.moritz.stadtgefluester;

import java.util.ArrayList;
import java.util.List;

import android.R.anim;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import bode.moritz.stadtgefluester.task.PhotoDetailsTask;

import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.photos.GeoData;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class StadtgefluesterMapActivity extends MapActivity {

	private MapController mapController;
	private final static GeoPoint STANDARD_LOCATION = new GeoPoint(
			(int) (53.079435 * 1E6), (int) (8.805456 * 1E6));	
	private HelloItemizedOverlay itemizedoverlay;
	private List<Overlay> mapOverlays;
	private StadtgefluesterApplication stadtgefluesterApplication;
	public Activity a =  this;
	private ImageView cameraIcon;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.map_screen);
		this.stadtgefluesterApplication = (StadtgefluesterApplication) this
				.getApplication();
		
		MapView mapView = (MapView) findViewById(R.id.mv_maps_screens);
		mapView.setSatellite(false);

		this.mapController = mapView.getController();
		this.mapController.setZoom(14);

		this.mapOverlays = mapView.getOverlays();
		addOverlays();
		this.mapController.animateTo(STANDARD_LOCATION);

		
		
		this.cameraIcon = (ImageView) findViewById(R.id.iv_map_screen_camera_icon);
		this.cameraIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				startActivity(intent);
			}
		});
	}

	private void addOverlays() {
		PhotoList photoList = stadtgefluesterApplication.getPhotoList();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.pin);
		itemizedoverlay = new HelloItemizedOverlay(drawable);
		
		int i = 0;
		ArrayList<Drawable> thumbnaildrawables = stadtgefluesterApplication.getPhotothumbnaillist();
		for (Photo photo : photoList) {
			GeoData geoData = photo.getGeoData();
			GeoPoint geoPoint = new GeoPoint(
					(int) (geoData.getLatitude() * 1E6),
					(int) (geoData.getLongitude() * 1E6));
			OverlayItem overlayItem = new OverlayItem(geoPoint, photo.getId(),
					"test");
			/*Add Picture-Thumbnails to Overlay Item*/
			Drawable thumbnail = thumbnaildrawables.get(i);
			thumbnail.setBounds(0, 0, thumbnail.getIntrinsicWidth(), thumbnail.getIntrinsicHeight());
			overlayItem.setMarker(thumbnail);
			itemizedoverlay.addOverlay(overlayItem);
			i++;
		}		
		mapOverlays.add(itemizedoverlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public class HelloItemizedOverlay extends ItemizedOverlay<OverlayItem> {

		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		
		
		public HelloItemizedOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}

		public void addOverlay(OverlayItem overlay) {
			mOverlays.add(overlay);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}

		@Override
		protected boolean onTap(int index) {
			Photo p =stadtgefluesterApplication.getPhotoList().get(index);
			new PhotoDetailsTask(p, a ).execute(stadtgefluesterApplication.getOAuthToken());
			return true;
			
			
//			Intent intent = new Intent(getApplicationContext(), StadtgefluesterDetailActivity.class);
//			startActivity(intent);
//			return super.onTap(index);
		}
	}

}
