package day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RuckSackArrangement {

    public static void main(String[] args) throws IOException {
        final List<String> strings = Files.readAllLines(Paths.get("src/day3/input.txt"));
        assert strings.size() % 3 == 0 : "All groups should be size 3";
        int counter = 0;
        List<Group> groups = new ArrayList<>();
        while (counter < strings.size()) {
            groups.add(new Group(strings.subList(counter, counter + 3)));
            counter += 3;
        }
        final int sum = groups.stream().map(Group::getBadge).mapToInt(Integer::intValue).sum();

        System.out.println("The priority is " + sum);
    }

    private static class Group{
        final RuckSack ruckSack1, ruckSack2, ruckSack3;

        public Group(List<String> contents) {
            assert contents.size() == 3 : "Groups must be composed of 3 rucksacks";
            ruckSack1 = new RuckSack(contents.get(0));
            ruckSack2 = new RuckSack(contents.get(1));
            ruckSack3 = new RuckSack(contents.get(2));
        }

        public int getBadge() {
            Set<Character> commonCharacters = ruckSack1.getCommonCharacters(ruckSack2.content);
            commonCharacters = ruckSack3.getCommonCharacters(commonCharacters);
            assert commonCharacters.size() == 1 : "Only one character should be common";
            return getPriority(commonCharacters.iterator().next());
        }
    }
    private static class RuckSack{
        final Set<Character> content = new HashSet<>();
        final Compartment compartment1, compartment2;

        public RuckSack(String content) {
            for (char c : content.toCharArray()) {
                this.content.add(c);
            }
            compartment1 = new Compartment(content.substring(0, content.length() / 2));
            compartment2 = new Compartment(content.substring(content.length() / 2));
        }

        private Character getDuplicatedCharacter() {
            return compartment1.getCommonCharacter(compartment2);
        }

        public Set<Character> getCommonCharacters(Set<Character> otherContent) {
            final HashSet<Character> commonCharacters = new HashSet<>(content);
            commonCharacters.retainAll(otherContent);
            return commonCharacters;
        }

    }

    private static class Compartment {
        final Set<Character> content = new HashSet<>();

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

    public static int getPriority(char character) {
        if (character >= 97) {
            return character - 97 + 1;
        } else {
            return character - 65 + 27;
        }
    }

}
