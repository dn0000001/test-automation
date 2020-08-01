package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.CheckBoxAJAX;
import com.automation.common.ui.app.components.CheckBoxLabel;
import com.automation.common.ui.app.components.ImageUpload;
import com.automation.common.ui.app.components.PrimeFacesRadioButtonGroup;
import com.automation.common.ui.app.components.RadioButtonGroup;
import com.automation.common.ui.app.components.RadioOption;
import com.automation.common.ui.app.components.SearchTextBox;
import com.automation.common.ui.app.components.Select;
import com.automation.common.ui.app.components.SelectEnhanced;
import com.automation.common.ui.app.components.SelectEnhancedAJAX;
import com.automation.common.ui.app.components.TabOffTextBox;
import com.automation.common.ui.app.components.TextBox;
import com.automation.common.ui.app.components.TextBoxBackspace;
import com.taf.automation.ui.support.AliasedString;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.WebComponent;
import ui.auto.core.data.DataTypes;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This is a fake page used to test accessing component data without binding the component
 */
public class FakeComponentsPage extends PageObjectV2 {
    @FindBy(id = "does not exist")
    private CheckBoxAJAX checkBoxAJAX;

    @FindBy(id = "does not exist")
    private CheckBoxLabel checkBoxLabel;

    @FindBy(id = "does not exist")
    private ImageUpload imageUpload;

    @FindBy(id = "does not exist")
    private PrimeFacesRadioButtonGroup primeFacesRadioButtonGroup;

    @SuppressWarnings("java:S3740")
    @FindBy(id = "does not exist")
    private RadioButtonGroup radioButtonGroup;

    @FindBy(id = "does not exist")
    private RadioOption radioOption;

    @FindBy(id = "does not exist")
    private SearchTextBox searchTextBox;

    @FindBy(id = "does not exist")
    private Select select;

    @FindBy(id = "does not exist")
    private SelectEnhanced selectEnhanced;

    @FindBy(id = "does not exist")
    private SelectEnhancedAJAX selectEnhancedAJAX;

    @FindBy(id = "does not exist")
    private TabOffTextBox tabOffTextBox;

    @FindBy(id = "does not exist")
    private TextBox textBox;

    @FindBy(id = "does not exist")
    private TextBoxBackspace textBoxBackspace;

    @FindBy(id = "does not exist")
    private WebComponent generalWebComponent;

    @FindBy(id = "does not exist")
    private AliasedString generalAliasedString;

    public enum Type {
        CHECK_BOX_AJAX,
        CHECK_BOX_LABEL,
        IMAGE_UPLOAD,
        PRIME_FACES_RADIO_BUTTON_GROUP,
        RADIO_BUTTON_GROUP,
        RADIO_OPTION,
        SEARCH_TEXT_BOX,
        SELECT,
        SELECT_ENHANCED,
        SELECT_ENHANCED_JAX,
        TAB_OFF_TEXT_BOX,
        TEXT_BOX,
        TEXT_BOX_BACKSPACE,
        WEB_COMPONENT,
        ALIASED_STRING
    }

    public FakeComponentsPage() {
        super();
    }

    public FakeComponentsPage(TestContext context) {
        super(context);
    }

    /**
     * This method will check if there is any data for any of the components<BR>
     * <B>Note: </B> No component here should bind the element as this method should be able to be a called
     * any time.
     *
     * @return true if any component has non-blank data else false
     */
    public boolean hasData() {
        return Utils.isNotBlank(checkBoxAJAX)
                || Utils.isNotBlank(checkBoxLabel)
                || Utils.isNotBlank(imageUpload)
                || Utils.isNotBlank(primeFacesRadioButtonGroup)
                || Utils.isNotBlank(radioButtonGroup)
                || Utils.isNotBlank(radioOption)
                || Utils.isNotBlank(searchTextBox)
                || Utils.isNotBlank(select)
                || Utils.isNotBlank(selectEnhanced)
                || Utils.isNotBlank(selectEnhancedAJAX)
                || Utils.isNotBlank(tabOffTextBox)
                || Utils.isNotBlank(textBox)
                || Utils.isNotBlank(textBoxBackspace)
                || Utils.isNotBlank(generalWebComponent)
                || Utils.isNotBlank(generalAliasedString)
                ;
    }

    public String getTestData(Type component, boolean resolveAliases) {
        if (component == Type.CHECK_BOX_AJAX) {
            return checkBoxAJAX.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.CHECK_BOX_LABEL) {
            return checkBoxLabel.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.IMAGE_UPLOAD) {
            return imageUpload.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.PRIME_FACES_RADIO_BUTTON_GROUP) {
            return primeFacesRadioButtonGroup.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.RADIO_BUTTON_GROUP) {
            return radioButtonGroup.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.RADIO_OPTION) {
            return radioOption.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.SEARCH_TEXT_BOX) {
            return searchTextBox.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.SELECT) {
            return select.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.SELECT_ENHANCED) {
            return selectEnhanced.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.SELECT_ENHANCED_JAX) {
            return selectEnhancedAJAX.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.TAB_OFF_TEXT_BOX) {
            return tabOffTextBox.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.TEXT_BOX) {
            return textBox.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.TEXT_BOX_BACKSPACE) {
            return textBoxBackspace.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.WEB_COMPONENT) {
            return generalWebComponent.getData(DataTypes.Data, resolveAliases);
        }

        if (component == Type.ALIASED_STRING) {
            return generalAliasedString.getData(DataTypes.Data, resolveAliases);
        }

        assertThat("Unsupported Component Type:  " + component, false);
        return null;
    }

    public TextBox getTextBoxComponent() {
        return textBox;
    }

}
