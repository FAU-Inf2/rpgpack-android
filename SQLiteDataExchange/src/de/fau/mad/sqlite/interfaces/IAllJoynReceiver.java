package de.fau.mad.sqlite.interfaces;

import java.util.Map;

public interface IAllJoynReceiver {
	Map<String, String> receive(String jsonString);
}
