package co.com.fredymosqueralemus.pelucitas.services;

import android.os.AsyncTask;

import com.instacart.library.truetime.TrueTime;

import java.io.IOException;

/**
 * Created by fredymosqueralemus on 4/10/17.
 */

public class InicializarTrueTimeTask extends AsyncTask<Void, Void,Void> {

    @Override
    protected Void doInBackground(Void... params) {
        try {
            TrueTime.build().initialize();
        } catch (IOException e) {
            e.printStackTrace();//TODO:Remover
        }
        return null;
    }
}