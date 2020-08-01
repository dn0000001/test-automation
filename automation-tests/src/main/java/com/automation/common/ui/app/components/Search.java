package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.Utils;
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
    private SelectEnhanced season;
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
        season = new SelectEnhanced(getCoreElement().findElement(By.name("season")));
        search = getCoreElement().findElement(By.xpath(".//input[@type='submit']"));
    }

    @Override
    public void setValue() {
        setValue(getData());
    }

    public void setValue(String data) {
        initAllData(data);

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

    private String[] getDataArray(String data) {
        String[] fromToInfo = Utils.splitData(data, "->", 4, "null");
        Utils.trim(fromToInfo);
        return fromToInfo;
    }

    private void initAllData(String data) {
        String[] fromToInfo = getDataArray(data);
        String[] initial = getDataArray(getData(DataTypes.Initial, true));
        String[] expected = getDataArray(getData(DataTypes.Expected, true));

        String playerInitialData = (initial.length == 0) ? null : initial[0];
        String playerExpectedData = (expected.length == 0) ? null : expected[0];
        player.initializeData(fromToInfo[0], playerInitialData, playerExpectedData);

        String teamInitialData = (initial.length == 0) ? null : initial[1];
        String teamExpectedData = (expected.length == 0) ? null : expected[1];
        team.initializeData(fromToInfo[1], teamInitialData, teamExpectedData);

        String divisionInitialData = (initial.length == 0) ? null : initial[2];
        String divisionExpectedData = (expected.length == 0) ? null : expected[2];
        division.initializeData(fromToInfo[2], divisionInitialData, divisionExpectedData);

        String seasonInitialData = (initial.length == 0) ? null : initial[3];
        String seasonExpectedData = (expected.length == 0) ? null : expected[3];
        season.initializeData(fromToInfo[3], seasonInitialData, seasonExpectedData);
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
