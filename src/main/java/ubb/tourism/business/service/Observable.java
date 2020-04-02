package ubb.tourism.business.service;

import ubb.tourism.controller.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {

    private List<Observer> observerList = new ArrayList<>();

    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    public void deleteObserver(Observer observer) {
        observerList.remove(observer);
    }

    public void notifyAllObservers() {
        observerList.forEach(Observer::update);
    }
}