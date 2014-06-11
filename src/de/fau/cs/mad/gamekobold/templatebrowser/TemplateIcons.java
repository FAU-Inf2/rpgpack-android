package de.fau.cs.mad.gamekobold.templatebrowser;

import java.util.HashMap;
import java.util.Map;

import de.fau.cs.mad.gamekobold.R;

public class TemplateIcons {

	private static TemplateIcons instance = null;

	private Map<Integer, String> iconMap;
	private Integer iconID = 0;

	private TemplateIcons() {
		initialiseIcons();
	}

	public static TemplateIcons getInstance() {
		if (instance == null) {
			instance = new TemplateIcons();
		}
		return instance;
	}

	public Integer addTemplateIcon(String iconReference) {
		int myID = iconID;
		iconMap.put(myID, iconReference);
		iconID++;
		return myID;
	}

	// public Integer addTemplateIcon(int iconReference) {
	// int myID = iconID;
	// iconMap.put(myID, iconReference);
	// iconID++;
	// return myID;
	// }

	public String getTempalteIcon(int iconID) {
		return iconMap.get(iconID);
	}

	private void initialiseIcons() {
		this.iconMap = new HashMap<Integer, String>();
		this.addTemplateIcon(Integer.toString(R.drawable.addphoto));
		this.addTemplateIcon(Integer.toString(R.drawable.addphoto_grey));
		this.addTemplateIcon(Integer.toString(R.drawable.dragon));
		this.addTemplateIcon(Integer.toString(R.drawable.vampir));
		this.addTemplateIcon(Integer.toString(R.drawable.eye));
	}

}
