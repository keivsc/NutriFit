package com.group21.NutriFit.database;

public abstract interface Database<T> {

    public abstract void load();
    public abstract boolean add(T item);
    public abstract boolean remove(T item);

}
