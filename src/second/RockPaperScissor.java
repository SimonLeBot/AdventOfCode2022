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
        final int scorePoints;

        public Game(String theirShape, String result) {
            this.theirShape = getShape(theirShape);
            switch (result) {
                case "X":
                    scorePoints = 0; break;
                case "Y":
                    scorePoints = 3; break;
                case "Z":
                    scorePoints = 6; break;
                default: throw new IllegalArgumentException("Incorrect input");
            }
            this.myShape = getShapeToAchieveResult();
        }

        private Shape getShapeToAchieveResult() {
            switch (theirShape) {
                case Rock:
                    switch (scorePoints) {
                        case 6: return Shape.Paper;
                        case 3: return Shape.Rock;
                        case 0: return Shape.Scissor;
                    }
                case Paper:
                    switch (scorePoints) {
                        case 6: return Shape.Scissor;
                        case 3: return Shape.Paper;
                        case 0: return Shape.Rock;
                    }
                case Scissor:
                    switch (scorePoints) {
                        case 6: return Shape.Rock;
                        case 3: return Shape.Scissor;
                        case 0: return Shape.Paper;
                    }
            }
            throw new IllegalArgumentException("Incorrect input");
        }

        public int getNumPoints() {
            return scorePoints + myShape.numPoints;
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
