package edu.ualberta.med.biobank.test;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public class BaseTest {

    private static final Random R = new Random();

    public String testName;

    protected String getMethodName() {
        return testName;
    }

    protected String getMethodNameR() {
        return testName + R.nextInt();
    }

    protected static Random getR() {
        return R;
    }

    @BeforeEach
    public void setup(TestInfo testInfo) {
        Optional<Method> testMethod = testInfo.getTestMethod();
        if (testMethod.isPresent()) {
            this.testName = testMethod.get().getName();
        }
    }
}
