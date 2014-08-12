package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

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
	private int height;
	private int width;
	private String background = "forest";
	private ArrayList<Path> paths = new ArrayList<Path>();
	private ArrayList<Path> undonePaths = new ArrayList<Path>();
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

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
		this.width = w;
		this.height = h;
		int id = getContext().getResources().getIdentifier(background,
				"drawable", getContext().getPackageName());
		canvasBitmap = BitmapFactory.decodeResource(getResources(), id);
		canvasBitmap = canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
		drawCanvas = new Canvas(canvasBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		for (Path p : paths)
			canvas.drawPath(p, drawPaint);
		canvas.drawPath(drawPath, drawPaint);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}

	private void touch_start(float x, float y) {
		undonePaths.clear();
		drawPath.reset();
		drawPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			drawPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		drawPath.lineTo(mX, mY);
		drawCanvas.drawPath(drawPath, drawPaint);// commit the path to our
													// offscreen
		paths.add(drawPath);
		drawPath = new Path();
	}

	public void setColor(String newColor) {
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}

	public void setBackground(String image) {
		this.background = image;
		Context context = getContext();
		int id = context.getResources().getIdentifier(image, "drawable",
				context.getPackageName());
		canvasBitmap = BitmapFactory.decodeResource(getResources(), id);
		canvasBitmap = canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
		invalidate();
	}

	public int getH() {
		return this.height;
	}

	public int getW() {
		return this.width;
	}

	public void undoLastStep() {
		if (paths.size() > 0) {
			resetBitmap();
			undonePaths.add(paths.remove(paths.size() - 1));
			invalidate();
		}
	}

	public void redoLastUndo() {
		if (undonePaths.size() > 0) {
			resetBitmap();
			paths.add(undonePaths.remove(undonePaths.size() - 1));
			invalidate();
		}
	}
	
	private void resetBitmap(){
		int id = getContext().getResources().getIdentifier(background,
				"drawable", getContext().getPackageName());
		canvasBitmap = BitmapFactory.decodeResource(getResources(), id);
		canvasBitmap = canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
	}
}
