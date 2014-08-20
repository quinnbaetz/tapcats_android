package com.mi.blockslide.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mi.blockslide.R;

/**
 * Created by quinn on 7/28/14.
 */
public class LevelAdapter extends BaseAdapter{

    private final Context mContext;

    public LevelAdapter(Context context){
        mContext = context;
    }
    @Override
    public int getCount() {
        return 13;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View v, ViewGroup parent) {
        TextView text;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_level, parent,
                    false);

            text = (TextView) v.findViewById(R.id.level);
            v.setTag(text);
        }
        text = (TextView) v.getTag();

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        if(sp.getInt("level", 1) >= i) {
            text.setTextColor(Color.BLACK);
        }else {
            text.setTextColor(Color.GRAY);
        }
        text.setText("level " + (i+1));
        return v;
    }
}
