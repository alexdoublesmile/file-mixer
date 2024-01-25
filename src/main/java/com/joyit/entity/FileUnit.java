package com.joyit.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUnit {
    private final List<String> fileNames = new ArrayList<>();

    private FileUnit(String... files) {
        fileNames.addAll(Arrays.asList(files));
    }

    public FileUnit(String fileName) {
        fileNames.add(fileName);
    }

    public List<String> getFileNames() {
        return new ArrayList<>(fileNames);
    }

    public void addFileName(String fileName) {
        fileNames.add(fileName);
    }

    public static FileUnit getTestFileUnit(String... files) {
        return new FileUnit(files);
    }
}
