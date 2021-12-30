package com.example.kursach88;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CollectionsResultBook implements ResultBook {

    private ObservableList<Result> resultList = FXCollections.observableArrayList();


    @Override
    public void add(Result result) {
        resultList.add(result);
    }

    public ObservableList<Result> getResultList() {
        return resultList;
    }

    public void print() {
        System.out.println();
        for (Result result : resultList) {
            System.out.println("Название: " + result.getsName() + " время: " + result.getsTime());
        }
    }

    public void fillTestData() {
        resultList.add(new Result("JPEG", 24));
        resultList.add(new Result("PNG", 200));
        resultList.add(new Result("JPEG2000", 73));
        resultList.add(new Result("JPEG", 12));
        resultList.add(new Result("JPEG2000", 50));
        resultList.add(new Result("PNG", 189));
    }
}
