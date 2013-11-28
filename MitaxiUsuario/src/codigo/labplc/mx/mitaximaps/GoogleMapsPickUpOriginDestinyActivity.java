package codigo.labplc.mx.mitaximaps;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import codigo.labplc.mx.mitaxi.R;
import codigo.labplc.mx.mitaxi.utils.Dialogues;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapsPickUpOriginDestinyActivity extends Activity implements
		OnMapClickListener, OnMapLongClickListener, OnMarkerDragListener {
	private GoogleMap map;
	
	private int countMarkers = 0;
	
	private TextView tv_information;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_pickuporigindestiny);
		
		initUIMap();
	}
	
	public void initUIMap() {
		tv_information = (TextView) findViewById(R.id.pickuporigindestiny_tv_information);
		tv_information.setOnClickListener(tvInformationOnClickListener);
		
		setUpMapIfNeeded();
		
		FragmentManager myFragmentManager = getFragmentManager();
		MapFragment myMapFragment = (MapFragment) myFragmentManager.findFragmentById(R.id.map);
		map = myMapFragment.getMap();
		
		map.setOnMapClickListener(this);
		map.setOnMapLongClickListener(this);
		map.setOnMarkerDragListener(this);
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
		if (!checkReady()) {
            return;
        }
	}
	
	private boolean checkReady() {
        if (map == null) {
            Dialogues.Log(getApplicationContext(), getString(R.string.map_not_ready), Log.INFO);
            return false;
        }
        return true;
    }
	
	OnClickListener tvInformationOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			tv_information.setVisibility(View.GONE);
		}
	};
	
	@Override
	public void onMapClick(LatLng point) {
		map.animateCamera(CameraUpdateFactory.newLatLng(point));
	}

	@Override
	public void onMapLongClick(LatLng point) {
		countMarkers++;
		
		if(countMarkers == 1) {
			tv_information.setVisibility(View.VISIBLE);
			tv_information.setText(getString(R.string.pickuporigindestiny_tv_infdestiny_text));
			addMarkerToMap(map, point, "Origen", true, "¡Aquí inicia el viaje!", BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		} else if(countMarkers == 2) {
			tv_information.setVisibility(View.VISIBLE);
			tv_information.setText(getString(R.string.pickuporigindestiny_tv_infdrag_text));
			addMarkerToMap(map, point, "Destino", true, "¡Aquí termina el viaje!", BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
		}
	}
	
	public void addMarkerToMap(GoogleMap map, LatLng point, String title, boolean dragable, String snippet, BitmapDescriptor icon) {
		Marker marker = map.addMarker(new MarkerOptions()
				.position(point)
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
	
	@Override
	public void onMarkerDrag(Marker marker) {}

	@Override
	public void onMarkerDragEnd(Marker marker) {}
	
	@Override
	public void onMarkerDragStart(Marker marker) {}
}