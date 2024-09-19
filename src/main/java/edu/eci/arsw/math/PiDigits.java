package edu.eci.arsw.math;

import java.util.ArrayList;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;

    private static ArrayList<PiThread> threads =  new ArrayList<PiThread>();
    private static Object lock =  new Object();
    private static byte[] digits;

    
    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count, int numThreads) {

        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }
        int range;
        if (count < numThreads){
            range = 1;
            for (int i = 0; i < count; i++){
                threads.add(new PiThread(start, range, Epsilon, DigitsPerSum, lock));
                threads.get(i).start();
                start += range;

            }
        }
        else{
            range = count / numThreads;
            for (int i = 0; i < numThreads; i++){
                if((count % numThreads != 0) && (i + 1 == numThreads)){
                    threads.add(new PiThread(start, (range + count % numThreads), Epsilon, DigitsPerSum, lock));
                    threads.get(i).start();
                    break;
                }
                threads.add(new PiThread(start, range, Epsilon, DigitsPerSum, lock));
                threads.get(i).start();
                start += range;
            }
        }
        digits = new byte[count];
        for (int i = 0; i < threads.size(); i++){
            try{
                PiThread thread = threads.get(i);
                thread.join();
                digits[i] = (byte) thread.getDigits()[i % range];
            }catch (Exception e){
                throw new RuntimeException(e);
            }

        }
        return digits;
    }
}
