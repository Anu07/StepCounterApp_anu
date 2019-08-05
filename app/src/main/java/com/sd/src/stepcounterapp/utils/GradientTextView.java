package com.sd.src.stepcounterapp.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import com.sd.src.stepcounterapp.R;

public class GradientTextView extends AppCompatTextView {
	
	private Typeface typeface;
	
	public GradientTextView(Context context) {
		super(context);
		init(null);
	}
	
	public GradientTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	
	public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}
	
	/*@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		//Setting the gradient if layout is changed
		if (changed) {
			getPaint().setShader(new LinearGradient(0, 0, getWidth(), getHeight(),
					ContextCompat.getColor(getContext(), R.color.gradientColorStart),
					ContextCompat.getColor(getContext(), R.color.gradientColorEnd),
					Shader.TileMode.CLAMP));
		}
	}*/
	
	
	private void init( AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomGradientTextView);
			String fontName = a.getString(R.styleable.CustomGradientTextView_appGradientfont);
			
			try {
				if (fontName != null) {
					Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/$fontName");
					typeface = myTypeface;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			a.recycle();
		}
	}
	
}