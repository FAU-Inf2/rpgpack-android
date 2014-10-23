package de.fau.cs.mad.rpgpack.template_generator;

import android.content.Context;
import android.widget.EditText;
import android.widget.TableRow;

//XXX: NOTE: this class is not used atm, just kept it because we might need the onSizeChanged later on...
public class ResizingEditText extends EditText{

	TableRow containingRow;
	TableFragment tableFragment;
	
	public ResizingEditText(Context context){
		super(context);
	}
	
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
