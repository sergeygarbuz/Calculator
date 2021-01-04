package ru.garbuz;


import java.util.ArrayList;

public enum RomanDigits {

    I, V, X, L, C, D, M;

    public static ArrayList<String> allAllowedRomanList = allRomans();

    private static ArrayList<String> allRomans() {
        ArrayList<String> allAllowedRomanList = new ArrayList<>();
        allAllowedRomanList.add("0");
        for (int i = 1; i < 4000; i++) {
            allAllowedRomanList.add(arabicToRoman(i));
        }
        return allAllowedRomanList;
    }

    public static String arabicToRoman(Integer arabicNumber) {
        int thousands = arabicNumber / 1000;
        int hundreds = (arabicNumber - 1000 * thousands) / 100;
        int tens = (arabicNumber - 1000 * thousands - 100 * hundreds) / 10;
        int ones = arabicNumber - 1000 * thousands - 100 * hundreds - 10 * tens;
        String roman = "";
        switch (thousands) {
            case 1:
                roman = roman + "M";
                break;
            case 2:
                roman = roman + "MM";
                break;
            case 3:
                roman = roman + "MMM";
                break;
        }
        switch (hundreds) {
            case 1:
                roman = roman + "C";
                break;
            case 2:
                roman = roman + "CC";
                break;
            case 3:
                roman = roman + "CCC";
                break;
            case 4:
                roman = roman + "CD";
                break;
            case 5:
                roman = roman + "D";
                break;
            case 6:
                roman = roman + "DC";
                break;
            case 7:
                roman = roman + "DCC";
                break;
            case 8:
                roman = roman + "DCCC";
                break;
            case 9:
                roman = roman + "CM";
                break;
        }
        switch (tens) {
            case 1:
                roman = roman + "X";
                break;
            case 2:
                roman = roman + "XX";
                break;
            case 3:
                roman = roman + "XXX";
                break;
            case 4:
                roman = roman + "XL";
                break;
            case 5:
                roman = roman + "L";
                break;
            case 6:
                roman = roman + "LX";
                break;
            case 7:
                roman = roman + "LXX";
                break;
            case 8:
                roman = roman + "LXXX";
                break;
            case 9:
                roman = roman + "XC";
                break;
        }
        switch (ones) {
            case 1:
                roman = roman + "I";
                break;
            case 2:
                roman = roman + "II";
                break;
            case 3:
                roman = roman + "III";
                break;
            case 4:
                roman = roman + "IV";
                break;
            case 5:
                roman = roman + "V";
                break;
            case 6:
                roman = roman + "VI";
                break;
            case 7:
                roman = roman + "VII";
                break;
            case 8:
                roman = roman + "VIII";
                break;
            case 9:
                roman = roman + "IX";
                break;
        }
        return roman;
    }


}





