package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.Toast;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;

public class SeleccionarFragmentMenuNavigationView {
    private final InicioActivity inicioActivity;

    public SeleccionarFragmentMenuNavigationView(InicioActivity inicioActivity) {
        this.inicioActivity = inicioActivity;
    }

    void seleccionarItem(MenuItem menuItem) {
        Fragment mFragment = null;
        FragmentManager mFragmentManager = inicioActivity.getSupportFragmentManager();
        int item = menuItem.getItemId();
        Intent mIntent;
        switch (item) {
            case R.id.menuitem_inicio:
                if (!inicioActivity.getSharedPreferencesSeguro().containsKey(Constantes.ISLOGGED)) {
                    inicioActivity.showToastMensajeErrorInicioSesion();
                } else {
                    mFragment = new InicioTiposDeNegociosFragment();
                    inicioActivity.getSupportActionBar().setTitle(inicioActivity.getString(R.string.str_inicio));
                }
                break;
            case R.id.menuitem_turnos:
                if (!inicioActivity.getSharedPreferencesSeguro().containsKey(Constantes.ISLOGGED)) {
                    inicioActivity.showToastMensajeErrorInicioSesion();
                } else {
                    mFragment = new TurnosXClienteFragment();
                    inicioActivity.getSupportActionBar().setTitle(inicioActivity.getString(R.string.str_turnos));
                }
                break;
            case R.id.menuitem_micuenta:
                if (!inicioActivity.getSharedPreferencesSeguro().containsKey(Constantes.ISLOGGED)) {
                    inicioActivity.showToastMensajeErrorInicioSesion();
                } else {
                    Intent intent = new Intent(inicioActivity, AdministrarMiPerfilActivity.class);
                    intent.putExtra(Constantes.CALL_FROM_ACTIVITY_CONFIGURACIONACTIVITY, ConfiguracionActivity.class.getName());
                    inicioActivity.startActivity(intent);
                }
                break;

            case R.id.menuitem_crearcuenta:
                mIntent = new Intent(inicioActivity, RegistrarCorreoContrasenaActivity.class);
                inicioActivity.startActivity(mIntent);
                break;

            case R.id.menuitem_iniciarsesion:
                if (!Constantes.SI.equals(inicioActivity.getSharedPreferencesSeguro().getString(Constantes.ISLOGGED))) {
                    mIntent = new Intent(inicioActivity, LoginActivity.class);
                    inicioActivity.startActivity(mIntent);
                } else {
                    Toast.makeText(inicioActivity, R.string.str_hasiniciadosesion,
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.menuitem_perfiladministrador_registrarminegocio:
                if (!inicioActivity.getSharedPreferencesSeguro().containsKey(Constantes.ISLOGGED)) {
                    inicioActivity.showToastMensajeErrorInicioSesion();
                } else {
                    mIntent = new Intent(inicioActivity, RegistrarMiNegocioActivity.class);
                    inicioActivity.startActivity(mIntent);
                }
                break;

            case R.id.menuitem_perfiladministrador_administrarnegocios:
                if (!inicioActivity.getSharedPreferencesSeguro().containsKey(Constantes.ISLOGGED)) {
                    inicioActivity.showToastMensajeErrorInicioSesion();
                } else {
                    mFragment = new AdministrarMisNegociosFragment();
                    inicioActivity.getSupportActionBar().setTitle(inicioActivity.getString(R.string.str_misnegocios));
                }
                break;


            case R.id.menuitem_configuracion:
                mIntent = new Intent(inicioActivity, ConfiguracionActivity.class);
                inicioActivity.startActivity(mIntent);
                break;

        }
        if (null != mFragment) {
            mFragmentManager.beginTransaction().replace(R.id.contenedor_activityhome, mFragment).commit();
        }

    }
}