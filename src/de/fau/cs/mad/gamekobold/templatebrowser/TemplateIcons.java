package de.fau.cs.mad.gamekobold.templatebrowser;

import java.util.HashMap;
import java.util.Map;

import de.fau.cs.mad.gamekobold.R;

public class TemplateIcons {

	private static TemplateIcons instance = null;
	
	private Map<Integer, Integer> iconMap;
	private Integer iconID = 0;
	
	private TemplateIcons() {
		initialiseIcons();
	}

	public static TemplateIcons getInstance(){
		if (instance == null) {
			instance = new TemplateIcons();
		}
		return instance;
	}
	
	public Integer addTemplateIcon(int iconReference) {
		int myID = iconID;
		iconMap.put(myID, iconReference);
		iconID++;
		return myID;
	}

	public int getTempalteIcon(int iconID) {
		return iconMap.get(iconID);
	}
	
	private void initialiseIcons() {
		this.iconMap = new HashMap<Integer, Integer>();
		this.addTemplateIcon(R.drawable.addphoto);
		this.addTemplateIcon(R.drawable.addphoto_grey);
		this.addTemplateIcon(R.drawable.dragon);
		this.addTemplateIcon(R.drawable.vampir);
		this.addTemplateIcon(R.drawable.eye);
	}
}





