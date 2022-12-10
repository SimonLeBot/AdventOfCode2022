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

        System.out.println(new Bridge(2).run(moves).distinctTailNodes.size() + " positions were visited with rope length 2.");
        System.out.println(new Bridge(10).run(moves).distinctTailNodes.size() + " positions were visited with rope length 10.");
    }

    private static Bridge.Move toMove(String line) {
        final String direction = line.split(" ")[0];
        final int numSteps = Integer.parseInt(line.split(" ")[1]);
        return switch (direction) {
            case "U" -> new Bridge.Move(numSteps, Bridge.Direction.Up);
            case "D" -> new Bridge.Move(numSteps, Bridge.Direction.Down);
            case "L" -> new Bridge.Move(numSteps, Bridge.Direction.Left);
            case "R" -> new Bridge.Move(numSteps, Bridge.Direction.Right);
            default -> throw new IllegalArgumentException("Invalid input : " + line);
        };
    }

    public static class Bridge {
        final Set<Node> distinctTailNodes = new HashSet<>();
        final List<Node> rope = new ArrayList<>();

        public Bridge(int ropeLength) {
            for (int i = 0 ; i < ropeLength ; i++) {
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
                newHeadNode = switch (move.direction) {
                    case Up -> new Node(currentHeadNode.x, currentHeadNode.y - 1);
                    case Down -> new Node(currentHeadNode.x, currentHeadNode.y + 1);
                    case Left -> new Node(currentHeadNode.x - 1, currentHeadNode.y);
                    case Right -> new Node(currentHeadNode.x + 1, currentHeadNode.y);
                };
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
        }

        private record Move(int numSteps, Direction direction) {
        }

        private record Node(int x, int y) {

            public boolean isNotAdjacent(Node node) {
                        return (Math.abs(x - node.x) > 1) || (Math.abs(y - node.y) > 1);
                    }

                    public Node getTailForHead(Node headNode) {
                        if (isNotAdjacent(headNode)) {
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
                            if (isNotAdjacent(newTailNode)) {
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
                        return y == node.y;
                    }

        }
    }
}
