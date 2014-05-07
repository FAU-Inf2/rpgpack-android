package de.fau.mad.sqlite.impl;

import de.fau.mad.sqlite.interfaces.IAllJoynSender;

public class DummyAllJoynSender implements IAllJoynSender {

	@Override
	public boolean send(String string) {
		// TODO AllJoynSender
		System.out.println("Sending some data via WLAN: " + string);
		
		//convert into Json-Data
		
		//return true is successful
		return true;
	}

}
