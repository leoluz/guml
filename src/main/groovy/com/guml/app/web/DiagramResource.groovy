package com.guml.app.web

import com.guml.domain.Diagram
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.GET

@RestController
@RequestMapping("/api/diagrams")
class DiagramResource {

    @RequestMapping(method = GET, value = "/{diagramId}")
    def getDiagram(@PathVariable diagramId) {
        def diagram = new Diagram()
        diagram.id = diagramId
        diagram.name = "Some cool diagram!"
        return diagram
    }
}
