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
        if (value.equals("ALL")) return ALL;
        if (value.equals("CURRENT")) return CURRENT;
        if (value.equals("PAST")) return PAST;
        if (value.equals("FUTURE")) return FUTURE;
        if (value.equals("WAITING")) return WAITING;
        if (value.equals("REJECTED")) return REJECTED;
        throw new IllegalArgumentException("Некорректный аргумент " + value);
    }
    }
