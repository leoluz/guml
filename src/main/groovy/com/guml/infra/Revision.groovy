package com.guml.infra

import java.time.LocalDate

public class Revision {

    private final String id;
    private final String author;
    private final LocalDate date;

    public Revision(String id, String author, LocalDate date) {
        this.id = id
        this.author = author
        this.date = date
    }

    String getId() {
        return id
    }

    String getAuthor() {
        return author
    }

    LocalDate getDate() {
        return date
    }

    @Override
    public String toString() {
        return String.format("%s by %s", id.substring(0, 6), author);
    }

}
