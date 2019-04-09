package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.ColumnPositionsExtractor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HerokuappColumnPositionsExtractor implements ColumnPositionsExtractor {
    @Override
    public By getColumnHeadersLocator() {
        return By.xpath("//*[@id='table1']/thead/tr/th");
    }

    @Override
    public String extractHeaderAsKey(WebElement header, int position) {
        return header.findElement(By.cssSelector("span")).getText();
    }

}
