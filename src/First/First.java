package First;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class First {
    public static void main(String[] args) throws IOException {
        final List<String> input = Files.readAllLines(Paths.get("src/First/input.txt"));

        int currentSum = 0;
        int maxSum = 0;
        for (String calories : input) {
            if (calories.isBlank()) {
                if (currentSum > maxSum) {
                    maxSum = currentSum;
                }
                currentSum = 0;
            } else {
                currentSum += Integer.parseInt(calories);
            }
        }
        if (currentSum > maxSum) {
            maxSum = currentSum;
        }
        System.out.println("The max calories is "  + maxSum);
    }
}
