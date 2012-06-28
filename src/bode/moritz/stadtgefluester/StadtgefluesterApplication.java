package bode.moritz.stadtgefluester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.REST;
import com.gmail.yuyang226.flickr.RequestContext;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthToken;
import com.gmail.yuyang226.flickr.people.User;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.gmail.yuyang226.flickr.photos.comments.Comment;

public class StadtgefluesterApplication extends Application {


	private static final String LOG_TAG = "Stadtgfluester";
	
	public static final String OAUTH_CALLBACK_SCHEME = "oauth-stadtgefluester-android";
	
	private static final String API_KEY = "fb39d904688ecbe4d5f4a173a959a657";
	public static final String API_SEC = "a3107c0ffa286691";
	
	public static final String PHOTO_SEARCH_TAG = "stadtgefluester";
	public static final String PHOTO_SEARCH_LAT = "53.06268";
	public static final String PHOTO_SEARCH_LON = "8.806658";
	public static final int PHOTO_SEARCH_ACCURACY = 13;
	
	private static final String STADTGEFLUESTER_PREFERENCES = "stadtgefluester_prefs";
	private static final String KEY_OAUTH_TOKEN = "key_oauth_token";
	private static final String KEY_TOKEN_SECRET = "key_token_secret";
	private static final String KEY_USER_NAME = "key_user_name";
	private static final String KEY_USER_ID = "key_user_id";
	public static final String PHOTO_ID_STRING = "photo_id_string";
	private HashMap<String, List<Comment>> commenthashMap = new HashMap<String, List<Comment>>();
	private PhotoList photoList;

	private ArrayList<Drawable> photothumbnaillist = new ArrayList<Drawable>();
	
	public void addCommentHash(String id, List<Comment> comment) {
		commenthashMap.put(id, comment);
	}

	public OAuth getOAuthToken() {
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(STADTGEFLUESTER_PREFERENCES,
				Context.MODE_PRIVATE);
		String oauthTokenString = settings.getString(KEY_OAUTH_TOKEN, null);
		String tokenSecret = settings.getString(KEY_TOKEN_SECRET, null);
		if (oauthTokenString == null && tokenSecret == null) {
			Log.i(LOG_TAG,"No oauth token retrieved"); //$NON-NLS-1$
			return null;
		}
		OAuth oauth = new OAuth();
		String userName = settings.getString(KEY_USER_NAME, null);
		String userId = settings.getString(KEY_USER_ID, null);
		if (userId != null) {
			User user = new User();
			user.setUsername(userName);
			user.setId(userId);
			oauth.setUser(user);
		}
		OAuthToken oauthToken = new OAuthToken();
		oauth.setToken(oauthToken);
		oauthToken.setOauthToken(oauthTokenString);
		oauthToken.setOauthTokenSecret(tokenSecret);
		return oauth;
	}
	
	public void saveOAuthToken(String userName, String userId, String token, String tokenSecret) {
    	SharedPreferences sp = getSharedPreferences(STADTGEFLUESTER_PREFERENCES,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(KEY_OAUTH_TOKEN, token);
		editor.putString(KEY_TOKEN_SECRET, tokenSecret);
		editor.putString(KEY_USER_NAME, userName);
		editor.putString(KEY_USER_ID, userId);
		editor.commit();
    }
	
	public Flickr getFlickr() {
		try {
			Flickr f = new Flickr(API_KEY, API_SEC, new REST());
			return f;
		} catch (ParserConfigurationException e) {
			return null;
		}
	}
	
	public Flickr getFlickrAuthed(String token, String secret) {
		Flickr f = getFlickr();
		RequestContext requestContext = RequestContext.getRequestContext();
		OAuth auth = new OAuth();
		auth.setToken(new OAuthToken(token, secret));
		requestContext.setOAuth(auth);
		return f;
	}

	public void setPhotoList(PhotoList result) {
		this.photoList = result;
	}

	public PhotoList getPhotoList() {
		return photoList;
	}

	public void replacePhotoInList(Photo tmp) {
		int i = photoList.indexOf(tmp);
		photoList.set(i, tmp);
	}

	public Photo getPhotoById(String photoID) {
		for (Photo photo : this.photoList) {
			if(photo.getId().equals(photoID)){
				return photo;
			}
		}
		return null;
	}

	public List<Comment> getComments(String photoID) {
		return commenthashMap.get(photoID);
		
	}

	public void addPhotoThumbNail(Drawable tmp) {
		getPhotothumbnaillist().add(tmp);		
	}

	public ArrayList<Drawable> getPhotothumbnaillist() {
		return photothumbnaillist;
	}

	public void setPhotothumbnaillist(ArrayList<Drawable> photothumbnaillist) {
		this.photothumbnaillist = photothumbnaillist;
	}
	
}
