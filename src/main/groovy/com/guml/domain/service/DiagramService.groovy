package com.guml.domain.service

import com.guml.domain.Diagram
import com.guml.domain.repository.DiagramRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@EnableScheduling
@Component
public class DiagramService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final DiagramRepository diagramRepository;

    @Autowired
    public DiagramService(DiagramRepository diagramRepository) {
        this.diagramRepository = diagramRepository
    }

    @Scheduled(fixedRateString = '${git.fetch.rate}')
    public void updateGitRepo() {
        log.info("Updating diagram repository...")
        diagramRepository.update();
    }

    public Diagram getDiagram(String id) {
        Optional<Diagram> diagram = diagramRepository.findById(id);
        if (diagram.isPresent()) {
            diagram.get()
        } else {
            throw new DiagramNotFoundException("Diagram with id ${id} not found!")
        }
    }
}
