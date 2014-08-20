package com.mi.blockslide.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mi.blockslide.R;


public class FontTextView extends TextView {

	public FontTextView(Context context) {
		super(context);
	}

	public FontTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.FontTextView, 0, 0);

		try {
			String font = a.getString(R.styleable.FontTextView_font);

			if (font != null) {
				Typeface type = Typeface.createFromAsset(context.getAssets(),
                        "fonts/" + font);
				this.setTypeface(type);
			}

		} finally {
			a.recycle();
		}

	}

}
