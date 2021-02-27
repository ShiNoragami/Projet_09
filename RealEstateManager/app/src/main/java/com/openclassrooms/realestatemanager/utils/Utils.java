package com.openclassrooms.realestatemanager.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    private static long dateToLong;

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars
     * @return
     */
    public static double convertDollarToEuro(double dollars) {

        return (double) Math.round(dollars * 0.812);
    }

    //convert euro to dollar
    public static double convertEuroToDollar(double euros) {
        return (double) Math.round(euros * 1.111);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return
     */
    public static String getTodayDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context
     * @return
     */

    public static boolean isInternetAvailable(Context context) {
//        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//        return wifi.isWifiEnabled();

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connMgr).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }

    public static long dateStringToLong(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy", Locale.FRANCE);

        try {

            Date date = sdf.parse(dateString);
            dateToLong = Objects.requireNonNull(date).getTime();
            return dateToLong;

        } catch (ParseException e) {
            e.getMessage();
        }
        return 0;
    }

    public static String longDateToString(long dateLong) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        return simpleDateFormat.format(dateLong);
    }
}
