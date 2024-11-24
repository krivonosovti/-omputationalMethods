package SPbU.VICHI;

import SPbU.VICHI.seven.first.sevenFirstTest;

import java.io.PrintStream;

public class Main {
    private static PrintStream printStream = new PrintStream(System.out);

    public static void main(String[] args) {
        sevenFirstTest testObj = new sevenFirstTest();
        testObj.test(System.out);
    }
}
