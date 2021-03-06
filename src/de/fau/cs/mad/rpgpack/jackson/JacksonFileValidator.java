package de.fau.cs.mad.rpgpack.jackson;

import java.io.File;
import java.io.FileInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonFileValidator {

	public static boolean isValidTemplate(final File file) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			FileInputStream inStream = new FileInputStream(file);
			@SuppressWarnings("unused")
			Template template = mapper.readValue(inStream, Template.class);
			inStream.close();
		}
		catch(Throwable e) {
			return false;
		}
		return true;
	}

	public static boolean isValidCharacter(final File file) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			FileInputStream inStream = new FileInputStream(file);
			@SuppressWarnings("unused")
			CharacterSheet character = mapper.readValue(inStream, CharacterSheet.class);
			inStream.close();
		}
		catch(Throwable e) {
			return false;
		}
		return true;
	}
}
