package org.realEstate.myRealEstateService.Enum;

public enum ObjectStatus {
    AVAILABLE("AVAILABLE"),
    SOLD("SOLD"),
    PENDING("PENDING");

    private final String value;

    ObjectStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
