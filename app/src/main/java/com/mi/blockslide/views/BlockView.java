package com.mi.blockslide.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mi.blockslide.AppConsts;
import com.mi.blockslide.GameController;
import com.mi.blockslide.Logger;
import com.mi.blockslide.R;
import com.mi.blockslide.model.Block;

/**
 * Created by quinn on 7/28/14.
 */
public class BlockView extends GamePieceView implements View.OnTouchListener {

    private final Paint paint;
    private final Paint whitePaint;
    private final Context mContext;
    int color = 0;
    public Block block;
    private Activity activity;
    private GameController gc;
    public int position = 0;
    private ValueAnimator anim;

    public BlockView(Context context) {
        this(context, null, 0);
        // TODO Auto-generated constructor stub
    }

    public BlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public BlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        setOnTouchListener(this);
        mContext = context;
        // load_paint = new Paint();
    }

    public void setBackground(boolean moving, boolean mouse){
        if(moving){
            switch(this.block.color){
                case 0:
                    this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bluecat_moving));
                    break;
                case 1:
                    this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.redcat_moving));
                    break;
                case 2:
                    this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.greencat_moving));
                    break;

            }
            this.invalidate();
            return;
        }
        if(mouse){
            switch(this.block.color){
                case 0:
                    this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bluecat_caughtmouse));
                    break;
                case 1:
                    this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.redcat_caughtmouse));
                    break;
                case 2:
                    this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.greencat_caughtmouse));
                    break;

            }
            this.invalidate();
            return;
        }
        switch(this.block.color){
            case 0:
                this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bluecat_notmoving));
                break;
            case 1:
                this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.redcat_notmoving));
                break;
            case 2:
                this.setImageDrawable(mContext.getResources().getDrawable(R.drawable.greencat_notmoving));
                break;

        }
        this.invalidate();
    }

    public void setGamePieces(Block block, GameController gc){
        this.block = block;
        this.gc = gc;
        setBackground(false, false);
        this.invalidate();
    }

    public void animateToPosition(final boolean goToNextLevel, final boolean didLose){
        Animator anim = null;
        if(getX() != getWidth()*block.location[0]) {
            anim = ObjectAnimator.ofFloat(this, "x", getWidth() * block.location[0]);

        }
        if(getY() != getHeight()*block.location[1]) {
            anim = ObjectAnimator.ofFloat(this, "y", getHeight() * block.location[1]);

        }
        if(anim!=null) {
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    new Handler().post(new Runnable() {
                        public void run() {
                        setBackground(true, false);
                        }
                    });

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    new Handler().post(new Runnable() {
                        public void run() {
                            if (goToNextLevel) {
                                gc.nextLevel();
                            }
                            if (didLose){
                                gc.lose();
                            }
                            setBackground(false, false);
                        }
                    });
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            anim.start();
        }

    }

    public void flash(){
        anim = ObjectAnimator.ofFloat(this, "alpha", 0, 1);
        anim.setRepeatCount(5);
        anim.start();

    }

    public void stopAnimation(){
        if(anim != null && anim.isRunning()){
            anim.end();
        }
        anim = null;
    }




    public void setActivity(Activity a){
        this.activity = a;
    }
    @Override
    public void onMeasure(int widthSpec, int heightSpec){
        int parentWidth = MeasureSpec.getSize(widthSpec);
        int parentHeight = MeasureSpec.getSize(heightSpec);
        int width = parentWidth/gc.level.cols;
        int height = parentHeight/gc.level.rows;
        this.setMeasuredDimension(width, height);
        this.setX(block.location[0]*width);
        this.setY(block.location[1]*height);
    }

    @Override
    public void onAttachedToWindow(){

    }


    @Override
    protected void onDraw(Canvas canvas) {
        switch (block.direction){
            case AppConsts.UP:
                canvas.rotate(180, getWidth()/2, getHeight()/2);
                break;
            case AppConsts.RIGHT:
                canvas.rotate(270, getWidth()/2, getHeight()/2);
                break;
            case AppConsts.DOWN:

                break;
            case AppConsts.LEFT:
                canvas.rotate(90, getWidth()/2, getHeight()/2);
                break;

        }

        super.onDraw(canvas);
        //canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        //drawArrow(canvas, block.direction, whitePaint);


        // canvas.drawRect(play_end, 0, load_end, height, load_paint);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Logger.Log("on Click view");
        gc.move(this, block.direction);
        return false;
    }
}
