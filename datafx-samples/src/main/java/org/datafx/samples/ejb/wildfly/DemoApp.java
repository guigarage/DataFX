package org.datafx.samples.ejb.wildfly;

import org.datafx.ejb.EjbLookup;
import org.datafx.ejb.EjbLookupFactory;
import org.datafx.ejb.JndiClassBinding;
import org.datafx.ejb.RemoteCalculator;

import javax.naming.NamingException;

public class DemoApp {

    public static void main(String[] args) {
        try {
            EjbLookup lookup = new EjbLookupFactory().get(DemoConfigurationProvider.NAME);
            lookup.add(new JndiClassBinding<>("ejb:/datafx-samples-ejb/CalculatorBean!" + RemoteCalculator.class.getName(), RemoteCalculator.class));
            RemoteCalculator calculator = lookup.lookup(RemoteCalculator.class);
            System.out.println(calculator.add(3, 3));
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
