package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import android.content.Context;
import android.util.Log;

public class TemplateLab {
	// tag for logging
	private static String LOG_TAG = "TemplateLab";

	private static TemplateLab sTemplateLab;
	private ArrayList<Template> templates;
	private Context appContext;
	// time stamp for the templates folder
	private long folderTimeStamp;

	TemplateLab(Context appContext) {
		this.appContext = appContext;
		templates = new ArrayList<Template>();
		// set time stamp to 0, so we are at least loading once
		folderTimeStamp = 0;
	}

	public static TemplateLab get(Context c) {
		if (sTemplateLab == null) {
			sTemplateLab = new TemplateLab(c.getApplicationContext());
		}
		return sTemplateLab;
	}

	public ArrayList<Template> getTemplates() {
		assureListIsUpToDate();
		return templates;
	}

	public Template getTemplate(String templateName) {
		assureListIsUpToDate();
		for (Template t : templates) {
			if (t.getTemplateName().equals(templateName))
				return t;
		}
		return null;
	}

	/**
	 * Assures that the template list is up to date. If any thing changed the
	 * list will be updated.
	 */
	private void assureListIsUpToDate() {
		// checks if a template has been created or deleted
		if (!checkForTemplateDirectoryChange()) {
			// only check every template for a change when not reloading the
			// whole list
			checkEveryTemplateForChanges();
		}
	}

	/**
	 * Checks the template directory for a change. Its time stamp is updated
	 * when a file is created or deleted. Only loads metadata for templates.
	 * 
	 * @return true if the template list has been reloaded, false otherwise.
	 */
	private boolean checkForTemplateDirectoryChange() {
		final File templateDir = JacksonInterface
				.getTemplateRootDirectory(appContext);
		if (templateDir != null) {
			final long newTimeStamp = templateDir.lastModified();
			if (folderTimeStamp < newTimeStamp) {
				templates.clear();

				// FIXME remove as not using
				// add default test data
				// addDefaultData();

				// update time stamp
				folderTimeStamp = newTimeStamp;
				// reload template list
				Log.d(LOG_TAG, "Reloading template list");
				Log.d(LOG_TAG,
						"Tempolate directory:" + templateDir.getAbsolutePath());
				if (templateDir.isDirectory()) {
					final File[] fileList = templateDir.listFiles();
					de.fau.cs.mad.gamekobold.jackson.Template loadedTemplate = null;
					for (final File file : fileList) {
						try {
							loadedTemplate = JacksonInterface.loadTemplate(
									file, true);
							if (loadedTemplate != null) {
								Template temp = new Template(
										loadedTemplate.templateName,
										loadedTemplate.gameName,
										loadedTemplate.author,
										loadedTemplate.date,
										loadedTemplate.iconID,
										loadedTemplate.description);
								if (temp.getTemplateName().equals("")) {
									temp.setTemplateName(file.getName());
								}
								temp.fileAbsolutePath = file.getAbsolutePath();
								// set time stamp
								temp.setFileTimeStamp(file.lastModified());
								// load the characters for the template
								loadCharacters(temp);
								// add the template to the list
								templates.add(temp);
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
				// fake item to create New Character from template
				GameCharacter createNewCharacter = new GameCharacter("+");
				for (Template t : templates) {
					t.addCharacter(createNewCharacter);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks every template if it has been changed. If so it and its characters
	 * will be reloaded.
	 */
	private void checkEveryTemplateForChanges() {
		for (Template template : templates) {
			final File templateFile = template.getTemplateFile();
			if (templateFile != null) {
				if (template.hasFileTimeStampChanged()) {
					// template file has been changed
					try {
						de.fau.cs.mad.gamekobold.jackson.Template loadedTemplate = JacksonInterface
								.loadTemplate(templateFile, true);
						if (loadedTemplate != null) {
							// take over changes
							template.setTemplateName(loadedTemplate.templateName);
							template.setWorldName(loadedTemplate.gameName);
							template.setAuthor(loadedTemplate.author);
							template.setDate(loadedTemplate.date);
							template.setIconID(loadedTemplate.iconID);
							template.setDescription(loadedTemplate.description);
							// update time stamp
							template.setFileTimeStamp(templateFile
									.lastModified());
							// load characters again
							loadCharacters(template);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Loads and adds the characters for the given template.
	 * 
	 * @param template
	 */
	private void loadCharacters(Template template) {
		final File characterDir = JacksonInterface.getDirectoryForCharacters(
				template, appContext, true);
		// nullpointer check
		if (characterDir != null) {
			// remove all old characters
			template.clearCharacters();

			final File[] characters = characterDir.listFiles();
			for (final File characterFile : characters) {
				try {
					final CharacterSheet sheet = JacksonInterface
							.loadCharacterSheet(characterFile, true);
					final GameCharacter character = new GameCharacter("");
					character.setCharacterName(sheet.name);
					character.setDescription(sheet.description);
					character.setTemplate(template);
					character.setFileAbsPath(characterFile.getAbsolutePath());
					template.addCharacter(character);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

	// FIXME remove as not using
	// private void addDefaultData() {
	// Template template1 = new Template("My First Template",
	// "Dungeons and Dragons", "Anna", "20.05.2014");
	// Template template2 = new Template("The Best Template",
	// "Vampire the Masquerade", "Anna", "24.05.2014");
	// Template template3 = new Template("Schwarze Auge Template",
	// "Das Schwarze Auge", "Anna", "21.03.2014");
	// Template template4 = new Template("Annas Template",
	// "Das Schwarze Auge", "Anna", "12.06.2014");
	//
	// GameCharacter character1t1 = new GameCharacter("Anna", "20.03.2014",
	// template1);
	// GameCharacter character2t1 = new GameCharacter("Bella", "22.04.2014",
	// template1);
	// GameCharacter character3t1 = new GameCharacter("Sisi", "23.05.2014",
	// template1);
	// GameCharacter character1t2 = new GameCharacter("Emma", "24.06.2014",
	// template2);
	// GameCharacter character2t2 = new GameCharacter("Nana", "25.07.2014",
	// template2);
	// GameCharacter character1t3 = new GameCharacter("Olly", "26.08.2014",
	// template3);
	// GameCharacter character1t4 = new GameCharacter("Hannah", "27.09.2014",
	// template4);
	// GameCharacter character2t4 = new GameCharacter("Meggy", "28.07.2014",
	// template4);
	//
	// template1.addCharacter(character1t1);
	// template1.addCharacter(character2t1);
	// template1.addCharacter(character3t1);
	// template2.addCharacter(character1t2);
	// template2.addCharacter(character2t2);
	// template3.addCharacter(character1t3);
	// template4.addCharacter(character1t4);
	// template4.addCharacter(character2t4);
	//
	// templates.add(template1);
	// templates.add(template2);
	// templates.add(template3);
	// templates.add(template4);
	// }
}
