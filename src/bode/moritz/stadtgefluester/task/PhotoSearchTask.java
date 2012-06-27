package bode.moritz.stadtgefluester.task;

import java.io.IOException;

import org.json.JSONException;

import bode.moritz.stadtgefluester.SplashscreenActivity;
import bode.moritz.stadtgefluester.StadtgefluesterApplication;
import bode.moritz.stadtgefluester.StadtgefluesterMapActivity;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthToken;
import com.gmail.yuyang226.flickr.photos.GeoData;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.gmail.yuyang226.flickr.photos.SearchParameters;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;

public class PhotoSearchTask extends AsyncTask<OAuth, Void, PhotoList>{

	
	private SplashscreenActivity activity;
	private StadtgefluesterApplication stadtgefluesterApplication;
	
	public PhotoSearchTask(SplashscreenActivity activity) {
		this.activity = activity;
		this.stadtgefluesterApplication = (StadtgefluesterApplication) activity.getApplication();
	}
	@Override
	protected PhotoList doInBackground(OAuth... params) {
		OAuthToken token = params[0].getToken();
		Flickr flickr = stadtgefluesterApplication.getFlickrAuthed(token.getOauthToken(), 
				token.getOauthTokenSecret());
		SearchParameters searchParameters = new SearchParameters();
		searchParameters.setTags(new String[]{StadtgefluesterApplication.PHOTO_SEARCH_TAG});
		

		try {
			PhotoList photoList = flickr.getPhotosInterface().search(searchParameters, 50, 1);
			//PhotoList newPhotoList =  new PhotoList();
			for (Photo photo : photoList) {
				GeoData geoData = flickr.getGeoInterface().getLocation(photo.getId());
				photo.setGeoData(geoData);
//				Photo newPhoto = flickr.getPhotosInterface().getInfo(photo.getId(), "");
//				newPhotoList.add(newPhoto);
			}
			return photoList;
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
	protected void onPostExecute(PhotoList result) {
		stadtgefluesterApplication.setPhotoList(result);
		Intent intent = new Intent(activity,StadtgefluesterMapActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}

}
