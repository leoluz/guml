package com.guml.app.web

import com.guml.app.DiagramNotFoundException
import com.guml.app.GitProjectService
import com.guml.domain.Diagram
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import static org.springframework.http.HttpHeaders.ACCEPT
import static org.springframework.http.HttpHeaders.CONTENT_TYPE
import static org.springframework.http.HttpStatus.NOT_FOUND
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
    def getDiagram(@PathVariable diagramId, @RequestHeader(ACCEPT) String acceptHeader) {
        log.info("GET /api/diagrams/${diagramId} Headers: ${ACCEPT}:${acceptHeader}")
        def body
        def headers = new HttpHeaders()
        if (acceptHeader == APPLICATION_JSON_VALUE) {
            body = getDiagramJson(diagramId)
            headers.add(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        } else {
            body = getDiagramImage(diagramId)
            headers.add(CONTENT_TYPE, IMAGE_PNG_VALUE)
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

    @ExceptionHandler(DiagramNotFoundException.class)
    def handleDiagramNotFound(Exception e) {
        new ResponseEntity<>(e.getMessage(), NOT_FOUND)
    }
}
