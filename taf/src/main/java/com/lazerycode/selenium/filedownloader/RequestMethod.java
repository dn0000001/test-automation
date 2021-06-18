package com.lazerycode.selenium.filedownloader;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * This code came from <a href="https://github.com/Ardesco/Powder-Monkey">https://github.com/Ardesco/Powder-Monkey</a>
 * which was found on the <a href="https://ardesco.lazerycode.com/testing/webdriver/2012/07/25/how-to-download-files-with-selenium-and-why-you-shouldnt.html">blog by Mark Collin</a>
 */
public enum RequestMethod {
    GET(new HttpGet()),
    POST(new HttpPost()),
    PUT(new HttpPut()),
    DELETE(new HttpDelete()),
    PATCH(new HttpPatch()),
    ;

    private final HttpRequestBase request;

    RequestMethod(HttpRequestBase requestMethod) {
        request = requestMethod;
    }

    public HttpRequestBase getRequestMethod() {
        return request;
    }

}
