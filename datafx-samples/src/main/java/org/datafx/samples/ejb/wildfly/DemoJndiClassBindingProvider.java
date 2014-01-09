package org.datafx.samples.ejb.wildfly;

import org.datafx.ejb.JndiClassBinding;
import org.datafx.ejb.JndiClassBindingProvider;
import org.datafx.ejb.RemoteCalculator;

import java.util.ArrayList;
import java.util.List;

public class DemoJndiClassBindingProvider implements JndiClassBindingProvider {

    @Override
    public List<JndiClassBinding<?>> getBindings() {
        List<JndiClassBinding<?>> bindings = new ArrayList<>();
        bindings.add(new JndiClassBinding<RemoteCalculator>("ejb:/datafx-samples-ejb/CalculatorBean!" + RemoteCalculator.class.getName(), RemoteCalculator.class));
        return bindings;
    }

    @Override
    public String getContextId() {
        return DemoConfigurationProvider.NAME;
    }
}
