package io.datafx.ejb;

import java.util.*;

public class EjbLookupFactory {

    public EjbLookup get(String name) {
        ServiceLoader<EjbLookupConfigurationProvider> loader = ServiceLoader
                .load(EjbLookupConfigurationProvider.class);
        Iterator<EjbLookupConfigurationProvider> providers = loader.iterator();
        while (providers.hasNext()) {
            EjbLookupConfigurationProvider provider = providers.next();
            if(isNameMatching(provider.getLookupName(), name)) {
                EjbLookupConfiguration configuration = provider.getConfiguration();
                EjbLookup lookup = configuration.createLookup();
                return lookup;
            }
        }
        return null;
    }

    public boolean isNameMatching(String providerName, String name) {
        if(providerName == null && name == null) {
            return true;
        }
        if(providerName == null && name != null) {
            if(name == "") {
                return true;
            }
            return false;
        }
        if(providerName != null && name == null) {
            if(providerName == "") {
                return true;
            }
            return false;
        }
        return providerName.equals(name);
    }

}
