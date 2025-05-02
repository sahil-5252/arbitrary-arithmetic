package arbitraryarithmetic;

import java.util.Arrays;

public class AFloat {
    int[] arr;
    boolean isNegative = false;
    int decimalDigits;
    boolean isDivision = false;
    static boolean condition;

    public AFloat() {
        this.arr = new int[] { 0 };
        this.decimalDigits = 0;
    }

    public AFloat(String s) {
        if (s.startsWith("-")) {
            isNegative = true;
            s = s.substring(1);
        }

        String[] parts = s.split("\\.");
        String intPart = parts[0];
        String fracPart = (parts.length > 1) ? parts[1] : "";

        this.decimalDigits = fracPart.length();
        String full = intPart + fracPart;
        this.arr = new int[full.length()];
        for (int i = 0; i < full.length(); i++) {
            this.arr[i] = Integer.parseInt(String.valueOf(full.charAt(i))); // returns the correct int from string
        }
    }

    public static AFloat parse(String s) {
        return new AFloat(s);
    }

    // align two AFloat instances by decimal places
    private static void alignDecimals(AFloat a, AFloat b) {
        int diff = Math.abs(a.decimalDigits - b.decimalDigits);
        if (a.decimalDigits < b.decimalDigits) {
            a.padRight(diff);
        } else if (b.decimalDigits < a.decimalDigits) {
            b.padRight(diff);
        }
    }

    private void padRight(int n) {
        this.decimalDigits += n;
        this.arr = Arrays.copyOf(this.arr, this.arr.length + n);
    }

    private void padLeft(int n) {
        int[] newArr = new int[this.arr.length + n];
        System.arraycopy(this.arr, 0, newArr, n, this.arr.length);
        this.arr = newArr;
    }

    private static AFloat addAFloat(AFloat a, AFloat b) {
        alignDecimals(a, b);
        int maxLength = Math.max(a.arr.length, b.arr.length);
        a.padLeft(maxLength - a.arr.length);
        b.padLeft(maxLength - b.arr.length);

        int[] res = new int[maxLength + 1];
        int carry = 0;
        for (int i = maxLength - 1; i >= 0; i--) {
            int sum = a.arr[i] + b.arr[i] + carry;
            res[i + 1] = sum % 10;
            carry = sum / 10;
        }
        res[0] = carry;

        AFloat result = new AFloat();
        result.arr = (carry == 0) ? Arrays.copyOfRange(res, 1, res.length) : res;
        result.decimalDigits = a.decimalDigits; // same after alignment
        return result;
    }

    public static AFloat add(AFloat a, AFloat b) {
        AFloat result;
        if (!a.isNegative && !b.isNegative) {
            return addAFloat(a, b);
        }
        if (!a.isNegative && b.isNegative) {
            result = subtractAFloat(a, b);
            if (!condition) {
                result.isNegative = true;
            }
            return result;
        }
        if (a.isNegative && !b.isNegative) {
            result = subtractAFloat(a, b);
            if (condition) {
                result.isNegative = true;
            }
            return result;
        }
        if (a.isNegative && b.isNegative) {
            result = addAFloat(a, b);
            result.isNegative = true;
            return result;
        }
        // Handle mixed signs as needed
        return null;
    }

    public static AFloat subtract(AFloat a, AFloat b) {
        AFloat result;
        if (!a.isNegative && !b.isNegative) {
            result = subtractAFloat(a, b);
            if (!condition) {
                result.isNegative = true;
            }
            return result;
        }
        if (!a.isNegative && b.isNegative) {
            result = addAFloat(a, b);
            return result;
        }
        if (a.isNegative && !b.isNegative) {
            result = addAFloat(a, b);
            result.isNegative = true;
            return result;
        }
        if (a.isNegative && b.isNegative) {
            result = subtractAFloat(a, b);
            if (condition) {
                result.isNegative = true;
            }
            return result;
        }
        // Handle mixed signs as needed
        return null;
    }

    // Subtraction, Multiplication and Division for AFloat class

