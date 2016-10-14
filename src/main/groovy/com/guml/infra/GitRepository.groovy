package com.guml.infra

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.merge.MergeStrategy

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

    public static GitRepository cloneRemote(String remoteUri, String branch, File localDirectory) {
        try {
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
}
