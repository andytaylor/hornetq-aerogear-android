package org.jboss.hornetq;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.PushRegistrar;
import org.jboss.aerogear.android.unifiedpush.Registrations;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements MessageHandler {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    registerWithGCM();
    }
	
    private void registerWithGCM()
    {
	    // access the registration object
	    PushRegistrar push = ((HornetQAeroGearApplication) getApplication())
	            .getRegistration();  // 1

	    // fire up registration..

	    // The method will attempt to register the device with GCM and the UnifiedPush server
	    push.register(getApplicationContext(), new Callback<Void>() {   // 2
	        private static final long serialVersionUID = 1L;

	        @Override
	        public void onSuccess(Void ignore) {
	            Toast.makeText(MainActivity.this, "Registration for GCM Succeeded!", // 3
	                    Toast.LENGTH_LONG).show();
	        }

	        @Override
	        public void onFailure(Exception exception) 
	        {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		    	builder.setTitle("Error during registration")
		    		   .setMessage("Please check your AeroGear Settings");
		    	AlertDialog dialog = builder.create();
		    	dialog.show();
		    	Log.e("GCMRegistration", exception.getStackTrace().toString());
	        }
	    });
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	        	Intent intent = new Intent(this, SettingsActivity.class);
	        	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	protected void onResume() {
	    super.onResume();
	    registerWithGCM();
	    Registrations.registerMainThreadHandler(this);  // 1
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    Registrations.unregisterMainThreadHandler(this); // 2
	}

	@Override
	public void onMessage(Context context, Bundle message) {   // 3
	    TextView text =  new TextView(MainActivity.this);
	    
	    text.setText(message.getString("alert"));
	    text.setTextSize(20f);
	    text.setBackgroundColor(Color.DKGRAY);
	    text.setTextColor(Color.WHITE);
	
	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    params.setMargins(0, 30, 0, 30);
	    
	    LinearLayout messages = (LinearLayout) findViewById(R.id.messages);
	    text.setPadding(20, 20, 20, 20);
	    text.setLayoutParams(params);
	    messages.addView(text);
	}

	@Override
	public void onDeleteMessage(Context context, Bundle message) {
	    // handle GoogleCloudMessaging.MESSAGE_TYPE_DELETED
	}

	@Override
	public void onError() {
	    // handle GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
	}

}
