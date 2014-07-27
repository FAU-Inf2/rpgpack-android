package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import android.content.Context;

public class TemplateLab {
	private static TemplateLab sTemplateLab;
	private ArrayList<Template> templates;
	private Context appContext;

	TemplateLab(Context appContext) {
		this.appContext = appContext;
		templates = new ArrayList<Template>();

		Template template1 = new Template("My First Template",
				"Dungeons and Dragons", "Anna", "20.05.2014");
		Template template2 = new Template("The Best Template",
				"Vampire the Masquerade", "Anna", "24.05.2014");
		Template template3 = new Template("Schwarze Auge Template",
				"Das Schwarze Auge", "Anna", "21.03.2014");
		Template template4 = new Template("Annas Template",
				"Das Schwarze Auge", "Anna", "12.06.2014");

		GameCharacter character1t1 = new GameCharacter("Anna", "20.03.2014",
				template1);
		GameCharacter character2t1 = new GameCharacter("Bella", "22.04.2014",
				template1);
		GameCharacter character3t1 = new GameCharacter("Sisi", "23.05.2014",
				template1);
		GameCharacter character1t2 = new GameCharacter("Emma", "24.06.2014",
				template2);
		GameCharacter character2t2 = new GameCharacter("Nana", "25.07.2014",
				template2);
		GameCharacter character1t3 = new GameCharacter("Olly", "26.08.2014",
				template3);
		GameCharacter character1t4 = new GameCharacter("Hannah", "27.09.2014",
				template4);
		GameCharacter character2t4 = new GameCharacter("Meggy", "28.07.2014",
				template4);

		template1.addCharacter(character1t1);
		template1.addCharacter(character2t1);
		template1.addCharacter(character3t1);
		template2.addCharacter(character1t2);
		template2.addCharacter(character2t2);
		template3.addCharacter(character1t3);
		template4.addCharacter(character1t4);
		template4.addCharacter(character2t4);

		templates.add(template1);
		templates.add(template2);
		templates.add(template3);
		templates.add(template4);
	}

	public static TemplateLab get(Context c) {
		if (sTemplateLab == null) {
			sTemplateLab = new TemplateLab(c.getApplicationContext());
		}
		return sTemplateLab;
	}

	public ArrayList<Template> getTemplates() {
		return templates;
	}

	public Template getGame(String templateName) {
		for (Template t : templates) {
			if (t.getTemplateName().equals(templateName))
				return t;
		}
		return null;
	}
}
