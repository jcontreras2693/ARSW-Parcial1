package edu.eci.arsw.math;

public class PiThread extends Thread {
    private int digitsPerSum;
    private double epsilon;
    private int start;
    private int count;
    private byte[] digits;
    private Object lock;
    private int calculated = 0;

    public PiThread(int start, int count, double epsilon, int digitsPerSum, Object lock) {
        this.start = start;
        this.count = count;
        this.epsilon = epsilon;
        this.digitsPerSum = digitsPerSum;
        this.lock = lock;
    }

    @Override
    public void run() {
        digits = new byte[count];
        double sum = 0;

        for (int i = 0; i < count; i++) {
            if (i % digitsPerSum == 0) {
                sum = 4 * sum(1, start) - 2 * sum(4, start) - sum(5, start) - sum(6, start);
                start += digitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
            calculated++;
        }
    }

    private double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    private int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result = (result * 16) % m;
                p -= power;
            }
            power /= 2;
            if (power > 0) {
                result = (result * result) % m;
            }
        }

        return result;
    }

    public byte[] getDigits() {
        return digits;
    }

    public int calculatedDigits() {
        return calculated;
    }
}
