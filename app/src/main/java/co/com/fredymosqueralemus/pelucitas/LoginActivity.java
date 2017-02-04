package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Fredy Mosquera Lemus on 2/02/17.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void abrirActivityRegistrarCorreoContrasena(View view){
        Intent mIntent = new Intent(this, RegistrarCorreoContrasenaActivity.class);
        startActivity(mIntent);
    }
}
