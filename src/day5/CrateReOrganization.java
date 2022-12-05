package day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CrateReOrganization {

    public static void main(String[] args) throws IOException {
        final List<List<String>> stacks = initStacks();
        Files.readAllLines(Paths.get("src/day5/move_input.txt")).forEach(move -> {
            Pattern movePattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
            final Matcher matcher = movePattern.matcher(move);
            if (matcher.matches()) {
                moveBetweenStacks(stacks, Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2))-1, Integer.parseInt(matcher.group(3))-1);
            } else {
                throw new IllegalArgumentException("The input does not match the regex");
            }
        });

        System.out.println("The results is " + stacks.stream().map(stack -> stack.get(stack.size()-1)).collect(Collectors.joining()));

    }

    private static void moveBetweenStacks(List<List<String>> stacks, int numItems, int sourceStackIndex, int targetStackIndex) {
        List<String> sourceStack = stacks.get(sourceStackIndex);
        List<String> targetStack = stacks.get(targetStackIndex);
        int indexOfInterest = sourceStack.size() - numItems;
        for (int i = 0 ; i < numItems ; i++) {
            targetStack.add(sourceStack.remove(indexOfInterest));
        }
    }

    private static List<List<String>> initStacks() throws IOException {
        List<List<String>> stacks = new ArrayList<>();
        for (int i = 0; i < 9 ; i++) {
            stacks.add(new ArrayList<>());
        }

        final List<String> lines = Files.readAllLines(Paths.get("src/day5/crate_input.txt"));
        Collections.reverse(lines);
        lines.forEach(line -> {
            final List<String> lineContent = getLineContent(line);
            assert lineContent.size() == stacks.size() : "The size should match";
            for (int i = 0 ; i < lineContent.size() ; i++) {
                if (!lineContent.get(i).isEmpty()) {
                    stacks.get(i).add(lineContent.get(i));
                }
            }
        });

        return stacks;
    }

    private static List<String> getLineContent(String line) {
        Pattern linePattern = Pattern.compile("(....)(....)(....)(....)(....)(....)(....)(....)(...)");
        Matcher lineMatcher = linePattern.matcher(line);
        List<String> result = new ArrayList<>();
        if (lineMatcher.matches()) {
            for (int i = 0 ; i < lineMatcher.groupCount() ; i++) {
                final String group = lineMatcher.group(i + 1);
                Pattern contentPattern = Pattern.compile(".*(\\w).*");
                final Matcher contentMatcher = contentPattern.matcher(group);
                if (contentMatcher.matches()) {
                    result.add(contentMatcher.group(1));
                } else {
                    result.add("");
                }
            }
        } else {
            linePattern = Pattern.compile("(....)(....)(....)(....)(....)(....)(...)");
            lineMatcher = linePattern.matcher(line);
            if (lineMatcher.matches()) {
                for (int i = 0; i < lineMatcher.groupCount(); i++) {
                    final String group = lineMatcher.group(i + 1);
                    Pattern contentPattern = Pattern.compile(".*(\\w).*");
                    final Matcher contentMatcher = contentPattern.matcher(group);
                    if (contentMatcher.matches()) {
                        result.add(contentMatcher.group(1));
                    } else {
                        result.add("");
                    }
                }
            }
        }
        return result;
    }
}
