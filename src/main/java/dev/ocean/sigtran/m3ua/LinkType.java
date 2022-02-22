/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

/**
 *
 * @author eatakishiyev
 */
public enum LinkType {
    CLIENT("CLIENT"),
    SERVER("SERVER");

    private String value;

    private LinkType(String value) {
        this.value = value;
    }

    public static LinkType getInstance(String value) {
        switch (value) {
            case "CLIENT":
                return CLIENT;
            case "SERVER":
                return SERVER;
            default:
                return CLIENT;
        }
    }

    public String getValue() {
        return value;
    }

}
