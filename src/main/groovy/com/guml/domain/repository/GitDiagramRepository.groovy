package com.guml.domain.repository

import com.guml.domain.Diagram
import com.guml.domain.Revision
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.merge.MergeStrategy

import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

public class GitDiagramRepository implements DiagramRepository {

    private static final String PATH_SEPARATOR_REPLACEMENT = "-";
    private static final String DIAGRAM_DSL_FILE_EXTENSION = ".puml";

    private final Repository gitRepo;
    private final String diagramsRootDirectory;

    private GitDiagramRepository(Repository gitRepo, String diagramsRootDirectory) {
        this.gitRepo = gitRepo;
        this.diagramsRootDirectory = diagramsRootDirectory;
    }

    @Override
    public Optional<Diagram> findById(String id) {
        final String relativePath = id.replace(PATH_SEPARATOR_REPLACEMENT, File.separator) + DIAGRAM_DSL_FILE_EXTENSION;
        final File diagramFile = Paths.get(gitRepo.getDirectory().getParent(), diagramsRootDirectory, relativePath).toFile();
        if (diagramFile.exists()) {
            final List<Revision> diagramHistory = getHistory(Paths.get(diagramsRootDirectory, relativePath).toString())
            return Optional.of(new Diagram(id, diagramFile.getText(), diagramHistory))
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void update() {
        try {
            Git git = new Git(gitRepo);
            git.pull().setStrategy(MergeStrategy.THEIRS).call();
        } catch (GitAPIException e) {
            throw new RepositoryException("Cannot pull from remote.", e);
        }
    }

    public static GitDiagramRepository cloneRemote(String remoteUri, String branch, String diagramsRootDirectory) {
        try {
            File localDirectory = Files.createTempDirectory("umlrepo").toFile();
            localDirectory.deleteOnExit();
            Repository gitRepo = Git.cloneRepository()
                    .setURI(remoteUri)
                    .setDirectory(localDirectory)
                    .setCloneAllBranches(true)
                    .setBranch(branch)
                    .call()
                    .getRepository();
            return new GitDiagramRepository(gitRepo, diagramsRootDirectory);
        } catch (IOException | GitAPIException e) {
            throw new RepositoryException("Cannot clone remote git repository.", e);
        }
    }

    private List<Revision> getHistory(String path) {
        List<Revision> history;
        try {
            history = new Git(gitRepo).log().addPath(path).call().collect({ revCommit ->
                final ZoneOffset commitTimeZoneOffset = ZoneOffset.ofTotalSeconds(revCommit.getAuthorIdent().getTimeZoneOffset())
                final ZonedDateTime commitTime = Instant.ofEpochSecond(revCommit.commitTime).atOffset(commitTimeZoneOffset).toZonedDateTime()
                return new Revision(revCommit.name, revCommit.authorIdent.name, commitTime, revCommit.getShortMessage())
            })
            return history;
        } catch (GitAPIException e) {
            throw new RepositoryException("Failed to retrieve file history.", e);
        }
    }
}
