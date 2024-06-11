package com.smartplant.smartplantandroid.core.data.observers;

import java.util.ArrayList;

public class ObservableData<T> implements Observable<T> {
    private final ArrayList<Observer<T>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(T data) {
        for (Observer<T> observer : observers) {
            observer.onUpdate(data);
        }
    }
}
