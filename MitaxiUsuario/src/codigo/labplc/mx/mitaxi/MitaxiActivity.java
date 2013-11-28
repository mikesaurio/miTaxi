package codigo.labplc.mx.mitaxi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * clase principal :D
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
		
		//solicitamos las preferencias del usuario
		SharedPreferences prefs = getSharedPreferences("MisPreferenciasPasajero",Context.MODE_PRIVATE);
		String nombre = prefs.getString("nombre", null);
		//si aun no tiene datos guardados en preferencias
		if(nombre == null){
			// se llama a la actividad de registro del taximetro
			Intent intent = new Intent(MitaxiActivity.this,MitaxiRegisterActivity.class);
			startActivity(intent);
			this.finish();
		}else{
			//se llama directo a la busqueda de taxista
			Intent intent = new Intent(MitaxiActivity.this,MitaxiGoogleMapsActivity.class);
			startActivity(intent);
			this.finish();
		}
		
	}

}