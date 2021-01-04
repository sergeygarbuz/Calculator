package ru.garbuz;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class MyExpression {

    private MyNumber a;
    private MyNumber b;
    private MyAction action;
    private TypeNumber resultType;

    public MyExpression parseExpression(String stringExpression) throws Exception {
        if (stringExpression.length()>=3) {
            String firstDigit = stringExpression.substring(0,1);
            String nextDigit;
            switch (defineDigitType(firstDigit)){
                //РИМСКИЕ
                case ROMAN:
                    //парсим первое римское число
                    a = new MyNumber();
                    a.setStringValue(firstDigit);
                    for(int i = 1; i < stringExpression.length(); i++){
                        if (action!=null) break; //выходим из цикла, если дошли до знака действия
                        nextDigit = stringExpression.substring(i,i+1);
                        switch (defineDigitType(nextDigit)){
                            case ROMAN:
                                a.setStringValue(a.getStringValue()+nextDigit);
                                break;
                            case ARABIC:
                                throw new Exception("Цифры должны быть либо только римскими либо только арабскими");
                            case UNKNOWN:
                                //конец первого римского числа, проверяем что дальше идет знак
                                if (defineAction(nextDigit)==MyAction.UNKNOWN){
                                    throw new Exception("В выражении использовано неизвестное действие");
                                }
                                else {
                                    action = defineAction(nextDigit);
                                }
                                break;
                       }
                    }
                    a.setIntegerValue(getRomanNumberValue(a.getStringValue()));
                    if (a.getIntegerValue()>10) throw new Exception("Числа должны быть не больше 10");
                    //парсим второе римское число
                    b = new MyNumber();
                    b.setStringValue("");
                    for(int i = a.getStringValue().length()+1; i < stringExpression.length(); i++){
                        nextDigit = stringExpression.substring(i,i+1);
                        switch (defineDigitType(nextDigit)){
                            case ROMAN:
                                b.setStringValue(b.getStringValue()+nextDigit);
                                break;
                            case ARABIC:
                                throw new Exception("Цифры должны быть либо только римскими либо только арабскими");
                            case UNKNOWN:
                                //конец второго римского числа, должен быть конец выражения
                                throw new Exception("Выражение должно заканчиваться после второго числа");
                        }
                    }
                    b.setIntegerValue(getRomanNumberValue(b.getStringValue()));
                    if (b.getIntegerValue()>10) throw new Exception("Числа должны быть не больше 10");
                    this.setResultType(TypeNumber.ROMAN);
                    return this;
                //АРАБСКИЕ
                case ARABIC:
                    //парсим первое арабское число
                    a = new MyNumber();
                    a.setStringValue(firstDigit);
                    for(int i = 1; i < stringExpression.length(); i++){
                        if (action!=null) break; //выходим из цикла, если дошли до знака действия
                        nextDigit = stringExpression.substring(i,i+1);
                        switch (defineDigitType(nextDigit)){
                            case ROMAN:
                                throw new Exception("Цифры должны быть либо только римскими либо только арабскими");
                            case ARABIC:
                                a.setStringValue(a.getStringValue()+nextDigit);
                                break;
                            case UNKNOWN:
                                //конец первого арабского числа, проверяем что дальше идет знак
                                if (defineAction(nextDigit)==MyAction.UNKNOWN){
                                    throw new Exception("В выражении использовано неизвестное действие");
                                }
                                else {
                                    action = defineAction(nextDigit);
                                }
                                break;
                        }
                    }
                    a.setIntegerValue(Integer.parseInt(a.getStringValue()));
                    if (a.getIntegerValue()>10) throw new Exception("Числа должны быть не больше 10");
                    //парсим второе арабское число
                    b = new MyNumber();
                    b.setStringValue("");
                    for(int i = a.getStringValue().length()+1; i < stringExpression.length(); i++){
                        nextDigit = stringExpression.substring(i,i+1);
                        switch (defineDigitType(nextDigit)){
                            case ROMAN:
                                throw new Exception("Цифры должны быть либо только римскими либо только арабскими");
                            case ARABIC:
                                b.setStringValue(b.getStringValue()+nextDigit);
                                break;
                            case UNKNOWN:
                                //конец второго арабского числа, должен быть конец выражения
                                throw new Exception("Выражение должно заканчиваться после второго числа");
                        }
                    }
                    b.setIntegerValue(Integer.parseInt(b.getStringValue()));
                    if (b.getIntegerValue()>10) throw new Exception("Числа должны быть не больше 10");
                    this.setResultType(TypeNumber.ARABIC);
                    return this;
                //НИ РИМСКИЕ НИ АРАБСКИЕ
                case UNKNOWN:
                    throw new Exception("Первый символ в выражении не является арабской или римской цифрой");
            }
        } else {
            throw new Exception("Вы ввели слишком мало символов");
        }
        return this;
    }

    private TypeNumber defineDigitType(String digit){
        for (RomanDigits romanDigit : RomanDigits.values()) {
            if (romanDigit.name().equals(digit)) {
                return TypeNumber.ROMAN;
            }
        }
        for (int i = 0; i <=9; i++) {
            if (Integer.toString(i).equals(digit)) {
                return TypeNumber.ARABIC;
            }
        }
        return TypeNumber.UNKNOWN;
    }

    private MyAction defineAction(String action){
        switch (action){
            case "+":
                return MyAction.PLUS;
            case "-":
                return MyAction.MINUS;
            case "*":
                return MyAction.MULTIPLICATION;
            case "/":
                return MyAction.DIVISION;
        }
        return MyAction.UNKNOWN;
    }

    private Integer getRomanNumberValue(String stringValue) throws Exception {
        ArrayList<String> allRomansList = allRomans();
        int value = allRomansList.indexOf(stringValue);
        if (value == -1){
            throw new Exception("Неправильная запись римского числа");
        }
        return value;
    }

    private ArrayList<String> allRomans() {
        ArrayList<String> allAllowedRomanList = new ArrayList<>();
        allAllowedRomanList.add("0");
        for (int i = 1; i < 4000; i++) {
            allAllowedRomanList.add(arabicToRoman(i));
        }
        return allAllowedRomanList;
    }

    private String arabicToRoman(Integer arabicNumber) {
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





    private Integer getRomanDigitValue(String stringValue) throws Exception {
        switch (stringValue){
            case "I":
                return 1;
            case "V":
                return 5;
            case "X":
                return 10;
            case "L":
                return 50;
            case "C":
                return 100;
            case "D":
                return 500;
            case "M":
                return 1000;
        }
        throw new Exception("Неизвестная римская цифра");
    }





    public String getRomanResult() throws Exception {
        return arabicToRoman(getResult());
    }

    private Integer getResult() throws Exception {
        switch (action){
            case PLUS:
                return a.getIntegerValue()+b.getIntegerValue();
            case MINUS:
                return a.getIntegerValue()-b.getIntegerValue();
            case MULTIPLICATION:
                return a.getIntegerValue()*b.getIntegerValue();
            case DIVISION:
                if (b.getIntegerValue()==0) {
                    throw new Exception("Делить на ноль нельзя");
                }
                else if (a.getIntegerValue()%b.getIntegerValue()==0){
                    return a.getIntegerValue()/b.getIntegerValue();
                }
                throw new Exception("Ответ должен быть целым числом");
        }
        throw new Exception("Неизвестное действие в методе getResult()");
    }

    public String getArabicResult() throws Exception {
        return getResult().toString();
    }


}
