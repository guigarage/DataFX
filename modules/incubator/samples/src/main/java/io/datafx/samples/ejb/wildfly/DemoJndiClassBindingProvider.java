package io.datafx.samples.ejb.wildfly;

import io.datafx.ejb.JndiClassBinding;
import io.datafx.ejb.JndiClassBindingProvider;
import io.datafx.ejb.RemoteCalculator;

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
