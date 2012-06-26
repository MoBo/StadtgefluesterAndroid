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
			(int) (53.063036 * 1E6), (int) (8.80831 * 1E6));
	private final static GeoPoint TEST_LOCATION1 = new GeoPoint(
			(int) (53.062521 * 1E6), (int) (8.805478 * 1E6));
	private final static GeoPoint TEST_LOCATION2 = new GeoPoint(
			(int) (53.063252 * 1E6), (int) (8.811057 * 1E6));
	private HelloItemizedOverlay itemizedoverlay;
	private List<Overlay> mapOverlays;
	private StadtgefluesterApplication stadtgefluesterApplication;
	public Activity a =  this;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.map_screen);
		this.stadtgefluesterApplication = (StadtgefluesterApplication) this
				.getApplication();

		MapView mapView = (MapView) findViewById(R.id.mv_maps_screens);
		mapView.setSatellite(false);

		this.mapController = mapView.getController();
		this.mapController.setZoom(17);

		this.mapOverlays = mapView.getOverlays();
		addOverlays();
		this.mapController.animateTo(STANDARD_LOCATION);

	}

	private void addOverlays() {
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.androidmarker);
		itemizedoverlay = new HelloItemizedOverlay(drawable);
		PhotoList photoList = stadtgefluesterApplication.getPhotoList();

		for (Photo photo : photoList) {
			GeoData geoData = photo.getGeoData();
			GeoPoint geoPoint = new GeoPoint(
					(int) (geoData.getLatitude() * 1E6),
					(int) (geoData.getLongitude() * 1E6));
			OverlayItem overlayItem = new OverlayItem(geoPoint, photo.getId(),
					"test");
			itemizedoverlay.addOverlay(overlayItem);
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
