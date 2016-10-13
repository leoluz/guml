package com.guml.app.repo;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GitService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Git cloneRepository(String remoteUri, String localDirectory, String branch) throws GitAPIException, IOException {
        File directory = initializeEmptyLocalDir(localDirectory);
        return Git.cloneRepository()
                .setURI(remoteUri)
                .setDirectory(directory)
                .setCloneAllBranches(true)
                .setBranch(branch)
                .call();
    }

    private File initializeEmptyLocalDir(String localDirectory) throws IOException {
        File localDir = new File(localDirectory);
        if (localDir.exists()) {
            FileUtils.delete(localDir, FileUtils.RECURSIVE);
            log.warn("Local directory cleared: {}", localDir.getAbsolutePath());
        }
        return localDir;
    }

}

// TODO -- Introduce a domain model
// TODO -- Handle exceptions
// TODO -- Expose a bean for the repo
