package com.learn.zktutorial.helper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class FileHelper {

    private FileHelper() {
        // utility class
    }

    /**
     * Creates a file relative to the project root if it does not exist.
     * Example:
     *  input: "logs/events.log"
     *  creates: <project-root>/logs/events.log
     */
    public static void createFileIfNotCreated(String suffixFile) {
        try {
            File projectRoot = new File(System.getProperty("user.dir"));
            File targetFile = new File(projectRoot, suffixFile);

            if (!targetFile.exists()) {
                // Creates parent directories + file
                FileUtils.touch(targetFile);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to create file: " + suffixFile,
                    e
            );
        }
    }

    public static void appendToFile(String file, String content) {
        try {
            FileUtils.writeStringToFile(
                    new File(System.getProperty("user.dir"), file),
                    content,
                    StandardCharsets.UTF_8,
                    true
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeFile(String file, String content) {
        try {
            FileUtils.writeStringToFile(
                    new File(System.getProperty("user.dir"), file),
                    content,
                    StandardCharsets.UTF_8,
                    false
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
