package com.guml.infra;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;

public class GitRepository {

    private final Repository gitRepo;

    private GitRepository(Repository gitRepo) {
        this.gitRepo = gitRepo;
    }

    public void refresh() {
        try {
            Git git = new Git(gitRepo);
            git.pull().setStrategy(MergeStrategy.THEIRS).call();
        } catch (GitAPIException e) {
            throw new GitException("Cannot pull from remote.", e);
        }
    }

    public static GitRepository cloneRemote(String remoteUri, String branch) {
        try {
            File localDirectory = Files.createTempDirectory("gumlrepo").toFile();
            Repository gitRepo = Git.cloneRepository()
                    .setURI(remoteUri)
                    .setDirectory(localDirectory)
                    .setCloneAllBranches(true)
                    .setBranch(branch)
                    .call()
                    .getRepository();
            return new GitRepository(gitRepo);
        } catch (IOException | GitAPIException e) {
            throw new GitException("Cannot clone remote git repository.", e);
        }
    }

    public static GitRepository readLocal(String localPath) {
        Repository gitRepo = null;
        try {
            gitRepo = Git.open(new File(localPath)).getRepository();
            return new GitRepository(gitRepo);
        } catch (IOException e) {
            throw new GitException("Cannot read local git repository.", e);
        }
    }

}
