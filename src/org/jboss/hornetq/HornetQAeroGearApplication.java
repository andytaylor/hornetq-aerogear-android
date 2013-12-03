package org.jboss.hornetq;

import java.net.URI;
import java.net.URISyntaxException;

import org.jboss.aerogear.android.unifiedpush.PushConfig;
import org.jboss.aerogear.android.unifiedpush.PushRegistrar;
import org.jboss.aerogear.android.unifiedpush.Registrations;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class HornetQAeroGearApplication extends Application {

    private PushRegistrar registration;

    @Override
    public void onCreate() {
        super.onCreate();
        getRegistration();
    }

    // Accessor method for Activities to access the 'PushRegistrar' object
    public PushRegistrar getRegistration() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Registrations registrations = new Registrations();
		String unfiedPushUrl = prefs.getString(getString(R.string.pref_aerogear_unified_push_url), "");
		String varientId = prefs.getString(getString(R.string.pref_aerogear_varient_id), "");
		String varientSecret = prefs.getString(getString(R.string.pref_aerogear_varient_secret), "");
		String gcmSenderId = prefs.getString(getString(R.string.pref_gcm_sender_id), "");
		String alias = prefs.getString(getString(R.string.pref_alias), "");

        try {
            PushConfig config = new PushConfig(new URI(unfiedPushUrl), gcmSenderId);  // 2
            config.setVariantID(varientId);
            config.setSecret(varientSecret);
            config.setAlias(alias);

            registration = registrations.push("unifiedpush", config);  // 3

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return registration;
    }
}