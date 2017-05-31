package com.example.muffin.cookingbook.models;


import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;


public class RealmString extends RealmObject{

    private String val;

    public RealmString(){}

    public RealmString(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
