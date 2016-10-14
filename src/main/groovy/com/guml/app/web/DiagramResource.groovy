package com.guml.app.web

import com.guml.app.GitProjectService
import com.guml.domain.Diagram
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE
import static org.springframework.web.bind.annotation.RequestMethod.GET

@RestController
@RequestMapping("/api/diagrams")
class DiagramResource {

    @Autowired
    GitProjectService projectService

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(method = GET, value = "/{diagramId}" )
    def getDiagram(@PathVariable diagramId, @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader) {
        def body
        def headers = new HttpHeaders()
        if (acceptHeader == APPLICATION_JSON_VALUE) {
            body = getDiagramJson(diagramId)
            headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
        } else {
            body = getDiagramImage(diagramId)
            headers.add(HttpHeaders.CONTENT_TYPE, IMAGE_PNG_VALUE)
        }
        new ResponseEntity<>(body, headers, HttpStatus.OK)
    }

    @RequestMapping(method = GET, value = "/{diagramId}", produces=APPLICATION_JSON_VALUE)
    def getDiagramJson(@PathVariable diagramId) {
        def diagram = new Diagram()
        diagram.id = diagramId
        diagram.name = "Some cool diagram!"
        diagram
    }

    @RequestMapping(method = GET, value = "/{diagramId}", produces=IMAGE_PNG_VALUE)
    def getDiagramImage(@PathVariable diagramId) {
        String diagramDsl = projectService.getDiagramDsl(diagramId)
        Diagram diagram = new Diagram()
        diagram.dsl = diagramDsl
        diagram.buildImage()
    }
}
