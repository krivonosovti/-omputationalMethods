package SPbU.VICHI.seven.first;

import SPbU.VICHI.Test;
import SPbU.VICHI.matrix.AbstractMatrix;
import SPbU.VICHI.matrix.SquareMatrix;
import SPbU.VICHI.matrix.Vector;

import java.io.PrintStream;
import java.security.SecureRandom;

public class sevenFirstTest implements Test {
    public void test (PrintStream printStream ) {

        int size = 50;


        SquareMatrix A = new SquareMatrix(size);
        for (int i = 1; i <= size; i++){
            for (int j = 1; j <= size; j++){
                A.setElement(i-1,j-1,1.0/(i+j-1.0));
            }
        }
        for (int i = 0; i < size; i++) {
            A.setElement(i, i, A.getElement(1,1)+0.0000001); //регуляризация 
        }
        AbstractMatrix tempX = new Vector(size);
        for (int j = 0; j < size; j++){
            tempX.setElement(j,0,1);
        }

        AbstractMatrix b = A.multiply(tempX);
        AbstractMatrix RES = new Vector(size);
        printStream.println(A);
//        printStream.println(b);
        SystemLinearEquations.GaussResult result = SystemLinearEquations.solve(A,b, SystemLinearEquations.PivotType.ROW);
        printStream.println((result.solution));
        printStream.println((result.solution).normMatrix(1));
        printStream.println(A.getCond(1));


        RES = (AbstractMatrix) (result.solution);
        RES = RES.multiply(-1);

        printStream.println(tempX.add(RES));
//        printStream.printf("Колл-во операций: %d%n", result.operationCount);
//
//        double exepect =  2.0/3.0*size*size*size;
//        printStream.println("ожидалось операций: "+ exepect);
//        printStream.printf("Определитель: " + result.determinant);

    }
    public void ttest (PrintStream printStream) {  //смотрю зависимость числа обусловлености и погрешность метода гаусса
        int size = 100;
        SecureRandom secureRandom = new SecureRandom();
        SquareMatrix A = new SquareMatrix(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                A.setElement(i, j, secureRandom.nextInt(1, 10));
            }
        }
        for (int i = 0; i < size; i++) {
            A.setElement(i, i, A.getElement(1,1)+0.0000001);
        }

        AbstractMatrix tempX = new Vector(size);
        for (int j = 0; j < size; j++){
            tempX.setElement(j,0,1);
        }

        AbstractMatrix b = A.multiply(tempX);

        AbstractMatrix RES = new Vector(size);
        printStream.println(A);
//        printStream.println(b);
        SystemLinearEquations.GaussResult result = SystemLinearEquations.solve(A,b, SystemLinearEquations.PivotType.ROW);
        printStream.println((result.solution));
        printStream.println((result.solution).normMatrix(1));
        printStream.println(A.getCond(1));

    }
}
