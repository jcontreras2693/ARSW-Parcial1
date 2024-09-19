package edu.eci.arsw.math;

import java.util.ArrayList;

public class PiThread extends Thread{
    private int DigitsPerSum;
    private double Epsilon;
    private int start;
    private int count;
    private byte[] digits;
    private Object lock;
    private boolean keepRunning = true;

    public PiThread(int start, int count, double Epsilon, int DigitsPerSum, Object lock){
        this.start = start;
        this.count = count;
        this.Epsilon = Epsilon;
        this.DigitsPerSum =  DigitsPerSum;
        this.lock = lock;
    }

    public void run(){
        while (keepRunning){
            //System.out.println("running");
            digits = new byte[count];
            double sum = 0;

            for (int i = 0; i < count; i++) {
                if (i % DigitsPerSum == 0) {
                    sum = 4 * sum(1, start)
                            - 2 * sum(4, start)
                            - sum(5, start)
                            - sum(6, start);

                    start += DigitsPerSum;
                }

                sum = 16 * (sum - Math.floor(sum));
                digits[i] = (byte) sum;
            }
            keepRunning = false;
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
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
    private int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }

    public void stopCalculating(){
        keepRunning = false;
    }

    public byte[] getDigits(){
        return  digits;
    }
}
