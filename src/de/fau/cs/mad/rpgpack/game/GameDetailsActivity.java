package de.fau.cs.mad.rpgpack.game;

import de.fau.cs.mad.rpgpack.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

public class GameDetailsActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return new GameDetailsFragment();
	}
}
