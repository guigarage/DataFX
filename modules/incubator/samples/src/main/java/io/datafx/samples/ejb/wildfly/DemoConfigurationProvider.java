package io.datafx.samples.ejb.wildfly;


import io.datafx.ejb.EjbLookupConfiguration;
import io.datafx.ejb.EjbLookupConfigurationProvider;
import io.datafx.ejb.jboss.JBossLookupConfiguration;

public class DemoConfigurationProvider implements EjbLookupConfigurationProvider {

    public static final String NAME = "wildfly-localhost";

    @Override
    public String getLookupName() {
        return NAME;
    }

    @Override
    public EjbLookupConfiguration getConfiguration() {
        return new JBossLookupConfiguration(NAME);
    }
}
