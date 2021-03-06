package com.example.lyrictextview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

public class LyricTextView extends TextView {
	private static final float MIN_PROGRESS = 0;
	private static final float MAX_PROGRESS = 1;
	private Matrix matrix = new Matrix();
	private Shader shader;
	private float progress = MIN_PROGRESS;
	private int coverColor, defaultColor;

	public LyricTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LyricTextView);
		coverColor = a.getColor(R.styleable.LyricTextView_coverColor, 0);
		
		ColorStateList textColors = getTextColors();
		defaultColor = textColors.getDefaultColor();
		if (coverColor == 0) {
			coverColor = (defaultColor & 0xff000000) | ((defaultColor & 0x00ffffff) ^ 0x00ffffff);
		}
		a.recycle();
	}

	public LyricTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LyricTextView(Context context) {
		this(context, null);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		if (w > 0) {
			TextPaint paint = getPaint();
			Bitmap bm = Bitmap.createBitmap(2, 1, Config.RGB_565);
			bm.setPixel(0, 0, coverColor);
			bm.setPixel(1, 0, defaultColor);
			shader = new BitmapShader(bm, TileMode.CLAMP, TileMode.CLAMP);
			paint.setShader(shader);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (shader != null) {
			matrix.setTranslate(getWidth() * progress - 1, 0);
			shader.setLocalMatrix(matrix);
		}
		super.onDraw(canvas);
	}
	
	public void setProgress(float progress) {
		if (progress < MIN_PROGRESS) progress = MIN_PROGRESS;
		if (progress > MAX_PROGRESS) progress = MAX_PROGRESS;
		this.progress = progress;
		invalidate();
	}
}
