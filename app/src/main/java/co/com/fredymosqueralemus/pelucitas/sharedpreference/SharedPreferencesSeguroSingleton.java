package co.com.fredymosqueralemus.pelucitas.sharedpreference;

import android.content.Context;

/**
 * Created by Fredy Mosquera Lemus on 12/02/17.
 */

public class SharedPreferencesSeguroSingleton {
    private static SharedPreferencesSeguro sharedPreferencesSeguro;

    private SharedPreferencesSeguroSingleton(){

    }
    public static SharedPreferencesSeguro getInstance(Context context, String preferenceName, String secureKey){
        if(null == sharedPreferencesSeguro){
            sharedPreferencesSeguro = new SharedPreferencesSeguro(context, preferenceName, secureKey);
            return sharedPreferencesSeguro;
        }
        return sharedPreferencesSeguro;
    }
}
