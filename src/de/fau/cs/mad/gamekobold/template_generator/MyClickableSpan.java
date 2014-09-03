package de.fau.cs.mad.gamekobold.template_generator;

//import com.nhaarman.supertooltips.ToolTip;
//import com.nhaarman.supertooltips.ToolTipRelativeLayout;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MyClickableSpan extends ClickableSpan {
//    View.OnClickListener mListener;
	View mainView;
	TableFragment mBelongsTo;

    public MyClickableSpan(View popupView, TableFragment tf) {
    	mainView = popupView;
    	mBelongsTo = tf;
    }

	@Override
    public void onClick(View widget) {
    	//        mListener.onClick(widget);
    	//        TextView tv = (TextView) widget;
    	//        System.out.println("tv.gettext() :: " + tv.getText());
    	//        Toast.makeText(MyActivity.this,tv.getText(),
    	//        Toast.LENGTH_SHORT).show();
//		Toast.makeText(mBelongsTo.getActivity(), "TOAST" ,Toast.LENGTH_SHORT).show();
        Log.d("MyClickableSpan", "MyClickableSpan -> onClick");
//    	ToolTipRelativeLayout toolTipRelativeLayout = (ToolTipRelativeLayout) mainView.findViewById(R.id.activity_main_tooltipRelativeLayout);
//    	ToolTip toolTip = new ToolTip()
//    	.withText("The tooltip")
//    	.withColor(R.color.red)
//    	.withShadow();
//        Log.d("MyClickableSpan", "toolTip==null? " + (toolTip==null) + "; text == "
//        		+ toolTip.getText());
//    	View myToolTipView = toolTipRelativeLayout.showToolTipForView(toolTip, widget);
////    	myToolTipView.setOnToolTipViewClickedListener(MainActivity.this);
    }
}