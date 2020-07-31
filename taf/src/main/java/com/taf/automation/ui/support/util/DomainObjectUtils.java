package com.taf.automation.ui.support.util;

import datainstiller.data.DataAliases;
import datainstiller.data.DataPersistence;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestCaseEvent;
import ru.yandex.qatools.allure.model.Description;
import ru.yandex.qatools.allure.model.DescriptionType;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.LabelName;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides utility methods for Domain Objects
 */
public class DomainObjectUtils {
    private DomainObjectUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    private static List<Label> getLabels(DataAliases aliases, String labelAlias, LabelName labelName) {
        List<Label> labels = new ArrayList<>();

        String label = aliases.get(labelAlias);
        if (label != null) {
            for (String value : label.split(",")) {
                labels.add(new Label().withName(labelName.value()).withValue(value.trim()));
            }
        }

        return labels;
    }

    public static void overwriteTestParameters(DataPersistence data) {
        if (data != null) {
            overwriteTestParameters(data.getDataAliases());
        }
    }

    public static void overwriteTestParameters(DataAliases aliases) {
        if (aliases == null) {
            return;
        }

        String name = aliases.get("test-name");
        String description = aliases.get("test-description");

        List<Label> labels = new ArrayList<>();
        labels.addAll(getLabels(aliases, "test-features", LabelName.FEATURE));
        labels.addAll(getLabels(aliases, "test-stories", LabelName.STORY));
        labels.addAll(getLabels(aliases, "test-issues", LabelName.ISSUE));
        labels.addAll(getLabels(aliases, "test-IDs", LabelName.TEST_ID));
        labels.addAll(getLabels(aliases, "test-severity", LabelName.SEVERITY));

        Allure.LIFECYCLE.fire((TestCaseEvent) context -> {
            context.getLabels().addAll(labels);
            if (name != null) {
                context.setName(name);
            }

            if (description != null) {
                context.setDescription(new Description().withType(DescriptionType.MARKDOWN).withValue(description));
            }
        });
    }

}
