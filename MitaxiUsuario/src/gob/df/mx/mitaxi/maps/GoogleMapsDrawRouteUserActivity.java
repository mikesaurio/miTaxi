package gob.df.mx.mitaxi.maps;

import gob.df.mx.mitaxi.ed.R;
import gob.df.mx.mitaxi.services.GeolocalizacionService;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class GoogleMapsDrawRouteUserActivity extends Activity {
	public GoogleMap map;
	private ArrayList<Location> listLocationTaxi = new ArrayList<Location>();
	private ArrayList<LatLng> listLatLngMarkersMap = new ArrayList<LatLng>();
	
	private String TAG = "GoogleMapsDrawRouteTaxiActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_drawrouteuser);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			listLocationTaxi = bundle.getParcelableArrayList("listLocationTaxi");
			
			Log.i(TAG, "Size list: " + listLocationTaxi.size());
		}
		
		setUpMapIfNeeded();
	}
	
	@Override
	protected void onPause() {
		unregisterReceiver(onBroadcast);
		
		Log.i(TAG, "onPause");
		
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		registerReceiver(onBroadcast, new IntentFilter("key"));
		
		Log.i(TAG, "onResume");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		Log.i(TAG, "onStart");
		
		startService();
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		Log.i(TAG, "onStop");
		
		stopService();
	}
	
	public void startService() {
		GeolocalizacionService.mainActivity = GoogleMapsDrawRouteUserActivity.this;
		Intent service = new Intent(GoogleMapsDrawRouteUserActivity.this, GeolocalizacionService.class);
		startService(service);
	}
	
	public void stopService() {
		GeolocalizacionService.mainActivity = null;
		Intent service = new Intent(GoogleMapsDrawRouteUserActivity.this, GeolocalizacionService.class);
		stopService(service);
	}
	
	private BroadcastReceiver onBroadcast = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Location location = intent.getParcelableExtra("location");
			listLocationTaxi.add(location);
			
			setUpMap();
		}
	};
	
	public void addMarkerToMap(GoogleMap map, LatLng latlng, String title, boolean dragable, String snippet, BitmapDescriptor icon) {
		Marker marker = map.addMarker(new MarkerOptions()
				.position(latlng)
				.title(title)
				.draggable(dragable)
				.icon(icon)
				.snippet(snippet)
				//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)).flat(true)
				.alpha(0.7f)
				.anchor(0.5F, 0.5F)
				.rotation(0.0F));

		marker.showInfoWindow();
	}
	
	public void addAllLocationToLatLng() {
		for(int i = 0; i < listLocationTaxi.size(); i++) {
			Location location = listLocationTaxi.get(i);
			LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
			listLatLngMarkersMap.add(latlng);
			
			addMarkerToMap(map, latlng, "Punto: " + (i + 1), false, getString(R.string.gps_location_found), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
			
			if(i == listLocationTaxi.size() - 1) {
				CameraUpdate center = CameraUpdateFactory.newLatLng(latlng);
				CameraUpdate zoom = CameraUpdateFactory.zoomTo(20);
			    map.moveCamera(center);
			    map.animateCamera(zoom, 2000, null);
			}
		}
	}
	
	private void drawPolyline() {
		PolylineOptions rectOptions = new PolylineOptions();
		rectOptions.addAll(listLatLngMarkersMap);
		rectOptions.width(15);
		rectOptions.color(Color.BLUE);
		rectOptions.geodesic(true);
		
		Polyline polyline = map.addPolyline(rectOptions);
		polyline.setPoints(listLatLngMarkersMap);
	}
	
	private void setUpMapIfNeeded() {
		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			if (map != null) {
				initMap();
				
				setUpMap();
			}
		}
	}
	
	public void initMap() {
		map.setMyLocationEnabled(true);
		map.setBuildingsEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.getUiSettings().setZoomControlsEnabled(true); //ZOOM
		map.getUiSettings().setCompassEnabled(true); //COMPASS
		map.getUiSettings().setZoomGesturesEnabled(true); //GESTURES ZOOM
		map.getUiSettings().setRotateGesturesEnabled(true); //ROTATE GESTURES
		map.getUiSettings().setScrollGesturesEnabled(true); //SCROLL GESTURES
		map.getUiSettings().setTiltGesturesEnabled(true); //TILT GESTURES
	}
	
	public void setUpMap() {
		addAllLocationToLatLng();
		
		drawPolyline();
	}
}