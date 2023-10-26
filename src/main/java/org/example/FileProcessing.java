package org.example;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Consumer;

public class FileProcessing {
    private static java.nio.charset.Charset fileCharset;
    public static List<String> readFile(Path path) throws IOException {
        List<String> fileContent;
        try {
            fileContent = Files.readAllLines(path, fileCharset/*Charset.forName("utf-8")*/);
        } catch(IOException e) {
            System.err.println("readFile:" + fileCharset.toString() + ":" + e.getMessage());
            fileCharset = Charset.forName("windows-1251");
            fileContent = Files.readAllLines(path, fileCharset);
        }
        return fileContent;
    }
    public static void writeFile(Path path, List<String> content) throws IOException {
        Files.write(path, content, fileCharset);
    }
    public void changeFileContent(Path path, Map<String, List<String>> params) throws IOException {
        Stream<String> fileContent;
        Stream<String> newFileContent;
        fileContent = readFile(path).stream();
        //fileContent.forEach(l -> System.out.println(l));
        if (params.get("s").size() > 1) {
            newFileContent = fileContent
                .map(l -> l.contains(params.get("s").get(0))
                        ? l.replace(params.get("s").get(0), params.get("s").get(1))
                        : l
                );
            //newFileContent.forEach(l -> System.out.println(l));
            writeFile(path, newFileContent.collect(Collectors.toList()));
        } else {
            fileContent
                .filter(l -> l.contains(params.get("s").get(0)))
                 .forEach(l -> System.out.println(path.getFileName() + "::" + l));
        }
    }
    public static List<Path> walkCatalog(Path path, List<String> maskList) throws IOException {
        try (Stream<Path> walk = Files.walk(path)) {
            return walk
                    .filter(Files::isRegularFile)
                    .filter(p -> maskList.stream().anyMatch(p.toString()::endsWith))
                    .collect(Collectors.toList());
        }
    }
    public List<String> normalizeMasks(List<String> masks) {
        return masks.stream()
                .flatMap(s -> Stream.of(s.split("\\s*;\\s*")))
                .map(s -> '.' + s.replace("*", "").replace(".", ""))
                .collect(Collectors.toList());
    }
    @FunctionalInterface
    public interface ThrowingConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }
    static <T, E extends Exception> Consumer<T> handlingConsumerWrapper(
        ThrowingConsumer<T, E> throwingConsumer, Class<E> exceptionClass) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = exceptionClass.cast(ex);
                    System.err.println(
                            "Exception point: " + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }
    public void run(@NotNull Map<String, List<String>> params) throws IOException {
        for (Map.Entry<String, List<String>> m : params.entrySet()) {
            System.out.println(m.getKey() + " " + m.getValue());
        }
        Path path = Paths.get(params.get("p").get(0)).normalize().toAbsolutePath();
        System.out.println(path);
        params.replace("m", normalizeMasks(params.get("m")));
        System.out.println(params.get("m"));
        List<Path> fileList;
        if (Files.isDirectory(path)) {
            try {
                fileList = walkCatalog(path, params.get("m"));
                fileList.forEach(
                    handlingConsumerWrapper(f -> changeFileContent(f, params), IOException.class)
                );
            } catch(IOException ex) {
                System.err.println(ex.getMessage());
            }
        } else {
            if (Files.exists(path) && Files.isRegularFile(path)) {
                Collections.singletonList(path).forEach(handlingConsumerWrapper(f -> changeFileContent(f, params), IOException.class));
            }
        }
    }
    FileProcessing() {
        this.fileCharset = StandardCharsets.UTF_8;
    }
}
