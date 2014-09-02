package de.fau.cs.mad.gamekobold.template_generator;

import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;

import de.fau.cs.mad.gamekobold.R;
import android.view.View;

public class MyClickableSpan extends android.text.style.ClickableSpan {
//    View.OnClickListener mListener;
	View mainView;

    public MyClickableSpan(View popupView) {
    	mainView = popupView;
    }

	@Override
    public void onClick(View widget) {
    	//        mListener.onClick(widget);
    	//        TextView tv = (TextView) widget;
    	//        System.out.println("tv.gettext() :: " + tv.getText());
    	//        Toast.makeText(MyActivity.this,tv.getText(),
    	//        Toast.LENGTH_SHORT).show();
    	ToolTipRelativeLayout toolTipRelativeLayout = (ToolTipRelativeLayout) mainView.findViewById(R.id.activity_main_tooltipRelativeLayout);
    	//
    	ToolTip toolTip = new ToolTip()
    	.withText("A beautiful View")
    	.withColor(R.color.red)
    	.withShadow();
    	View myToolTipView = toolTipRelativeLayout.showToolTipForView(toolTip, widget);
//    	myToolTipView.setOnToolTipViewClickedListener(MainActivity.this);
    }
}