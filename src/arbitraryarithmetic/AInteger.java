package arbitraryarithmetic;

import java.util.Arrays;

public class AInteger {
    int arr[];
    boolean isNegative = false;
    static boolean condition;

    public AInteger() {
        this.arr = new int[] { 0 };
    }

    public AInteger(String s) {
        this.arr = new int[(s.startsWith("-") ? s.length()-1 : s.length() )];

        if (s.startsWith("-")) {
            isNegative = true;
            s = s.substring(1);
        }
        String ch[] = s.split("");

        for (int i = 0; i < s.length(); i++) {
            this.arr[i] = Integer.parseInt(String.valueOf(ch[i]));
        }
    }

    // copy constructor
    public AInteger(AInteger other) {
        this.arr = new int[other.arr.length];
        System.arraycopy(other.arr, 0, this.arr, 0, other.arr.length);
    }

    // returns parsed AInteger instance
    public static AInteger parse(String s) {
        return new AInteger(s);
    }

    // function to add two instances of AInteger class
    public static AInteger addAInteger(AInteger num1, AInteger num2) {
        int i = num1.arr.length - 1, j = num2.arr.length - 1, carry = 0;

        int maxLen = Math.max(num1.arr.length, num2.arr.length);
        int sum_arr[] = new int[maxLen + 1];
        int k = 0;

        while (i >= 0 || j >= 0) {
            int digit1 = (i >= 0) ? num1.arr[i] : 0;
            int digit2 = (j >= 0) ? num2.arr[j] : 0;
            int sum = digit1 + digit2 + carry;
            sum_arr[k] = sum % 10;
            carry = sum / 10;
            i--;
            j--;
            k++;
        }
        sum_arr[k] = carry;

        // reverse array
        int zero_pos = sum_arr.length - 1;
        int newLength = sum_arr.length;
        while (sum_arr[zero_pos] == 0) {
            if (zero_pos > 0) {
                newLength--;
                zero_pos--;
                continue;
            }
            break;
        }
        int rev_array[] = new int[newLength];
        for (int m = 0; m < newLength; m++) {
            rev_array[newLength - m - 1] = sum_arr[m];
        }
        sum_arr = rev_array;
        AInteger sum = new AInteger();
        sum.arr = sum_arr;
        return sum;
    }

    static AInteger subtractAInteger(AInteger num1, AInteger num2) {
        // int i=num1.arr.length-1, j=num2.arr.length-1, borrow=0;

        // int maxLen = Math.max(num1.arr.length, num2.arr.length);
        condition = (num1.arr.length > num2.arr.length);
        if (num1.arr.length == num2.arr.length) {
            condition = num1.arr[0] > num2.arr[0];
            int idx = 0;
            while (num1.arr[idx] == num2.arr[idx] && idx < num1.arr.length - 1) {
                // condition = num1.arr[idx] > num2.arr[idx];
                idx++;
            }
            idx = (idx == num1.arr.length) ? idx - 1 : idx;
            condition = num1.arr[idx] > num2.arr[idx];
        }
        AInteger largeNum = (condition) ? num1 : num2;
        AInteger smallNum = (condition) ? num2 : num1;

        int diff_arr[] = new int[largeNum.arr.length];
        int i = largeNum.arr.length - 1, j = smallNum.arr.length - 1, borrow = 0;
        int k = 0;

        while (i >= 0 || j >= 0) {
            // int digit1 = (i>=0) ? largeNum.arr[i] : 0;
            int digit1 = largeNum.arr[i];
            int digit2 = (j >= 0) ? smallNum.arr[j] : 0;
            int diff = 10 + digit1 - borrow - digit2;
            diff_arr[k] = diff % 10;
            borrow = (digit1 - borrow < digit2) ? 1 : 0;
            i--;
            j--;
            k++;
        }
        // reverse array
        int zero_pos = diff_arr.length - 1;
        int newLength = diff_arr.length;
        while (diff_arr[zero_pos] == 0) {
            if (zero_pos > 0) {
                newLength--;
                zero_pos--;
                continue;
            }
            break;
        }

        int rev_array[] = new int[newLength];
        for (int m = 0; m < newLength; m++) {
            rev_array[newLength - m - 1] = diff_arr[m];
        }
        diff_arr = rev_array;
        AInteger diff = new AInteger();
        diff.arr = diff_arr;
        return diff;
    }

    public static AInteger multiply(AInteger num1, AInteger num2) {
        int len1 = num1.arr.length;
        int len2 = num2.arr.length;
        int[] result = new int[len1 + len2];
    
        for (int i = len1 - 1; i >= 0; i--) {
            for (int j = len2 - 1; j >= 0; j--) {
                int mul = num1.arr[i] * num2.arr[j];
                int p1 = i + j;
                int p2 = i + j + 1;
    
                int sum = mul + result[p2];
                result[p2] = sum % 10;
                result[p1] += sum / 10;
            }
        }
    
        // Remove leading zeros
        int start = 0;
        while (start < result.length - 1 && result[start] == 0) {
            start++;
        }
    
        int[] cleanResult = Arrays.copyOfRange(result, start, result.length);
        AInteger product = new AInteger();
        product.arr = cleanResult;
        product.isNegative = num1.isNegative ^ num2.isNegative;
    
        return product;
    }

