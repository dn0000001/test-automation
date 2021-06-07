package com.taf.automation.ui.support.testng;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.TestNGException;
import org.testng.annotations.Parameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class TestParameterValidator implements IInvokedMethodListener {
    @SuppressWarnings("java:S112")
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        Method testMethod = method.getTestMethod().getConstructorOrMethod().getMethod();
        Annotation paramAnnotation = testMethod.getAnnotation(Parameters.class);
        if (paramAnnotation != null && testResult.getTestContext() != null && method.getTestMethod().isTest()) {
            String[] params = ((Parameters) paramAnnotation).value();
            for (String param : params) {
                String value = testResult.getTestContext().getCurrentXmlTest().getParameter(param);
                if (value == null) {
                    String xmlSuiteFileName = testResult.getTestContext().getSuite().getXmlSuite().getFileName();
                    String xmlSuiteInfo = xmlSuiteFileName != null ? "in " + xmlSuiteFileName : "";
                    throw new TestNGException("Parameter '" + param + "' is required by method " + testMethod.getName()
                            + " in class " + testMethod.getDeclaringClass().getName()
                            + " but has not been marked @Optional or defined\n"
                            + xmlSuiteInfo);
                }
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        //
    }

}
