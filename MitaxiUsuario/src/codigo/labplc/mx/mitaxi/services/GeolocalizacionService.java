package codigo.labplc.mx.mitaxi.services;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import codigo.labplc.mx.mitaxi.R;

public class GeolocalizacionService extends Service {
	private LocationManager locationManager;
	private LocationListener locationListener;
	private ArrayList<Location> listLocationDrive = new ArrayList<Location>();
	
	private int minUpdateTimeLocation = 1000 * 30; //30 segundos
	private int minUpdateDistanceLocation = 0; //0 metros
	
	public static Activity mainActivity;
	
	private String TAG = "GeolocalizacionService";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.i(TAG, "onCreate");
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Log.i(TAG, "onDestroy");
		
		if(locationManager != null) {
			if(locationListener != null) {
				locationManager.removeUpdates(locationListener);
			}
		}
	}
	
	@Override
	public int onStartCommand(Intent intenc, int flags, int idArranque) {
		
		Log.i(TAG, "onStartCommand");
		
		startRequestLocationUpdate();
		
		return START_STICKY;
	}

	public void startRequestLocationUpdate() {
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minUpdateTimeLocation, minUpdateDistanceLocation, locationListener);
			
			Log.i(TAG, "GPS On");
			
		} else {
			mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showDialogTurnOnGPS(getString(R.string.gps_off_aviso), getString(R.string.gps_off_message));
				}
			});
			
			Log.i(TAG, "GPS Off");
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
	 * Muestra diálogo en dado caso que el GPS está apagado
	 * 
	 * @param titulo Título del diálogo
	 * @param message Mensaje del diálogo
	 */
	public void showDialogTurnOnGPS(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle(title);
        builder.setMessage(message);
		builder.setPositiveButton(getString(R.string.dialog_confirm_message), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				startActivity(settingsIntent);
			}
		});
		builder.setNegativeButton(getString(R.string.dialog_decline_message), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
	}
	
	public void updateBestLocation(Location location) {
		Message message = new Message();
		message.obj = location;
		handlerUpdateLocation.sendMessage(message);
	}
	
	public Handler handlerUpdateLocation = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Location location = (Location) msg.obj;
			
			if (msg.obj != null) {
				addLocationToList(location);
				
				sendBroadcast(location);
			}
		}
	};
	
	public void sendBroadcast(Location location) {
		getApplicationContext().sendBroadcast(new Intent("key").putExtra("location", location));
	}
	
	public void addLocationToList(Location location) {
		listLocationDrive.add(location);
	}
	
	public class MyLocationListener implements LocationListener {
		private static final int TIME_UPDATE = 1000 * 30; //30 SEGUNDOS
		private Location curentBestLocation = null;
		
		@Override
		public void onLocationChanged(Location location) {
			
			Log.i(TAG, "onLocationChanged");
			
			if(location != null) {
				if (curentBestLocation != null) {
					if (isBetterLocation(location, curentBestLocation)) {
						curentBestLocation = location;
						
						updateBestLocation(location);
					}
				} else {
					curentBestLocation = location;
					
					updateBestLocation(location);
				}
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		
		/**
		 * Determines whether one Location reading is better than the current
		 * Location fix
		 * 
		 * @param location
		 *            The new Location that you want to evaluate
		 * @param currentBestLocation
		 *            The current Location fix, to which you want to compare the new
		 *            one
		 */
		protected boolean isBetterLocation(Location location, Location currentBestLocation) {
			if (currentBestLocation == null) {
				// A new location is always better than no location
				return true;
			}

			// Check whether the new location fix is newer or older
			long timeDelta = location.getTime() - currentBestLocation.getTime();
			boolean isSignificantlyNewer = timeDelta > TIME_UPDATE;
			boolean isSignificantlyOlder = timeDelta < -TIME_UPDATE;
			boolean isNewer = timeDelta > 0;

			// If it's been more than two minutes since the current location, use
			// the new location
			// because the user has likely moved
			if (isSignificantlyNewer) {
				return true;
				// If the new location is more than two minutes older, it must be
				// worse
			} else if (isSignificantlyOlder) {
				return false;
			}

			// Check whether the new location fix is more or less accurate
			int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
					.getAccuracy());
			boolean isLessAccurate = accuracyDelta > 0;
			boolean isMoreAccurate = accuracyDelta < 0;
			boolean isSignificantlyLessAccurate = accuracyDelta > 200;

			// Check if the old and new location are from the same provider
			boolean isFromSameProvider = isSameProvider(location.getProvider(),
					currentBestLocation.getProvider());

			// Determine location quality using a combination of timeliness and
			// accuracy
			if (isMoreAccurate) {
				return true;
			} else if (isNewer && !isLessAccurate) {
				return true;
			} else if (isNewer && !isSignificantlyLessAccurate
					&& isFromSameProvider) {
				return true;
			}
			return false;
		}

		/** Checks whether two providers are the same */
		private boolean isSameProvider(String provider1, String provider2) {
			if (provider1 == null) {
				return provider2 == null;
			}
			return provider1.equals(provider2);
		}
	}
}