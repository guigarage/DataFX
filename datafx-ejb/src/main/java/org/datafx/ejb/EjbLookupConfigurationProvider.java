package org.datafx.ejb;

public interface EjbLookupConfigurationProvider {

    String getLookupName();

    EjbLookupConfiguration getConfiguration();
}
