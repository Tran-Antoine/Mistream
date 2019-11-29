package net.akami.mistream.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;
public class ProbabilityLawTest {

    @Test
    public void hundred_percents_probability() {

        ProbabilityLaw<String> test = new ProbabilityLaw<>();
        test.add("success", 1f);
        test.add("fail", 0f);

        for(int i = 0; i < 10; i++) {
            Assertions.assertThat(test.draw()).isEqualTo("success");
        }
    }

    @Test
    public void random_probability() {

        ProbabilityLaw<String> test = new ProbabilityLaw<>();
        test.add("success", 0.15f);
        test.add("fail", 0.3f);
        test.add("other", 0.55f);

        float s = 0;
        float f = 0;
        float o = 0;

        float n = 50000;
        for(int i = 0; i < n; i++) {
            switch (test.draw()) {
                case "success":
                    s++;
                    break;
                case "fail":
                    f++;
                    break;
                default:
                    o++;
            }
        }
        // You should approximately find 0.15, 0.3 and 0.55
        System.out.println("Success : " + s / n);
        System.out.println("Fails   : " + f / n);
        System.out.println("Other   : " + o / n);
    }
}
