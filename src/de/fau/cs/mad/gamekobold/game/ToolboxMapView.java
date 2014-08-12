package de.fau.cs.mad.gamekobold.game;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ToolboxMapView extends View {

	private Path drawPath;
	private Paint drawPaint, canvasPaint;
	private int paintColor = 0xFF660000;
	private Canvas drawCanvas;
	private Bitmap canvasBitmap;

	public ToolboxMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupMap();
	}

	private void setupMap() {
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(20);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int id = getContext().getResources().getIdentifier("forest", "drawable", getContext().getPackageName());
		canvasBitmap = BitmapFactory.decodeResource(getResources(), id);
		canvasBitmap =  canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
		drawCanvas = new Canvas(canvasBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			drawPath.lineTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_UP:
			drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			break;
		default:
			return false;
		}
		invalidate();
		return true;
	}

	public void setColor(String newColor) {
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}
	
	public void setBackground(String image) {
		invalidate();
		Context context = getContext();
		Bitmap bmp;
		int id = context.getResources().getIdentifier(image, "drawable", context.getPackageName());
		canvasBitmap = BitmapFactory.decodeResource(getResources(), id);
		canvasBitmap =  canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
		drawCanvas.setBitmap(canvasBitmap);
		invalidate();
	}

}
