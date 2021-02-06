package com.taf.automation.ui.support.util;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import ui.auto.core.pagecomponent.PageComponent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This class provides a generic way to modify the test data using the TestNG parameters when you are using a base
 * template data file.
 */
public class PageObjectUtils {
    private static final String ADD_DATA_PREFIX = "add-data-";
    private static final String REMOVE_DATA_PREFIX = "remove-data-";

    private PageObjectUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    public static String getAddDataPrefix() {
        return ADD_DATA_PREFIX;
    }

    public static String getRemoveDataPrefix() {
        return REMOVE_DATA_PREFIX;
    }

    /**
     * @param params - Test Parameters
     * @return true if any test parameters would need to be processed otherwise false
     */
    public static boolean isAnyToProcess(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return false;
        }

        for (Map.Entry<String, String> item : params.entrySet()) {
            if (StringUtils.startsWithIgnoreCase(item.getKey(), getAddDataPrefix())
                    || StringUtils.startsWithIgnoreCase(item.getKey(), getRemoveDataPrefix())
            ) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get specific parameters that start with the prefix
     *
     * @param prefix       - Prefix to find the parameters
     * @param params       - The test parameters to search
     * @param removePrefix - true to remove the prefix in the returned map
     * @return test parameters that start with the prefix
     */
    public static Map<String, String> getSpecificParameters(String prefix, Map<String, String> params, boolean removePrefix) {
        Map<String, String> specificParameters = new HashMap<>();

        for (Map.Entry<String, String> item : params.entrySet()) {
            if (StringUtils.startsWith(item.getKey(), prefix)) {
                if (removePrefix) {
                    specificParameters.put(StringUtils.removeStart(item.getKey(), prefix), item.getValue());
                } else {
                    specificParameters.put(item.getKey(), item.getValue());
                }
            }
        }

        return specificParameters;
    }

    /**
     * Process the TestNG parameters to modify the Object to be written<BR>
     * <B>Notes:</B>
     * <UL>
     * <LI>The parameter needs to be prefixed with the add/remove prefix to be processed</LI>
     * <LI>The parameter needs to equal the variable name in the object being passed to the method</LI>
     * <LI>Only supported data fields are written (provided they are not annotated to skip)</LI>
     * <LI>Use an injected ITestContext by TestNG to get the parameters (iTestContext.getCurrentXmlTest().getAllParameters())</LI>
     * </UL>
     *
     * @param obj    - Object to be written to
     * @param params - TestNG parameters to be processed
     */
    public static void process(Object obj, Map<String, String> params) {
        List<Field> fields = FieldUtils.getAllFieldsList(obj.getClass());
        Map<String, Field> cache = fields
                .stream()
                .collect(Collectors.toMap(Field::getName, item -> item, (lhs, rhs) -> rhs));

        for (Map.Entry<String, String> item : params.entrySet()) {
            if (StringUtils.startsWithIgnoreCase(item.getKey(), getAddDataPrefix())) {
                String key = StringUtils.removeStartIgnoreCase(item.getKey(), getAddDataPrefix());
                Field field = cache.get(key);
                performAddDataToField(item.getKey(), field, obj, item.getValue());
            } else if (StringUtils.startsWithIgnoreCase(item.getKey(), getRemoveDataPrefix())) {
                String key = StringUtils.removeStartIgnoreCase(item.getKey(), getRemoveDataPrefix());
                Field field = cache.get(key);
                performRemoveDataFromField(item.getKey(), field, obj);
            }
        }
    }

    /**
     * Add data to the field in the object
     *
     * @param rawParameter - Parameter name being processed (only for logging)
     * @param field        - Field to be written to
     * @param obj          - Object to be written to
     * @param value        - Value to be written to the field
     */
    private static void performAddDataToField(String rawParameter, Field field, Object obj, String value) {
        performAddRemoveField(true, rawParameter, field, obj, value);
    }

    /**
     * Remove data from the field in the object<BR>
     * <B>Note: </B> The null value (or false if boolean primitive) is being written which causes the field to be
     * skipped when attempting to be set by the framework.
     *
     * @param rawParameter - Parameter name being processed (only for logging)
     * @param field        - Field to be written to
     * @param obj          - Object to be written to
     */
    private static void performRemoveDataFromField(String rawParameter, Field field, Object obj) {
        performAddRemoveField(false, rawParameter, field, obj, null);
    }

    /**
     * Add/Remove field in the object
     *
     * @param add          - true for adding, false for removing (only for logging)
     * @param rawParameter - Parameter name being processed (only for logging)
     * @param field        - Field to be written to
     * @param obj          - Object to be written to
     * @param value        - Value to be written to the field
     */
    private static void performAddRemoveField(boolean add, String rawParameter, Field field, Object obj, String value) {
        if (skipField(field)) {
            return;
        }

        final String error = "Could not %s data for parameter (%s) due to:  %s";
        try {
            writeField(field, obj, value);
        } catch (Exception ex) {
            assertThat(String.format(error, add ? "add" : "remove", rawParameter, ex.getMessage()), false);
        }
    }

    /**
     * @param field - Field to be written to
     * @return true to skip writing the field
     */
    private static boolean skipField(Field field) {
        return field == null || field.isAnnotationPresent(XStreamOmitField.class);
    }

    /**
     * Write the supported types of fields<BR>
     * <B>Supported types:</B>
     * <OL>
     * <LI>PageComponent.class</LI>
     * <LI>String.class</LI>
     * <LI>Boolean.class</LI>
     * <LI>boolean.class</LI>
     * <LI>Integer.class</LI>
     * <LI>int.class</LI>
     * </OL>
     * <B>Notes:</B>
     * <UL>
     * <LI>PageComponent fields use the value with initial &amp; expected data as null</LI>
     * <LI>Boolean fields are converted using BooleanUtils</LI>
     * <LI>Integer fields are converted using NumberUtils</LI>
     * </UL>
     *
     * @param field - Field to be written to
     * @param obj   - Object to be written to
     * @param value - Value to write to the field
     * @throws IllegalAccessException if the field cannot be written to (which includes the type was not supported)
     */
    private static void writeField(Field field, Object obj, String value) throws IllegalAccessException {
        if (PageComponent.class.isAssignableFrom(field.getType())) {
            PageComponent component = (PageComponent) FieldUtils.readField(field, obj, true);
            component.initializeData(value, null, null);
        } else if (String.class.isAssignableFrom(field.getType())) {
            FieldUtils.writeField(field, obj, value, true);
        } else if (Boolean.class.isAssignableFrom(field.getType()) || boolean.class.isAssignableFrom(field.getType())) {
            Boolean convertValue = (value == null && !field.getType().isPrimitive()) ? null : BooleanUtils.toBoolean(value);
            FieldUtils.writeField(field, obj, convertValue, true);
        } else if (Integer.class.isAssignableFrom(field.getType()) || int.class.isAssignableFrom(field.getType())) {
            Integer convertValue = (value == null && !field.getType().isPrimitive()) ? null : NumberUtils.toInt(value);
            FieldUtils.writeField(field, obj, convertValue, true);
        } else {
            throw new IllegalAccessException("Unsupported data type:  " + field.getType());
        }
    }

}
