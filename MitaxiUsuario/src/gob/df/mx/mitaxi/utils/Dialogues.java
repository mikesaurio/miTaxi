package gob.df.mx.mitaxi.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Dialogues {
	
	public static void Toast(Context context, String text, int duration) {
		Toast.makeText(context, text, duration).show();
	}
	
	public static void Log(Context context, String text, int type) {
		if(type == Log.DEBUG) {
			Log.d(context.getClass().getName().toString(), text);
		} else if(type == Log.ERROR) {
			Log.e(context.getClass().getName().toString(), text);
		} else if(type == Log.INFO) {
			Log.i(context.getClass().getName().toString(), text);
		} else if(type == Log.VERBOSE) {
			Log.v(context.getClass().getName().toString(), text);
		} else if(type == Log.WARN) {
			Log.w(context.getClass().getName().toString(), text);
		}
	}
	
	/*public static void DialogRegister(Activity activity, final EditText et_aux, String tv_message, String et_text, String et_hint, float x, float y) {
		Dialog dialog = new Dialog(activity, android.R.style.Theme_Holo_Light);
		dialog.setContentView(R.layout.dialog_register);
		
		TextView tv_descripction = (TextView) dialog.findViewById(R.id.dialog_register_tv_description);
		tv_descripction.setText(tv_message);
		
		final EditText et_selected = (EditText) dialog.findViewById(R.id.dialog_register_et_selected);
		et_selected.setText(et_text);
		et_selected.setHint(et_hint);
		
		startAnimationViewZoomIn(et_selected, x, y);
		
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				et_aux.setText(et_selected.getText().toString());
				et_aux.setHint(et_selected.getText().toString());
				
				int[] location = new int[2];
				et_selected.getLocationOnScreen(location);
				
				startAnimationViewZoomOut(et_selected, location[0], location[1]);
				
				dialog.dismiss();
			}
		});
		
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.show();
	}*/
}