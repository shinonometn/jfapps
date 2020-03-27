package com.hyd.jfapps.launcher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JarScanner {

    private final static Logger log = LoggerFactory.getLogger(JarScanner.class);

    public static List<File> scanAppJars(String dir) {
        File file = new File(dir);
        if (!file.exists() || !file.isDirectory()) {
            return Collections.emptyList();
        }

        File[] files = file.listFiles(f -> f.isFile() && f.getName().endsWith(".jar"));
        if (files == null || files.length == 0) {
            return Collections.emptyList();
        }

        return Stream.of(files)
            .peek(f -> log.info("plugin found: {}", f.getName()))
            .collect(Collectors.toList());
    }
}