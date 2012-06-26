package de.unibremen.util;

import android.app.Activity;
import android.widget.Toast;


public class Util {
	
	/**
	 * Makes a short notification on UI
	 * @param message message to be displayed
	 * @param a the application Context
	 */
	public static void makeToast(String message, Activity a){
		
			Toast.makeText(a.getApplicationContext(), message, Toast.LENGTH_SHORT)
					.show();		
	}
}
