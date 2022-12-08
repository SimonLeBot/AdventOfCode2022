package day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TreetpTreeHouse {
    public static void main(String[] args) throws IOException {
        final Forest forest = new Forest(Files.readAllLines(Paths.get("src/day8/input.txt")));
        System.out.println("There are "  + forest.getNumVisibleTrees() + " trees visible from outside");
    }

    public static class Forest {
        final List<List<Tree>> trees;

        public Forest(List<String> input) {
            trees = input.stream()
                    .map(line -> Arrays.stream(line.split(""))
                            .map(s -> new Tree(Integer.parseInt(s)))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
        }

        public int getNumVisibleTrees() {
            int numVisibleTrees = 0;
            for (int rowIndex = 0; rowIndex < getNumRows() ;rowIndex++) {
                numVisibleTrees += numVisibleTreeOnLine(getRow(rowIndex));
            }
            for (int rowIndex = getNumRows() - 1; rowIndex >=0 ;rowIndex--) {
                final List<Tree> row = new ArrayList<>(getRow(rowIndex));
                Collections.reverse(row);
                numVisibleTrees += numVisibleTreeOnLine(row);
            }

            for (int columnIndex = 0; columnIndex < getNumColumns() ;columnIndex++) {
                numVisibleTrees += numVisibleTreeOnLine(getColumn(columnIndex));
            }
            for (int columnIndex = getNumColumns() - 1; columnIndex >= 0  ;columnIndex--) {
                final List<Tree> column = new ArrayList<>(getColumn(columnIndex));
                Collections.reverse(column);
                numVisibleTrees += numVisibleTreeOnLine(column);
            }

            return numVisibleTrees;
        }

        private int numVisibleTreeOnLine(List<Tree> line) {
            int highestTreeSize = -1;
            int numVisibleTrees = 0;
            for (Tree tree : line){
                if (tree.height > highestTreeSize) {
                    highestTreeSize = tree.height;
                    if (!tree.hasBeenVisited) {
                        numVisibleTrees++;
                        tree.hasBeenVisited = true;
                    }
                }
            }
            return numVisibleTrees;
        }

        private List<Tree> getRow(int rowIndex) {
            return trees.get(rowIndex);
        }

        private List<Tree> getColumn(int columnIndex) {
            return trees.stream().map(r -> r.get(columnIndex))
                    .collect(Collectors.toList());
        }

        private int getNumRows() {
            return trees.size();
        }

        private int getNumColumns() {
            return trees.get(0).size();
        }
    }

    private static class Tree {
        final int height;
        boolean hasBeenVisited;
        public Tree(int height) {
            this.height = height;
            hasBeenVisited = false;
        }

        @Override
        public String toString() {
            return String.valueOf(height);
        }
    }
}
