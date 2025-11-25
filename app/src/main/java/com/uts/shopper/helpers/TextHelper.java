package com.uts.shopper.helpers;

import java.text.NumberFormat;
import java.util.Locale;

public class TextHelper {
    public static String formatearNumero(String valor) {
        // Separar parte entera y decimal
        String[] partes = valor.split("\\.");

        String entero = partes[0];
        String decimal = partes.length > 1 ? partes[1] : null;

        // Usar NumberFormat con Locale alemán (que usa . como separador de miles)
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
        nf.setGroupingUsed(true);

        // Formatear parte entera
        String enteroFormateado = nf.format(Long.parseLong(entero));

        // Unir todo
        if (decimal != null)
            return enteroFormateado + "." + decimal;

        return enteroFormateado;
    }

}
