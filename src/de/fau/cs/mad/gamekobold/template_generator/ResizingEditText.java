package de.fau.cs.mad.gamekobold.template_generator;

import android.content.Context;
import android.widget.EditText;
import android.widget.TableRow;

public class ResizingEditText extends EditText{

	TableRow containingRow;
	TableFragment tableFragment;
	
	public ResizingEditText(Context context, TableRow row, TableFragment tF) {
		super(context);
		tableFragment = tF;
		containingRow = row;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//		tableFragment.checkResize(w, h, this, containingRow);
		super.onSizeChanged(w, h, oldw, oldh);
	}

}
