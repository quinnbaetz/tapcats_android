package com.mi.blockslide;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.applovin.adview.AppLovinAdView;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdk;
import com.mi.blockslide.model.Arrow;
import com.mi.blockslide.model.Block;
import com.mi.blockslide.model.Goal;
import com.mi.blockslide.model.Level;
import com.mi.blockslide.model.Levels;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.mi.blockslide.model.Space;
import com.mi.blockslide.views.ArrowView;
import com.mi.blockslide.views.BlockView;
import com.mi.blockslide.views.GoalView;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.UpdateManager;

public class BlockActivity extends Activity {

    int currentLevel = -1;
    private FrameLayout boardView;
    private FrameLayout overlay;
    private boolean adsShown = false;
    private Button hint;
    private GameController gc;
    private Button restart;
    private Tracker tracker;
    private Button menu;
    private Button levelText;
    private ImageView image;

    public void nextLevel() {

        switch(currentLevel){
            case 0:
                image.setImageDrawable(this.getResources().getDrawable(R.drawable.to_mice));

            break;
            case 2:
                image.setImageDrawable(this.getResources().getDrawable(R.drawable.cats_push));

            break;
            case 5:
                image.setImageDrawable(this.getResources().getDrawable(R.drawable.slip_on_fish));
            break;
            default:
                image.setImageDrawable(this.getResources().getDrawable(R.drawable.win));
            break;
        }

        image.setVisibility(View.VISIBLE);
        tracker.track(gc.level.name, "completed", "");


        overlay.setBackgroundColor(Color.GREEN);
        ObjectAnimator.ofFloat(overlay, "alpha", 1, 0).start();
        boardView.removeAllViews();


        if(currentLevel < 12) {
            currentLevel++;
        }
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        int highLevel = sp.getInt("level", 1);
        edit.putInt("level", Math.max(currentLevel, highLevel));
        edit.commit();

    }

    public void showAds(){
        adsShown = true;

        tracker.track("Ads", "show", "");

        AppLovinSdk sdk = AppLovinSdk.getInstance(this);

        AppLovinAdView adView = new AppLovinAdView(sdk, AppLovinAdSize.BANNER, this);
        adView.loadNextAd();

        // Add the view into the layout
        ViewGroup adHome = (ViewGroup) findViewById(R.id.ad_home);
        adHome.addView(adView);
    }


    public void lose() {


        new Handler().post(new Runnable() {
            public void run() {
                reset(false);

                Logger.Log("Made it to lose");


                image.setImageDrawable(BlockActivity.this.getResources().getDrawable(R.drawable.lose_up));
                Logger.Log("set drawable");

                image.setVisibility(View.VISIBLE);
                Logger.Log("set visibility");

                if (currentLevel > 6) {
                    if (Math.random() < .3) {
                        AppLovinInterstitialAd.show(BlockActivity.this);
                    }
                }
            }
        });


    }

    public void reset(){
        reset(true);
    }
    public void reset(boolean forceNew){
        tracker.track(gc.level.name, "failed", "");
        overlay.setBackgroundColor(Color.RED);
        ObjectAnimator.ofFloat(overlay, "alpha", 1, 0).start();
        boardView.removeAllViews();

        if(forceNew) {
            setupGame();
        }

        if(currentLevel > 2 && !adsShown) {
            showAds();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);

        boardView = (FrameLayout) findViewById(R.id.gameboard);
        overlay = (FrameLayout) findViewById(R.id.overlay);
        image = (ImageView) findViewById(R.id.image);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setVisibility(View.GONE);
                setupGame();
            }
        });


        hint = (Button) findViewById(R.id.hint);
        restart = (Button) findViewById(R.id.restart);
        levelText = (Button) findViewById(R.id.level_name);

        menu = (Button) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(BlockActivity.this, LevelActivity.class);
                BlockActivity.this.startActivity(myIntent);
                BlockActivity.this.finish();
            }
        });
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gc != null){
                    tracker.track("clicked", "hint", "");
                    tracker.track(gc.level.name, "hint", "");
                    gc.showHint();
                }
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tracker.track("clicked", "restart", "");
                reset();
            }
        });
        if(getIntent().getExtras() != null) {
            currentLevel = getIntent().getExtras().getInt("level", -1);
        }
        if(currentLevel == -1){
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(this);
            currentLevel = sp.getInt("level", 0);
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) boardView.getLayoutParams();
        layoutParams.height = size.x;
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        boardView.setLayoutParams(layoutParams);


        adsShown = false;

        if(currentLevel == 0){
            image.setImageDrawable(this.getResources().getDrawable(R.drawable.tap_cat));
            image.setVisibility(View.VISIBLE);
        }else {
            setupGame();
        }

    }

    public void setupGame(){


        Gson gson = new Gson();
        InputStream inputStream = getResources().openRawResource(R.raw.levels);
        Reader reader = new InputStreamReader(inputStream);
        Levels levels = gson.fromJson(reader, Levels.class);
        Logger.Log(gson.toJson(levels).toString());


        Level level = levels.levels.get(currentLevel);

        if(levelText != null) {
            levelText.setText("Level " + (currentLevel + 1));
        }

        Logger.Log(gson.toJson(level).toString());
        gc = new GameController(level, this);

        for(Goal goal : level.goals){
            GoalView goalView = new GoalView(this);
            goalView.setGamePieces(goal, gc);
            boardView.addView(goalView);
            gc.addGoal(goalView);
        }


        for(Block block : level.blocks){
            BlockView blockView = new BlockView(this);
            blockView.setGamePieces(block, gc);
            blockView.setActivity(this);
            boardView.addView(blockView);
            gc.addBlock(blockView);
        }

        if(level.arrows != null) {
            for (Arrow arrow : level.arrows) {
                ArrowView arrowView = new ArrowView(this);
                arrowView.setGamePieces(arrow, gc);
                boardView.addView(arrowView);
                gc.addArrow(arrowView);
            }
        }
    }

    @Override
    public void onStop(){
        tracker.stop();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        tracker.destroy();
        super.onDestroy();
    }


    private void checkForCrashes() {
        CrashManager.register(this, "78cba21ae8fde14e080581221ee8eee7",
                new CrashManagerListener() {
                    public boolean shouldAutoUploadCrashes() {
                        return true;
                    }
                }
        );
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        if(!Consts.ENV.equals("prod")) {
            UpdateManager.register(this, "78cba21ae8fde14e080581221ee8eee7");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        checkForCrashes();
        checkForUpdates();
        tracker = Tracker.getInstance(this);
        if(gc != null && gc.level != null) {
            tracker.track("View", "Level", ""+(currentLevel+1));
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

}
