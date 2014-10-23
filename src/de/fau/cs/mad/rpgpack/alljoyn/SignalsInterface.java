package de.fau.cs.mad.rpgpack.alljoyn;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusSignal;

@BusInterface(name="de.fau.cs.mad.gamekobold.signalinterface")
public interface SignalsInterface {
	@BusSignal
	void testSignal(int number) throws BusException;
}
