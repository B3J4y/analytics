package de.itj.analytics.action;

public interface Action<T> {
    void execute();
    T getResult();
}