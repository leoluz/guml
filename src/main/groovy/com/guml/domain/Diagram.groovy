package com.guml.domain

import net.sourceforge.plantuml.SourceStringReader

public class Diagram {

    private final String id;
    private final String dsl;
    private final List<Revision> history;

    public Diagram(String id, String dsl, List<Revision> history) {
        this.id = id;
        this.dsl = dsl;
        this.history = history;
    }

    public String getId() {
        return id
    }

    public String getDsl() {
        return dsl
    }

    public List<Revision> getHistory() {
        return history
    }

    public byte[] buildImage() {
        OutputStream os = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(dsl);
        reader.generateImage(os);
        return os.toByteArray();
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Diagram diagram = (Diagram) o

        if (dsl != diagram.dsl) return false
        if (history != diagram.history) return false
        if (id != diagram.id) return false

        return true
    }

    int hashCode() {
        int result
        result = id.hashCode()
        result = 31 * result + (dsl != null ? dsl.hashCode() : 0)
        result = 31 * result + (history != null ? history.hashCode() : 0)
        return result
    }

    @Override
    public String toString() {
        return id;
    }

}
