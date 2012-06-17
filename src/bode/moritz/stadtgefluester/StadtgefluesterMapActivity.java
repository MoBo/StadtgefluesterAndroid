package bode.moritz.stadtgefluester;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class StadtgefluesterMapActivity extends MapActivity {

	private MapController mapController;
	private final static GeoPoint STANDARD_LOCATION= new GeoPoint((int)(53.063249*1E6), (int)(8.811057*1E6));
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setContentView(R.layout.map_screen);
		
		MapView mapView = (MapView) findViewById(R.id.mv_maps_screens);
	    mapView.setSatellite(true);
	    
	    this.mapController = mapView.getController();
	    this.mapController.setZoom(18);
	    this.mapController.animateTo(STANDARD_LOCATION);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
