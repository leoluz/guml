package com.guml.app

import com.guml.domain.DiagramRepository
import com.guml.domain.GitDiagramRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class ApplicationConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    public DiagramRepository setupRepository(
            @Value('${git.repo.remote}') String gitRemoteRepoUrl,
            @Value('${git.branch}') String gitBranch,
            @Value('${git.rootDir}') String diagramsRootDirectory) {
        DiagramRepository repo = GitDiagramRepository.cloneRemote(gitRemoteRepoUrl, gitBranch, diagramsRootDirectory);
        log.info("Cloned remote repository {}.", gitRemoteRepoUrl)
        return repo;
    }

}
