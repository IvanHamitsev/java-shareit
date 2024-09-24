package ru.practicum.shareit.booking.model;

public enum BookingStatusType {
    WAITING("WAITING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    CANCELED("CANCELED");

    private final String title;

    BookingStatusType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static BookingStatusType fromString(String value) {
        if (value.equals("WAITING")) return WAITING;
        if (value.equals("APPROVED")) return APPROVED;
        if (value.equals("REJECTED")) return REJECTED;
        if (value.equals("CANCELED")) return CANCELED;
        throw new IllegalArgumentException("Некорректный аргумент " + value);
    }
}
