package ru.practicum.shareit.booking.dto;

public enum RequestType {
    ALL("ALL"),
    CURRENT("CURRENT"),
    PAST("PAST"),
    FUTURE("FUTURE"),
    WAITING("WAITING"),
    REJECTED("REJECTED");

    private final String title;

    RequestType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static RequestType fromString(String value) {
        if (value.equalsIgnoreCase("ALL")) return ALL;
        if (value.equalsIgnoreCase("CURRENT")) return CURRENT;
        if (value.equalsIgnoreCase("PAST")) return PAST;
        if (value.equalsIgnoreCase("FUTURE")) return FUTURE;
        if (value.equalsIgnoreCase("WAITING")) return WAITING;
        if (value.equalsIgnoreCase("REJECTED")) return REJECTED;
        throw new IllegalArgumentException("Incorrect state name " + value);
    }
    }
