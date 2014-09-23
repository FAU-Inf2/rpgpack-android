package de.fau.cs.mad.gamekobold;

import android.graphics.Color;
import android.os.Bundle;

public class TemplateMenu  extends AbstractThreeButtonMenu {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// gradient colors
		// button 1 gradient color
		int[] gradient1 = {
			// start color
			Color.rgb(0, 0, 255),
			// end color
			Color.rgb(0, 0, 203)
		};
		// button 2 gradient color
		int[] gradient2 = {
			// start color
			Color.rgb(0, 0, 203),
			// end color
			Color.rgb(0, 0, 151)
		};
		// button 3 gradient color
		int[] gradient3 = {
			// start color
			Color.rgb(0, 0, 151),
			// end color
			Color.rgb(0, 0, 100)
		};

		// Set the texts
		// button 1
		setButton1MainText(getString(R.string.menu_new_template));
		setButton1DescriptionText("Beschreibung");
		
		// button 2
		setButton2MainText(getString(R.string.menu_edit_templates));
		setButton2DescriptionText("Beschreibung");

		// button 3
		setButton3MainText(getString(R.string.menu_download_templates));
		setButton3DescriptionText("Beschreibung");

		// set the colors for the gradients
		setButton1Color(gradient1);
		setButton2Color(gradient2);
		setButton3Color(gradient3);
	}

	@Override
	protected void button1Action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void button2Action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void button3Action() {
		// TODO Auto-generated method stub
		
	}

}
