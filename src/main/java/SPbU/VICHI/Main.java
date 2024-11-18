package SPbU.VICHI;

import SPbU.VICHI.matrix.AbstractMatrix;
import SPbU.VICHI.matrix.Vector;

import java.io.PrintStream;

public class Main {
    private static PrintStream printStream = new PrintStream(System.out);

    public static void main(String[] args) {
        AbstractMatrix matrix = new AbstractMatrix(3, 3);
        matrix.setElement(0, 0, 1.0);
        matrix.setElement(0, 1, 2.0);
        matrix.setElement(0, 2, 3.0);
        matrix.setElement(1, 0, 4.0);
        matrix.setElement(1, 1, 5.0);
        matrix.setElement(1, 2, 6.0);
        matrix.setElement(2, 0, 7.0);
        matrix.setElement(2, 1, 8.0);
        matrix.setElement(2, 2, 10.0);

        printStream.println("Matrix:");
        printStream.println(matrix);
        printStream.println(matrix.getInverse());
        Vector a = new Vector(3);
    }
}
