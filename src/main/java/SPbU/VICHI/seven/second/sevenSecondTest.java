package SPbU.VICHI.seven.second;

import SPbU.VICHI.Test;
import SPbU.VICHI.matrix.AbstractMatrix;
import SPbU.VICHI.matrix.SquareMatrix;
import SPbU.VICHI.matrix.Vector;
import SPbU.VICHI.seven.first.SystemLinearEquations;

import java.io.PrintStream;
import java.security.SecureRandom;

public class sevenSecondTest implements Test {

    int size = 5;
    public void test(PrintStream printStream) {
        testLUPDsolution(printStream);
    }
    private void testLU (PrintStream printStream) {
        //Провека LU разложения

        SquareMatrix A = new SquareMatrix(size);
        for (int i = 1; i <= size; i++){
            for (int j = 1; j <= size; j++){
                A.setElement(i-1,j-1,1.0/(i+j-1.0));
            }
        }
        printStream.println(A);

        LUDecomposition Adecomposition = new LUDecomposition(A);
        SquareMatrix U = Adecomposition.getU(), L = Adecomposition.getL();
//        printStream.println(L.multiply(U));
//        printStream.println(U);
//        printStream.println(L);
        AbstractMatrix temp = new Vector(size);
        temp = A.multiply(-1);

        printStream.println((L.multiply(U)).add(temp));


        //Решение СЛУ



    }
    private void testLUsolution (PrintStream printStream ) {
        SquareMatrix A = new SquareMatrix(size);
        createMatrix(A);
        AbstractMatrix tempX = new Vector(size);
        for (int j = 0; j < size; j++){
            tempX.setElement(j,0,1);
        }

        AbstractMatrix b = A.multiply(tempX);

        LUDecomposition Adecomposition = new LUDecomposition(A);
        SquareMatrix U = Adecomposition.getU(), L = Adecomposition.getL();

        printStream.println(A);
        printStream.println(A.getCond(1));
//        printStream.println(b);
//        printStream.println(tempX);
        printStream.println("L and U =");
        printStream.println(L);
        printStream.println(U);


        //LUx=b -> 1) Ly=b   2) Ux = y
        SystemLinearEquations.GaussResult y = SystemLinearEquations.solve(L,b, SystemLinearEquations.PivotType.COLUMN);
        AbstractMatrix Y = y.solution;
        printStream.println(Y);

        SystemLinearEquations.GaussResult x = SystemLinearEquations.solve(U,Y, SystemLinearEquations.PivotType.COLUMN);
        printStream.println(x.solution);
    }

    private void testLUPDsolution (PrintStream printStream ) {
        SquareMatrix A = new SquareMatrix(size);
        createMatrix(A);
        AbstractMatrix tempX = new Vector(size);
        for (int j = 0; j < size; j++){
            tempX.setElement(j,0,1);
        }

        AbstractMatrix b = A.multiply(tempX);

        LUPDecomposition Adecomposition = new LUPDecomposition(A);
        AbstractMatrix U = Adecomposition.getU(), L = Adecomposition.getL(), P = Adecomposition.getP();

        printStream.println(A);
        printStream.println(A.getCond(1));
//        printStream.println(b);
//        printStream.println(tempX);
        printStream.println("L and U =");
        printStream.println(L);
        printStream.println(U);
        printStream.println(P);

        printStream.println((P.multiply(A))); //тут какой-то косяк с инверсиями ( !
    }
    private void createMatrix(AbstractMatrix A) {
        SecureRandom random = new SecureRandom();
        int t;
        for (int i = 0; i < size; i++) {
           for (int j = 0; j < size; j++){
               t = random.nextInt(1,50);
               A.setElement(i,j,t);
           }
        }
    }
}
