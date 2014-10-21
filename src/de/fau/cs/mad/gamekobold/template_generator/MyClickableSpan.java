package de.fau.cs.mad.gamekobold.template_generator;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import com.nhaarman.supertooltips.*;
//import com.nhaarman.supertooltips.ToolTipRelativeLayout;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.matrix.MatrixItem;
import android.os.Handler;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class MyClickableSpan extends ClickableSpan {
//    View.OnClickListener mListener;
	View mainView;
	TableFragment mBelongsTo;
	static Stack<ToolTipView> activeToolTips= new Stack<ToolTipView>();

    public MyClickableSpan(View popupView, TableFragment tf) {
    	mainView = popupView;
    	mBelongsTo = tf;
    }

	@Override
    public void onClick(View widget) {
    	TextView tv = (TextView) widget;
    	Spanned s = (Spanned) tv.getText();
    	if(s != null){
    		int start = s.getSpanStart(this);
    		int end = s.getSpanEnd(this);
    		while(s.charAt(start) == '@'){
    			start++;
    		}
    		//note: add +1 to start to skip @
    		String toSearch = s.subSequence(start, end).toString();
    		String toShow = null;
    		//        Log.d("MyClickableSpan", "toSearch == " + toSearch);
    		//        Log.d("MyClickableSpan", "toSearch.amoutChars == " + toSearch.length());
    		ArrayList<MatrixItem> items = SlideoutNavigationActivity.theActiveActivity.getRootFragment().getAllMatrixReferences();
    		for(MatrixItem item: items){
    			//	        Log.d("MyClickableSpan", "itemName == " + item.getItemName());
    			//	        Log.d("MyClickableSpan", "itemName.amoutChars == " + item.getItemName().length());
    			if(item.getItemName().equals(toSearch)){
    				toShow = item.getItemName() + ": " + item.getValue();
    				//		        Log.d("MyClickableSpan", "valueToShow is set!");
    			}
    		}

    		//		Toast.makeText(mBelongsTo.getActivity(), "TOAST" ,Toast.LENGTH_SHORT).show();
    		//        Log.d("MyClickableSpan", "MyClickableSpan -> onClick");
    		ToolTipRelativeLayout toolTipRelativeLayout = (ToolTipRelativeLayout) mainView.findViewById(R.id.activity_main_tooltipRelativeLayout);
    		//if value was not found -> set another message
    		if(toShow == null){
    			toShow = "\"" + toSearch + "\"" + 
    					mBelongsTo.getResources().getString(R.string.reference_inexistent);
    		}
    		ToolTip toolTip = new ToolTip()
    		.withText(toShow)
    		.withColor(mBelongsTo.getResources().getColor(R.color.a_green));
    		//    	.withShadow();

    		//
    		//        Log.d("MyClickableSpan", "toolTip==null? " + (toolTip==null) + "; text == "
    		//        		+ toolTip.getText());
    		//first remove all other (old) tooltips
    		ToolTipView toRemove = null;
    		try{
    			while((toRemove = activeToolTips.pop()) != null){
    				toRemove.remove();
    			}
    		}catch(EmptyStackException e){
    			//if it is emtpy -> do nothing
    		}
    		//now show new tooltip and add it to active list
    		final ToolTipView myToolTipView = toolTipRelativeLayout.showToolTipForView(toolTip, widget);
    		activeToolTips.add(myToolTipView);
    		//remove the new tooltip anyway after some time
    		Handler handler = new Handler();
    		handler.postDelayed(new Runnable(){
    			public void run(){
    				myToolTipView.remove();
    			}
    		}, 3000);
    		//    	myToolTipView.
    		////    	myToolTipView.setOnToolTipViewClickedListener(MainActivity.this);
    	}
    }
}