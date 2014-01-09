package org.datafx.ejb;

import java.util.List;

public interface JndiClassBindingProvider {

	List<JndiClassBinding<?>> getBindings();
	
	String getContextId(); //TODO: Soll im Default null zur�ck liefern. Dies ist f�r das default EjbLookup gedacht.
}
