package org.datafx.ejb.glassfish;

import org.datafx.ejb.EjbLookupConfiguration;

public class GlassfishLookupConfiguration extends EjbLookupConfiguration {

	public GlassfishLookupConfiguration(String host, int port) {
		addToEnvironment("java.naming.factory.initial","com.sun.enterprise.naming.SerialInitContextFactory");  
		addToEnvironment("java.naming.factory.url.pkgs","com.sun.enterprise.naming");  
		addToEnvironment("java.naming.factory.state","com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");  
		addToEnvironment("org.omg.CORBA.ORBInitialHost", host);  
		addToEnvironment("org.omg.CORBA.ORBInitialPort", port + ""); 
	}
}
