package com.joyit.service;

import com.joyit.entity.FileUnit;
import com.joyit.util.Counter;
import com.joyit.util.PreviousString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.shuffle;

public class MixService {
    public void mix(String dirPathStr) {
        final Path dirPath = Path.of(dirPathStr);

        final ArrayList<FileUnit> fileUnits = new ArrayList<>();
        try (Stream<Path> files = Files.list(dirPath)) {
            PreviousString previousNum = new PreviousString("");
            files.forEach(path -> {
                final String fileName = path.getFileName().toString();
                final String primaryNum = fileName.substring(0, 3);
                if (isContinue(previousNum, primaryNum)) {
                    getLast(fileUnits).addFileName(path.toString());
                } else {
                    fileUnits.add(new FileUnit(path.toString()));
                    previousNum.setValue(primaryNum);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        shuffle(fileUnits);

        try {
            setOrderNames(fileUnits);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getPrimaryNum(Counter counter) {
        return format("%03d", counter.getCounter());
    }

    private String getSecondaryNum(Counter counter) {
        return format("%02d", counter.getCounter());
    }

    private boolean isContinue(PreviousString previousNum, String currentNum) {
        return previousNum.getValue().equals(currentNum);
    }

    private FileUnit getLast(ArrayList<FileUnit> fileUnits) {
        return fileUnits.get(fileUnits.size() - 1);
    }

    private void setOrderNames(ArrayList<FileUnit> fileUnits) throws IOException {
        final Counter primaryCounter = new Counter(1);
        final Counter secondaryCounter = new Counter(1);
        for (FileUnit fileUnit : fileUnits) {
            secondaryCounter.reset();
            for (String pathStr : fileUnit.getFileNames()) {
                final Path filePath = Path.of(pathStr);
                final Path parentPath = filePath.getParent();
                String cleanName = cleanTheName(filePath.getFileName().toString());
                String resultName = getPrimaryNum(primaryCounter) + "." + getSecondaryNum(secondaryCounter) + " " + cleanName;
                final Path resultPath = Path.of(parentPath.toString(), resultName);
                Files.move(filePath, resultPath);
                secondaryCounter.increase();
            }
            primaryCounter.increase();
        }
    }

    private String cleanTheName(String fileName) {
        return fileName.charAt(3) == '.'
                ? fileName.substring(7)
                : fileName;
    }
}
