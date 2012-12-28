package businessTwitter.android.util.search;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

public class GeoLocationHelper {
	
	public static double getLonByLocation(Context context, String ciudad){
		Geocoder geocoder = new Geocoder(context);
		try {
			List<Address> result = geocoder.getFromLocationName(ciudad, 1);
			if(result != null && result.isEmpty() == false){
				return result.get(0).getLongitude();
			}
		} catch (IOException e) {
			return 0D;
		}
		
		return 0D;
	}
	
	public static double getLatByLocation(Context context, String ciudad){
		Geocoder geocoder = new Geocoder(context);
		try {
			List<Address> result = geocoder.getFromLocationName(ciudad, 1);
			if(result != null && result.isEmpty() == false){
				return result.get(0).getLatitude();
			}
		} catch (IOException e) {
			return 0D;
		}
		
		return 0D;
	}
	
	public static Location getLatLonByLocation(Context context){
		@SuppressWarnings("static-access")
		LocationManager lm = (LocationManager)context.getSystemService(context.LOCATION_SERVICE); 
		Location l = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		return l;
	}
	
	public static String getLocality(Context context){
		@SuppressWarnings("static-access")
		LocationManager lm = (LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		if(lm == null) return "";
		Location l = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(l == null) return "";
		Geocoder gc = new Geocoder(context);
		
		try {
			List<Address> address = gc.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
			if(address != null && address.isEmpty() == false)
				return address.get(0).getLocality();
		} catch (Exception e) {
			return "";
		}
		
		return "";
	}
	
	public static String getCountry(Context context){
		@SuppressWarnings("static-access")
		LocationManager lm = (LocationManager)context.getSystemService(context.LOCATION_SERVICE); 
		Location l = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Geocoder gc = new Geocoder(context);
		
		try {
			List<Address> address = gc.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
			if(address != null && address.isEmpty() == false)
				return address.get(0).getCountryName();
		} catch (IOException e) {
			return "";
		}
		
		return "";
	}

}