    public static AInteger divide(AInteger dividend, AInteger divisor) {
        if (divisor.arr.length == 1 && divisor.arr[0] == 0) {
            throw new ArithmeticException("Error: division by zero");
        }
        boolean b1 = dividend.isNegative;
        boolean b2 = divisor.isNegative;
        dividend.isNegative = false;
        divisor.isNegative = false;
        AInteger result = new AInteger();
        result.arr = new int[dividend.arr.length];
        AInteger current = new AInteger("0");
        int dividendArr[] = dividend.arr;
        int idx = 0;
        for (int digit:dividendArr) {
            // Shift left and add digit
            current = multiply(current, new AInteger("10"));
            current = sum(current, new AInteger(String.valueOf(digit)));
            int count = 0;
            while (subtract(current, divisor).isNegative == false) {
                current = subtract(current, divisor);
                count++;
            }
            result.arr[idx++] = count;
        }
        // Remove leading zeros
        int start = 0;
        while (start < result.arr.length - 1 && result.arr[start] == 0) {
            start++;
        }
    
        result.arr = Arrays.copyOfRange(result.arr, start, result.arr.length);
        result.isNegative = b1 ^ b2;
    
        return result;
    }

    public static AInteger sum(AInteger num1, AInteger num2) {
        AInteger result = new AInteger();
        boolean firstNegative = num1.isNegative;
        boolean secondNegative = num2.isNegative;
        if (!firstNegative && !secondNegative) {
            return addAInteger(num1, num2);
        }
        else if (!firstNegative && secondNegative) {
            // check if num1 > num2
            int temp_array[] = subtractAInteger(num1, num2).arr;
            if (temp_array[0] == 0) {
                return new AInteger("0");
            }
            result.arr = new int[temp_array.length];
            if (!condition) {
                result.isNegative = true;
            }
            System.arraycopy(temp_array, 0, result.arr, 0, temp_array.length);
        }
        else if (firstNegative && !secondNegative) {
            AInteger first = new AInteger();
            first.arr = new int[num1.arr.length];
            first.isNegative = true;
            System.arraycopy(num1.arr, 0, first.arr, 0, num1.arr.length);
            num1 = first;
            // check if num1 > num2
            int temp_array[] = subtractAInteger(num1, num2).arr;
            if (temp_array[0] == 0) {
                return new AInteger("0");
            }
            result.arr = new int[temp_array.length];
            if (condition) {
                result.isNegative = true;
            }
            System.arraycopy(temp_array, 0, result.arr, 0, temp_array.length);
        }
        if (firstNegative && secondNegative) {
            AInteger sum = AInteger.addAInteger(num1, num2);
            result.arr = new int[sum.arr.length];
            result.isNegative = true;
            System.arraycopy(sum.arr, 0, result.arr, 0, sum.arr.length);
        }
        return result;
    }

    public static AInteger subtract(AInteger num1, AInteger num2) {
        AInteger result = new AInteger();
        boolean firstNegative = num1.isNegative;
        boolean secondNegative = num2.isNegative;
        if (!firstNegative && secondNegative) {
            return addAInteger(num1, num2);
        } else if (!firstNegative && !secondNegative) {
            // check if num1 > num2
            int temp_array[] = subtractAInteger(num1, num2).arr;
            if (temp_array[0] == 0) {
                return new AInteger("0");
            }
            result.arr = new int[temp_array.length];
            if (!condition) {
                result.isNegative = true;
            }
            System.arraycopy(temp_array, 0, result.arr, 0, temp_array.length);
        } else if (firstNegative && secondNegative) {
            // check if num1 > num2
            int temp_array[] = subtractAInteger(num1, num2).arr;
            if (temp_array[0] == 0) {
                return new AInteger("0");
            }
            result.arr = new int[temp_array.length];
            if (condition) {
                result.isNegative = true;
            }
            System.arraycopy(temp_array, 0, result.arr, 0, temp_array.length);
        }
        if (firstNegative && !secondNegative) {
            AInteger sum = AInteger.addAInteger(num1, num2);
            result.arr = new int[sum.arr.length];
            result.isNegative = true;
            System.arraycopy(sum.arr, 0, result.arr, 0, sum.arr.length);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(isNegative ? "-" : "");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        AInteger a = new AInteger("2");
        AInteger b = new AInteger("0");
        AInteger diffInt = divide(a, b);
        System.out.println(diffInt);
    }
}