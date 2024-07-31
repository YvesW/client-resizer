package com.ywcode.clientresizer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MonitorAttribute {
    Disabled("Disabled"),
    IDstring("ID String"),
    Bounds("Bounds"),
    Dimensions("Dimensions"),
    RefreshRate("Refresh Rate");

    private final String option;

    @Override
    public String toString() {
        return option;
    }

}