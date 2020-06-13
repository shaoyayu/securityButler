package icu.shaoyayu.android.security.butler;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Integer[] s = new Integer[]{50,50,50,50};
        double p = (100/s.length)*0.01;
        System.out.println(p);
        double x = 0;
        for (Integer integer : s) {
            x = x +integer*p;
            System.out.println(x);
        }
    }
}