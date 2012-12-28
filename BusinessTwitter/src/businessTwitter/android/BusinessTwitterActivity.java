package businessTwitter.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import businessTwitter.android.constantes.ConstantesNavegacion;
import businessTwitter.android.util.search.GeoLocationHelper;

public class BusinessTwitterActivity extends Activity {
	
	private Spinner cmbTipo;
	private Button btnBuscar;
	private EditText editTitulo;
	private EditText editCiudad;
//	private CheckBox chkLocalizacion;
	private CheckBox chkConLink;
	private ImageView btnPublicarAnuncio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        this.cargarCmbTipo();
        this.cargarBtnBuscar();
        this.cargarEditTitulo();
        this.cargarEditCiudad();     
//        this.cargarChkLocalizacion();
        this.cargarChkConLink();
        this.cargarBtnPublicarAnuncio();
    }
    
    private void cargarBtnPublicarAnuncio() {
		this.btnPublicarAnuncio = (ImageView)findViewById(R.id.btnPublicarAnuncio);
		this.btnPublicarAnuncio.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				irPublicarAnuncio();
			}
		});
		
	}
    
    private void irPublicarAnuncio(){
    	Intent intent = new Intent(ConstantesNavegacion.INTENT_ACTION_VIEW_PUBLISH);
    	startActivity(intent);
    }

	private void cargarChkConLink() {
    	this.chkConLink = (CheckBox)findViewById(R.id.chkConLinks);
	}

//	private void cargarChkLocalizacion() {
//		this.chkLocalizacion = (CheckBox)findViewById(R.id.chkLocalizacion);
//		this.chkLocalizacion.setOnClickListener(new OnClickListener() {
//    		
//			public void onClick(View v) {
//				marcarLocalizacion();				
//			}
//		});
//	}
    
//    private void marcarLocalizacion(){
//    	if(this.chkLocalizacion.isChecked()){
//    		String ciudad = GeoLocationHelper.getLocality(BusinessTwitterActivity.this);
//        	this.editCiudad.setText(ciudad);
//        	this.editCiudad.setEnabled(false);
//    	}else{
//    		this.editCiudad.setText("");
//    		this.editCiudad.setEnabled(true);
//    	}
//    }

	private void buscarArticulos(){
    	BusinessTwitterApplication app = (BusinessTwitterApplication)getApplication();
    	app.setCiudad(this.editCiudad.getText().toString());    	
    	app.setTipo(this.cmbTipo.getSelectedItem().toString());
    	app.setTitulo(this.editTitulo.getText().toString());
    	app.setNumPagina(1);
    	app.setConLinks(this.chkConLink.isChecked());
    	
//    	if(this.chkLocalizacion.isChecked()){
//    		Location location = GeoLocationHelper.getLatLonByLocation(BusinessTwitterActivity.this);
//    		app.setLongitude(location.getLongitude());
//    		app.setLatitude(location.getLatitude());
//    	}else{
    		if(editCiudad.getText().toString().equals("") == false){
    			double lon = GeoLocationHelper.getLonByLocation(BusinessTwitterActivity.this, editCiudad.getText().toString());
    			double lat = GeoLocationHelper.getLatByLocation(BusinessTwitterActivity.this, editCiudad.getText().toString());
    			if(lon != 0D && lat != 0D){
		    		app.setLongitude(lon);
		    		app.setLatitude(lat);		    		
    			}
    			else
    			{
    				app.setCiudad("");
    			}
    		}
//    	}
    	
    	Intent intent = new Intent(ConstantesNavegacion.INTENT_ACTION_VIEW_LIST);
    	startActivity(intent);
    }
    
    private void cargarEditCiudad(){
    	this.editCiudad = (EditText)findViewById(R.id.editCiudad);
    	String ciudad = GeoLocationHelper.getLocality(BusinessTwitterActivity.this);
    	this.editCiudad.setText(ciudad);
    }
    
    private void cargarEditTitulo(){
    	this.editTitulo = (EditText)findViewById(R.id.editTitulo);
    }
    
    private void cargarBtnBuscar(){
    	this.btnBuscar = (Button) findViewById(R.id.btnBuscar);
    	this.btnBuscar.setOnClickListener(new OnClickListener() {
    		
			public void onClick(View v) {
				buscarArticulos();				
			}
		});
    }
    
    private void cargarCmbTipo(){
    	this.cmbTipo = (Spinner) findViewById(R.id.cmbTipoBusqueda);
        this.cmbTipo.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v,
					int pos, long id) {
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
    }
}