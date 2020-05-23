package com.taf.automation.ui.support.testng;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.annotations.Parameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class TestParameterValidator implements IInvokedMethodListener {
    private static final String MSG = "Parameter %s for method %s in class %s was not found in test suite file!";

    @SuppressWarnings("java:S112")
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        Method testMethod = method.getTestMethod().getConstructorOrMethod().getMethod();
        Annotation paramAnnotation = testMethod.getAnnotation(Parameters.class);
        if (paramAnnotation != null && testResult.getTestContext() != null) {
            String[] params = ((Parameters) paramAnnotation).value();
            for (String param : params) {
                String value = testResult.getTestContext().getCurrentXmlTest().getParameter(param);
                if (value == null) {
                    String msg = String.format(MSG, param, testMethod.getName(), testMethod.getDeclaringClass().getName());
                    throw new RuntimeException(msg);
                }
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        //
    }

}
