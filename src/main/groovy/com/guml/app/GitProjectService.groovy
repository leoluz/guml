package com.guml.app

import com.guml.infra.GitRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import java.nio.file.Files

@Service
@EnableScheduling
public class GitProjectService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value('${git.repo.remote}')
    private String gitRemoteRepoUrl

    @Value('${git.branch}')
    private String gitBranch

    File localDirectory

    private GitRepository gitRepository;

    @PostConstruct
    private void init() {
        localDirectory = Files.createTempDirectory("umlrepo").toFile()
        localDirectory.deleteOnExit()
        gitRepository = GitRepository.cloneRemote(gitRemoteRepoUrl, gitBranch, localDirectory)
        log.info("Cloned remote repository {}.", gitRemoteRepoUrl)
    }

    @Scheduled(fixedRateString = '${git.fetch.rate}')
    public void updateGitRepo() {
        log.info("Refreshing git repository...")
        gitRepository.refresh()
    }

    public String getDiagramDsl(String diagramId) {
        getDiagramFile(diagramId).text
    }

    private File getDiagramFile(String diagramId) {
        String diagramPath = "${localDirectory.absolutePath}/dsl/";
        diagramPath += "${diagramId.replace('-', '/').toLowerCase()}.puml"
        log.info("Getting dsl from path: ${diagramPath}")

        new File(diagramPath)
    }

}
