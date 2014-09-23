package de.fau.cs.mad.gamekobold;

import de.fau.cs.mad.gamekobold.templatestore.TemplateStoreMainActivity;
import android.content.Intent;
import android.os.Bundle;

public class DownloadTemplateMenu  extends AbstractThreeButtonMenu {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final int startColor = getResources().getColor(R.color.menu_templates_start_color);
		final int endColor = getResources().getColor(R.color.menu_templates_end_color);
		final int[] midColors = getMidGradientColors(startColor, endColor);
		setTitle(R.string.menu_download_templates);
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
		setButton1MainText(getString(R.string.menu_template_store));
		setButton1DescriptionText(getString(R.string.menu_template_store_description));
		
		// button 2
		setButton2MainText(getString(R.string.menu_template_import));
		setButton2DescriptionText(getString(R.string.menu_template_import_description));

		// button 3
		setButton3MainText(getString(R.string.menu_scan_qrcode));
		setButton3DescriptionText(getString(R.string.menu_scan_qrcode_description));

		// set the colors for the gradients
		setButton1Color(gradient1);
		setButton2Color(gradient2);
		setButton3Color(gradient3);
	}

	@Override
	protected void button1Action() {
		Intent intent = new Intent(DownloadTemplateMenu.this,
				TemplateStoreMainActivity.class);
		startActivity(intent);
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
