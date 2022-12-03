package day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RuckSackArrangement {

    public static void main(String[] args) throws IOException {
        final int sum = Files.readAllLines(Paths.get("src/day3/input.txt")).stream()
                .map(RuckSack::new)
                .map(RuckSack::getPriority)
                .mapToInt(Integer::intValue).sum();

        System.out.println("The priority is " + sum);
    }

    private static class RuckSack{
        final Compartment compartment1, compartment2;

        public RuckSack(String content) {
            compartment1 = new Compartment(content.substring(0, content.length() / 2));
            compartment2 = new Compartment(content.substring(content.length() / 2));
        }

        private Character getDuplicatedCharacter() {
            return compartment1.getCommonCharacter(compartment2);
        }

        public int getPriority() {
            final char duplicatedCharacter = getDuplicatedCharacter();
            if (duplicatedCharacter >= 97) {
                return duplicatedCharacter - 97 + 1;
            } else {
                return duplicatedCharacter - 65 + 27;
            }
        }
    }

    private static class Compartment {
        final List<Character> content = new ArrayList<>();

        public Compartment(String content) {
            for (char c : content.toCharArray()) {
                this.content.add(c);
            }
        }

        public Character getCommonCharacter(Compartment otherCompartment) {
            final Optional<Character> first = content.stream()
                    .filter(otherCompartment.content::contains)
                    .findFirst();
            if (first.isPresent()) {
                return first.get();
            } else {
                System.out.println("Could not find a common element");
                return '!';
            }
        }
    }
}
