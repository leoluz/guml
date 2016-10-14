package com.guml.app

import com.guml.domain.Diagram
import com.guml.domain.DiagramRepository
import net.sourceforge.plantuml.SourceStringReader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
@EnableScheduling
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

    public Optional<Diagram> getDiagram(String id) {
        return diagramRepository.findById(id);
    }

    public byte[] buildImage(Diagram diagram) {
        log.info("Generating diagram image from DSL for {}", diagram);
        OutputStream os = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(diagram.getDsl());
        reader.generateImage(os);
        return os.toByteArray();
    }

}
