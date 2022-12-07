package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileStructure {

    public static void main(String[] args) throws IOException {
        final Directory fileSystem = constructFileSystem();
        final int totalSize = getDirectoriesUnder100000(fileSystem).stream().mapToInt(Directory::getSize).sum();

        System.out.println("The response is " + totalSize);

        int spaceToReclaim = 30000000 - (70000000 - fileSystem.getSize());
        final Directory directory = getDirectoriesReclaimingEnoughSpace(fileSystem, spaceToReclaim).stream()
                .min(Comparator.comparingInt(Directory::getSize)).get();

        System.out.println("The response is " + directory.getSize());

    }

    private static Set<Directory> getDirectoriesReclaimingEnoughSpace(Directory directory, int spaceToReclaim) {
        Deque<Directory> queue = new ArrayDeque<>();
        Set<Directory> selectedDirectories = new HashSet<>();
        queue.add(directory);
        while (!queue.isEmpty()) {
            Directory processedDirectory = queue.pop();
            if (processedDirectory.getSize() >= spaceToReclaim) {
                selectedDirectories.add(processedDirectory);
            }
            queue.addAll(processedDirectory.directories);
        }
        return selectedDirectories;
    }

    private static Set<Directory> getDirectoriesUnder100000(Directory directory) {
        Deque<Directory> queue = new ArrayDeque<>();
        Set<Directory> selectedDirectories = new HashSet<>();
        queue.add(directory);
        while (!queue.isEmpty()) {
            Directory processedDirectory = queue.pop();
            if (processedDirectory.getSize() <= 100000) {
                selectedDirectories.add(processedDirectory);
            }
            queue.addAll(processedDirectory.directories);
        }
        return selectedDirectories;
    }

    private static Directory constructFileSystem() throws IOException {
        final List<String> input = Files.readAllLines(Paths.get("src/day7/input.txt"));
        Directory currentDirectory = new Directory("");
        Stack<Directory> directoryHistory = new Stack<>();

        Pattern listPattern = Pattern.compile("\\$ ls");
        Pattern cdPattern = Pattern.compile("\\$ cd (.*)");
        Pattern fileListingPattern = Pattern.compile("(\\d*) (.*)");
        Pattern directoryListingPattern = Pattern.compile("dir (.*)");

        for (String line : input) {
            final Matcher cdPatternMatcher = cdPattern.matcher(line);
            if (cdPatternMatcher.matches()) {
                final String nextDirectory = cdPatternMatcher.group(1);
                if (nextDirectory.equals("..")) {
                    currentDirectory = directoryHistory.pop();
                } else if (nextDirectory.equals("/")){
                    while (!directoryHistory.isEmpty()) {
                        currentDirectory = directoryHistory.pop();
                    }
                } else {
                    if (currentDirectory.hasDirectory(nextDirectory)) {
                        directoryHistory.push(currentDirectory);
                        currentDirectory = currentDirectory.getDirectory(nextDirectory);

                    } else {
                        System.out.println("Trying to go to an unknown directory with line " + line);
                    }
                }

            } else {
                final Matcher fileListingPatternMatcher = fileListingPattern.matcher(line);
                final Matcher directoryListingPatternMatcher = directoryListingPattern.matcher(line);
                if (fileListingPatternMatcher.matches()) {
                    currentDirectory.add(new File(Integer.parseInt(fileListingPatternMatcher.group(1)), fileListingPatternMatcher.group(2)));
                } else if (directoryListingPatternMatcher.matches()) {
                    currentDirectory.add(new Directory(directoryListingPatternMatcher.group(1)));
                } else {
                    if (!listPattern.matcher(line).matches()) {
                        System.out.println("Error with line " + line);
                    }
                }
            }
        }
        while (!directoryHistory.isEmpty()) {
            currentDirectory = directoryHistory.pop();
        }
        return currentDirectory;
    }


    private static class Directory {
        List<Directory> directories;
        List<File> files;
        String name;

        public Directory(String name) {
            this.name = name;
            this.directories = new ArrayList<>();
            this.files = new ArrayList<>();
        }

        public boolean hasDirectory(String name) {
            return directories.stream().anyMatch(d -> d.name.equals(name));
        }

        public Directory getDirectory(String name) {
            return directories.stream().filter(d -> d.name.equals(name)).findFirst().get();
        }

        public void add(Directory directory) {
            directories.add(directory);
        }

        public void add(File file) {
            files.add(file);
        }

        public int getSize() {
            int directorySize = 0;
            for (File file : files) {
                directorySize += file.getSize();
            }
            for (Directory directory : directories) {
                directorySize += directory.getSize();
            }
            return directorySize;
        }

        @Override
        public String toString() {
            return "Directory{" +
                    "name='" + name + '\'' +
                    ", size='" + getSize() + '\'' +
                    '}';
        }
    }

    private static class File {
        String name;
        int size;

        public File(int size, String name) {
            this.name = name;
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        @Override
        public String toString() {
            return "File{" +
                    "name='" + name + '\'' +
                    ", size=" + size +
                    '}';
        }
    }
}
