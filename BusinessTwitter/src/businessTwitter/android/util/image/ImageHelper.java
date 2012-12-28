package businessTwitter.android.util.image;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageHelper {
	
	public static Bitmap getBitmapByURL(String url){
		Bitmap bm = null;
		
		try {
			bm = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		
		return bm;
	}

}
