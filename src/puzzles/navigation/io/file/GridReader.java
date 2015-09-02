package puzzles.navigation.io.file;

import puzzles.navigation.GridState;
import puzzles.navigation.Position;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GridReader {

    private static final int DIMENSIONS = 0;
    private static final int START_AND_GOAL = 1;;

    public static GridState loadFile(File file) {
        char[][] grid = new char[0][0];
        int height = 0;
        int width = 0;
        int[] start = new int[0];
        int[] goal = new int[0];

        if (file != null) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                int counter = 0;
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] splitGroups = line.split("\\)");
                    if (counter == DIMENSIONS) {
                        int[] dimensions = getGroup(splitGroups[0]);
                        height = dimensions[0];
                        width = dimensions[1];
                        grid = new char[dimensions[0]][dimensions[1]];
                    } else if (counter == START_AND_GOAL) {
                        start = getGroup(splitGroups[0]);
                        goal = getGroup(splitGroups[1]);
                        grid[start[0]][start[1]] = GridState.START;
                        grid[goal[0]][goal[1]] = GridState.GOAL;
                    } else {
                        int[] barrier = getGroup(splitGroups[0]);
                        int barrierX = barrier[0];
                        int barrierY = barrier[1];
                        int barrierWidth = barrier[2];
                        int barrierHeight = barrier[3];
                        for (int i = barrierX; i < barrierX + barrierWidth; i++) {
                            for (int j = barrierY; j < barrierY + barrierHeight; j++) {
                                grid[i][j] = GridState.BARRIER;
                            }
                        }

                    }
                    counter++;
                }
            } catch (IOException io) {
                System.err.println("Failed to read file");
            }
            return new GridState(grid, height, width, new Position(start), new Position(goal), new Position(start));
        }
        return null;
    }

    public static int[] getGroup(String text) {
        int indexOfOpeningParenthesis = text.indexOf('(');
        String[] stringValues = text.substring(indexOfOpeningParenthesis + 1).split(",");
        int[] values = new int[stringValues.length];
        for (int i = 0; i < stringValues.length; i++) {
            values[i] = Integer.parseInt(stringValues[i]);
        }
        return values;
    }
}
