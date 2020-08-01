package com.taf.automation.ui.support;

import com.taf.automation.ui.support.util.Utils;

/**
 * The purpose of this class is to store various accents sets
 */
public class Accents {
    /**
     * All the code points for French Accents from <a
     * href="http://character-code.com/french-html-codes.php">character-code.com</a>
     */
    private static final int[] _French = new int[]{
            //
            192, // Ã
            224, // Ã 
            194, // Ã
            226, // Ã¢
            198, // Ã
            230, // Ã¦
            199, // Ã
            231, // Ã§
            200, // Ã
            232, // Ã¨
            201, // Ã
            233, // Ã©
            202, // Ã
            234, // Ãª
            203, // Ã
            235, // Ã«
            206, // Ã
            238, // Ã®
            207, // Ã
            239, // Ã¯
            212, // Ã
            244, // Ã´
            338, // Å
            339, // Å
            217, // Ã
            249, // Ã¹
            219, // Ã
            251, // Ã»
            220, // Ã
            252 // Ã¼
    };

    /**
     * All the code points for Czech, Slovak & Slovenian from <a
     * href="http://character-code.com/czech_slovak_slovenian-html-codes.php">character-code.com</a>
     */
    private static final int[] _Czech = new int[]{
            //
            193, // Ã
            225, // Ã¡
            260, // Ä
            261, // Ä
            196, // Ã
            228, // Ã¤
            201, // Ã
            233, // Ã©
            280, // Ä
            281, // Ä
            282, // Ä
            283, // Ä
            205, // Ã
            237, // Ã­
            211, // Ã
            243, // Ã³
            212, // Ã
            244, // Ã´
            218, // Ã
            250, // Ãº
            366, // Å®
            367, // Å¯
            221, // Ã
            253, // Ã½
            268, // Ä
            269, // Ä
            271, // Ä
            357, // Å¥
            313, // Ä¹
            314, // Äº
            327, // Å
            328, // Å
            340, // Å
            341, // Å
            344, // Å
            345, // Å
            352, // Å 
            353, // Å¡
            381, // Å½
            382 // Å¾
    };

    /**
     * All the code points for German from <a
     * href="http://character-code.com/german-html-codes.php">character-code.com</a>
     */
    private static final int[] _German = new int[]{
            //
            196, // Ã
            228, // Ã¤
            201, // Ã
            233, // Ã©
            214, // Ã
            246, // Ã¶
            220, // Ã
            252, // Ã¼
            223 // Ã
    };

    /**
     * All the code points for Hawaiian from <a
     * href="http://character-code.com/hawaiian-html-codes.php">character-code.com</a>
     */
    private static final int[] _Hawaiian = new int[]{
            //
            196, // Ã
            228, // Ã¤
            274, // Ä
            275, // Ä
            298, // Äª
            299, // Ä«
            332, // Å
            333, // Å
            362, // Åª
            363, // Å«
            699 // Ê» (Lowercase y-umlaut)
    };

    /**
     * All the code points for Italian from <a
     * href="http://character-code.com/italian-html-codes.php">character-code.com</a>
     */
    private static final int[] _Italian = new int[]{
            //
            192, // Ã
            224, // Ã 
            193, // Ã
            225, // Ã¡
            200, // Ã
            232, // Ã¨
            201, // Ã
            233, // Ã©
            204, // Ã
            236, // Ã¬
            205, // Ã
            237, // Ã­
            210, // Ã
            242, // Ã²
            211, // Ã
            243, // Ã³
            217, // Ã
            249, // Ã¹
            218, // Ã
            250 // Ãº
    };

    /**
     * All the code points for Polish from <a
     * href="http://character-code.com/polish-html-codes.php">character-code.com</a>
     */
    private static final int[] _Polish = new int[]{
            //
            260, // Ä
            261, // Ä
            280, // Ä
            281, // Ä
            211, // Ã
            243, // Ã³
            262, // Ä
            263, // Ä
            321, // Å
            322, // Å
            323, // Å
            324, // Å
            346, // Å
            347, // Å
            377, // Å¹
            378, // Åº
            379, // Å»
            380 // Å¼
    };

    /**
     * All the code points for Romanian from <a
     * href="http://character-code.com/romanian-html-codes.php">character-code.com</a>
     */
    private static final int[] _Romanian = new int[]{
            //
            258, // Ä
            259, // Ä
            194, // Ã
            226, // Ã¢
            206, // Ã
            238, // Ã®
            536, // È
            537, // È
            350, // Å
            351, // Å
            538, // È
            539, // È
            354, // Å¢
            355 // Å£
    };

