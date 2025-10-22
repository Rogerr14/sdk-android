package com.nuvei.nuveisdk.model.addCard;

import android.graphics.drawable.Drawable;

import java.util.List;
import java.util.regex.Pattern;

public class CardInfoModel {
    private final String type;              // Nombre: Visa, Mastercard, etc.
    private final Pattern regex;            // Regex compilado
    private final String mask;              // #### #### #### ####
    private final int cvcNumber;            // Longitud del CVC
    private final List<Integer> validLengths; // Longitudes válidas del número
    private final String typeCode;          // Código: vi, mc, ax...
    private final int iconRes;            // URL de icono
    private final int[] gradientColor; // Colores para gradiente

    public CardInfoModel(String type,
                          String regex,
                          String mask,
                          int cvcNumber,
                          List<Integer> validLengths,
                          String typeCode,
                         int iconRes,
                          int[] gradientColor) {
        this.type = type;
        this.regex = Pattern.compile(regex);
        this.mask = mask;
        this.cvcNumber = cvcNumber;
        this.validLengths = validLengths;
        this.typeCode = typeCode;
        this.iconRes = iconRes;
        this.gradientColor = gradientColor;
    }

    // Getters
    public String getType() { return type; }
    public Pattern getRegex() { return regex; }
    public String getMask() { return mask; }
    public int getCvcNumber() { return cvcNumber; }
    public List<Integer> getValidLengths() { return validLengths; }
    public String getTypeCode() { return typeCode; }
    public int getIconRes() {
        return iconRes;
    }
    public int[] getGradientColor() { return gradientColor; }
}
