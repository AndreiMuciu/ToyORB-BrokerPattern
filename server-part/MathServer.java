public class MathServer {
    public MathServer() {
        // Constructor
    }
    public int add(int a, int b) {
        return a + b;
    }
    public int subtract(int a, int b) {
        return a - b;
    }
    public int multiply(int a, int b) {
        return a * b;
    }
    public int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }
    public int power(int a, int b) {
        return (int) Math.pow(a, b);
    }
    public int factorial(int a) {
        if (a < 0) {
            throw new IllegalArgumentException("Negative number");
        }
        int result = 1;
        for (int i = 1; i <= a; i++) {
            result *= i;
        }
        return result;
    }
    public double sqrt(int a) {
        if (a < 0) {
            throw new IllegalArgumentException("Negative number");
        }
        return Math.sqrt(a);
    }
}
