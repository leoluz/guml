package com.guml.domain.repository

import com.guml.domain.Diagram

public interface DiagramRepository {

    Optional<Diagram> findById(String id);

    void update();

}
