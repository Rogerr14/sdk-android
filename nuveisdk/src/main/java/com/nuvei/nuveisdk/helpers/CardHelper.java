package com.nuvei.nuveisdk.helpers;

import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.time.LocalDate;
import java.util.Date;

public class CardHelper {



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


//    static boolean validateExpiryDate(String expiryDate){
//
//   }

}