    /**
     * All the code points for Russian from <a
     * href="http://character-code.com/russian-html-codes.php">character-code.com</a>
     */
    private static final int[] _Russian = new int[]{
            //
            1040, // Ð
            1072, // Ð°
            1041, // Ð
            1073, // Ð±
            1042, // Ð
            1074, // Ð²
            1043, // Ð
            1075, // Ð³
            1044, // Ð
            1076, // Ð´
            1045, // Ð
            1077, // Ðµ
            1046, // Ð
            1078, // Ð¶
            1047, // Ð
            1079, // Ð·
            1048, // Ð
            1080, // Ð¸
            1049, // Ð
            1081, // Ð¹
            1050, // Ð
            1082, // Ðº
            1051, // Ð
            1083, // Ð»
            1052, // Ð
            1084, // Ð¼
            1053, // Ð
            1085, // Ð½
            1054, // Ð
            1086, // Ð¾
            1055, // Ð
            1087, // Ð¿
            1056, // Ð 
            1088, // Ñ
            1057, // Ð¡
            1089, // Ñ
            1058, // Ð¢
            1090, // Ñ
            1059, // Ð£
            1091, // Ñ
            1060, // Ð¤
            1092, // Ñ
            1061, // Ð¥
            1093, // Ñ
            1062, // Ð¦
            1094, // Ñ
            1063, // Ð§
            1095, // Ñ
            1064, // Ð¨
            1096, // Ñ
            1065, // Ð©
            1097, // Ñ
            1066, // Ðª
            1098, // Ñ
            1067, // Ð«
            1099, // Ñ
            1068, // Ð¬
            1100, // Ñ
            1069, // Ð­
            1101, // Ñ
            1070, // Ð®
            1102, // Ñ
            1071, // Ð¯
            1103 // Ñ
    };

    /**
     * All the code points for Spanish from <a
     * href="http://character-code.com/spanish-html-codes.php">character-code.com</a>
     */
    private static final int[] _Spanish = new int[]{
            //
            193, // Ã
            225, // Ã¡
            201, // Ã
            233, // Ã©
            205, // Ã
            237, // Ã­
            209, // Ã
            241, // Ã±
            211, // Ã
            243, // Ã³
            218, // Ã
            250, // Ãº
            220, // Ã
            252, // Ã¼
    };

    /**
     * All the code points for Turkish from <a
     * href="http://character-code.com/turkish-html-codes.php">character-code.com</a>
     */
    private static final int[] _Turkish = new int[]{
            //
            304, // Ä°
            305, // Ä±
            214, // Ã
            246, // Ã¶
            220, // Ã
            252, // Ã¼
            199, // Ã
            231, // Ã§
            286, // Ä
            287, // Ä
            350, // Å
            351, // Å
    };

    /**
     * Get the French Accents string
     *
     * @return String
     */
    public static String getFrench() {
        return Utils.toString(_French);
    }

    /**
     * Get the Czech, Slovak &amp; Slovenian Accents string
     *
     * @return String
     */
    public static String getCzech() {
        return Utils.toString(_Czech);
    }

    /**
     * Get the German Accents string
     *
     * @return String
     */
    public static String getGerman() {
        return Utils.toString(_German);
    }

    /**
     * Get the Hawaiian Accents string
     *
     * @return String
     */
    public static String getHawaiian() {
        return Utils.toString(_Hawaiian);
    }

    /**
     * Get the Italian Accents string
     *
     * @return String
     */
    public static String getItalian() {
        return Utils.toString(_Italian);
    }

    /**
     * Get the Polish Accents string
     *
     * @return String
     */
    public static String getPolish() {
        return Utils.toString(_Polish);
    }

    /**
     * Get the Romanian Accents string
     *
     * @return String
     */
    public static String getRomanian() {
        return Utils.toString(_Romanian);
    }

    /**
     * Get the Russian Accents string
     *
     * @return String
     */
    public static String getRussian() {
        return Utils.toString(_Russian);
    }

    /**
     * Get the Spanish Accents string
     *
     * @return String
     */
    public static String getSpanish() {
        return Utils.toString(_Spanish);
    }

    /**
     * Get the Turkish Accents string
     *
     * @return String
     */
    public static String getTurkish() {
        return Utils.toString(_Turkish);
    }

}

