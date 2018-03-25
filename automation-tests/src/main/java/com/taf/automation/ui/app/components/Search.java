package com.taf.automation.ui.app.components;

import com.taf.automation.ui.support.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

/**
 * Component team work with the Search fields on TNHC
 */
public class Search extends PageComponent {
    private TextBox player;
    private TextBox team;
    private TextBox division;
    private Select season;
    private WebElement search;

    public Search() {

    }

    public Search(WebElement element) {
        super(element);
    }

    @Override
    protected void init() {
        player = new TextBox(getCoreElement().findElement(By.id("player")));
        team = new TextBox(getCoreElement().findElement(By.id("team")));
        division = new TextBox(getCoreElement().findElement(By.id("division")));
        season = new Select(getCoreElement().findElement(By.name("season")));
        search = getCoreElement().findElement(By.xpath(".//input[@type='submit']"));
    }

    @Override
    public void setValue() {
        setValue(getData());
    }

    public void setValue(String data) {
        String[] fromToInfo = Utils.splitData(data, "->", 4, "null");
        Utils.trim(fromToInfo);

        player.initializeData(fromToInfo[0], null, null);
        team.initializeData(fromToInfo[1], null, null);
        division.initializeData(fromToInfo[2], null, null);
        season.initializeData(fromToInfo[3], null, null);

        if (player.getData() != null) {
            setPlayer();
        }

        if (team.getData() != null) {
            setTeam();
        }

        if (division.getData() != null) {
            setDivision();
        }

        if (season.getData() != null) {
            setSeason();
        }
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        player.validateData(validationMethod);
        team.validateData(validationMethod);
        division.validateData(validationMethod);
        season.validateData(validationMethod);
    }

    @Step("Set the Player Field")
    private void setPlayer() {
        player.setValue();
    }

    @Step("Set the Team Field")
    private void setTeam() {
        team.setValue();
    }

    @Step("Set the Division Field")
    private void setDivision() {
        division.setValue();
    }

    @Step("Set the Season Field")
    private void setSeason() {
        season.setValue();
    }

    @Step("Click Search")
    public void clickSearch() {
        search.click();
    }

}
