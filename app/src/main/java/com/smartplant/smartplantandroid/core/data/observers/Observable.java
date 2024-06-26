package com.smartplant.smartplantandroid.core.data.observers;

public interface Observable<T> {
    void addObserver(Observer<T> observer);

    void removeObserver(Observer<T> observer);

    void notifyObservers(T obj);
}
