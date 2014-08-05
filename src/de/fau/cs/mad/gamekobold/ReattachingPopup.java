package de.fau.cs.mad.gamekobold;

import de.fau.cs.mad.gamekobold.template_generator.TableFragment;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.PopupWindow;

public class ReattachingPopup extends PopupWindow{
	Fragment belongsTo = null;
	ReattachingPopup popup = null;
	int gravity = -1;
	int x = -1;
	int y = -1;

	public ReattachingPopup(Fragment frag, View contentView, int width, int height, boolean focusable){
		super(contentView, width, height, focusable);
		belongsTo = frag;
		popup = this;
//		PopupWindow newPopup = new PopupWindow();
//		this.setOnDismissListener(new PopupWindow.OnDismissListener() {
//	        @Override
//	        public void onDismiss() {
//	            if(belongsTo instanceof TableFragment){
//	            	TableFragment tf = (TableFragment) belongsTo;
//	            	tf.currentPopups.remove(popup);
//	            }
//	        }
//	    });
	}
	
	@Override
	public void dismiss(){
		if(belongsTo instanceof TableFragment){
        	TableFragment tf = (TableFragment) belongsTo;
        	tf.currentlyShownPopups.remove(popup);
        }
		super.dismiss();
	}
	
	public void saveDismiss(){
		super.dismiss();
	}
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y){
//				final int popupWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (dpWidth*0.9), getResources().getDisplayMetrics()));
        DisplayMetrics displayMetrics = belongsTo.getActivity().getResources().getDisplayMetrics();
		float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
		setWidth(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (dpWidth*0.9), belongsTo.getResources().getDisplayMetrics())));
		this.gravity = gravity;
		this.x = x;
		this.y = y;
		if(belongsTo instanceof TableFragment){
        	TableFragment tf = (TableFragment) belongsTo;
        	tf.currentlyShownPopups.add(popup);
        }
		if(parent == null){
			Log.d("ReattachingPopup", "parent IS null!");
		}
		super.showAtLocation(parent, gravity, x, y);
	}
	
	public void show(Fragment frag, View parent){
		belongsTo = frag;
		Log.d("ReattachingPopup", "showing popup again");
		showAtLocation(parent, gravity, x, y);
	}
	
}
