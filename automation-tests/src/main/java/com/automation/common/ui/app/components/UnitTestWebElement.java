package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WebElement purely for unit testing purposes
 */
public class UnitTestWebElement implements WebElement {
    private final Map<String, String> attributes;
    private final List<WebElement> theFindElements;
    private boolean selected;
    private boolean displayed;
    private boolean enabled;
    private String text;
    private String tagName;
    private WebElement theFindElement;
    private Point location;
    private Dimension size;
    private Rectangle rect;

    public UnitTestWebElement() {
        attributes = new HashMap<>();
        theFindElements = new ArrayList<>();
        selected = false;
        displayed = true;
        enabled = true;
        text = "";
        tagName = "";
    }

    public UnitTestWebElement withAttribute(String attribute, String value) {
        attributes.put(attribute, value);
        return this;
    }

    public UnitTestWebElement withSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public UnitTestWebElement withDisplayed(boolean displayed) {
        this.displayed = displayed;
        return this;
    }

    public UnitTestWebElement withEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public UnitTestWebElement withText(String text) {
        this.text = text;
        return this;
    }

    public UnitTestWebElement withTagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public UnitTestWebElement withElement(WebElement element) {
        this.theFindElement = element;
        return this;
    }

    public UnitTestWebElement withElement(List<WebElement> elements) {
        this.theFindElements.addAll(elements);
        return this;
    }

    public UnitTestWebElement withPoint(Point location) {
        this.location = location;
        return this;
    }

    public UnitTestWebElement withPoint(int x, int y) {
        return withPoint(new Point(x, y));
    }

    public UnitTestWebElement withSize(Dimension size) {
        this.size = size;
        return this;
    }

    public UnitTestWebElement withSize(int width, int height) {
        return withSize(new Dimension(width, height));
    }

    public UnitTestWebElement withRect(Rectangle rect) {
        this.rect = rect;
        return this;
    }

    public UnitTestWebElement withRect(int x, int y, int height, int width) {
        return withRect(new Rectangle(x, y, height, width));
    }

    public UnitTestWebElement withRect(Point p, Dimension d) {
        return withRect(new Rectangle(p, d));
    }

    @Override
    public void click() {
        //
    }

    @Override
    public void submit() {
        //
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        //
    }

    @Override
    public void clear() {
        //
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public String getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return theFindElements;
    }

    @Override
    public WebElement findElement(By by) {
        return theFindElement;
    }

    @Override
    public boolean isDisplayed() {
        return displayed;
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public Rectangle getRect() {
        return rect;
    }

    @Override
    public String getCssValue(String propertyName) {
        return attributes.get(propertyName);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) {
        return null;
    }

    @Override
    public String toString() {
        ToStringBuilder.setDefaultStyle(Utils.getDefaultStyle());
        return ReflectionToStringBuilder.toStringExclude(this);
    }

}
