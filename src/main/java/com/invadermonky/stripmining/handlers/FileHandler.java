package com.invadermonky.stripmining.handlers;

import com.invadermonky.stripmining.StripMining;
import com.invadermonky.stripmining.util.LogHelper;
import net.minecraftforge.fml.common.Loader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;

public class FileHandler {
    /**
     * Copies all resource files found in a resource folder within the jar to an external location.
     *
     * @param source the string path to the resource folder using the jar as a root
     * @param target the string path to for the copy target location
     */
    public static void copyFromJar(String source, final Path target) throws URISyntaxException, IOException {

        URI resourceUri = StripMining.class.getResource("").toURI();
        FileSystem fileSystem = FileSystems.newFileSystem(resourceUri, Collections.emptyMap());

        final Path jarPath = fileSystem.getPath(source);

        Files.walkFileTree(jarPath, new SimpleFileVisitor<Path>() {
            private Path currentTarget;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                currentTarget = target.resolve(jarPath.relativize(dir).toString());
                Files.createDirectories(currentTarget);
                return FileVisitResult.CONTINUE;
            }

            @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, target.resolve(jarPath.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
        });

        fileSystem.close();
    }

    /**
     * Walks through the specified toolFolder config folder and reads all .json file contents.
     *
     * @param toolFolder the config tool folder
     * @return map containing all json file names and file contents
     */
    public static HashMap<String,String> getToolConfigs(String toolFolder) {
        HashMap<String,String> fileContents = new HashMap<>();
        Path configPath = Paths.get(Loader.instance().getConfigDir().getAbsolutePath(), StripMining.MOD_ID, toolFolder);
        try {
            Files.walkFileTree(configPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if(dir.toFile().isDirectory() && !dir.equals(configPath))
                        return FileVisitResult.SKIP_SUBTREE;
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    final String name = file.getFileName().toString();
                    if (name.toLowerCase(java.util.Locale.ROOT).endsWith(".json")) {
                        fileContents.put(file.getFileName().toString(), getFileContents(file));
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            LogHelper.debug(e);
        }

        return fileContents;
    }

    /**
     * Reads a passed file and returns the contents as a string.
     *
     * @param path the path of the file to read
     * @return string containing the file contents
     */
    public static String getFileContents(Path path) throws IOException {
        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
