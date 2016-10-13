package com.guml.app;

import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.guml.app.repo.GitService;

@Component
public class GitRepoInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${git.repo.remote}")
    private String gitRemoteRepoUrl;

    @Value("${git.repo.local_dir}")
    private String gitLocalRepoDir;

    @Value("${git.branch}")
    private String gitBranch;

    @Autowired
    private GitService gitService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            Git repo = gitService.cloneRepository(gitRemoteRepoUrl, gitLocalRepoDir, gitBranch);
            log.info("Git repository cloned at {}.", gitLocalRepoDir);
        } catch (GitAPIException | IOException e) {
            log.error("Cannot initialize git repository.", e);
        }
    }

}
