package de.sevenfactory.mia.common;

public abstract class Repository<T> {
    private T Api;
    
    public Repository(T inApi) {
        Api = inApi;
    }
    
    protected T getApi() {
        return Api;
    }
}
