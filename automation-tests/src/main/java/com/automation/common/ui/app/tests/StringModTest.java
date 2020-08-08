package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.StringMod;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This class is for unit testing the StringMod class
 */
@Listeners(AllureTestNGListener.class)
public class StringModTest {
    private static final String ELEPHANT = "elePHant";
    private static final String SPACES = "      ";

    @Features("StringMod")
    @Stories("Append")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runAppendTest() {
        boolean assertion;
        String reason;

        StringMod mod = new StringMod();
        String sAppend1 = Rand.alphanumeric(5, 10);
        mod.append(sAppend1);
        assertion = sAppend1.equals(mod.get());
        reason = "Append #1 failed";
        assertThat(reason, assertion);

        String sAppend2 = Rand.alphanumeric(5, 10);
        String sAll = sAppend1 + sAppend2;
        mod.append(sAppend2);
        assertion = sAll.equals(mod.get());
        reason = "Append #2 failed";
        assertThat(reason, assertion);

        mod = new StringMod(sAppend1);
        assertion = sAppend1.equals(mod.get());
        reason = "Append #3 failed";
        assertThat(reason, assertion);

        mod.append(sAppend2);
        assertion = sAll.equals(mod.get());
        reason = "Append #4 failed";
        assertThat(reason, assertion);

        StringMod mod2 = new StringMod().append(sAppend1).append(sAppend2);
        assertion = mod2.equals(mod);
        reason = "Append #5 failed";
        assertThat(reason, assertion);

        assertion = sAll.equals(mod2.get());
        reason = "Append #6 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Prepend")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runPrependTest() {
        boolean assertion;
        String reason;

        StringMod mod = new StringMod();
        String sPrepend1 = Rand.alphanumeric(5, 10);
        mod.prepend(sPrepend1);
        assertion = sPrepend1.equals(mod.get());
        reason = "Prepend #1 failed";
        assertThat(reason, assertion);

        String sPrepend2 = Rand.alphanumeric(5, 10);
        String sAll = sPrepend2 + sPrepend1;
        mod.prepend(sPrepend2);
        assertion = sAll.equals(mod.get());
        reason = "Prepend #2 failed";
        assertThat(reason, assertion);

        mod = new StringMod(sPrepend1);
        assertion = sPrepend1.equals(mod.get());
        reason = "Prepend #3 failed";
        assertThat(reason, assertion);

        mod.prepend(sPrepend2);
        assertion = sAll.equals(mod.get());
        reason = "Prepend #4 failed";
        assertThat(reason, assertion);

        StringMod mod2 = new StringMod().prepend(sPrepend1).prepend(sPrepend2);
        assertion = mod2.equals(mod);
        reason = "Prepend #5 failed";
        assertThat(reason, assertion);

        assertion = sAll.equals(mod2.get());
        reason = "Prepend #6 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Remove Non-Digits")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runRemoveNonDigitsTest() {
        boolean assertion;
        String reason;

        String onlyLetters = Rand.letters(5, 10);
        StringMod mod = new StringMod(onlyLetters);
        mod.removeNonDigits();
        assertion = mod.get().equals("");
        reason = "Non-Digits #1 failed";
        assertThat(reason, assertion);

        String onlyDigits = Rand.numbers(5, 10);
        mod = new StringMod(onlyDigits);
        mod.removeNonDigits();
        assertion = mod.get().equals(onlyDigits);
        reason = "Non-Digits #2 failed";
        assertThat(reason, assertion);

        String before = Rand.numbers(5, 10);
        String after = Rand.numbers(5, 10);
        String mix = before + onlyLetters + after;
        mod = new StringMod(mix);
        mod.removeNonDigits();
        assertion = mod.get().equals(before + after);
        reason = "Non-Digits #3 failed";
        assertThat(reason, assertion);

        int size = 10;
        String onlyDigits2 = Rand.numbers(size);
        String onlyLeters2 = Rand.letters(size);
        StringBuilder alternate = new StringBuilder();
        for (int i = 0; i < size; i++) {
            alternate.append(onlyDigits2.charAt(i));
            alternate.append(onlyLeters2.charAt(i));
        }

        mod = new StringMod(alternate.toString());
        mod.removeNonDigits();
        assertion = mod.get().equals(onlyDigits2);
        reason = "Non-Digits #4 failed";
        assertThat(reason, assertion);

        StringBuilder unknownOrder = new StringBuilder(onlyDigits2);
        for (int i = 0; i < size; i++) {
            unknownOrder.insert(Rand.randomRangeIndex(0, unknownOrder.length()), onlyLeters2.charAt(i));
        }

        mod = new StringMod(unknownOrder.toString());
        mod.removeNonDigits();
        assertion = mod.get().equals(onlyDigits2);
        reason = "Non-Digits #5 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Remove Non-Letters")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runRemoveNonLettersTest() {
        boolean assertion;
        String reason;

        String onlyDigits = Rand.numbers(5, 10);
        StringMod mod = new StringMod(onlyDigits);
        mod.removeNonLetters();
        assertion = mod.get().equals("");
        reason = "Non-Letters #1 failed";
        assertThat(reason, assertion);

        String onlyLetters = Rand.letters(5, 10);
        mod = new StringMod(onlyLetters);
        mod.removeNonLetters();
        assertion = mod.get().equals(onlyLetters);
        reason = "Non-Letters #2 failed";
        assertThat(reason, assertion);

        String before = Rand.letters(5, 10);
        String after = Rand.letters(5, 10);
        String mix = before + onlyDigits + after;
        mod = new StringMod(mix);
        mod.removeNonLetters();
        assertion = mod.get().equals(before + after);
        reason = "Non-Letters #3 failed";
        assertThat(reason, assertion);

        int size = 10;
        String onlyDigits2 = Rand.numbers(size);
        String onlyLeters2 = Rand.letters(size);
        StringBuilder alternate = new StringBuilder();
        for (int i = 0; i < size; i++) {
            alternate.append(onlyDigits2.charAt(i));
            alternate.append(onlyLeters2.charAt(i));
        }

        mod = new StringMod(alternate.toString());
        mod.removeNonLetters();
        assertion = mod.get().equals(onlyLeters2);
        reason = "Non-Letters #4 failed";
        assertThat(reason, assertion);

        StringBuilder unknownOrder = new StringBuilder(onlyLeters2);
        for (int i = 0; i < size; i++) {
            unknownOrder.insert(Rand.randomRangeIndex(0, unknownOrder.length()), onlyDigits2.charAt(i));
        }

        mod = new StringMod(unknownOrder.toString());
        mod.removeNonLetters();
        assertion = mod.get().equals(onlyLeters2);
        reason = "Non-Letters #5 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Remove Non-Alphanumeric")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runRemoveNonAlphanumericTest() {
        boolean assertion;
        String reason;

        String onlyInvalid = Rand.onlyChars(Rand.randomRange(5, 10), Rand.getOnlyExtendedLetters());
        StringMod mod = new StringMod(onlyInvalid);
        mod.removeNonAlphanumeric();
        assertion = mod.get().equals("");
        reason = "Non-Alpha #1 failed";
        assertThat(reason, assertion);

        String onlyAlphanumeric = Rand.alphanumeric(5, 10);
        mod = new StringMod(onlyAlphanumeric);
        mod.removeNonAlphanumeric();
        assertion = mod.get().equals(onlyAlphanumeric);
        reason = "Non-Alpha #2 failed";
        assertThat(reason, assertion);

        String before = Rand.alphanumeric(5, 10);
        String after = Rand.alphanumeric(5, 10);
        String mix = before + onlyInvalid + after;
        mod = new StringMod(mix);
        mod.removeNonAlphanumeric();
        assertion = mod.get().equals(before + after);
        reason = "Non-Alpha #3 failed";
        assertThat(reason, assertion);

        int size = 10;
        String onlyInvalid2 = Rand.onlyChars(size, Rand.getOnlyExtendedLetters());
        String onlyAlphanumeric2 = Rand.alphanumeric(size);
        StringBuilder alternate = new StringBuilder();
        for (int i = 0; i < size; i++) {
            alternate.append(onlyInvalid2.charAt(i));
            alternate.append(onlyAlphanumeric2.charAt(i));
        }

        mod = new StringMod(alternate.toString());
        mod.removeNonAlphanumeric();
        assertion = mod.get().equals(onlyAlphanumeric2);
        reason = "Non-Alpha #4 failed";
        assertThat(reason, assertion);

        StringBuilder unknownOrder = new StringBuilder(onlyAlphanumeric2);
        for (int i = 0; i < size; i++) {
            unknownOrder.insert(Rand.randomRangeIndex(0, unknownOrder.length()), onlyInvalid2.charAt(i));
        }

        mod = new StringMod(unknownOrder.toString());
        mod.removeNonAlphanumeric();
        assertion = mod.get().equals(onlyAlphanumeric2);
        reason = "Non-Alpha #5 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Equal")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runEqualTest() {
        boolean assertion;
        String reason;

        StringMod actual = new StringMod("");
        StringMod expected = new StringMod(null);

        assertion = actual.equals(expected);
        reason = "Equal #1 failed";
        assertThat(reason, assertion);

        assertion = actual.equalsIgnoreCase(expected);
        reason = "Equal #2 failed";
        assertThat(reason, assertion);

        assertion = expected.equalsIgnoreCase(actual);
        reason = "Equal #3 failed";
        assertThat(reason, assertion);

        String randomCase = Rand.alphanumeric(20, 30);
        String lowercase = randomCase.toLowerCase();
        String uppercase = randomCase.toUpperCase();

        actual = new StringMod(randomCase);
        expected = new StringMod(randomCase);

        assertion = expected.equals(actual);
        reason = "Equal #4 failed";
        assertThat(reason, assertion);

        actual = new StringMod(randomCase);
        expected = new StringMod(lowercase);

        assertion = expected.equalsIgnoreCase(actual);
        reason = "Equal #5 failed";
        assertThat(reason, assertion);

        assertion = actual.equalsIgnoreCase(expected);
        reason = "Equal #6 failed";
        assertThat(reason, assertion);

        actual = new StringMod(randomCase);
        expected = new StringMod(uppercase);

        assertion = expected.equalsIgnoreCase(actual);
        reason = "Equal #7 failed";
        assertThat(reason, assertion);

        assertion = actual.equalsIgnoreCase(expected);
        reason = "Equal #8 failed";
        assertThat(reason, assertion);

        actual = new StringMod(lowercase);
        expected = new StringMod(uppercase);

        assertion = expected.equalsIgnoreCase(actual);
        reason = "Equal #9 failed";
        assertThat(reason, assertion);

        assertion = actual.equalsIgnoreCase(expected);
        reason = "Equal #10 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Not Equal")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runNotEqualTest() {
        boolean assertion;
        String reason;

        String sActual = Rand.alphanumeric(5, 10);
        String sExpected = Rand.alphanumeric(11, 20);
        StringMod actual = new StringMod(sActual);
        StringMod expected = new StringMod(sExpected);

        assertion = !actual.equals(expected);
        reason = "Not Equal #1 failed";
        assertThat(reason, assertion);

        assertion = !expected.equals(actual);
        reason = "Not Equal #2 failed";
        assertThat(reason, assertion);

        assertion = !actual.equalsIgnoreCase(expected);
        reason = "Not Equal #3 failed";
        assertThat(reason, assertion);

        assertion = !expected.equalsIgnoreCase(actual);
        reason = "Not Equal #4 failed";
        assertThat(reason, assertion);

        assertion = actual.get().equals(sActual);
        reason = "No Mod #1 failed";
        assertThat(reason, assertion);

        assertion = expected.get().equals(sExpected);
        reason = "No Mod #2 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Insert")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runInsertTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String cat = "caT";
        String dogs = "dOGs";
        String elephant = ELEPHANT;

