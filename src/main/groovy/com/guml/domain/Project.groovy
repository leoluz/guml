package com.guml.domain;

import java.util.List;

public class Project {

    private final List<Diagram> diagrams;

    public Project(List<Diagram> diagrams) {
        this.diagrams = diagrams;
    }

    public List<Diagram> getDiagrams() {
        return diagrams;
    }

}
