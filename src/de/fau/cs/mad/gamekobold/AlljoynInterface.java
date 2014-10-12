package de.fau.cs.mad.gamekobold;


import org.alljoyn.bus.BusException;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusMethod;

import de.fau.cs.mad.gamekobold.templatebrowser.Template;


@BusInterface(name="de.fau.cs.mad.gamekobold.SimpleInterface")
public interface AlljoynInterface {
	@BusMethod
	String Ping(String inStr) throws BusException;
    
    @BusMethod
    String count() throws BusException;
    
    @BusMethod 
    String receiveTemplate(String tpl) throws BusException;
}