        String phrase = cat + dogs;
        StringMod mod = new StringMod(phrase);
        mod.insert(cat.length(), elephant);
        assertion = mod.get().equals(cat + elephant + dogs);
        reason = "Insert #1 failed";
        assertThat(reason, assertion);

        phrase = cat + dogs;
        mod = new StringMod(phrase);
        mod.insert(0, elephant);
        assertion = mod.get().equals(elephant + cat + dogs);
        reason = "Insert #2 failed";
        assertThat(reason, assertion);

        phrase = cat + dogs;
        mod = new StringMod(phrase);
        mod.insert(phrase.length(), elephant);
        assertion = mod.get().equals(cat + dogs + elephant);
        reason = "Insert #3 failed";
        assertThat(reason, assertion);

        // Invalid
        phrase = mod.get();
        mod.insert(phrase.length() + 1, one);
        assertion = mod.get().equals(phrase);
        reason = "Insert #4 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Replace")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runReplaceTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";
        String dogs = "dOGs";
        String elephant = ELEPHANT;
        String two = "two";

        // No matches
        String phrase = cat + dogs;
        StringMod mod = new StringMod(phrase);
        mod.replace(test, elephant);
        assertion = mod.get().equals(phrase);
        reason = "Replace #1 failed";
        assertThat(reason, assertion);

        phrase = cat + dogs;
        mod = new StringMod(phrase);
        mod.replace(cat.toLowerCase(), elephant);
        assertion = mod.get().equals(phrase);
        reason = "Replace #2 failed";
        assertThat(reason, assertion);

        // 1 match
        phrase = cat + one + dogs;
        mod = new StringMod(phrase);
        mod.replace(one, two);
        assertion = mod.get().equals(cat + two + dogs);
        reason = "Replace #3 failed";
        assertThat(reason, assertion);

        // 1 match replace with smaller string
        phrase = cat + elephant + dogs;
        mod = new StringMod(phrase);
        mod.replace(elephant, two);
        assertion = mod.get().equals(cat + two + dogs);
        reason = "Replace #4 failed";
        assertThat(reason, assertion);

        // 1 match replace with larger string
        phrase = cat + one + dogs;
        mod = new StringMod(phrase);
        mod.replace(one, elephant);
        assertion = mod.get().equals(cat + elephant + dogs);
        reason = "Replace #5 failed";
        assertThat(reason, assertion);

        // Multiple matches
        phrase = cat + one + dogs + one;
        mod = new StringMod(phrase);
        mod.replace(one, two);
        assertion = mod.get().equals(cat + two + dogs + two);
        reason = "Replace #6 failed";
        assertThat(reason, assertion);

        // Multiple matches replace with smaller string
        phrase = elephant + cat + elephant + dogs;
        mod = new StringMod(phrase);
        mod.replace(elephant, two);
        assertion = mod.get().equals(two + cat + two + dogs);
        reason = "Replace #7 failed";
        assertThat(reason, assertion);

        // Multiple matches with larger string
        phrase = cat + one + dogs + one;
        mod = new StringMod(phrase);
        mod.replace(one, elephant);
        assertion = mod.get().equals(cat + elephant + dogs + elephant);
        reason = "Replace #8 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Remove")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runRemoveTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";
        String dogs = "dOGs";

        // No matches
        String phrase = cat + dogs;
        StringMod mod = new StringMod(phrase);
        mod.remove(test);
        assertion = mod.get().equals(phrase);
        reason = "Remove #1 failed";
        assertThat(reason, assertion);

        phrase = cat + dogs;
        mod = new StringMod(phrase);
        mod.remove(cat.toLowerCase());
        assertion = mod.get().equals(phrase);
        reason = "Remove #2 failed";
        assertThat(reason, assertion);

        // 1 match
        phrase = cat + one + dogs;
        mod = new StringMod(phrase);
        mod.remove(one);
        assertion = mod.get().equals(cat + dogs);
        reason = "Remove #3 failed";
        assertThat(reason, assertion);

        // Multiple matches
        phrase = cat + one + dogs + one;
        mod = new StringMod(phrase);
        mod.remove(one);
        assertion = mod.get().equals(cat + dogs);
        reason = "Remove #4 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Replace First RegEx")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runReplaceFirstRegExTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";
        String dogs = "dOGs";
        String elephant = ELEPHANT;
        String two = "two";

        // Replacement at end

        String phrase = one + test;
        StringMod mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, dogs);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceFirstRegEx #1 failed";
        assertThat(reason, assertion);

        // No modifications
        mod.replaceFirstRegEx(dogs.toUpperCase(), test);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceFirstRegEx #2 failed";
        assertThat(reason, assertion);

        // Replacement at beginning

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, dogs);
        assertion = mod.get().equals(dogs + one);
        reason = "ReplaceFirstRegEx #3 failed";
        assertThat(reason, assertion);

        // Replacement in middle

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, dogs);
        assertion = mod.get().equals(cat + dogs + one);
        reason = "ReplaceFirstRegEx #4 failed";
        assertThat(reason, assertion);

        // Replacement at end - Smaller

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, two);
        assertion = mod.get().equals(one + two);
        reason = "ReplaceFirstRegEx #5 failed";
        assertThat(reason, assertion);

        // Replacement at beginning - Smaller

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, two);
        assertion = mod.get().equals(two + one);
        reason = "ReplaceFirstRegEx #6 failed";
        assertThat(reason, assertion);

        // Replacement in middle - Smaller

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, two);
        assertion = mod.get().equals(cat + two + one);
        reason = "ReplaceFirstRegEx #7 failed";
        assertThat(reason, assertion);

        // Replacement at end - Larger

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, elephant);
        assertion = mod.get().equals(one + elephant);
        reason = "ReplaceFirstRegEx #8 failed";
        assertThat(reason, assertion);

        // Replacement at beginning - Larger

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, elephant);
        assertion = mod.get().equals(elephant + one);
        reason = "ReplaceFirstRegEx #9 failed";
        assertThat(reason, assertion);

        // Replacement in middle - Larger

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, elephant);
        assertion = mod.get().equals(cat + elephant + one);
        reason = "ReplaceFirstRegEx #10 failed";
        assertThat(reason, assertion);

        // Repeating - Replacement at end

        phrase = one + test + test;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, dogs);
        assertion = mod.get().equals(one + dogs + test);
        reason = "ReplaceFirstRegEx #11 failed";
        assertThat(reason, assertion);

        // Repeating - Replacement at beginning

        phrase = test + test + one;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, dogs);
        assertion = mod.get().equals(dogs + test + one);
        reason = "ReplaceFirstRegEx #12 failed";
        assertThat(reason, assertion);

        // Repeating - Replacement in middle

        phrase = cat + test + test + one;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, dogs);
        assertion = mod.get().equals(cat + dogs + test + one);
        reason = "ReplaceFirstRegEx #13 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, dogs);
        assertion = mod.get().equals(cat + dogs + one + test);
        reason = "ReplaceFirstRegEx #14 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement - Smaller

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, two);
        assertion = mod.get().equals(cat + two + one + test);
        reason = "ReplaceFirstRegEx #15 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement - Larger

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceFirstRegEx(test, elephant);
        assertion = mod.get().equals(cat + elephant + one + test);
        reason = "ReplaceFirstRegEx #16 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Remove First RegEx")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runRemoveFirstRegExTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";
        String dogs = "dOGs";

        // Remove at end

        String phrase = one + test;
        StringMod mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(one);
        reason = "RemoveFirstRegEx #1 failed";
        assertThat(reason, assertion);

        // No modifications
        mod.removeFirstRegEx(dogs.toUpperCase());
        assertion = mod.get().equals(one);
        reason = "RemoveFirstRegEx #2 failed";
        assertThat(reason, assertion);

        // Remove at beginning

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(one);
        reason = "RemoveFirstRegEx #3 failed";
        assertThat(reason, assertion);

        // Remove in middle

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirstRegEx #4 failed";
        assertThat(reason, assertion);

        // Remove at end - Smaller

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(one);
        reason = "RemoveFirstRegEx #5 failed";
        assertThat(reason, assertion);

        // Remove at beginning - Smaller

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(one);
        reason = "RemoveFirstRegEx #6 failed";
        assertThat(reason, assertion);

        // Remove in middle - Smaller

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirstRegEx #7 failed";
        assertThat(reason, assertion);

        // Remove at end - Larger

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(one);
        reason = "RemoveFirstRegEx #8 failed";
        assertThat(reason, assertion);

        // Remove at beginning - Larger

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(one);
        reason = "RemoveFirstRegEx #9 failed";
        assertThat(reason, assertion);

        // Remove in middle - Larger

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirstRegEx #10 failed";
        assertThat(reason, assertion);

        // Remove - Replacement at end

        phrase = one + test + test;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(one + test);
        reason = "RemoveFirstRegEx #11 failed";
        assertThat(reason, assertion);

        // Remove - Replacement at beginning

        phrase = test + test + one;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(test + one);
        reason = "RemoveFirstRegEx #12 failed";
        assertThat(reason, assertion);

        // Remove - Replacement in middle

        phrase = cat + test + test + one;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(cat + test + one);
        reason = "RemoveFirstRegEx #13 failed";
        assertThat(reason, assertion);

        // Remove - Alternating replacement

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(cat + one + test);
        reason = "RemoveFirstRegEx #14 failed";
        assertThat(reason, assertion);

        // Remove - Alternating replacement - Smaller

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(cat + one + test);
        reason = "RemoveFirstRegEx #15 failed";
        assertThat(reason, assertion);

        // Remove - Alternating replacement - Larger

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.removeFirstRegEx(test);
        assertion = mod.get().equals(cat + one + test);
        reason = "RemoveFirstRegEx #16 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Replace First")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runReplaceFirstTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";
        String dogs = "dOGs";
        String elephant = ELEPHANT;
        String two = "two";

        // Replacement at end

        String phrase = one + test.toUpperCase();
        StringMod mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceFirst #1 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceFirst #2 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, false);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceFirst #3 failed";
        assertThat(reason, assertion);

        // No modifications
        mod.replaceFirst(dogs.toUpperCase(), test, false);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceFirst #4 failed";
        assertThat(reason, assertion);

        // Replacement at beginning

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(dogs + one);
        reason = "ReplaceFirst #5 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(dogs + one);
        reason = "ReplaceFirst #6 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, false);
        assertion = mod.get().equals(dogs + one);
        reason = "ReplaceFirst #7 failed";
        assertThat(reason, assertion);

        // Replacement in middle

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(cat + dogs + one);
        reason = "ReplaceFirst #8 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(cat + dogs + one);
        reason = "ReplaceFirst #9 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, false);
        assertion = mod.get().equals(cat + dogs + one);
        reason = "ReplaceFirst #10 failed";
        assertThat(reason, assertion);

        // Replacement at end - Smaller

        phrase = one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, true);
        assertion = mod.get().equals(one + two);
        reason = "ReplaceFirst #11 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, true);
        assertion = mod.get().equals(one + two);
        reason = "ReplaceFirst #12 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, false);
        assertion = mod.get().equals(one + two);
        reason = "ReplaceFirst #13 failed";
        assertThat(reason, assertion);

        // Replacement at beginning - Smaller

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, true);
        assertion = mod.get().equals(two + one);
        reason = "ReplaceFirst #14 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, true);
        assertion = mod.get().equals(two + one);
        reason = "ReplaceFirst #15 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, false);
        assertion = mod.get().equals(two + one);
        reason = "ReplaceFirst #16 failed";
        assertThat(reason, assertion);

        // Replacement in middle - Smaller

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, true);
        assertion = mod.get().equals(cat + two + one);
        reason = "ReplaceFirst #17 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, true);
        assertion = mod.get().equals(cat + two + one);
        reason = "ReplaceFirst #18 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, false);
        assertion = mod.get().equals(cat + two + one);
        reason = "ReplaceFirst #19 failed";
        assertThat(reason, assertion);

        // Replacement at end - Larger

        phrase = one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, true);
        assertion = mod.get().equals(one + elephant);
        reason = "ReplaceFirst #20 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, true);
        assertion = mod.get().equals(one + elephant);
        reason = "ReplaceFirst #21 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, false);
        assertion = mod.get().equals(one + elephant);
        reason = "ReplaceFirst #22 failed";
        assertThat(reason, assertion);

        // Replacement at beginning - Larger

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, true);
        assertion = mod.get().equals(elephant + one);
        reason = "ReplaceFirst #23 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, true);
        assertion = mod.get().equals(elephant + one);
        reason = "ReplaceFirst #24 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, false);
        assertion = mod.get().equals(elephant + one);
        reason = "ReplaceFirst #25 failed";
        assertThat(reason, assertion);

        // Replacement in middle - Larger

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, true);
        assertion = mod.get().equals(cat + elephant + one);
        reason = "ReplaceFirst #26 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, true);
        assertion = mod.get().equals(cat + elephant + one);
        reason = "ReplaceFirst #27 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, false);
        assertion = mod.get().equals(cat + elephant + one);
        reason = "ReplaceFirst #28 failed";
        assertThat(reason, assertion);

        // Repeating - Replacement at end

        phrase = one + test.toUpperCase() + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(one + dogs + test.toUpperCase());
        reason = "ReplaceFirst #29 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase() + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(one + dogs + test.toLowerCase());
        reason = "ReplaceFirst #30 failed";
        assertThat(reason, assertion);

        phrase = one + test + test;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, false);
        assertion = mod.get().equals(one + dogs + test);
        reason = "ReplaceFirst #31 failed";
        assertThat(reason, assertion);

        // Repeating - Replacement at beginning

        phrase = test.toUpperCase() + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(dogs + test.toUpperCase() + one);
        reason = "ReplaceFirst #32 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(dogs + test.toLowerCase() + one);
        reason = "ReplaceFirst #33 failed";
        assertThat(reason, assertion);

        phrase = test + test + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, false);
        assertion = mod.get().equals(dogs + test + one);
        reason = "ReplaceFirst #34 failed";
        assertThat(reason, assertion);

        // Repeating - Replacement in middle

        phrase = cat + test.toUpperCase() + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(cat + dogs + test.toUpperCase() + one);
        reason = "ReplaceFirst #35 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(cat + dogs + test.toLowerCase() + one);
        reason = "ReplaceFirst #36 failed";
        assertThat(reason, assertion);

        phrase = cat + test + test + one;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, false);
        assertion = mod.get().equals(cat + dogs + test + one);
        reason = "ReplaceFirst #37 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(cat + dogs + one + test.toUpperCase());
        reason = "ReplaceFirst #38 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, true);
        assertion = mod.get().equals(cat + dogs + one + test.toLowerCase());
        reason = "ReplaceFirst #39 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, dogs, false);
        assertion = mod.get().equals(cat + dogs + one + test);
        reason = "ReplaceFirst #40 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement - Smaller

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, true);
        assertion = mod.get().equals(cat + two + one + test.toUpperCase());
        reason = "ReplaceFirst #41 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, true);
        assertion = mod.get().equals(cat + two + one + test.toLowerCase());
        reason = "ReplaceFirst #42 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, two, false);
        assertion = mod.get().equals(cat + two + one + test);
        reason = "ReplaceFirst #43 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement - Larger

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, true);
        assertion = mod.get().equals(cat + elephant + one + test.toUpperCase());
        reason = "ReplaceFirst #44 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, true);
        assertion = mod.get().equals(cat + elephant + one + test.toLowerCase());
        reason = "ReplaceFirst #45 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceFirst(test, elephant, false);
        assertion = mod.get().equals(cat + elephant + one + test);
        reason = "ReplaceFirst #46 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Remove First")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runRemoveFirstTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";
        String dogs = "dOGs";

        // Remove at end

        String phrase = one + test.toUpperCase();
        StringMod mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #1 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #2 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #3 failed";
        assertThat(reason, assertion);

        // No modifications
        mod.removeFirst(dogs.toUpperCase(), false);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #4 failed";
        assertThat(reason, assertion);

        // Remove at beginning

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #5 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #6 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #7 failed";
        assertThat(reason, assertion);

        // Remove in middle

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirst #8 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirst #9 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirst #10 failed";
        assertThat(reason, assertion);

        // Remove at end - Smaller

        phrase = one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #11 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #12 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #13 failed";
        assertThat(reason, assertion);

        // Remove at beginning - Smaller

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #14 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #15 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #16 failed";
        assertThat(reason, assertion);

        // Remove in middle - Smaller

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirst #17 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirst #18 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirst #19 failed";
        assertThat(reason, assertion);

        // Remove at end - Larger

        phrase = one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #20 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #21 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #22 failed";
        assertThat(reason, assertion);

        // Remove at beginning - Larger

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #23 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #24 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveFirst #25 failed";
        assertThat(reason, assertion);

        // Remove in middle - Larger

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirst #26 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirst #27 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveFirst #28 failed";
        assertThat(reason, assertion);

        // Remove - Replacement at end

        phrase = one + test.toUpperCase() + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one + test.toUpperCase());
        reason = "RemoveFirst #29 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase() + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(one + test.toLowerCase());
        reason = "RemoveFirst #30 failed";
        assertThat(reason, assertion);

        phrase = one + test + test;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(one + test);
        reason = "RemoveFirst #31 failed";
        assertThat(reason, assertion);

        // Remove - Replacement at beginning

        phrase = test.toUpperCase() + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(test.toUpperCase() + one);
        reason = "RemoveFirst #32 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(test.toLowerCase() + one);
        reason = "RemoveFirst #33 failed";
        assertThat(reason, assertion);

        phrase = test + test + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(test + one);
        reason = "RemoveFirst #34 failed";
        assertThat(reason, assertion);

        // Remove - Replacement in middle

        phrase = cat + test.toUpperCase() + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one);
        reason = "RemoveFirst #35 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one);
        reason = "RemoveFirst #36 failed";
        assertThat(reason, assertion);

        phrase = cat + test + test + one;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(cat + test + one);
        reason = "RemoveFirst #37 failed";
        assertThat(reason, assertion);

        // Remove - Alternating replacement

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one + test.toUpperCase());
        reason = "RemoveFirst #38 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one + test.toLowerCase());
        reason = "RemoveFirst #39 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(cat + one + test);
        reason = "RemoveFirst #40 failed";
        assertThat(reason, assertion);

        // Remove - Alternating replacement - Smaller

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one + test.toUpperCase());
        reason = "RemoveFirst #41 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one + test.toLowerCase());
        reason = "RemoveFirst #42 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(cat + one + test);
        reason = "RemoveFirst #43 failed";
        assertThat(reason, assertion);

        // Remove - Alternating replacement - Larger

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one + test.toUpperCase());
        reason = "RemoveFirst #44 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeFirst(test, true);
        assertion = mod.get().equals(cat + one + test.toLowerCase());
        reason = "RemoveFirst #45 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.removeFirst(test, false);
        assertion = mod.get().equals(cat + one + test);
        reason = "RemoveFirst #46 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Replace Last")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runReplaceLastTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";
        String dogs = "dOGs";
        String elephant = ELEPHANT;
        String two = "two";

        // Replacement at end

        String phrase = one + test.toUpperCase();
        StringMod mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceLast #1 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceLast #2 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, false);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceLast #3 failed";
        assertThat(reason, assertion);

        // No modifications
        mod.replaceLast(dogs.toUpperCase(), test, false);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceLast #4 failed";
        assertThat(reason, assertion);

        // Replacement at beginning

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(dogs + one);
        reason = "ReplaceLast #5 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(dogs + one);
        reason = "ReplaceLast #6 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, false);
        assertion = mod.get().equals(dogs + one);
        reason = "ReplaceLast #7 failed";
        assertThat(reason, assertion);

        // Replacement in middle

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(cat + dogs + one);
        reason = "ReplaceLast #8 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(cat + dogs + one);
        reason = "ReplaceLast #9 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, false);
        assertion = mod.get().equals(cat + dogs + one);
        reason = "ReplaceLast #10 failed";
        assertThat(reason, assertion);

        // Replacement at end - Smaller

        phrase = one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, true);
        assertion = mod.get().equals(one + two);
        reason = "ReplaceLast #11 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, true);
        assertion = mod.get().equals(one + two);
        reason = "ReplaceLast #12 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, false);
        assertion = mod.get().equals(one + two);
        reason = "ReplaceLast #13 failed";
        assertThat(reason, assertion);

        // Replacement at beginning - Smaller

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, true);
        assertion = mod.get().equals(two + one);
        reason = "ReplaceLast #14 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, true);
        assertion = mod.get().equals(two + one);
        reason = "ReplaceLast #15 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, false);
        assertion = mod.get().equals(two + one);
        reason = "ReplaceLast #16 failed";
        assertThat(reason, assertion);

        // Replacement in middle - Smaller

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, true);
        assertion = mod.get().equals(cat + two + one);
        reason = "ReplaceLast #17 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, true);
        assertion = mod.get().equals(cat + two + one);
        reason = "ReplaceLast #18 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, false);
        assertion = mod.get().equals(cat + two + one);
        reason = "ReplaceLast #19 failed";
        assertThat(reason, assertion);

        // Replacement at end - Larger

        phrase = one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, true);
        assertion = mod.get().equals(one + elephant);
        reason = "ReplaceLast #20 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, true);
        assertion = mod.get().equals(one + elephant);
        reason = "ReplaceLast #21 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, false);
        assertion = mod.get().equals(one + elephant);
        reason = "ReplaceLast #22 failed";
        assertThat(reason, assertion);

        // Replacement at beginning - Larger

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, true);
        assertion = mod.get().equals(elephant + one);
        reason = "ReplaceLast #23 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, true);
        assertion = mod.get().equals(elephant + one);
        reason = "ReplaceLast #24 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, false);
        assertion = mod.get().equals(elephant + one);
        reason = "ReplaceLast #25 failed";
        assertThat(reason, assertion);

        // Replacement in middle - Larger

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, true);
        assertion = mod.get().equals(cat + elephant + one);
        reason = "ReplaceLast #26 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, true);
        assertion = mod.get().equals(cat + elephant + one);
        reason = "ReplaceLast #27 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, false);
        assertion = mod.get().equals(cat + elephant + one);
        reason = "ReplaceLast #28 failed";
        assertThat(reason, assertion);

        // Repeating - Replacement at end

        phrase = one + test.toUpperCase() + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(one + test.toUpperCase() + dogs);
        reason = "ReplaceLast #29 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase() + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(one + test.toLowerCase() + dogs);
        reason = "ReplaceLast #30 failed";
        assertThat(reason, assertion);

        phrase = one + test + test;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, false);
        assertion = mod.get().equals(one + test + dogs);
        reason = "ReplaceLast #31 failed";
        assertThat(reason, assertion);

        // Repeating - Replacement at beginning

        phrase = test.toUpperCase() + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(test.toUpperCase() + dogs + one);
        reason = "ReplaceLast #32 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(test.toLowerCase() + dogs + one);
        reason = "ReplaceLast #33 failed";
        assertThat(reason, assertion);

        phrase = test + test + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, false);
        assertion = mod.get().equals(test + dogs + one);
        reason = "ReplaceLast #34 failed";
        assertThat(reason, assertion);

        // Repeating - Replacement in middle

        phrase = cat + test.toUpperCase() + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + dogs + one);
        reason = "ReplaceLast #35 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + dogs + one);
        reason = "ReplaceLast #36 failed";
        assertThat(reason, assertion);

        phrase = cat + test + test + one;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, false);
        assertion = mod.get().equals(cat + test + dogs + one);
        reason = "ReplaceLast #37 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one + dogs);
        reason = "ReplaceLast #38 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one + dogs);
        reason = "ReplaceLast #39 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceLast(test, dogs, false);
        assertion = mod.get().equals(cat + test + one + dogs);
        reason = "ReplaceLast #40 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement - Smaller

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one + two);
        reason = "ReplaceLast #41 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one + two);
        reason = "ReplaceLast #42 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceLast(test, two, false);
        assertion = mod.get().equals(cat + test + one + two);
        reason = "ReplaceLast #43 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement - Larger

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one + elephant);
        reason = "ReplaceLast #44 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one + elephant);
        reason = "ReplaceLast #45 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceLast(test, elephant, false);
        assertion = mod.get().equals(cat + test + one + elephant);
        reason = "ReplaceLast #46 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Remove Last")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runRemoveLastTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";
        String dogs = "dOGs";

        // Remove at end

        String phrase = one + test.toUpperCase();
        StringMod mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #1 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #2 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #3 failed";
        assertThat(reason, assertion);

        // No modifications
        mod.removeLast(dogs.toUpperCase(), false);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #4 failed";
        assertThat(reason, assertion);

        // Remove at beginning

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #5 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #6 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #7 failed";
        assertThat(reason, assertion);

        // Remove in middle

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveLast #8 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveLast #9 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveLast #10 failed";
        assertThat(reason, assertion);

        // Remove at end - Smaller

        phrase = one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #11 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #12 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #13 failed";
        assertThat(reason, assertion);

        // Remove at beginning - Smaller

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #14 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #15 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #16 failed";
        assertThat(reason, assertion);

        // Remove in middle - Smaller

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveLast #17 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveLast #18 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveLast #19 failed";
        assertThat(reason, assertion);

        // Remove at end - Larger

        phrase = one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #20 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #21 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #22 failed";
        assertThat(reason, assertion);

        // Remove at beginning - Larger

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #23 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #24 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveLast #25 failed";
        assertThat(reason, assertion);

        // Remove in middle - Larger

        phrase = cat + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveLast #26 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveLast #27 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(cat + one);
        reason = "RemoveLast #28 failed";
        assertThat(reason, assertion);

        // Remove - Replacement at end

        phrase = one + test.toUpperCase() + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one + test.toUpperCase());
        reason = "RemoveLast #29 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase() + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(one + test.toLowerCase());
        reason = "RemoveLast #30 failed";
        assertThat(reason, assertion);

        phrase = one + test + test;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(one + test);
        reason = "RemoveLast #31 failed";
        assertThat(reason, assertion);

        // Remove - Replacement at beginning

        phrase = test.toUpperCase() + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(test.toUpperCase() + one);
        reason = "RemoveLast #32 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(test.toLowerCase() + one);
        reason = "RemoveLast #33 failed";
        assertThat(reason, assertion);

        phrase = test + test + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(test + one);
        reason = "RemoveLast #34 failed";
        assertThat(reason, assertion);

        // Remove - Replacement in middle

        phrase = cat + test.toUpperCase() + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one);
        reason = "RemoveLast #35 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one);
        reason = "RemoveLast #36 failed";
        assertThat(reason, assertion);

        phrase = cat + test + test + one;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(cat + test + one);
        reason = "RemoveLast #37 failed";
        assertThat(reason, assertion);

        // Remove - Alternating replacement

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one);
        reason = "RemoveLast #38 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one);
        reason = "RemoveLast #39 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(cat + test + one);
        reason = "RemoveLast #40 failed";
        assertThat(reason, assertion);

        // Remove - Alternating replacement - Smaller

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one);
        reason = "RemoveLast #41 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one);
        reason = "RemoveLast #42 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(cat + test + one);
        reason = "RemoveLast #43 failed";
        assertThat(reason, assertion);

        // Remove - Alternating replacement - Larger

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one);
        reason = "RemoveLast #44 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeLast(test, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one);
        reason = "RemoveLast #45 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.removeLast(test, false);
        assertion = mod.get().equals(cat + test + one);
        reason = "RemoveLast #46 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Replace Ends With")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runReplaceEndsWithTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";
        String dogs = "dOGs";
        String elephant = ELEPHANT;
        String two = "two";

        // Replacement

        String phrase = one + test.toUpperCase();
        StringMod mod = new StringMod(phrase);
        mod.replaceEndsWith(test, dogs, true);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceEndsWith #1 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, dogs, true);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceEndsWith #2 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, dogs, false);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceEndsWith #3 failed";
        assertThat(reason, assertion);

        // No modifications
        mod.replaceEndsWith(dogs.toUpperCase(), test, false);
        assertion = mod.get().equals(one + dogs);
        reason = "ReplaceEndsWith #4 failed";
        assertThat(reason, assertion);

        // Replacement - Smaller

        phrase = one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, two, true);
        assertion = mod.get().equals(one + two);
        reason = "ReplaceEndsWith #5 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, two, true);
        assertion = mod.get().equals(one + two);
        reason = "ReplaceEndsWith #6 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, two, false);
        assertion = mod.get().equals(one + two);
        reason = "ReplaceEndsWith #7 failed";
        assertThat(reason, assertion);

        // Replacement - Larger

        phrase = one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, elephant, true);
        assertion = mod.get().equals(one + elephant);
        reason = "ReplaceEndsWith #8 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, elephant, true);
        assertion = mod.get().equals(one + elephant);
        reason = "ReplaceEndsWith #9 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, elephant, false);
        assertion = mod.get().equals(one + elephant);
        reason = "ReplaceEndsWith #10 failed";
        assertThat(reason, assertion);

        // Repeating - Replacement

        phrase = one + test.toUpperCase() + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, dogs, true);
        assertion = mod.get().equals(one + test.toUpperCase() + dogs);
        reason = "ReplaceEndsWith #11 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase() + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, dogs, true);
        assertion = mod.get().equals(one + test.toLowerCase() + dogs);
        reason = "ReplaceEndsWith #12 failed";
        assertThat(reason, assertion);

        phrase = one + test + test;
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, dogs, false);
        assertion = mod.get().equals(one + test + dogs);
        reason = "ReplaceEndsWith #13 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, dogs, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one + dogs);
        reason = "ReplaceEndsWith #14 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, dogs, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one + dogs);
        reason = "ReplaceEndsWith #15 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, dogs, false);
        assertion = mod.get().equals(cat + test + one + dogs);
        reason = "ReplaceEndsWith #16 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement - Smaller

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, two, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one + two);
        reason = "ReplaceEndsWith #17 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, two, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one + two);
        reason = "ReplaceEndsWith #18 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, two, false);
        assertion = mod.get().equals(cat + test + one + two);
        reason = "ReplaceEndsWith #19 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement - Larger

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, elephant, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one + elephant);
        reason = "ReplaceEndsWith #20 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, elephant, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one + elephant);
        reason = "ReplaceEndsWith #21 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.replaceEndsWith(test, elephant, false);
        assertion = mod.get().equals(cat + test + one + elephant);
        reason = "ReplaceEndsWith #22 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Remove Ends With")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runRemoveEndsWithTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";

        // Removal at beginning

        String phrase = one + test.toUpperCase();
        StringMod mod = new StringMod(phrase);
        mod.removeEndsWith(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveEndsWith #1 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeEndsWith(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveEndsWith #2 failed";
        assertThat(reason, assertion);

        phrase = one + test;
        mod = new StringMod(phrase);
        mod.removeEndsWith(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveEndsWith #3 failed";
        assertThat(reason, assertion);

        // No modifications
        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeEndsWith(test.toUpperCase(), false);
        assertion = mod.get().equals(phrase);
        reason = "RemoveEndsWith #4 failed";
        assertThat(reason, assertion);

        // Repeating - Removal at beginning

        phrase = one + test.toUpperCase() + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeEndsWith(test, true);
        assertion = mod.get().equals(one + test.toUpperCase());
        reason = "RemoveEndsWith #5 failed";
        assertThat(reason, assertion);

        phrase = one + test.toLowerCase() + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeEndsWith(test, true);
        assertion = mod.get().equals(one + test.toLowerCase());
        reason = "RemoveEndsWith #6 failed";
        assertThat(reason, assertion);

        phrase = one + test + test;
        mod = new StringMod(phrase);
        mod.removeEndsWith(test, false);
        assertion = mod.get().equals(one + test);
        reason = "RemoveEndsWith #7 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating Removal

        phrase = cat + test.toUpperCase() + one + test.toUpperCase();
        mod = new StringMod(phrase);
        mod.removeEndsWith(test, true);
        assertion = mod.get().equals(cat + test.toUpperCase() + one);
        reason = "RemoveEndsWith #8 failed";
        assertThat(reason, assertion);

        phrase = cat + test.toLowerCase() + one + test.toLowerCase();
        mod = new StringMod(phrase);
        mod.removeEndsWith(test, true);
        assertion = mod.get().equals(cat + test.toLowerCase() + one);
        reason = "RemoveEndsWith #9 failed";
        assertThat(reason, assertion);

        phrase = cat + test + one + test;
        mod = new StringMod(phrase);
        mod.removeEndsWith(test, false);
        assertion = mod.get().equals(cat + test + one);
        reason = "RemoveEndsWith #10 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Replace Starts With")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runReplaceStartsWithTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";
        String dogs = "dOGs";
        String elephant = ELEPHANT;
        String two = "two";

        // Replacement at beginning

        String phrase = test.toUpperCase() + one;
        StringMod mod = new StringMod(phrase);
        mod.replaceStartsWith(test, dogs, true);
        assertion = mod.get().equals(dogs + one);
        reason = "ReplaceStartsWith #1 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, dogs, true);
        assertion = mod.get().equals(dogs + one);
        reason = "ReplaceStartsWith #2 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, dogs, false);
        assertion = mod.get().equals(dogs + one);
        reason = "ReplaceStartsWith #3 failed";
        assertThat(reason, assertion);

        // No modifications
        mod.replaceStartsWith(dogs.toUpperCase(), test, false);
        assertion = mod.get().equals(dogs + one);
        reason = "ReplaceStartsWith #4 failed";
        assertThat(reason, assertion);

        // Replacement at beginning - Smaller

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, two, true);
        assertion = mod.get().equals(two + one);
        reason = "ReplaceStartsWith #5 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, two, true);
        assertion = mod.get().equals(two + one);
        reason = "ReplaceStartsWith #6 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, two, false);
        assertion = mod.get().equals(two + one);
        reason = "ReplaceStartsWith #7 failed";
        assertThat(reason, assertion);

        // Replacement at beginning - Larger

        phrase = test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, elephant, true);
        assertion = mod.get().equals(elephant + one);
        reason = "ReplaceStartsWith #8 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, elephant, true);
        assertion = mod.get().equals(elephant + one);
        reason = "ReplaceStartsWith #9 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, elephant, false);
        assertion = mod.get().equals(elephant + one);
        reason = "ReplaceStartsWith #10 failed";
        assertThat(reason, assertion);

        // Repeating - Replacement at beginning

        phrase = test.toUpperCase() + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, dogs, true);
        assertion = mod.get().equals(dogs + test.toUpperCase() + one);
        reason = "ReplaceStartsWith #11 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, dogs, true);
        assertion = mod.get().equals(dogs + test.toLowerCase() + one);
        reason = "ReplaceStartsWith #12 failed";
        assertThat(reason, assertion);

        phrase = test + test + one;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, dogs, false);
        assertion = mod.get().equals(dogs + test + one);
        reason = "ReplaceStartsWith #13 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement

        phrase = test.toUpperCase() + one + test.toUpperCase() + cat;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, dogs, true);
        assertion = mod.get().equals(dogs + one + test.toUpperCase() + cat);
        reason = "ReplaceStartsWith #14 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one + test.toLowerCase() + cat;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, dogs, true);
        assertion = mod.get().equals(dogs + one + test.toLowerCase() + cat);
        reason = "ReplaceStartsWith #15 failed";
        assertThat(reason, assertion);

        phrase = test + one + test + cat;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, dogs, false);
        assertion = mod.get().equals(dogs + one + test + cat);
        reason = "ReplaceStartsWith #16 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement - Smaller

        phrase = test.toUpperCase() + one + test.toUpperCase() + cat;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, two, true);
        assertion = mod.get().equals(two + one + test.toUpperCase() + cat);
        reason = "ReplaceStartsWith #17 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one + test.toLowerCase() + cat;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, two, true);
        assertion = mod.get().equals(two + one + test.toLowerCase() + cat);
        reason = "ReplaceStartsWith #18 failed";
        assertThat(reason, assertion);

        phrase = test + one + test + cat;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, two, false);
        assertion = mod.get().equals(two + one + test + cat);
        reason = "ReplaceStartsWith #19 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating replacement - Larger

        phrase = test.toUpperCase() + one + test.toUpperCase() + cat;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, elephant, true);
        assertion = mod.get().equals(elephant + one + test.toUpperCase() + cat);
        reason = "ReplaceStartsWith #20 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one + test.toLowerCase() + cat;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, elephant, true);
        assertion = mod.get().equals(elephant + one + test.toLowerCase() + cat);
        reason = "ReplaceStartsWith #21 failed";
        assertThat(reason, assertion);

        phrase = test + one + test + cat;
        mod = new StringMod(phrase);
        mod.replaceStartsWith(test, elephant, false);
        assertion = mod.get().equals(elephant + one + test + cat);
        reason = "ReplaceStartsWith #22 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Remove Starts With")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runRemoveStartsWithTest() {
        boolean assertion;
        String reason;

        String one = "oNe";
        String test = "Test";
        String cat = "caT";

        // Removal at beginning

        String phrase = test.toUpperCase() + one;
        StringMod mod = new StringMod(phrase);
        mod.removeStartsWith(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveStartsWith #1 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeStartsWith(test, true);
        assertion = mod.get().equals(one);
        reason = "RemoveStartsWith #2 failed";
        assertThat(reason, assertion);

        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeStartsWith(test, false);
        assertion = mod.get().equals(one);
        reason = "RemoveStartsWith #3 failed";
        assertThat(reason, assertion);

        // No modifications
        phrase = test + one;
        mod = new StringMod(phrase);
        mod.removeStartsWith(test.toUpperCase(), false);
        assertion = mod.get().equals(phrase);
        reason = "RemoveStartsWith #4 failed";
        assertThat(reason, assertion);

        // Repeating - Removal at beginning

        phrase = test.toUpperCase() + test.toUpperCase() + one;
        mod = new StringMod(phrase);
        mod.removeStartsWith(test, true);
        assertion = mod.get().equals(test.toUpperCase() + one);
        reason = "RemoveStartsWith #5 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + test.toLowerCase() + one;
        mod = new StringMod(phrase);
        mod.removeStartsWith(test, true);
        assertion = mod.get().equals(test.toLowerCase() + one);
        reason = "RemoveStartsWith #6 failed";
        assertThat(reason, assertion);

        phrase = test + test + one;
        mod = new StringMod(phrase);
        mod.removeStartsWith(test, false);
        assertion = mod.get().equals(test + one);
        reason = "RemoveStartsWith #7 failed";
        assertThat(reason, assertion);

        // Repeating - Alternating Removal

        phrase = test.toUpperCase() + one + test.toUpperCase() + cat;
        mod = new StringMod(phrase);
        mod.removeStartsWith(test, true);
        assertion = mod.get().equals(one + test.toUpperCase() + cat);
        reason = "RemoveStartsWith #8 failed";
        assertThat(reason, assertion);

        phrase = test.toLowerCase() + one + test.toLowerCase() + cat;
        mod = new StringMod(phrase);
        mod.removeStartsWith(test, true);
        assertion = mod.get().equals(one + test.toLowerCase() + cat);
        reason = "RemoveStartsWith #9 failed";
        assertThat(reason, assertion);

        phrase = test + one + test + cat;
        mod = new StringMod(phrase);
        mod.removeStartsWith(test, false);
        assertion = mod.get().equals(one + test + cat);
        reason = "RemoveStartsWith #10 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Substring")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runSubstringTest() {
        boolean assertion;
        String reason;

        String cat = "caT";
        String dogs = "dOGs";
        String elephant = ELEPHANT;

        String phrase = cat + dogs + elephant;
        StringMod mod = new StringMod(phrase);
        mod.substring(0);
        assertion = mod.get().equals(phrase);
        reason = "Substring #1 failed";
        assertThat(reason, assertion);

        mod.substring(0, cat.length());
        assertion = mod.get().equals(cat);
        reason = "Substring #2 failed";
        assertThat(reason, assertion);

        phrase = cat + dogs + elephant;
        mod = new StringMod(phrase);
        mod.substring(cat.length());
        assertion = mod.get().equals(dogs + elephant);
        reason = "Substring #3 failed";
        assertThat(reason, assertion);

        phrase = cat + dogs + elephant;
        mod = new StringMod(phrase);
        mod.substring(cat.length(), cat.length() + dogs.length());
        assertion = mod.get().equals(dogs);
        reason = "Substring #4 failed";
        assertThat(reason, assertion);

        phrase = cat + dogs + elephant;
        mod = new StringMod(phrase);
        mod.substring(cat.length() + dogs.length(), phrase.length());
        assertion = mod.get().equals(elephant);
        reason = "Substring #5 failed";
        assertThat(reason, assertion);

        // Invalid (stored value not modified

        mod.substring(phrase.length());
        assertion = mod.get().equals(elephant);
        reason = "Substring #6 failed";
        assertThat(reason, assertion);

        mod.substring(2, phrase.length());
        assertion = mod.get().equals(elephant);
        reason = "Substring #7 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Trim")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runTrimTest() {
        boolean assertion;
        String reason;

        String spaces = SPACES;
        String random = Rand.alphanumeric(5, 10);

        StringMod mod = new StringMod(random);
        mod.append(spaces);
        mod.prepend(spaces);

        assertion = mod.get().equals(spaces + random + spaces);
        reason = "Trim #1 failed";
        assertThat(reason, assertion);

        mod.trim();
        assertion = mod.get().equals(random);
        reason = "Trim #2 failed";
        assertThat(reason, assertion);

        String random2 = Rand.alphanumeric(1, 5);
        mod = new StringMod(spaces + random2 + spaces + random + spaces);
        mod.trim();
        assertion = mod.get().equals(random2 + spaces + random);
        reason = "Trim #3 failed";
        assertThat(reason, assertion);

        mod = new StringMod(spaces + random);
        mod.trim();
        assertion = mod.get().equals(random);
        reason = "Trim #4 failed";
        assertThat(reason, assertion);

        mod = new StringMod(random + spaces);
        mod.trim();
        assertion = mod.get().equals(random);
        reason = "Trim #5 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Trim Non-Visible")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runTrimNonVisibleTest() {
        boolean assertion;
        String reason;

        String spaces = SPACES;
        String random = Rand.alphanumeric(5, 10);

        StringMod mod = new StringMod(random);
        mod.append(spaces);
        mod.prepend(spaces);

        assertion = mod.get().equals(spaces + random + spaces);
        reason = "Trim Non-Invisible #1 failed";
        assertThat(reason, assertion);

        mod.trimNonVisible();
        assertion = mod.get().equals(random);
        reason = "Trim Non-Invisible #2 failed";
        assertThat(reason, assertion);

        String random2 = Rand.alphanumeric(1, 5);
        mod = new StringMod(spaces + random2 + spaces + random + spaces);
        mod.trimNonVisible();
        assertion = mod.get().equals(random2 + spaces + random);
        reason = "Trim Non-Invisible #3 failed";
        assertThat(reason, assertion);

        mod = new StringMod(spaces + random);
        mod.trimNonVisible();
        assertion = mod.get().equals(random);
        reason = "Trim Non-Invisible #4 failed";
        assertThat(reason, assertion);

        mod = new StringMod(random + spaces);
        mod.trimNonVisible();
        assertion = mod.get().equals(random);
        reason = "Trim Non-Invisible #5 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Trim All")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runTrimAllTest() {
        boolean assertion;
        String reason;

        String spaces = SPACES;
        String random = Rand.alphanumeric(5, 10);

        StringMod mod = new StringMod(random);
        mod.append(spaces);
        mod.prepend(spaces);

        assertion = mod.get().equals(spaces + random + spaces);
        reason = "Trim All #1 failed";
        assertThat(reason, assertion);

        mod.trimAll();
        assertion = mod.get().equals(random);
        reason = "Trim All #2 failed";
        assertThat(reason, assertion);

        String random2 = Rand.alphanumeric(1, 5);
        mod = new StringMod(spaces + random2 + spaces + random + spaces);
        mod.trimAll();
        assertion = mod.get().equals(random2 + spaces + random);
        reason = "Trim All #3 failed";
        assertThat(reason, assertion);

        mod = new StringMod(spaces + random);
        mod.trimAll();
        assertion = mod.get().equals(random);
        reason = "Trim All #4 failed";
        assertThat(reason, assertion);

        mod = new StringMod(random + spaces);
        mod.trimAll();
        assertion = mod.get().equals(random);
        reason = "Trim All #5 failed";
        assertThat(reason, assertion);
    }

    @Features("StringMod")
    @Stories("Split")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void runSplitTest() {
        boolean assertion;
        String reason;

        // test suite : positions; test suite : positions
        String test1Name = "test1";
        String test1Positions = "1-3";
        String test1 = test1Name + ":" + test1Positions;
        String test2Name = "test2";
        String test2Positions = "2-8";
        String test2 = test2Name + ":" + test2Positions;
        String test3Name = "test3";
        String test3Positions = "8,15-20";
        String test3 = test3Name + ":" + test3Positions;
        String example = test1 + ";" + test2 + ";" + test3;

        StringMod mod = new StringMod(example);
        mod.split(";", 0);
        assertion = mod.get().equals(test1);
        reason = "Split #1 failed";
        assertThat(reason, assertion);

        mod.split(":", 1);
        assertion = mod.get().equals(test1Positions);
        reason = "Split #2 failed";
        assertThat(reason, assertion);

        mod = new StringMod(example);
        mod.split(";", 1);
        assertion = mod.get().equals(test2);
        reason = "Split #3 failed";
        assertThat(reason, assertion);

        mod.split(":", 0);
        assertion = mod.get().equals(test2Name);
        reason = "Split #4 failed";
        assertThat(reason, assertion);

        mod = new StringMod(example);
        mod.split(";", 2);
        assertion = mod.get().equals(test3);
        reason = "Split #5 failed";
        assertThat(reason, assertion);

        mod.split(":", 0);
        assertion = mod.get().equals(test3Name);
        reason = "Split #6 failed";
        assertThat(reason, assertion);

        //

        mod = new StringMod(example);
        mod.split(";", 5);
        assertion = mod.get().equals(example);
        reason = "Split Invalid #1 failed";
        assertThat(reason, assertion);

        mod.split("\\", 0);
        assertion = mod.get().equals(example);
        reason = "Split Invalid #2 failed";
        assertThat(reason, assertion);
    }

}
