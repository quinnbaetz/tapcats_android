package com.mi.blockslide;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.applovin.sdk.AppLovinSdk;
import com.mi.blockslide.adapters.LevelAdapter;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.UpdateManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class LevelActivity extends Activity {

    private ListView levels;
    private LevelAdapter levelAdapter;
    private Tracker tracker;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        levels = (ListView) findViewById(R.id.levels);
        levelAdapter = new LevelAdapter(this);
        levels.setAdapter(levelAdapter);

        webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("http://upload.wikimedia.org/wikipedia/commons/f/fd/Ghostscript_Tiger.svg");


        levels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //start level
                SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(LevelActivity.this);
                if(sp.getInt("level", 1) >= i) {
                    Intent myIntent = new Intent(LevelActivity.this, BlockActivity.class);
                    myIntent.putExtra("level", i);
                    LevelActivity.this.startActivity(myIntent);

                }
            }
        });
        AppLovinSdk.initializeSdk(this);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TakeScreenshot();


            }
        }, 1000);
    }
    public void savePhoto(Bitmap bmp)
    {
        File imageFileFolder = new File(Environment.getExternalStorageDirectory(), "Rotate");
        imageFileFolder.mkdir();
        FileOutputStream out = null;
        Calendar c = Calendar.getInstance();
        String date = fromInt(c.get(Calendar.MONTH))
                + fromInt(c.get(Calendar.DAY_OF_MONTH))
                + fromInt(c.get(Calendar.YEAR))
                + fromInt(c.get(Calendar.HOUR_OF_DAY))
                + fromInt(c.get(Calendar.MINUTE))
                + fromInt(c.get(Calendar.SECOND));
        File imageFileName = new File(imageFileFolder, date.toString() + ".jpg");
        try
        {
            out = new FileOutputStream(imageFileName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            scanPhoto(imageFileName.toString());
            out = null;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public String fromInt(int val)
    {
        return String.valueOf(val);
    }

    MediaScannerConnection msConn;
    public void scanPhoto(final String imageFileName)
    {
        msConn = new MediaScannerConnection(LevelActivity.this, new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
                msConn.scanFile(imageFileName, null);

            }

            public void onScanCompleted(String path, Uri uri) {
                if(msConn != null) {
                    msConn.disconnect();
                }
            }
        });
        msConn.connect();
    }
    

    private void TakeScreenshot()
    {
        Picture picture = webview.capturePicture();
        Bitmap  b = Bitmap.createBitmap( picture.getWidth(),
                picture.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas( b );

        picture.draw( c );
        FileOutputStream fos = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
            fos = new FileOutputStream( file);
            if ( fos != null )
            {
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                fos.close();
            }
            scanPhoto(file.toString());
        }
        catch( Exception e )
        {

        }
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.level, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();
        levelAdapter.notifyDataSetInvalidated();


        tracker = Tracker.getInstance(this);
        tracker.track("View", "Levels", "");

    }


}
