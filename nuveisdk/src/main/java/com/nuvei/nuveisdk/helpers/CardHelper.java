package com.nuvei.nuveisdk.helpers;

import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.nuvei.nuveisdk.model.addCard.CardInfoModel;
import com.nuvei.nuveisdk.model.addCard.CardTypes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;




public class CardHelper {

    private static final int LENGTH_COMMON_CARD = 16;
    private static final int LENGTH_AMERICAN_EXPRESS = 15;
    private static final int LENGTH_DINERS_CLUB = 14;
    private static String CARD_BRAND = null;


    /**
     * Checks the input string to see whether or not it is a valid Luhn number.
     *
     * @param cardNumber a String that may or may not represent a valid Luhn number
     * @return {@code true} if and only if the input value is a valid Luhn number
     */
    static boolean validLuhnNumber(String cardNumber){
        boolean isOdd= true;
        int sum = 0;

        for(int index = cardNumber.length() - 1; index >= 0; index--){
            char c = cardNumber.charAt(index);
            if(!Character.isDigit(c)){
                return  false;
            }

            int digitInteger = Character.getNumericValue(c);
            isOdd = !isOdd;
            if(isOdd){
                digitInteger *=2;
            }

            if(digitInteger > 9){
                digitInteger -= 9;
            }

            sum += digitInteger;
        }
        return sum%10 == 0;
    }



    public static int completeYear(int year) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int currentYear = LocalDate.now().getYear();
            int currentCentury = (currentYear / 100) * 100;

            if (year < (currentYear % 100)) {
                return currentCentury + 100 + year;
            } else {
                return currentCentury + year;
            }
        }
        return 0;
    }



    public static CardInfoModel getCardInfo(String number) {
        // Limpiamos: quitamos todo lo que no sea dígito
        String clean = number.replaceAll("\\D", "");

        for (CardInfoModel info : CardTypes.ALL) {
            if (info.getRegex().matcher(clean).find()) {
                return info;
            }
        }

        // Si no se encontró, devolvemos un "Unknown"
        return new CardInfoModel(
                "Unknown",
                "^$",                              // regex vacío
                "#### #### #### ####",
                3,
                java.util.Arrays.asList(16),
                "",
                "",
                java.util.Arrays.asList("#cccccc", "#999999") // colores neutros
        );
    }


    public static String formatCardNumber(String number) {
        // 1. limpiar: solo dígitos
        String clean = number.replaceAll("\\D", "");

        // 2. obtener la máscara según el tipo de tarjeta
        CardInfoModel def = getCardInfo(clean);
        String mask = def.getMask();

        // 3. construir resultado
        StringBuilder result = new StringBuilder();
        int i = 0;

        for (char c : mask.toCharArray()) {
            if (c == '#') {
                if (i >= clean.length()) break; // si no hay más dígitos
                result.append(clean.charAt(i++));
            } else {
                if (i < clean.length()) result.append(c);
            }
        }

        return result.toString();
    }
//    static boolean validateExpiryDate(String expiryDate){
//
//   }

}
