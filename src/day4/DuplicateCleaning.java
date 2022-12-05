package day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DuplicateCleaning {

    public static void main(String[] args) throws IOException {
        final long count = Files.readAllLines(Paths.get("src/day4/input.txt")).stream()
                .map(Pair::new)
                .filter(Pair::overlap)
                .count();

        System.out.println(count + " pairs overlap");
    }

    private static class Pair {
        final Assignment assignment1, assignment2;
        public Pair(String pair) {
            assignment1 = new Assignment(pair.split(",")[0]);
            assignment2 = new Assignment(pair.split(",")[1]);
        }

        public boolean containUnnecessaryAssignment() {
            return assignment1.contain(assignment2) || assignment2.contain(assignment1);
        }

        public boolean overlap() {
            return assignment1.overlap(assignment2);
        }

    }

    private static class Assignment {
        final int start, end;
        public Assignment(String assignment) {
            start = Integer.parseInt(assignment.split("-")[0]);
            end = Integer.parseInt(assignment.split("-")[1]);
        }

        public boolean contain(Assignment assignment2) {
            return this.start <= assignment2.start && this.end >= assignment2.end;
        }

        public boolean overlap(Assignment assignment2) {
            return this.start <= assignment2.end && this.end >= assignment2.start;
        }
    }
}
