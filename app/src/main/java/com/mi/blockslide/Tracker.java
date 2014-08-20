package com.mi.blockslide;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class Tracker {
	String TAG = "TRACKER";

	private static Tracker instance;

	private MixpanelAPI mp;

    public static synchronized Tracker getInstance(){
        return instance;
    }
	public static synchronized Tracker getInstance(Context context) {
		if (instance == null) {

			MixpanelAPI mp = MixpanelAPI.getInstance(
					context, Consts.MIXPANEL_TOKEN);

			instance = new Tracker(mp);

		}
		return instance;
	}

	private Tracker(MixpanelAPI mp) {
	    this.mp = mp;
	}

	
	/*public void signup(User user){
		if(mp != null){
			mp.alias(user.getId(), null);
            Tracker.getInstance().trackOnce("Mixpanel", "create alias","succeeded");
			login(user);
		}
	}
	public void login(User user){
		if(mp != null){
			//mp.identify(user.getId());
			//mp.getPeople().set("Plan", "Premium");
			mp.getPeople().identify(user.getId());
			JSONObject props = new JSONObject();
			try {
				props.put("$name", user.getUser_name());
				props.put("$phone", user.getPhone());
				props.put("$created", user.getCreated_dttm());

				mp.getPeople().setOnce(props);
                mp.getPeople().set("version", Consts.short_app_name);
				
				//mp.registerSuperPropertiesOnce(props);
				BonfireLogger.Log("mixpanel" + user.getUser_name()+" - "+ user.getPhone() + " "+user.getCreated_dttm());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mp.getPeople().initPushHandling(Consts.SENDER_ID);
			
		}
	}*/



    public void track(String category, String action, String label, long value){
        track(category, action, label, value, null);
    }

    public void track(String category, String action, String label, JSONObject props) {
        track(category, action, label, 0, props);
    }

    public void track(String category, String action, String label, String uid) {
        JSONObject props = new JSONObject();
        try {
            props.put("uid", uid);
            track(category, action, label, 0, props);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        track(category, action, label, 0, null);
    }
	public void track(String category, String action, String label, long value, JSONObject props) {
		try {

            if (mp != null) {
                if(props == null){
                    props = new JSONObject();
                }
                props.put("value", value);
                mp.track(category+"_"+action+"_"+label, props);
            }

		} catch (Exception e) {
			Logger.Log(e);
		}
	}

	public void track(String category, String action, String label) {
		track(category, action, label, 0l);
	}
	public void track(String key) {
		track(key, (long) 0);
	}

	public void track(String key, Long value) {
		try {
			String category = key;
			String action = null;
			String label = null;

			String[] split_key = key.split("_");
			if (split_key.length > 0)
				category = split_key[0];
			if (split_key.length > 1)
				action = split_key[1];
			if (split_key.length > 2)
				label = split_key[2];

			track(category, action, label, value);
			
		} catch (Exception e) {
			Logger.Log(e);
		}
	}

	
	public void destroy() {
		instance = null;
	}
	
	public void flush() {
		if (mp != null) {
			mp.flush();
		}
	}

	public void stop() {
		if (mp != null) {
			mp.flush();
			mp = null;
		}

		instance = null;

	}


}
