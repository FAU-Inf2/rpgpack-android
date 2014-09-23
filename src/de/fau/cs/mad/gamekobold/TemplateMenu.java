package de.fau.cs.mad.gamekobold;

import de.fau.cs.mad.gamekobold.templatebrowser.CreateNewTemplateActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateBrowserActivity;
import de.fau.cs.mad.gamekobold.templatestore.TemplateStoreMainActivity;
import android.content.Intent;
import android.os.Bundle;

public class TemplateMenu  extends AbstractThreeButtonMenu {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final int startColor = getResources().getColor(R.color.menu_templates_start_color);
		final int endColor = getResources().getColor(R.color.menu_templates_end_color);
		final int[] midColors = getMidGradientColors(startColor, endColor);
		setTitle(R.string.menu_your_templates);
		// gradient colors
		// button 1 gradient color
		int[] gradient1 = {
			// start color
			startColor,
			// end color
			midColors[0]
		};
		// button 2 gradient color
		int[] gradient2 = {
			// start color
			midColors[0],
			// end color
			midColors[1]
		};
		// button 3 gradient color
		int[] gradient3 = {
			// start color
			midColors[1],
			// end color
			endColor
		};

		// Set the texts
		// button 1
		setButton1MainText(getString(R.string.menu_new_template));
		setButton1DescriptionText(getString(R.string.menu_new_template_description));
		
		// button 2
		setButton2MainText(getString(R.string.menu_edit_templates));
		setButton2DescriptionText(getString(R.string.menu_edit_templates_description));

		// button 3
		setButton3MainText(getString(R.string.menu_download_templates));
		setButton3DescriptionText(getString(R.string.menu_download_templates_description));

		// set the colors for the gradients
		setButton1Color(gradient1);
		setButton2Color(gradient2);
		setButton3Color(gradient3);
	}

	@Override
	protected void button1Action() {
		Intent i = new Intent(TemplateMenu.this,
				CreateNewTemplateActivity.class);
		startActivity(i);
	}

	@Override
	protected void button2Action() {
		Intent i = new Intent(TemplateMenu.this,
				TemplateBrowserActivity.class);
		startActivity(i);
	}

	@Override
	protected void button3Action() {
		Intent intent = new Intent(TemplateMenu.this,
				TemplateStoreMainActivity.class);
		startActivity(intent);
	}

}
