package com.taf.automation.ui.support;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of Annotations for dynamic locators
 */
public class DynamicAnnotations extends Annotations {
    private final Field field;
    private final Map<String, String> substitutions;

    public DynamicAnnotations(final Field field, final Map<String, String> substitutions) {
        super(field);
        this.field = field;
        this.substitutions = substitutions;
    }

    public boolean isLookupCached() {
        return field.getAnnotation(CacheLookup.class) != null;
    }

    public By buildBy() {
        assertValidAnnotations();
        By ans = null;
        FindBys findBys = field.getAnnotation(FindBys.class);
        if (findBys != null) {
            ans = buildByFromFindBys(findBys);
        }

        FindAll findAll = field.getAnnotation(FindAll.class);
        if (ans == null && findAll != null) {
            ans = buildBysFromFindByOneOf(findAll);
        }

        FindBy findBy = field.getAnnotation(FindBy.class);
        if (ans == null && findBy != null) {
            ans = buildByFromFindBy(findBy);
        }

        if (ans == null) {
            ans = buildByFromDefault();
        }

        if (ans == null) {
            throw new IllegalArgumentException("Cannot determine how to locate element " + field);
        }

        return ans;
    }

    protected By buildByFromDefault() {
        return new ByIdOrName(field.getName());
    }

    protected By buildByFromFindBys(final FindBys findBys) {
        assertValidFindBys(findBys);

        FindBy[] findByArray = findBys.value();
        By[] byArray = new By[findByArray.length];
        for (int i = 0; i < findByArray.length; i++) {
            byArray[i] = buildByFromFindBy(findByArray[i]);
        }

        return new ByChained(byArray);
    }

    protected By buildBysFromFindByOneOf(final FindAll findBys) {
        assertValidFindAll(findBys);

        FindBy[] findByArray = findBys.value();
        By[] byArray = new By[findByArray.length];
        for (int i = 0; i < findByArray.length; i++) {
            byArray[i] = buildByFromFindBy(findByArray[i]);
        }

        return new ByAll(byArray);
    }

    protected By buildByFromFindBy(final FindBy findBy) {
        assertValidFindBy(findBy);

        By ans = buildByFromShortFindBy(findBy);
        if (ans == null) {
            ans = buildByFromLongFindBy(findBy);
        }

        return ans;
    }

    /**
     * The only thing that is different from the default Selenium implementation is that the locator string is
     * processed for substitutions by the processForSubstitutions(using) method
     *
     * @param findBy
     * @return
     */
    protected By buildByFromLongFindBy(final FindBy findBy) {
        How how = findBy.how();
        String using = findBy.using();

        switch (how) {
            case CLASS_NAME:
                String className = processForSubstitutions(using);
                return By.className(className);

            case CSS:
                String css = processForSubstitutions(using);
                return By.cssSelector(css);

            case ID:
                String id = processForSubstitutions(using);
                return By.id(id);

            case ID_OR_NAME:
                String idOrName = processForSubstitutions(using);
                return new ByIdOrName(idOrName);

            case LINK_TEXT:
                String linkText = processForSubstitutions(using);
                return By.linkText(linkText);

            case NAME:
                String name = processForSubstitutions(using);
                return By.name(name);

            case PARTIAL_LINK_TEXT:
                String partialLinkText = processForSubstitutions(using);
                return By.partialLinkText(partialLinkText);

            case TAG_NAME:
                String tagName = processForSubstitutions(using);
                return By.tagName(tagName);

            case XPATH:
                String xpath = processForSubstitutions(using);
                return By.xpath(xpath);

            default:
                // Note that this shouldn't happen (eg, the above matches all possible values for the How enum)
                throw new IllegalArgumentException("Cannot determine how to locate element " + field);
        }
    }

