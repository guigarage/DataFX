package org.datafx.samples.ejb.wildfly;

//import org.datafx.ejb.jboss.JBossLookupConfiguration;
import org.datafx.ejb.EjbLookupConfiguration;
import org.datafx.ejb.EjbLookupConfigurationProvider;

public class DemoConfigurationProvider implements EjbLookupConfigurationProvider {

    public static final String NAME = "wildfly-localhost";

    @Override
    public String getLookupName() {
        return NAME;
    }

    @Override
    public EjbLookupConfiguration getConfiguration() {
        return null;
        //return new JBossLookupConfiguration(NAME);
    }
}
