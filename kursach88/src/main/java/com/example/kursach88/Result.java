package com.example.kursach88;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Result {
    private SimpleStringProperty sName = new SimpleStringProperty("");
    private SimpleIntegerProperty sTime = new SimpleIntegerProperty();

    public Result() {
    }

    public Result(String sName, int sTime) {
        this.sName = new SimpleStringProperty(sName);
        this.sTime = new SimpleIntegerProperty(sTime);
    }

    public String getsName() {
        return sName.get();
    }

    public void setsName(String sName) {
        this.sName.set(sName);
    }

    public int getsTime() {
        return sTime.get();
    }

    public void setsTime(int sTime) {
        this.sTime.set(sTime);
    }

    public SimpleStringProperty sNameProperty() {
        return sName;
    }

    public SimpleIntegerProperty sTimeProperty() {
        return sTime;
    }

    @Override
    public String toString() {
       /* return "Алгоритм{"+
            "название='" + sName + '\'' +
                    ", время='" + sTime + '\'' +
                    '}';

        */
        return "" + sTime;
    }
}
