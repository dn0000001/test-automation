package com.taf.automation.ui.support;

import com.taf.automation.ui.support.util.AssertJUtil;

@SuppressWarnings("java:S3252")
public class StringCompare {

    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    private static String cleanArray(String[] array) {
        StringBuilder cleanString = new StringBuilder();
        for (String word : array) {
            String clean = word.trim();
            if (!clean.isEmpty()) {
                cleanString.append(clean).append(" ");
            }
        }
        return cleanString.toString().trim();
    }

    public static String normalize(String in) {
        String[] words = in.replaceAll("\\W", " ").split(" ");
        return cleanArray(words);
    }

    public static String normalizeIgnoreDigits(String in) {
        String[] words = in.replaceAll("[\\W,0-9]", " ").split(" ");
        return cleanArray(words);
    }

    public static int computeLevenshteinDistance(CharSequence seq1, CharSequence seq2) {
        int maxLength = Math.max(seq1.length(), seq2.length());
        int[][] distance = new int[seq1.length() + 1][seq2.length() + 1];

        for (int i = 0; i <= seq1.length(); i++) {
            distance[i][0] = i;
        }

        for (int j = 1; j <= seq2.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= seq1.length(); i++) {
            for (int j = 1; j <= seq2.length(); j++) {
                distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + ((seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? 0 : 1));
            }
        }

        return distance[seq1.length()][seq2.length()];
    }

    public static void assertEqualOmitSpacesAndNonWord(String str1, String str2) {
        AssertJUtil.assertThat(normalize(str1)).isEqualTo(normalize(str2));
    }

}
