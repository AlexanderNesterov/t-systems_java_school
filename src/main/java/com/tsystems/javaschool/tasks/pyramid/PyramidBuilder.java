package com.tsystems.javaschool.tasks.pyramid;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        // TODO : Implement your solution here

        if (inputNumbers.contains(null)) {
            throw new CannotBuildPyramidException();
        }

        int iterationNumber = checkArray(inputNumbers);

        if (iterationNumber == -1) {
            throw new CannotBuildPyramidException();
        }

        int width = 2 * iterationNumber - 1;
        inputNumbers.sort(Comparator.naturalOrder());
        int[][] array = new int[iterationNumber][width];
        int index = 0, middle = width / 2;

        //System.out.println(iterationNumber + " " + middle);

        for (int i = 0; i < iterationNumber; i++) {
            for (int j = middle - i; j <= middle + i; j += 2) {
                array[i][j] = inputNumbers.get(index++);
            }
        }

/*        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }

            System.out.println();
        }*/

        return array;
    }

    private int checkArray(List<Integer> inputNumber) {
        int length = inputNumber.size();
        int iterationNumber = 0;
        long i = 1;
        int j = 2;

        while (i < length) {
            i += j++;
            iterationNumber++;
        }

        return i != length ? -1 : ++iterationNumber;
    }

/*    public static void main(String[] args) throws InterruptedException {
        new PyramidBuilder().buildPyramid(Arrays.asList(11, 1, 21, 12, 3, 16, 2, 13, 9, 4, 17, 5, 14, 10, 18, 8, 7, 19, 15, 6, 20));
    }*/
}
