package First;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class First {
    public static void main(String[] args) throws IOException {
        final List<String> input = Files.readAllLines(Paths.get("src/First/input.txt"));

        int currentSum = 0;
        List<Integer> maxSums = new ArrayList<>();
        maxSums.add(0);
        maxSums.add(0);
        maxSums.add(0);
        for (String calories : input) {
            if (calories.isBlank()) {
                updateMaxes(currentSum, maxSums);
                currentSum = 0;
            } else {
                currentSum += Integer.parseInt(calories);
            }
        }
        updateMaxes(currentSum, maxSums);

        System.out.println("The max calories is "  + maxSums.stream().mapToInt(Integer::intValue).sum());
    }

    private static void updateMaxes(int currentSum, List<Integer> maxSums) {
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        int minMax = maxSums.stream().min(Integer::compareTo).get();
        if (currentSum > minMax) {
            maxSums.remove((Integer) minMax);
            maxSums.add(currentSum);
        }
    }
}
