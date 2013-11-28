package codigo.labplc.mx.mitaxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import codigo.labplc.mx.mitaximaps.GoogleMapsDrawRouteUserActivity;
import codigo.labplc.mx.mitaximaps.GoogleMapsPickUpOriginDestinyActivity;

public class MitaxiGoogleMapsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_googlemaps);
		
		LinearLayout container_origindestination = (LinearLayout) findViewById(R.id.googlemaps_ll_container_origindestination);
		LinearLayout container_drawroute = (LinearLayout) findViewById(R.id.googlemaps_ll_container_drawroute);
		
		container_origindestination.setOnClickListener(OriginDestinationOnClickListener);
		container_drawroute.setOnClickListener(DrawRouteOnClickListener);
	}
	
	OnClickListener OriginDestinationOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MitaxiGoogleMapsActivity.this, GoogleMapsPickUpOriginDestinyActivity.class);
			startActivity(intent);
		}
	};
	
	OnClickListener DrawRouteOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MitaxiGoogleMapsActivity.this, GoogleMapsDrawRouteUserActivity.class);
			startActivity(intent);
		}
	};
}