package SPbU.VICHI;

import SPbU.VICHI.seven.first.sevenFirstTest;
import SPbU.VICHI.seven.second.sevenSecondTest;

import java.io.PrintStream;

public class Main {
    private static PrintStream printStream = new PrintStream(System.out);

    public static void main(String[] args) {
        Test testObj = new sevenSecondTest();
        testObj.test(System.out);
    }
}
