package gob.df.mx.mitaxi;

import gob.df.mx.mitaxi.beans.User;
import gob.df.mx.mitaxi.ed.R;
import gob.df.mx.mitaxi.utils.Dialogues;
import gob.df.mx.mitaxi.utils.RegularExpressions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * Actividad que registra a los pasajeros
 * 
 * @version 0.1
 * @author mikesaurio
 * 
 */
public class MitaxiRegisterActivity extends Activity {

	// declaracion de variables
	private int COUNT_CURRENT_PAGE = 1;// posicion en la que estas
	private final int COUNT_TOTAL_PAGES = 2;// total de oaginas

	private Button btn_next;
	private Button btn_previous;
	private EditText et_infousername;
	private EditText et_infouserlastname;
	private EditText et_infouseremail;
	private EditText et_infouserteluser;
	private EditText et_infousertelemergency;

	private EditText et_backgroundshadow;
	private EditText edittext_aux;

	private int IS_STRING = 1;
	private int IS_NUMBER = 2;
	private int IS_EMAIL = 3;

	private boolean isEtSelected = false;

	private ViewFlipper vf_container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_register);
	

		// cargamos la UI
		initUI();
	}

	/**
	 * metodo que crea la vista del regirstro
	 * 
	 * @return void
	 */
	public void initUI() {
		/** creamos las instancias */
		vf_container = (ViewFlipper) findViewById(R.id.mitaxiregister_vf_container);

		et_infousername = (EditText) findViewById(R.id.mitaxiregister_et_infousername);
		et_infouserlastname = (EditText) findViewById(R.id.mitaxiregister_et_infouserlastname);
		et_infouseremail = (EditText) findViewById(R.id.mitaxiregister_et_infouseremail);
		et_infouserteluser = (EditText) findViewById(R.id.mitaxiregister_et_infouserteluser);
		et_infousertelemergency = (EditText) findViewById(R.id.mitaxiregister_et_infousertelemergency);

		et_infousername.setTag(IS_STRING);
		et_infouserlastname.setTag(IS_STRING);
		et_infouseremail.setTag(IS_EMAIL);
		et_infouserteluser.setTag(IS_NUMBER);
		et_infousertelemergency.setTag(IS_NUMBER);
		
		
		et_infousername.setText("mike");
		et_infouserlastname.setText("moran");
		et_infouseremail.setText("mikesaurio@gmail.com");
		et_infouserteluser.setText("123432");
		et_infousertelemergency.setText("1234543");

		/** Creamos los escuchas */
		et_infousername.setOnClickListener(EditTextSelectedOnClickListener);
		et_infouserlastname.setOnClickListener(EditTextSelectedOnClickListener);
		et_infouseremail.setOnClickListener(EditTextSelectedOnClickListener);
		et_infouserteluser.setOnClickListener(EditTextSelectedOnClickListener);
		et_infousertelemergency
				.setOnClickListener(EditTextSelectedOnClickListener);

		et_infousername.addTextChangedListener(new CurrencyTextWatcher(
				et_infousername));
		et_infouserlastname.addTextChangedListener(new CurrencyTextWatcher(
				et_infouserlastname));
		et_infouseremail.addTextChangedListener(new CurrencyTextWatcher(
				et_infouseremail));
		et_infouserteluser.addTextChangedListener(new CurrencyTextWatcher(
				et_infouserteluser));
		et_infousertelemergency.addTextChangedListener(new CurrencyTextWatcher(
				et_infousertelemergency));

		btn_next = (Button) findViewById(R.id.mitaxiregister_btn_next);
		btn_previous = (Button) findViewById(R.id.mitaxiregister_btn_previous);

		btn_next.setOnClickListener(btnNextOnClickListener);
		btn_previous.setOnClickListener(btnPreviousOnClickListener);

		// si estamos en la primera pagina el bot—n back es invisible
		if (COUNT_CURRENT_PAGE == 1) {
			btn_previous.setVisibility(View.INVISIBLE);
		}
	}

	OnClickListener EditTextSelectedOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			showSoftKeyboard();
			onClickEditTextSelected(v);
		}
	};

	/**
	 * Seleccionamos el textEdit a llenar
	 * 
	 * @param v
	 */
	public void onClickEditTextSelected(View v) {
		isEtSelected = true;
		if (isEtSelected) {
			((RelativeLayout) findViewById(R.id.mitaxiregister_rl_container_backgroundshadow))
					.setVisibility(View.VISIBLE);

			editTextEnable(false);// bloqueamos los editText

			edittext_aux = ((EditText) v);
			edittext_aux.setText(((EditText) v).getText().toString());
			int[] location = new int[2];
			edittext_aux.getLocationInWindow(location);
			edittext_aux.setVisibility(View.INVISIBLE);

			et_backgroundshadow = (EditText) findViewById(R.id.mitaxiregister_et_backgroundshadow);
			et_backgroundshadow.setVisibility(View.VISIBLE);
			et_backgroundshadow.setHint(edittext_aux.getHint().toString());
			et_backgroundshadow.setText(edittext_aux.getText().toString());
			et_backgroundshadow.requestFocus();

			ImageButton btn_backgroundshadow_ok = (ImageButton) findViewById(R.id.mitaxiregister_btn_backgroundshadow_ok);
			btn_backgroundshadow_ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					hideSoftKeyboard(v);
					hideEditTextSelected();
				}
			});

			RelativeLayout container_backgroundshadow = (RelativeLayout) findViewById(R.id.mitaxiregister_rl_container_backgroundshadow);
			container_backgroundshadow.setVisibility(ViewGroup.VISIBLE);
		}
	}

	public void showSoftKeyboard() {
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public void hideSoftKeyboard(View v) {
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
			Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
		} else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * al cerrar el texto de llenado
	 */
	public void hideEditTextSelected() {
		editTextEnable(true);
		isEtSelected = false;
		int[] location = new int[2];
		edittext_aux.getLocationOnScreen(location);
		edittext_aux.setText(et_backgroundshadow.getText().toString());
		edittext_aux.setVisibility(View.VISIBLE);
		edittext_aux.clearFocus();

		((RelativeLayout) findViewById(R.id.mitaxiregister_rl_container_backgroundshadow))
				.setVisibility(View.GONE);
	}

	/**
	 * bloqueamos los editText
	 * 
	 * @param b
	 *            (boolean) tipo de bloqueo
	 */
	public void editTextEnable(boolean b) {
		et_infousername.setEnabled(b);
		et_infouserlastname.setEnabled(b);
		et_infouseremail.setEnabled(b);
		et_infouserteluser.setEnabled(b);
		et_infousertelemergency.setEnabled(b);

	}

	@Override
	public void onBackPressed() {
		if (isEtSelected) {
			hideEditTextSelected();
		} else {
			super.onBackPressed();
		}
	}

	private class CurrencyTextWatcher implements TextWatcher {
		private boolean mEditing;
		private EditText et_aux;

		public CurrencyTextWatcher(EditText et_aux) {
			mEditing = false;
			this.et_aux = et_aux;
		}

		public synchronized void afterTextChanged(Editable s) {
			if (!mEditing) {
				mEditing = true;

				String expression = s.toString();

				if (!expression.equals("")) {
					int etType = Integer.parseInt(et_aux.getTag().toString());

					if (etType == IS_STRING) {
						if (!RegularExpressions.isString(expression))
							setErrorMessage(
									getString(R.string.mitaxiregister_error_string),
									et_aux);
						else
							et_aux.setError(null);
					} else if (etType == IS_EMAIL) {
						if (!RegularExpressions.isEmail(expression))
							setErrorMessage(
									getString(R.string.mitaxiregister_error_email),
									et_aux);
						else
							et_aux.setError(null);
					} else if (etType == IS_NUMBER) {
						if (!RegularExpressions.isNumber(expression))
							setErrorMessage(
									getString(R.string.mitaxiregister_error_number),
									et_aux);
						else
							et_aux.setError(null);
					}
				}

				mEditing = false;
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	OnClickListener btnNextOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			plusOneCurrentPage();

			if (COUNT_CURRENT_PAGE == COUNT_TOTAL_PAGES) {
				btn_next.setText(getString(R.string.mitaxiregister_btn_finish));

				btn_next.setOnClickListener(btnFinishOnClickListener);

				vf_container.showPrevious();
			}

			if (COUNT_CURRENT_PAGE > 1) {
				btn_previous.setVisibility(View.VISIBLE);
			}
		}
	};

	OnClickListener btnPreviousOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			minusOneCurrentPage();

			if (COUNT_CURRENT_PAGE == COUNT_TOTAL_PAGES - 1) {
				btn_next.setText(getString(R.string.mitaxiregister_btn_next));

				btn_next.setOnClickListener(btnNextOnClickListener);

				vf_container.showPrevious();
			}

			if (COUNT_CURRENT_PAGE == 1) {
				btn_previous.setVisibility(View.INVISIBLE);
			}
		}
	};

	OnClickListener btnFinishOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			saveUserInfo();
			Intent intent = new Intent(MitaxiRegisterActivity.this,
					MitaxiGoogleMapsActivity.class);
			startActivity(intent);
		}
	};

	public boolean isEmptyAnyEditText() {
		boolean empty = false;
		if (et_infousername.getText().length() == 0) {
			empty = true;
		}
		if (et_infouserlastname.getText().length() == 0) {
			empty = true;
		}
		if (et_infouseremail.getText().length() == 0) {
			empty = true;
		}
		if (et_infouserteluser.getText().length() == 0) {
			empty = true;
		}
		if (et_infousertelemergency.getText().length() == 0) {
			empty = true;
		}

		return empty;
	}

	public void saveUserInfo() {
		User user = new User();
		user.setName(et_infousername.getText().toString());
		user.setLastname(et_infouserlastname.getText().toString());
		user.setEmail(et_infouseremail.getText().toString());
		user.setTeluser(et_infouserteluser.getText().toString());
		user.setTelemergency(et_infousertelemergency.getText().toString());
		
		SharedPreferences prefs = getSharedPreferences("MisPreferenciasPasajero",Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("nombre", user.getName()+" "+ user.getLastname());
			editor.putString("correo", user.getName());
			editor.putString("num", user.getTeluser());
			editor.putString("num", user.getTelemergency());
			editor.commit();
	}

	/**
	 * aumenta una pagina la ubicacion
	 * 
	 * @return void
	 */
	public void plusOneCurrentPage() {
		COUNT_CURRENT_PAGE++;
	}

	/**
	 * disminuye una pagina la ubicacion
	 * 
	 * @return void
	 */
	public void minusOneCurrentPage() {
		COUNT_CURRENT_PAGE--;
	}

	/**
	 * envia un mensaje de error cuando se ingresa mal un tipo de dato
	 * 
	 * @param errorMessage
	 *            (String) mensaje de error
	 * @param viewId
	 *            (editText) llenado
	 */
	public void setErrorMessage(String errorMessage, EditText viewId) {
		viewId.clearFocus();
		viewId.setFocusableInTouchMode(true);
		Dialogues.Toast(getApplicationContext(), errorMessage,
				Toast.LENGTH_SHORT);

		int errorColor = Color.WHITE;
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(
				errorMessage);
		ssbuilder.setSpan(fgcspan, 0, errorMessage.length(), 0);
		viewId.setError(ssbuilder);
	}



}