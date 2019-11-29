package net.akami.mistream.gamedata;

import java.util.List;

public abstract class DataHandler {

    // Providers of data, such as car locations, field info, etc
    protected final List<DataProvider> dataProviders;

    public DataHandler() {
        this.dataProviders = loadProviders();
    }

    protected abstract List<DataProvider> loadProviders();

    public void add(DataProvider target) {
        dataProviders.add(target);
    }

    public void remove(DataProvider target) {
        dataProviders.remove(target);
    }

    public <T extends DataProvider> T data(Class<T> clazz) {
        for(DataProvider provider : dataProviders) {
            if(provider.getClass().equals(clazz)) {
                return (T) provider;
            }
        }
        throw new IllegalStateException("Could not find the DataProvider corresponding to : " + clazz);
    }
}
