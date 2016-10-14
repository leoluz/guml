package com.guml.infra

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.merge.MergeStrategy

import java.nio.file.Files
import java.time.Instant
import java.time.ZoneId

public class GitRepository {

    private final Repository gitRepo

    private GitRepository(Repository gitRepo) {
        this.gitRepo = gitRepo
    }

    public void refresh() {
        try {
            Git git = new Git(gitRepo);
            git.pull().setStrategy(MergeStrategy.THEIRS).call()
        } catch (GitAPIException e) {
            throw new GitException("Cannot pull from remote.", e)
        }
    }

    public File getLocalRepository() {
       gitRepo.directory.parentFile
    }

    public List<Revision> getHistory(String path) {
        List<Revision> history;
        try {
            history = new Git(gitRepo).log().addPath(path).call().collect({ revCommit ->
                new Revision(revCommit.name, revCommit.authorIdent.name, Instant.ofEpochSecond(revCommit.commitTime).atZone(ZoneId.systemDefault()).toLocalDate())
            })
            return history
        } catch (GitAPIException e) {
            throw new GitException("Failed to retrieve file history.", e)
        }
    }

    public static GitRepository cloneRemote(String remoteUri, String branch) {
        try {
            File localDirectory = Files.createTempDirectory("umlrepo").toFile()
            localDirectory.deleteOnExit()
            Repository gitRepo = Git.cloneRepository()
                    .setURI(remoteUri)
                    .setDirectory(localDirectory)
                    .setCloneAllBranches(true)
                    .setBranch(branch)
                    .call()
                    .getRepository();
            return new GitRepository(gitRepo)
        } catch (IOException | GitAPIException e) {
            throw new GitException("Cannot clone remote git repository.", e)
        }
    }
}
