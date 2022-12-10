package day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Decoding {
    private static final int NUM_DISTINCT_CHARACTERS = 14;

    public static void main(String[] args) throws IOException {
        final List<Character> input = Files.readAllLines(Paths.get("src/day6/input.txt"))
                .get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        int index = 0;
        while(!areLastXCharactersDifferent(input, index)) {
            index++;
        }
        if (index + NUM_DISTINCT_CHARACTERS < input.size()) {
            System.out.println((index + NUM_DISTINCT_CHARACTERS) + " characters need to be read");
        } else {
            System.out.println("Error");
        }
    }

    private static boolean areLastXCharactersDifferent(List<Character> input, int currentIndex) {
        return input.subList(currentIndex, currentIndex + Decoding.NUM_DISTINCT_CHARACTERS).stream()
                .distinct()
                .count() == Decoding.NUM_DISTINCT_CHARACTERS;
    }
}
