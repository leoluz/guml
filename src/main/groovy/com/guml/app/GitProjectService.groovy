package com.guml.app;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.guml.infra.GitRepository;

@Service
@EnableScheduling
public class GitProjectService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value('${git.repo.remote}')
    private String gitRemoteRepoUrl;

    @Value('${git.branch}')
    private String gitBranch;

    private GitRepository gitRepository;

    @PostConstruct
    private void init() {
        gitRepository = GitRepository.cloneRemote(gitRemoteRepoUrl, gitBranch);
        log.info("Cloned remote repository {}.", gitRemoteRepoUrl);
    }

    @Scheduled(fixedRate = 10000L)
    public void updateGitRepo() {
        log.info("Refreshing git repository...");
        gitRepository.refresh();
    }

}
