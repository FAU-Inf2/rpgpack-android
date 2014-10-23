package de.fau.cs.mad.rpgpack.template_generator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.NumberPicker;

public class CustomNumberPicker extends NumberPicker
{
    public CustomNumberPicker(Context context, AttributeSet attrs, int
defStyle)
    {
        super(context, attrs, defStyle);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomNumberPicker(Context context)
    {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        /* Prevent parent controls from stealing our events once we've
gotten a touch down */
        if (ev.getActionMasked() != MotionEvent.ACTION_UP)
        {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }
    
}