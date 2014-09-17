package io.datafx.ejb.jboss;

import io.datafx.ejb.EjbLookupConfiguration;
import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

import javax.naming.Context;
import java.util.Hashtable;
import java.util.Properties;

public class JBossLookupConfiguration extends EjbLookupConfiguration {

    public JBossLookupConfiguration(String contextId) {
        super(contextId);
        addToEnvironment(createDefaultContextEnvironment());
    }

    public JBossLookupConfiguration() {
        addToEnvironment(createDefaultContextEnvironment());
    }

    public JBossLookupConfiguration(String host, int port, String username, String password) {
        this(createDefaultEjbClientProperties(host, port, username, password), createDefaultContextEnvironment());
    }

    public JBossLookupConfiguration(Properties jbossEjbClientProperties) {
        EJBClientConfiguration ejbClientConfiguration = new PropertiesBasedEJBClientConfiguration(jbossEjbClientProperties);
        ContextSelector<EJBClientContext> ejbClientContextSelector = new ConfigBasedEJBClientContextSelector(ejbClientConfiguration);
        EJBClientContext.setSelector(ejbClientContextSelector);
    }

    public JBossLookupConfiguration(Properties jbossEjbClientProperties, Hashtable<?, ?> contextEnvironment) {
        this(jbossEjbClientProperties);
        addToEnvironment(contextEnvironment);
    }

    private static Hashtable<?, ?> createDefaultContextEnvironment() {
        Hashtable<String, String> environment = new Hashtable<>();
        environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        return environment;
    }

    private static Properties createDefaultEjbClientProperties(String host, int port, String username, String password) {
        Properties jbossEjbClientProperties = new Properties();
        jbossEjbClientProperties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        jbossEjbClientProperties.put("remote.connections", "default");
        jbossEjbClientProperties.put("remote.connection.default.host", host);
        jbossEjbClientProperties.put("remote.connection.default.port", port);
        jbossEjbClientProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
        jbossEjbClientProperties.put("remote.connection.default.username", username);
        jbossEjbClientProperties.put("remote.connection.default.password", password);
        return jbossEjbClientProperties;
    }

}
