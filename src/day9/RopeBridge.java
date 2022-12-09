package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RopeBridge {

    public static void main(String[] args) throws IOException {
        final List<Bridge.Move> moves = Files.readAllLines(Paths.get("src/day9/input.txt")).stream()
                .map(RopeBridge::toMove)
                .collect(Collectors.toList());

        final Set<Bridge.Node> distinctTailNodes = new Bridge().run(moves).distinctTailNodes;
        final int numPositionsVisited = distinctTailNodes.size();
        System.out.println(numPositionsVisited + " positions were visited.");
    }

    private static Bridge.Move toMove(String line) {
        final String direction = line.split(" ")[0];
        final int numSteps = Integer.parseInt(line.split(" ")[1]);
        switch (direction) {
            case "U": return new Bridge.Move(numSteps, Bridge.Direction.Up);
            case "D": return new Bridge.Move(numSteps, Bridge.Direction.Down);
            case "L": return new Bridge.Move(numSteps, Bridge.Direction.Left);
            case "R": return new Bridge.Move(numSteps, Bridge.Direction.Right);
            default:
                throw new IllegalArgumentException("Invalid input : " + line);
        }
    }

    public static class Bridge {
        Node head, tail;
        Set<Node> distinctTailNodes = new HashSet<>();
        List<Node> rope = new ArrayList<>();

        public Bridge() {
            head = new Node(0, 0);
            tail = new Node(0, 0);

            for (int i = 0 ; i < 10 ; i++) {
                rope.add(new Node(0, 0));
            }
        }

        public Bridge run(List<Move> moves) {
            moves.forEach(this::run);
            return this;
        }

        private void run(Move move) {
            for (int step = 0 ; step < move.numSteps ; step++) {
                Node newHeadNode = new Node(0, 0);
                final Node currentHeadNode = rope.get(0);
                switch (move.direction) {
                    case Up: newHeadNode = new Node(currentHeadNode.x, currentHeadNode.y - 1); break;
                    case Down: newHeadNode = new Node(currentHeadNode.x, currentHeadNode.y + 1); break;
                    case Left: newHeadNode = new Node(currentHeadNode.x - 1, currentHeadNode.y); break;
                    case Right: newHeadNode = new Node(currentHeadNode.x + 1, currentHeadNode.y); break;
                }
                rope.remove(0);
                rope.add(0, newHeadNode);
                for (int nodeIndex = 1 ; nodeIndex < rope.size() ; nodeIndex++) {
                    final Node node = rope.remove(nodeIndex);
                    rope.add(nodeIndex, node.getTailForHead(rope.get(nodeIndex-1)));
                }
                distinctTailNodes.add(rope.get(rope.size() - 1));

            }
        }

        private enum Direction{
            Up, Down, Left, Right
        };

        private static class Move {
            final int numSteps;
            final Direction direction;

            public Move(int numSteps, Direction direction) {
                this.numSteps = numSteps;
                this.direction = direction;
            }
        }

        private static class Node {
            final int x, y;
            public Node(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public boolean isAdjacent(Node node) {
                return (Math.abs(x - node.x) <= 1) && (Math.abs(y - node.y) <= 1);
            }

            public Node getTailForHead(Node headNode) {
                if(!isAdjacent(headNode)) {
                    int newTailNodeX = Integer.MAX_VALUE, newTailNodeY = Integer.MAX_VALUE;

                    if (x - headNode.x > 1) {
                        newTailNodeX = x - 1;
                    } else if (headNode.x - x > 1) {
                        newTailNodeX = x + 1;
                    }

                    if (y - headNode.y > 1) {
                        newTailNodeY = y - 1;
                    } else if (headNode.y - y > 1) {
                        newTailNodeY = y + 1;
                    }

                    if (newTailNodeX == Integer.MAX_VALUE) {
                        newTailNodeX = headNode.x;
                    }
                    if (newTailNodeY == Integer.MAX_VALUE) {
                        newTailNodeY = headNode.y;
                    }

                    final Node newTailNode = new Node(newTailNodeX, newTailNodeY);
                    if (!isAdjacent(newTailNode)) {
                        System.out.println("Error finding tail for " + headNode + ". " + newTailNode + " is incorrect");
                    }
                    return newTailNode;
                } else {
                    return this;
                }
            }

            @Override
            public String toString() {
                return x + "," + y;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Node node = (Node) o;

                if (x != node.x) return false;
                if (y != node.y) return false;

                return true;
            }

            @Override
            public int hashCode() {
                int result = x;
                result = 31 * result + y;
                return result;
            }
        }
    }
}
