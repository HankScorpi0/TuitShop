package businessTwitter.android;

import java.util.GregorianCalendar;

import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import businessTwitter.android.constantes.ConstantesNavegacion;
import businessTwitter.android.util.image.ImageHelper;

public class BusinessTwitterDetailActivity extends Activity{
	
	private ImageView btnBuscar;
	private ImageView btnAtras;
	private Tweet tweet;		
	private LinearLayout linearLayoutContenido;
	private Button btnEnviarMensaje;
	private EditText editMensaje;
	private TextView txtTotalLetras;
	
	private String twitterAccesToken = "";
	private String twitterAccesTokenSecret = "";

	private Twitter twitter;
	private RequestToken rt = null;
	
	private static final int SIGNIN_TWITTER = 0;
	
	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.detaillayout);
		
		this.cargarBtnEnviarMensaje();
		this.cargarBtnBuscar();
		this.cargarBtnAtras();
		this.cargarLinerLayoutContenido();
		this.cargarEditMensaje();
		this.cargarTxtTotalLetras();
	}
	
	private void cargarTxtTotalLetras() {
		this.txtTotalLetras  =(TextView)findViewById(R.id.txtTotalLetras);
		this.txtTotalLetras.setText(getString(R.string.totalLetras) + ": 140");
	}
	
	private void cargarEditMensaje() {
		this.editMensaje = (EditText)findViewById(R.id.editDM);
		this.editMensaje.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {
				
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				totalLetrasTweet();
				
			}
			
		});
	}
	
	private void totalLetrasTweet(){
		int total = 140 - (this.editMensaje.getText().toString().length());		
		this.txtTotalLetras.setText(getString(R.string.totalLetras) + ": " + total);
	}

	private void cargarLinerLayoutContenido() {
		this.linearLayoutContenido = (LinearLayout)findViewById(R.id.linearLayoutContenido);		
	}
	
	private void cargarBtnEnviarMensaje(){
    	this.btnEnviarMensaje = (Button)findViewById(R.id.btnEnviarMensaje);    	
    	this.btnEnviarMensaje.setOnClickListener(new OnClickListener() {
    		
			public void onClick(View v) {
				enviarDM();				
			}
		});
    }
	
	private void enviarDM(){
		
		if (this.editMensaje.getText().toString().length() > 140) {
			Toast toast = Toast.makeText(BusinessTwitterDetailActivity.this,
					getString(R.string.tweetmax140), 5);
			toast.show();
			return;
		}
		
		this.comprobarEstadoTwitter();
		
		Configuration conf = new ConfigurationBuilder()
		.setOAuthConsumerKey(getString(R.string.consumerKey))
		.setOAuthConsumerSecret(getString(R.string.consumerSecret))
		.setOAuthAccessToken(twitterAccesToken)
		.setOAuthAccessTokenSecret(twitterAccesTokenSecret).build();
		
		Twitter t = new TwitterFactory(conf).getInstance();
		
		try {		
			t.sendDirectMessage(this.tweet.getFromUserId(), this.editMensaje.getText().toString());
			Toast toast = Toast.makeText(BusinessTwitterDetailActivity.this, getString(R.string.tweetPublicado), 5);
			toast.show();
			Intent intent = new Intent(ConstantesNavegacion.INTENT_ACTION_VIEW_SEARCH);
	    	startActivity(intent);
		} catch (TwitterException e) {
			Toast toast = Toast.makeText(BusinessTwitterDetailActivity.this, getString(R.string.errPublicarTweet), 5);
			toast.show();
		}
	}
	
	private void comprobarEstadoTwitter() {
		SharedPreferences settings = getSharedPreferences("TwitterPreferences", MODE_PRIVATE);

		twitterAccesToken = settings.getString("twitterAccesToken", "");
		twitterAccesTokenSecret = settings.getString("twitterAccesTokenSecret","");

		twitter = new TwitterFactory().getInstance();

		if (twitterAccesToken.length() == 0) {
			twitter.setOAuthConsumer(getString(R.string.consumerKey), getString(R.string.consumerSecret));
			try {
				rt = twitter.getOAuthRequestToken(getString(R.string.CALLBACKURL));

			} catch (TwitterException e) {
				//e.printStackTrace();
			}
			Intent intent = new Intent(this, WebActivity.class);
			intent.putExtra("URL", rt.getAuthenticationURL());
			startActivityForResult(intent, SIGNIN_TWITTER);
		}
		
	}
	
	@Override
 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SIGNIN_TWITTER){
				String oauthVerifier = (String) data.getExtras().get(
						"oauth_verifier");
				AccessToken at = null;
				try {
					at = twitter.getOAuthAccessToken(rt, oauthVerifier);
	
					SharedPreferences appSettings = getSharedPreferences("TwitterPreferences", MODE_PRIVATE);
					SharedPreferences.Editor prefEditor = appSettings.edit();
					prefEditor.putString("twitterAccesToken", at.getToken());
					prefEditor.putString("twitterAccesTokenSecret", at.getTokenSecret());
					prefEditor.commit();
	
				} catch (TwitterException e) {
					//e.printStackTrace();
				}
			}
		}
	}

	private void cargarBtnAtras(){
    	this.btnAtras = (ImageView) findViewById(R.id.imageViewLeft);    	
    	
    	this.btnAtras.setOnClickListener(new OnClickListener() {
    		
			public void onClick(View v) {
				buscarArticulosAtras();				
			}
		});
    }
	
	private void cargarBtnBuscar(){
    	this.btnBuscar = (ImageView) findViewById(R.id.btnBuscar);
    	this.btnBuscar.setOnClickListener(new OnClickListener() {
    		
			public void onClick(View v) {
				buscarArticulos();				
			}
		});
    }

	private void buscarArticulosAtras(){
		Intent intent = new Intent(ConstantesNavegacion.INTENT_ACTION_VIEW_LIST);
    	startActivity(intent);
	}
	
	private void buscarArticulos()
    {    	    	
    	Intent intent = new Intent(ConstantesNavegacion.INTENT_ACTION_VIEW_SEARCH);
    	startActivity(intent);
    }
	
	@Override
	protected void onResume(){
		super.onResume();
		
		if(this.tweet != null) return;
		
		BusinessTwitterApplication app = (BusinessTwitterApplication)getApplication();
		this.tweet = app.getTweetSelected();
		
		this.cargarArticulo();
	}
	
	private void cargarArticulo(){	
		TextView contenido;
		TextView usuario;
		TextView usuarioId;
		ImageView usuarioImagen;
		TextView info;
		ImageView articuloImagen;
		TextView urlImagen;
				
		LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 3, 5, 3);
		
		LinearLayout lAux = new LinearLayout(BusinessTwitterDetailActivity.this);
		lAux.setOrientation(LinearLayout.HORIZONTAL);
		lAux.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT));
		
		usuarioImagen = new ImageView(BusinessTwitterDetailActivity.this);			
		Bitmap bm = ImageHelper.getBitmapByURL(tweet.getProfileImageUrl());
		if(bm != null)
			usuarioImagen.setImageBitmap(bm);
		lAux.addView(usuarioImagen, params);
	
		usuario = new TextView(BusinessTwitterDetailActivity.this);
		usuario.setText(tweet.getFromUserName());
		usuario.setTextSize(14f);
		usuario.setTypeface(Typeface.DEFAULT_BOLD);
		params.gravity = Gravity.CENTER_VERTICAL;
		lAux.addView(usuario, params);
		
		usuarioId = new TextView(BusinessTwitterDetailActivity.this);
		usuarioId.setText("@" + tweet.getFromUser());
		usuarioId.setTextSize(14f);
		usuarioId.setTypeface(Typeface.DEFAULT_BOLD);
		params.gravity = Gravity.CENTER_VERTICAL;
		lAux.addView(usuarioId, params);
		
		this.linearLayoutContenido.addView(lAux, params);
		
		contenido = new TextView(BusinessTwitterDetailActivity.this);
		contenido.setText("@" + tweet.getText());
		contenido.setTextSize(16f);
		params.gravity = Gravity.CENTER_VERTICAL;
		this.linearLayoutContenido.addView(contenido, params);
		
		if(tweet.getURLEntities() != null && tweet.getURLEntities().length>0){
			urlImagen = new TextView(BusinessTwitterDetailActivity.this);		
			urlImagen.setTextSize(14f);			
			urlImagen.setText("+info: " + tweet.getURLEntities()[0].getURL().toString());
			Linkify.addLinks(urlImagen, Linkify.WEB_URLS);
			this.linearLayoutContenido.addView(urlImagen, params);
		}
	
		if(tweet.getMediaEntities() != null && tweet.getMediaEntities().length>0){
			articuloImagen = new ImageView(BusinessTwitterDetailActivity.this);
			Bitmap bmArticulo = ImageHelper.getBitmapByURL(tweet.getMediaEntities()[0].getDisplayURL());
			if(bmArticulo != null)
				articuloImagen.setImageBitmap(bmArticulo);
			this.linearLayoutContenido.addView(articuloImagen, params);
		}
		
		info = new TextView(BusinessTwitterDetailActivity.this);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(tweet.getCreatedAt());
		if(tweet.getLocation() != null && tweet.getLocation().equals("") == false)
			info.setText(gc.getTime().toLocaleString() + " - " + tweet.getLocation());
		else
			info.setText(gc.getTime().toLocaleString());
		info.setTextSize(12f);
		params.gravity = Gravity.CENTER_VERTICAL;
		this.linearLayoutContenido.addView(info, params);				
	}
}
