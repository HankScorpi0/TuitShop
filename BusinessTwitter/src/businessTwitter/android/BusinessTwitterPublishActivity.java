package businessTwitter.android;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import businessTwitter.android.constantes.ConstantesNavegacion;
import businessTwitter.android.util.hashtag.HashTagHelper;

public class BusinessTwitterPublishActivity extends Activity{
	private ProgressDialog progressDialog;
	
	private ImageView btnBuscar;
	private EditText editTitulo;
	private EditText editDescripcion;
	private Button btnPublicar;
	private ImageView btnCamara;
	private ImageView imagenSubida;
	private TextView txtTotalLetras;
	private Spinner cmbTipo;
	
	private String twitterAccesToken = "";
	private String twitterAccesTokenSecret = "";

	private Twitter twitter;
	private RequestToken rt = null;
	
	private static final int MAKE_PHOTO = 2;
	private static final int SELECT_PICTURE = 1;
	private static final int SIGNIN_TWITTER = 0;
	
	private String selectedImagePath;
	
	private Bitmap bitmap;

	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.publishlayout);
		
		this.cargarBtnBuscar();
		this.cargarBtnPublicar();
		this.cargarEditTitulo();
		this.comprobarEstadoTwitter();
		this.cargarBtnAnyadirImagen();
		this.cargarImagenSubida();
		this.cargarBtnCamara();
		this.cargarEditDescripcion();
		this.cargarTxtTotalLetras();
		this.cargarCmbTipo();
	}
	
	private void cargarCmbTipo(){
    	this.cmbTipo = (Spinner) findViewById(R.id.cmbAccion);
        this.cmbTipo.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v,
					int pos, long id) {
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
    }
	
	private void cargarTxtTotalLetras() {
		this.txtTotalLetras  =(TextView)findViewById(R.id.txtTotalLetras);
		this.txtTotalLetras.setText(getString(R.string.totalLetras) + ": 140");
	}

	private void cargarEditDescripcion() {
		this.editDescripcion = (EditText)findViewById(R.id.editDescripcion);
		this.editDescripcion.addTextChangedListener(new TextWatcher(){

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
		int total = 140 - (this.editDescripcion.getText().toString().length() + this.editTitulo.getText().toString().length());
		if(this.selectedImagePath != null && this.selectedImagePath.equals("") == false)
			total = total - 30;
		this.txtTotalLetras.setText(getString(R.string.totalLetras) + ": " + total);
	}

	private void cargarBtnCamara() {
		this.btnCamara = (ImageView)findViewById(R.id.btnFotoCamara);
		this.btnCamara.setOnClickListener(new OnClickListener() {
    		
			public void onClick(View v) {
				hacerFoto();				
			}
		});
	}
	
	private void hacerFoto(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(intent, MAKE_PHOTO);
	}

	private void cargarImagenSubida() {
		imagenSubida = (ImageView)findViewById(R.id.imagenSubida);
		
	}

	private void cargarBtnAnyadirImagen() {
		((ImageView) findViewById(R.id.btnAnyadirFoto))
        .setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }
        });
		
	}		

	private void cargarEditTitulo() {
		this.editTitulo = (EditText)findViewById(R.id.editTitulo);		
		this.editTitulo.addTextChangedListener(new TextWatcher(){

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

	private void cargarBtnPublicar() {
		this.btnPublicar = (Button) findViewById(R.id.btnPublicarAnuncio);
    	this.btnPublicar.setOnClickListener(new OnClickListener() {
    		
			public void onClick(View v) {
				publicarAnuncio();				
			}
		});
		
	}

	@Override
 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		   
		if (requestCode == MAKE_PHOTO && resultCode == Activity.RESULT_OK){
//			try {
				// We need to recyle unused bitmaps
				if (bitmap != null) {
					bitmap.recycle();
				}

//				if(data.getData() != null)
//				{
					bitmap = (Bitmap)data.getExtras().get("data");
//					InputStream stream = getContentResolver().openInputStream(data.getData());
//					selectedImagePath = getPath(data.getData());
//					bitmap = BitmapFactory.decodeStream(stream);
//					stream.close();
					this.imagenSubida.setImageBitmap(bitmap);
//				}
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
		}
		
		if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                this.imagenSubida.setImageURI(selectedImageUri);
            }
        }
		
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
		if (resultCode == Activity.RESULT_CANCELED) {
			if (requestCode == SIGNIN_TWITTER){
				Intent intent = new Intent(ConstantesNavegacion.INTENT_ACTION_VIEW_SEARCH);
		    	startActivity(intent);
			}
			
		}
	}
	
	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	private final Handler handler = new Handler(){
		public void handleMessage(final Message msg){
			progressDialog.dismiss();	
			Toast toast = Toast.makeText(BusinessTwitterPublishActivity.this, getString(R.string.tweetPublicado), 5);
			toast.show();
			Intent intent = new Intent(ConstantesNavegacion.INTENT_ACTION_VIEW_SEARCH);
	    	startActivity(intent);
		}
	};
	
	public void publicarAnuncio() {
		// Comprobamos el m‡ximo de 140 caracteres, contemplando la imagen.
		int total = 0;
		if (this.selectedImagePath != null
				&& this.selectedImagePath.equals("") == false)
			total = total + 30;
		if (this.cmbTipo.getSelectedItem().toString().length() 
				+ this.editTitulo.getText().toString().length()
				+ this.editDescripcion.getText().toString().length() + total > 140) {
			Toast toast = Toast.makeText(BusinessTwitterPublishActivity.this,
					getString(R.string.tweetmax140), 5);
			toast.show();
			return;
		}

		this.progressDialog = ProgressDialog.show(this, getString(R.string.cargando), getString(R.string.publicandoTweet), true, false);
		new Thread() {
			public void run() {

				Configuration conf = new ConfigurationBuilder()
						.setOAuthConsumerKey(getString(R.string.consumerKey))
						.setOAuthConsumerSecret(getString(R.string.consumerSecret))
						.setOAuthAccessToken(twitterAccesToken)
						.setOAuthAccessTokenSecret(twitterAccesTokenSecret)
						.build();
				Twitter t = new TwitterFactory(conf).getInstance();
				
				try {
					String url = "";

					// Galer’a
					if (selectedImagePath != null && selectedImagePath.equals("") == false) {

						Configuration confTwitPic = new ConfigurationBuilder()
								.setOAuthConsumerKey(getString(R.string.consumerKey))
								.setOAuthConsumerSecret(getString(R.string.consumerSecret))
								.setOAuthAccessToken(twitterAccesToken)
								.setOAuthAccessTokenSecret(twitterAccesTokenSecret)
								.build();

						ImageUpload upload = new ImageUploadFactory(confTwitPic).getInstance(MediaProvider.TWITGOO);
						url = upload.upload(new File(selectedImagePath), editTitulo.getText().toString());
					}

					// C‡mara
					if (bitmap != null) {

						Configuration confTwitPic = new ConfigurationBuilder()
								.setOAuthConsumerKey(getString(R.string.consumerKey))
								.setOAuthConsumerSecret(getString(R.string.consumerSecret))
								.setOAuthAccessToken(twitterAccesToken)
								.setOAuthAccessTokenSecret(twitterAccesTokenSecret)
								.build();

						ImageUpload upload = new ImageUploadFactory(confTwitPic).getInstance(MediaProvider.TWITGOO);
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bitmap.compress(CompressFormat.JPEG, 100, stream);
						InputStream is = new ByteArrayInputStream(stream.toByteArray());
						url = upload.upload(editTitulo.getText().toString(), is);
					}

					t.updateStatus(cmbTipo.getSelectedItem().toString() + " " +
							HashTagHelper.convertStringToStringHashtag(editTitulo.getText().toString()) + "." +
							editDescripcion.getText().toString() + "." + url);
				} catch (TwitterException e) {
					Toast toast = Toast.makeText(BusinessTwitterPublishActivity.this, getString(R.string.errPublicarTweet), 5);
					toast.show();
				}

				handler.sendEmptyMessage(0);
			}
		}.start();
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
	
	@Override
	protected void onResume(){
		super.onResume();
	}
}
