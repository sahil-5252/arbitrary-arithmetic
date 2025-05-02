import arbitraryarithmetic.AFloat;
import arbitraryarithmetic.AInteger;

public class MyInfArith {
    public static void main(String[] args) {
        // Error handling in case of user entering wrong format
        if (args.length != 4) {
            System.out.println("Usage: java MyInfArith <int/float> <add/sub/mul/div> <num1> <num2>");
            return;
        }

        // 4 command line arguments
        String type = args[0];
        String operation = args[1];
        String input1 = args[2];
        String input2 = args[3];

        if (type.equals("int")) {
            AInteger num1 = new AInteger(input1);
            AInteger num2 = new AInteger(input2);
            AInteger result;

            switch (operation) {
                case "add" -> result = AInteger.sum(num1, num2);
                case "sub" -> result = AInteger.subtract(num1, num2);
                case "mul" -> result = AInteger.multiply(num1, num2);
                case "div" -> result = AInteger.divide(num1, num2);
                default -> {
                    System.out.println("Unknown operation; try again");
                    return;
                }
            }
            System.out.println(result);
        } else if (type.equals("float")) {
            AFloat num1 = new AFloat(input1);
            AFloat num2 = new AFloat(input2);
            AFloat result;

            switch (operation) {
                case "add" -> result = AFloat.add(num1, num2);
                case "sub" -> result = AFloat.subtract(num1, num2);
                case "mul" -> result = AFloat.multiply(num1, num2);
                case "div" -> result = AFloat.divide(num1, num2);
                default -> {
                    System.out.println("Unknown operation");
                    return;
                }
            }
            System.out.println(result);
        } else {
            System.out.println("Unknown type (use 'int' or 'float')");
        }
    }
}
