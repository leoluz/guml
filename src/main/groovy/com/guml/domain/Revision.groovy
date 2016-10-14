package com.guml.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

public class Revision {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final String id;
    private final String author;
    private final ZonedDateTime time;
    private final String timeString;
    private final String comment;

    public Revision(String id, String author, ZonedDateTime time) {
        this(id, author, time, "");
    }

    public Revision(String id, String author, ZonedDateTime time, String comment) {
        this.id = id;
        this.author = author;
        this.time = time;
        this.timeString = formatTime(this.time);
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public ZonedDateTime getTime() {
        return time;
    }

    public String getComment() {
        return comment;
    }

    @JsonIgnore
    public String getTimeString() {
        return timeString;
    }

    public boolean equals(o) {
        if (this.is(o)) return true;
        if (getClass() != o.class) return false;

        Revision that = (Revision) o;

        if (author != that.author) return false;
        if (comment != that.comment) return false;
        if (id != that.id) return false;
        if (time != that.time) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = id.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s by %s at %s", id.substring(0, 7), author, timeString);
    }

    private static String formatTime(ZonedDateTime time) {
        return time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

}
