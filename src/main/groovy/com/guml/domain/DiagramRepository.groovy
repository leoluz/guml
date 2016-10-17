package com.guml.domain

public interface DiagramRepository {

    Optional<Diagram> findById(String id);

    void update();

}
