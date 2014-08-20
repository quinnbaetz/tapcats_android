package com.mi.blockslide.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;

import com.mi.blockslide.R;

public class LargerClickAreaButton extends Button {
	private int offsetLeft;
	private int offsetRight;
	private int offsetTop;
	private int offsetBottom;
	private boolean listenerSet = false;
	public LargerClickAreaButton(Context context) {
		super(context);
	}

	public LargerClickAreaButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LargerClickAreaButton(Context context, AttributeSet attrs,
                                 int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.LargerClickAreaButton, 0, 0);
		
		try {
			int offset = a.getDimensionPixelSize(
					R.styleable.LargerClickAreaButton_clickPadding, 0);
			offsetLeft = a.getDimensionPixelSize(
					R.styleable.LargerClickAreaButton_clickPadding, offset);
			offsetRight = a.getDimensionPixelSize(
					R.styleable.LargerClickAreaButton_clickPadding, offset);
			offsetTop = a.getDimensionPixelSize(
					R.styleable.LargerClickAreaButton_clickPadding, offset);
			offsetBottom = a.getDimensionPixelSize(
					R.styleable.LargerClickAreaButton_clickPadding, offset);

			String font = a.getString(R.styleable.LargerClickAreaButton_font);

			if (font != null) {
				Typeface type = Typeface.createFromAsset(context.getAssets(),
                        "fonts/" + font);
				this.setTypeface(type);
			}


		} finally {
			a.recycle();
		}


	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//TODO: this shoudln't be done in offset
		if (!listenerSet && (offsetLeft > 0 || offsetRight > 0 || offsetTop > 0 || offsetBottom > 0)) {
			listenerSet = true;
			final Button mButton = this;
			View parent = (View) this.getParent();
			parent.post(new Runnable() {
				public void run() {
					Rect delegateArea = new Rect();
					Button delegate = mButton;
					delegate.getHitRect(delegateArea);
					delegateArea.top -= offsetTop;
					delegateArea.bottom += offsetBottom;
					delegateArea.left -= offsetLeft;
					delegateArea.right += offsetRight;
					TouchDelegate expandedArea = new TouchDelegate(
							delegateArea, delegate);
					if (View.class.isInstance(delegate.getParent())) {
						((View) delegate.getParent())
								.setTouchDelegate(expandedArea);
					}
				};
			});
		}
		
	}
	
}
