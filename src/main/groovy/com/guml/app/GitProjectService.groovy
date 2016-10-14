package com.guml.app

import com.guml.infra.GitRepository
import com.guml.infra.Revision
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

@Service
@EnableScheduling
public class GitProjectService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value('${git.repo.remote}')
    private String gitRemoteRepoUrl

    @Value('${git.branch}')
    private String gitBranch

    private GitRepository gitRepository;

    @PostConstruct
    private void init() {
        gitRepository = GitRepository.cloneRemote(gitRemoteRepoUrl, gitBranch)
        log.info("Cloned remote repository {}.", gitRemoteRepoUrl)
    }

    @Scheduled(fixedRateString = '${git.fetch.rate}')
    public void updateGitRepo() {
        log.info("Refreshing git repository...")
        gitRepository.refresh()
    }

    public String getDiagramDsl(String diagramId) {
        try {
            getDiagramFile(diagramId).text
        } catch (FileNotFoundException e) {
            throw new DiagramNotFoundException("Diagram with id ${diagramId} not found!", e)
        }
    }

    public List<Revision> getDiagramHistory(String diagramId) {
        String diagramPath = getDiagramFile(diagramId).getCanonicalPath()
                .replace(gitRepository.localRepository.canonicalPath + "/", "")
        return gitRepository.getHistory(diagramPath)
    }

    private File getDiagramFile(String diagramId) {
        File localDirectory = gitRepository.getLocalRepository()
        String diagramPath = "${localDirectory.absolutePath}/dsl/"
        diagramPath += "${diagramId.replace('-', '/').toLowerCase()}.puml"
        log.info("Getting dsl from path: ${diagramPath}")
        new File(diagramPath)
    }


}