    /**
     * The only thing that differs from the default Selenium implementation is that the locator string is processed
     * for substitutions by processForSubstitutions(using)
     *
     * @param findBy
     * @return By
     */
    protected By buildByFromShortFindBy(final FindBy findBy) {
        if (!"".equals(findBy.className())) {
            String className = processForSubstitutions(findBy.className());
            return By.className(className);
        }

        if (!"".equals(findBy.css())) {
            String css = processForSubstitutions(findBy.css());
            return By.cssSelector(css);
        }

        if (!"".equals(findBy.id())) {
            String id = processForSubstitutions(findBy.id());
            return By.id(id);
        }

        if (!"".equals(findBy.linkText())) {
            String linkText = processForSubstitutions(findBy.linkText());
            return By.linkText(linkText);
        }

        if (!"".equals(findBy.name())) {
            String name = processForSubstitutions(findBy.name());
            return By.name(name);
        }

        if (!"".equals(findBy.partialLinkText())) {
            String partialLinkText = processForSubstitutions(findBy.partialLinkText());
            return By.partialLinkText(partialLinkText);
        }

        if (!"".equals(findBy.tagName())) {
            String tagName = processForSubstitutions(findBy.tagName());
            return By.tagName(tagName);
        }

        if (!"".equals(findBy.xpath())) {
            String xpath = processForSubstitutions(findBy.xpath());
            return By.xpath(xpath);
        }

        return null;
    }


    /**
     * The method looks for instances of ${key} and if there is a key in the substitutions map that is equal to 'key',
     * the substring ${key} is replaced by the value mapped to 'key'
     *
     * @param locator - Locator
     * @return String
     */
    private String processForSubstitutions(final String locator) {
        String[] split = StringUtils.substringsBetween(locator, "${", "}");
        List<String> subs = new ArrayList<>();
        if (split != null) {
            subs = Arrays.asList(split);
        }

        String processed = locator;
        for (String sub : subs) {
            //If there is no matching key, the substring "${...}" is treated as a literal
            if (substitutions.get(sub) != null) {
                processed = StringUtils.replace(processed, "${" + sub + "}", substitutions.get(sub));
            }
        }

        return processed;
    }

    protected void assertValidAnnotations() {
        FindBys findBys = field.getAnnotation(FindBys.class);
        FindAll findAll = field.getAnnotation(FindAll.class);
        FindBy findBy = field.getAnnotation(FindBy.class);

        if (findBys != null && findBy != null) {
            throw new IllegalArgumentException("If you use a '@FindBys' annotation, you must not also use a '@FindBy' annotation");
        }

        if (findAll != null && findBy != null) {
            throw new IllegalArgumentException("If you use a '@FindAll' annotation, you must not also use a '@FindBy' annotation");
        }

        if (findAll != null && findBys != null) {
            throw new IllegalArgumentException("If you use a '@FindAll' annotation, you must not also use a '@FindBys' annotation");
        }
    }

    private void assertValidFindBys(final FindBys findBys) {
        for (FindBy findBy : findBys.value()) {
            assertValidFindBy(findBy);
        }
    }

    private void assertValidFindAll(final FindAll findBys) {
        for (FindBy findBy : findBys.value()) {
            assertValidFindBy(findBy);
        }
    }

    private void assertValidFindBy(final FindBy findBy) {
        if (findBy.how() != null) {
            if (findBy.using() == null) {
                throw new IllegalArgumentException("If you set the 'how' property, you must also set 'using'");
            }
        }

        Set<String> finders = new HashSet<>();
        if (!"".equals(findBy.using())) {
            finders.add("how: " + findBy.using());
        }
        if (!"".equals(findBy.className())) {
            finders.add("class name:" + findBy.className());
        }

        if (!"".equals(findBy.css())) {
            finders.add("css:" + findBy.css());
        }

        if (!"".equals(findBy.id())) {
            finders.add("id: " + findBy.id());
        }

        if (!"".equals(findBy.linkText())) {
            finders.add("link text: " + findBy.linkText());
        }

        if (!"".equals(findBy.name())) {
            finders.add("name: " + findBy.name());
        }

        if (!"".equals(findBy.partialLinkText())) {
            finders.add("partial link text: " + findBy.partialLinkText());
        }

        if (!"".equals(findBy.tagName())) {
            finders.add("tag name: " + findBy.tagName());
        }
        if (!"".equals(findBy.xpath())) {
            finders.add("xpath: " + findBy.xpath());
        }

        // A zero count is okay: it means to look by name or id.
        if (finders.size() > 1) {
            String message = "You must specify at most one location strategy. Number found: %d (%s)";
            String log = String.format(message, finders.size(), finders.toString());
            throw new IllegalArgumentException(log);
        }
    }

}
