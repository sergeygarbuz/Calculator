package ru.garbuz;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyExpression {

    private MyNumber a;
    private MyNumber b;
    private MyAction action;
    private TypeNumber resultType;

    public MyExpression(String stringExpression) throws Exception {
        a = new MyNumber();
        b = new MyNumber();
        action = null;
        resultType = null;
        if (stringExpression.length()<3) {
            throw new Exception("Вы ввели слишком мало символов");
        }
        else{
            String firstDigit = stringExpression.substring(0,1);
            String nextDigit;
            switch (defineDigitType(firstDigit)) {
                case ROMAN:                     //парсим первое римское число
                    a.setStringValue(firstDigit);
                    for (int i = 1; i < stringExpression.length(); i++) {
                        if (action != null) break; //выходим из цикла, если дошли до знака действия
                        nextDigit = stringExpression.substring(i, i + 1);
                        switch (this.defineDigitType(nextDigit)) {
                            case ROMAN:
                                a.setStringValue(a.getStringValue() + nextDigit);
                                break;
                            case ARABIC:
                                throw new Exception("Цифры должны быть либо только римскими либо только арабскими");
                            case UNKNOWN:   //конец первого римского числа, проверяем что дальше идет знак
                                if (defineAction(nextDigit) == MyAction.UNKNOWN) {
                                    throw new Exception("В выражении использовано неизвестное действие");
                                } else {
                                    action = defineAction(nextDigit);
                                }
                                break;
                        }
                    }
                    a.setIntegerValue(getRomanNumberValue(a.getStringValue()));
                    if (a.getIntegerValue() > 10) throw new Exception("Числа должны быть не больше 10");
                    //парсим второе римское число
                    b.setStringValue("");
                    for (int i = a.getStringValue().length() + 1; i < stringExpression.length(); i++) {
                        nextDigit = stringExpression.substring(i, i + 1);
                        switch (this.defineDigitType(nextDigit)) {
                            case ROMAN:
                                b.setStringValue(b.getStringValue() + nextDigit);
                                break;
                            case ARABIC:
                                throw new Exception("Цифры должны быть либо только римскими либо только арабскими");
                            case UNKNOWN:
                                //конец второго римского числа, должен быть конец выражения
                                throw new Exception("Неверно написано второе число");
                        }
                    }
                    b.setIntegerValue(getRomanNumberValue(b.getStringValue()));
                    if (b.getIntegerValue() > 10) throw new Exception("Числа должны быть не больше 10");
                    resultType = TypeNumber.ROMAN;
                    break;
                case ARABIC:                      //парсим первое арабское число
                    a.setStringValue(firstDigit);
                    for (int i = 1; i < stringExpression.length(); i++) {
                        if (action != null) break; //выходим из цикла, если дошли до знака действия
                        nextDigit = stringExpression.substring(i, i + 1);
                        switch (this.defineDigitType(nextDigit)) {
                            case ROMAN:
                                throw new Exception("Цифры должны быть либо только римскими либо только арабскими");
                            case ARABIC:
                                a.setStringValue(a.getStringValue() + nextDigit);
                                break;
                            case UNKNOWN: //конец первого арабского числа, проверяем что дальше идет знак
                                if (defineAction(nextDigit) == MyAction.UNKNOWN) {
                                    throw new Exception("В выражении использовано неизвестное действие");
                                } else {
                                    action = defineAction(nextDigit);
                                }
                                break;
                        }
                    }
                    a.setIntegerValue(Integer.parseInt(a.getStringValue()));
                    if (a.getIntegerValue() > 10) throw new Exception("Числа должны быть не больше 10");
                    //парсим второе арабское число
                    b.setStringValue("");
                    for (int i = a.getStringValue().length() + 1; i < stringExpression.length(); i++) {
                        nextDigit = stringExpression.substring(i, i + 1);
                        switch (this.defineDigitType(nextDigit)) {
                            case ROMAN:
                                throw new Exception("Цифры должны быть либо только римскими либо только арабскими");
                            case ARABIC:
                                b.setStringValue(b.getStringValue() + nextDigit);
                                break;
                            case UNKNOWN: //конец второго арабского числа, должен быть конец выражения
                                throw new Exception("Неверно написано второе число");
                        }
                    }
                    b.setIntegerValue(Integer.parseInt(b.getStringValue()));
                    if (b.getIntegerValue() > 10) throw new Exception("Числа должны быть не больше 10");
                    resultType = TypeNumber.ARABIC;
                    break;
                case UNKNOWN:   //НИ РИМСКИЕ НИ АРАБСКИЕ
                    throw new Exception("Первый символ в выражении не является арабской или римской цифрой");
            }
        }
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
        int value = RomanDigits.allAllowedRomanList.indexOf(stringValue);
        if (value == -1){
            throw new Exception("Неправильная запись римского числа");
        }
        return value;
    }

    public String getRomanResult() throws Exception {
        if (getResult()>0){
            return RomanDigits.arabicToRoman(getResult());
        }
        throw new Exception("Ответ меньше единицы. В римской записи числа больше нуля");
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
