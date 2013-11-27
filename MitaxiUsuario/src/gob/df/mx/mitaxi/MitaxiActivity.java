package gob.df.mx.mitaxi;

import gob.df.mx.mitaxi.ed.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * clase principal
 * 
 * @version 0.1
 * @author mikesaurio
 * 
 */
public class MitaxiActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi);
		
		SharedPreferences prefs = getSharedPreferences("MisPreferenciasPasajero",Context.MODE_PRIVATE);
		String nombre = prefs.getString("nombre", null);
	//	Log.d("SharedPreferences", nombre+"");
		if(nombre == null){
			// se llama a la actividad de registro del taximetro
			Intent intent = new Intent(MitaxiActivity.this,MitaxiRegisterActivity.class);
			startActivity(intent);
		}else{
			//se llama directo a la busqueda de taxista
			Intent intent = new Intent(MitaxiActivity.this,MitaxiGoogleMapsActivity.class);
			startActivity(intent);
		}
		
	}

}