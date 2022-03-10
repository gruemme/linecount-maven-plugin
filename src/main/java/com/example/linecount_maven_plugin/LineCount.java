package com.example.linecount_maven_plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "linecount")
public class LineCount extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;
    @Parameter(defaultValue = "linecount.txt")
    private String outputFileName;

    public void execute() throws MojoFailureException {
        List<Path> javaFiles = getAllJavaFilesInProject();
        long numberOfLines = countLinesOfFiles(javaFiles);
        getLog().info("Counted " + numberOfLines + " of source code");
        writeToFile(numberOfLines);
    }

    private List<Path> getAllJavaFilesInProject() throws MojoFailureException {
        try {
            return Files.walk(Paths.get(project.getBasedir().getAbsolutePath()))
                    .filter(p -> !Files.isDirectory(p))
                    .filter(p -> p.toString().toLowerCase().endsWith("java"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new MojoFailureException("Cannot walk though dir", e);
        }
    }

    private long countLinesOfFiles(List<Path> files) {
        return files.stream()
                .mapToLong(this::countLinesOfFile)
                .sum();
    }

    private long countLinesOfFile(Path path) {
        try {
            return Files.lines(path, StandardCharsets.UTF_8).count();
        } catch (IOException e) {
            getLog().error(e.getCause());
        }

        return 0;
    }

    private void writeToFile(long numberOfLines) throws MojoFailureException {
        String fileName = project.getBuild().getOutputDirectory() + File.separator + outputFileName;
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
            printWriter.print("Project: " + project.getArtifactId());
            printWriter.print(System.lineSeparator());
            printWriter.print("Date: " + new Date());
            printWriter.print(System.lineSeparator());
            printWriter.print("Number of java source code lines: ");
            printWriter.print(numberOfLines);
            printWriter.print(System.lineSeparator());
            printWriter.close();
        } catch (IOException e) {
            throw new MojoFailureException("Can not create FileWriter", e);
        }
        getLog().info("Wrote result to file: " + fileName);
    }
}