    public static AFloat subtractAFloat(AFloat num1, AFloat num2) {
        condition = ((num1.arr.length - num1.decimalDigits) > (num2.arr.length - num2.decimalDigits));
        if (num1.arr.length - num1.decimalDigits == num2.arr.length - num2.decimalDigits) {
            condition = num1.arr[0] > num2.arr[0];
            int idx = 0;
            while (num1.arr[idx] == num2.arr[idx] && idx < num1.arr.length - 1) {
                idx++;
            }
            idx = (idx == num1.arr.length) ? idx - 1 : idx;
            condition = num1.arr[idx] > num2.arr[idx];
        }
        AFloat largeNum = (condition) ? num1 : num2;
        AFloat smallNum = (condition) ? num2 : num1;

        int commonDecimal = Math.max(largeNum.decimalDigits, smallNum.decimalDigits);

        // Normalize both numbers to the same decimal scale
        AFloat n1 = normalizeDecimal(largeNum, commonDecimal);
        AFloat n2 = normalizeDecimal(smallNum, commonDecimal);

        // Use AInteger subtraction logic
        AInteger a1 = new AInteger();
        AInteger a2 = new AInteger();
        a1.arr = n1.arr;
        a2.arr = n2.arr;
        a1.isNegative = false;
        a2.isNegative = false;
        AInteger result = AInteger.subtract(a1, a2);

        AFloat output = new AFloat("0.0");
        output.arr = result.arr;
        output.isNegative = false; // gives absolute difference
        output.decimalDigits = commonDecimal;
        return output;
    }

    public static AFloat multiply(AFloat num1, AFloat num2) {
        int totalDecimal = num1.decimalDigits + num2.decimalDigits;

        AInteger a1 = new AInteger(num1.getDigitString());
        AInteger a2 = new AInteger(num2.getDigitString());
        AInteger product = AInteger.multiply(a1, a2);

        AFloat output = new AFloat("0.0");
        output.arr = product.arr;
        output.isNegative = (num1.isNegative && !num2.isNegative) || (!num1.isNegative && num2.isNegative);
        if ("0".equals(product.toString())) output.isNegative = false;
        output.decimalDigits = totalDecimal;
        return output;
    }

    public static AFloat divide(AFloat num1, AFloat num2) {
        if (num2.arr.length == 1 && num2.arr[0] == 0) {
            throw new ArithmeticException("Error: division by zero");
        }
        int precision = 30; 
    
        int scale = precision + num2.decimalDigits - num1.decimalDigits;
        AInteger a1 = new AInteger(num1.getDigitString());
        for (int i = 0; i < scale; i++) {
            a1 = AInteger.multiply(a1, new AInteger("10"));
        }
        AInteger a2 = new AInteger(num2.getDigitString());
    
        AInteger quotient = AInteger.divide(a1, a2);
    
        AFloat result = new AFloat("0.0");
        result.arr = quotient.arr;
        result.isNegative = num1.isNegative ^ num2.isNegative;
        if (result.arr.length == 1 && result.arr[0] == 0) result.isNegative = false;
        result.decimalDigits = precision;
    
        return result;
    }    

    private static AFloat normalizeDecimal(AFloat num, int decimalPlaces) {
        AFloat result = num;
        int diff = decimalPlaces - num.decimalDigits;
        for (int i = 0; i < diff; i++) {
            result = multiply(result, new AFloat("10"));
        }
        result.decimalDigits = decimalPlaces;
        return result;
    }

    private String getDigitString() {
        StringBuilder sb = new StringBuilder();
        for (int digit : this.arr)
            sb.append(Math.abs(digit));
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(isNegative ? "-" : "");
        for (int i = 0; i < arr.length; i++) {
            if (i == arr.length - decimalDigits)
                sb.append(".");
            sb.append(arr[i]);
        }
        String output = sb.toString();
        if (isDivision) {
            output = output.substring(0, arr.length-decimalDigits);
        }
        return output;
    }

    public static void main(String[] args) {
        AFloat num1 = new AFloat("-2");
        AFloat num2 = new AFloat("400");
        AFloat result = multiply(num1, num2);
        System.out.println(result);
    }
}