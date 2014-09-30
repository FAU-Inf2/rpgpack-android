package de.fau.cs.mad.gamekobold;

import de.fau.cs.mad.gamekobold.alljoyn.TemplateListActivity;
import de.fau.cs.mad.gamekobold.game.CreateNewGameActivity;
import de.fau.cs.mad.gamekobold.game.GameBrowserActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateBrowserActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class GameMenu extends AbstractThreeButtonMenu {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final int startColor = getResources().getColor(
				R.color.menu_games_start_color);
		final int endColor = getResources().getColor(
				R.color.menu_games_end_color);
		final int[] midColors = getMidGradientColors(startColor, endColor);
		setTitle(R.string.menu_your_games);
		// gradient colors
		// button 1 gradient color
		int[] gradient1 = {
				// start color
				startColor,
				// end color
				midColors[0] };
		// button 2 gradient color
		int[] gradient2 = {
				// start color
				midColors[0],
				// end color
				midColors[1] };
		// button 3 gradient color
		int[] gradient3 = {
				// start color
				midColors[1],
				// end color
				endColor };

		// Set the texts
		// button 1
		setButton1MainText(getString(R.string.menu_new_game));
		setButton1DescriptionText(getString(R.string.menu_new_game_description));

		// button 2
		setButton2MainText(getString(R.string.menu_continue_game));
		setButton2DescriptionText(getString(R.string.menu_continue_game_description));

		// button 3
		// TODO right text
		setButton3MainText(getString(R.string.menu_alljoyn));
		setButton3DescriptionText(getString(R.string.menu_alljoyn_description));

		// set the colors for the gradients
		setButton1Color(gradient1);
		setButton2Color(gradient2);
		setButton3Color(gradient3);
	}

	@Override
	protected void button1Action() {
		// Start create new game activity
		Intent i = new Intent(GameMenu.this, CreateNewGameActivity.class);
		startActivity(i);
	}

	@Override
	protected void button2Action() {
		// Start game browser
		Intent i = new Intent(GameMenu.this, GameBrowserActivity.class);
		startActivity(i);
	}

	@Override
	protected void button3Action() {
		Toast.makeText(GameMenu.this, "Alljoyn", Toast.LENGTH_LONG).show();
		// Start template list
		Intent i = new Intent(GameMenu.this, TemplateListActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(i);
	}
}
