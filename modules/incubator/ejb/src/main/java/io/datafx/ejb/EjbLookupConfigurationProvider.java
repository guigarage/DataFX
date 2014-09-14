package io.datafx.ejb;

public interface EjbLookupConfigurationProvider {

    String getLookupName();

    EjbLookupConfiguration getConfiguration();
}
