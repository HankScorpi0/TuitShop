package businessTwitter.android;

import java.util.List;

import twitter4j.Tweet;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import businessTwitter.android.adapter.TweetAdapter;
import businessTwitter.android.constantes.ConstantesNavegacion;
import businessTwitter.android.messageGetter.TweetGetter;

public class BusinessTwitterListActivity extends Activity {
	
	private ProgressDialog progressDialog;
	private TweetAdapter tweetAdapter;
	private List<Tweet> tweets;
	private ListView listView;
	private ImageView btnBuscar;
	private ImageView btnAtras;
	private ImageView btnAdelante;
	private int numPagina;

	private final Handler handler = new Handler(){
		public void handleMessage(final Message msg){
			progressDialog.dismiss();			
			if(tweets == null || tweets.isEmpty()){	
				View empty = findViewById(R.id.empty);
				listView.setEmptyView(empty);
			}else{
				tweetAdapter = new TweetAdapter(BusinessTwitterListActivity.this, tweets);						
				listView.setAdapter(tweetAdapter);
			}
		}
	};
	
	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.listlayout);
		
		this.initListView();
		this.cargarBtnBuscar();
		this.cargarBtnAtras();
		this.cargarBtnAdelante();
	}
	
	private void buscarArticulosAtras(){
		this.numPagina = this.numPagina - 1;
		if(this.numPagina == -1)
			this.numPagina = 0;	
		
		BusinessTwitterApplication app = (BusinessTwitterApplication)getApplication();
		String titulo = app.getTitulo();
		String ciudad = app.getCiudad();
		String tipo = app.getTipo();
		boolean conLinks = app.isConLinks();
		double longitude = app.getLongitude();
		double latitude = app.getLatitude();
		
		this.cargarArticulos(titulo, ciudad, tipo, this.numPagina, conLinks, longitude, latitude);
	}
	
	private void buscarArticulosAdelante(){
		this.numPagina = this.numPagina + 1;
		
		BusinessTwitterApplication app = (BusinessTwitterApplication)getApplication();
		String titulo = app.getTitulo();
		String ciudad = app.getCiudad();
		String tipo = app.getTipo();
		boolean conLinks = app.isConLinks();
		double longitude = app.getLongitude();
		double latitude = app.getLatitude();
		
		this.cargarArticulos(titulo, ciudad, tipo, this.numPagina, conLinks, longitude, latitude);
	}
	
	private void cargarBtnAtras(){
    	this.btnAtras = (ImageView) findViewById(R.id.imageViewLeft);    	
    	
    	this.btnAtras.setOnClickListener(new OnClickListener() {
    		
			public void onClick(View v) {
				buscarArticulosAtras();				
			}
		});
    }
	
	private void cargarBtnAdelante(){
    	this.btnAdelante = (ImageView) findViewById(R.id.imageViewRight);
    	this.btnAdelante.setOnClickListener(new OnClickListener() {
    		
			public void onClick(View v) {
				buscarArticulosAdelante();				
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
	
	private void buscarArticulos()
    {    	    	
    	Intent intent = new Intent(ConstantesNavegacion.INTENT_ACTION_VIEW_SEARCH);
    	startActivity(intent);
    }
	
	private void initListView(){
		listView = (ListView)findViewById(R.id.list);
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		this.listView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> a, View v, int i,
					long l) {
				BusinessTwitterApplication app = (BusinessTwitterApplication)getApplication();
				app.setTweetSelected(tweets.get(i));
				app.setNumPagina(numPagina);
				
				Intent intent = new Intent(ConstantesNavegacion.INTENT_ACTION_VIEW_DETAIL);
		    	startActivity(intent);
				
			}			
		});
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		BusinessTwitterApplication app = (BusinessTwitterApplication)getApplication();
		String titulo = app.getTitulo();
		String ciudad = app.getCiudad();
		String tipo = app.getTipo();
		boolean conLinks = app.isConLinks();
		this.numPagina = app.getNumPagina();
		double longitude = app.getLongitude();
		double latitude = app.getLatitude();
		
		if(this.tweets == null || this.tweets.isEmpty())
			this.cargarArticulos(titulo, ciudad, tipo, this.numPagina, conLinks, longitude, latitude);
	}
	
	private void cargarArticulos(final String titulo, final String ciudad, final String tipo, 
			final int numPagina, final boolean conLinks, final double latitude, final double longitude){
		final TweetGetter tweetGetter = new TweetGetter();
		
		this.progressDialog = ProgressDialog.show(this, getString(R.string.cargando), getString(R.string.buscandoAnuncios), true, false);
		new Thread(){
			public void run(){
				tweets = tweetGetter.getTweetsBySearch(tipo, titulo, ciudad, numPagina, conLinks, latitude, longitude);				
				handler.sendEmptyMessage(0);
			}
		}.start();
	}
}
