package com.revizor.repos;

/**
 * Created by w32blaster on 26/10/14.
 */
public enum BranchColor {
    BLUE("85a6d6"),
    YELLOW("ddcd5d"),
    BROWN("c78639");

    private final String color;

    BranchColor(String hexColor) {
        this.color = hexColor;
    }

    public String getColor() {
        return this.color;
    }
}
