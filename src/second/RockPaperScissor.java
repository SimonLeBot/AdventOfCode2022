package second;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RockPaperScissor {
    public static void main(String[] args) throws IOException {
        final int sum = Files.readAllLines(Path.of("src/second/input.txt")).stream()
                .map(game -> new Game(game.split(" ")[0], game.split(" ")[1]))
                .map(Game::getNumPoints)
                .mapToInt(Integer::intValue).sum();

        System.out.println("Total score is " + sum);
    }

    private static class Game {
        final Shape theirShape;
        final Shape myShape;

        public Game(String theirShape, String myShape) {
            this.theirShape = getShape(theirShape);
            this.myShape = getShape(myShape);
        }

        private int getScorePoints() {
            switch (myShape) {
                case Rock:
                    switch (theirShape) {
                        case Rock: return 3;
                        case Paper: return 0;
                        case Scissor: return 6;
                    }
                case Paper:
                    switch (theirShape) {
                        case Rock: return 6;
                        case Paper: return 3;
                        case Scissor: return 0;
                    }
                case Scissor:
                    switch (theirShape) {
                        case Rock: return 0;
                        case Paper: return 6;
                        case Scissor: return 3;
                    }
            }
            throw new IllegalArgumentException("Incorrect input");
        }

        public int getNumPoints() {
            return getScorePoints() + myShape.numPoints;
        }
    }

    private static Shape getShape(String shape) {
        switch (shape) {
            case "A":
            case "X" :
                return Shape.Rock;
            case "B":
            case "Y" :
                return Shape.Paper;
            case "C":
            case "Z" :
                return Shape.Scissor;
            default:
                throw new IllegalArgumentException(shape + " is not a valid input");
        }
    }

    private enum Shape {
        Rock(1),
        Paper(2),
        Scissor(3);

        final int numPoints;
        Shape(int numPoints) {
            this.numPoints = numPoints;
        }
    }
}
