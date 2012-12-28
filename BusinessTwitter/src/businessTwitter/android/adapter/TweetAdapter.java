package businessTwitter.android.adapter;

import java.util.GregorianCalendar;
import java.util.List;

import twitter4j.Tweet;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class TweetAdapter extends BaseAdapter{
	
	private final Context context;
	private final List<Tweet> tweets;
	
	public TweetAdapter(Context context, List<Tweet> tweets){
		this.context = context;
		this.tweets = tweets;
	}
	
	public int getCount() {
		return this.tweets.size();
	}

	public Object getItem(int position) {
		return this.tweets.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = this.tweets.get(position);
		return new TweetListView(this.context, tweet);
	}
	
	public final class TweetListView extends LinearLayout{		
		private TextView contenido;
		private TextView usuario;
		private ImageView usuarioImagen;
		private TextView info;
		
		public TweetListView(Context context, Tweet tweet){
			super(context);
			
			setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 3, 5, 3);
			
			//Cargamos la imagen lazy y con cache
			this.usuarioImagen = new ImageView(context);
			this.usuarioImagen.setLayoutParams(params);						
			DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory()
				.cacheOnDisc()
				.build();
			
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
			imageLoader.displayImage(tweet.getProfileImageUrl(), this.usuarioImagen, options);
			 
			this.addView(this.usuarioImagen, params);
			
			LinearLayout lAux = new LinearLayout(context);
			lAux.setOrientation(LinearLayout.VERTICAL);
			lAux.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT));
			
			this.usuario = new TextView(context);
			this.usuario.setText(tweet.getFromUserName() + " @" + tweet.getFromUser());
			this.usuario.setTextSize(12f);
			this.usuario.setTypeface(Typeface.DEFAULT_BOLD);
			params.gravity = Gravity.CENTER_VERTICAL;
			lAux.addView(this.usuario);
			
			this.contenido = new TextView(context);
			this.contenido.setText(tweet.getText());
			this.contenido.setTextSize(12f);
			params.gravity = Gravity.CENTER_VERTICAL;
			lAux.addView(this.contenido);
			
			this.info = new TextView(context);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(tweet.getCreatedAt());
			if(tweet.getLocation() != null && tweet.getLocation().equals("") == false)
				this.info.setText(gc.getTime().toLocaleString() + " - " + tweet.getLocation());
			else
				this.info.setText(gc.getTime().toLocaleString());
			this.info.setTextSize(12f);
			this.info.setTypeface(Typeface.DEFAULT_BOLD);
			params.gravity = Gravity.CENTER_VERTICAL;
			lAux.addView(this.info);
			
			this.addView(lAux, params);
		}
	}

}
