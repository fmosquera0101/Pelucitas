package co.com.fredymosqueralemus.pelucitas;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instacart.library.truetime.TrueTime;

import java.io.IOException;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.PerfilesXUsuario;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.services.NotificadorReservaAgendaService;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;


public class InicioActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private SharedPreferencesSeguro sharedPreferencesSeguro;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser firebaseUser;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        startNotificadorReservaAgendaService();
        new InicializarTrueTimeTask().execute();
        Intent mItent = getIntent();
        context = this;
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (null == firebaseUser) {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            iniciarSesionConEmailPassword(mItent);
            inicializarAuthStateListener();
            inicializarViewsNavigationView();

            settearMenuNavigationView();

            FragmentManager mFragmentManager = getSupportFragmentManager();
            Fragment mFragment = new InicioTiposDeNegociosFragment();
            getSupportActionBar().setTitle(getString(R.string.app_name));
            mFragmentManager.beginTransaction().replace(R.id.contenedor_activityhome, mFragment).commit();

        }
    }

    private void settearMenuNavigationView() {
        if (sharedPreferencesSeguro.containsKey(Constantes.ISLOGGED)) {
            settearMenuXPerfilUsuario();
        } else {
            navigationView.inflateMenu(R.menu.menu_drawer_cliente);
        }
    }

    private void inicializarViewsNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layouthome);
        actionBarDrawerToggle = getActionBarDrawerToggle();
        actionBarDrawerToggle.syncState();
        navigationView = getNavigationView();
    }

    private void inicializarAuthStateListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    private void iniciarSesionConEmailPassword(Intent mItent) {
        if (!LoginActivity.class.getName().equals(mItent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_HOME)) && Constantes.SI.equals(sharedPreferencesSeguro.getString(Constantes.ISLOGGED))) {
            firebaseAuth.signInWithEmailAndPassword(sharedPreferencesSeguro.getString(Constantes.CORREO), sharedPreferencesSeguro.getString(Constantes.CONTRASENA)).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void startNotificadorReservaAgendaService() {
        Intent intentService = new Intent(this, NotificadorReservaAgendaService.class);
        startService(intentService);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ActionBarDrawerToggle getActionBarDrawerToggle() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_abrir, R.string.drawer_cerrar
        );
        return actionBarDrawerToggle;

    }

    private NavigationView getNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                seleccionarItem(item);
                drawerLayout.closeDrawers();
                return false;
            }
        });
        return navigationView;
    }

    private void seleccionarItem(MenuItem menuItem) {
        Fragment mFragment = null;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        int item = menuItem.getItemId();
        Intent mIntent;
        switch (item) {
            case R.id.menuitem_inicio:
                if (!sharedPreferencesSeguro.containsKey(Constantes.ISLOGGED)) {
                    showToastMensajeErrorInicioSesion();
                } else {
                    mFragment = new InicioTiposDeNegociosFragment();
                    getSupportActionBar().setTitle(getString(R.string.str_inicio));
                }
                break;
            case R.id.menuitem_turnos:
                if (!sharedPreferencesSeguro.containsKey(Constantes.ISLOGGED)) {
                    showToastMensajeErrorInicioSesion();
                } else {
                    mFragment = new TurnosXClienteFragment();
                    getSupportActionBar().setTitle(getString(R.string.str_turnos));
                }
                break;
            case R.id.menuitem_micuenta:
                if (!sharedPreferencesSeguro.containsKey(Constantes.ISLOGGED)) {
                    showToastMensajeErrorInicioSesion();
                } else {
                    Intent intent = new Intent(this, AdministrarMiPerfilActivity.class);
                    intent.putExtra(Constantes.CALL_FROM_ACTIVITY_CONFIGURACIONACTIVITY, ConfiguracionActivity.class.getName());
                    startActivity(intent);
                }
                break;

            case R.id.menuitem_crearcuenta:
                mIntent = new Intent(this, RegistrarCorreoContrasenaActivity.class);
                startActivity(mIntent);
                break;

            case R.id.menuitem_iniciarsesion:
                if (!Constantes.SI.equals(sharedPreferencesSeguro.getString(Constantes.ISLOGGED))) {
                    mIntent = new Intent(this, LoginActivity.class);
                    startActivity(mIntent);
                } else {
                    Toast.makeText(InicioActivity.this, R.string.str_hasiniciadosesion,
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.menuitem_perfiladministrador_registrarminegocio:
                if (!sharedPreferencesSeguro.containsKey(Constantes.ISLOGGED)) {
                    showToastMensajeErrorInicioSesion();
                } else {
                    mIntent = new Intent(this, RegistrarMiNegocioActivity.class);
                    startActivity(mIntent);
                }
                break;

            case R.id.menuitem_perfiladministrador_administrarnegocios:
                if (!sharedPreferencesSeguro.containsKey(Constantes.ISLOGGED)) {
                    showToastMensajeErrorInicioSesion();
                } else {
                    mFragment = new AdministrarMisNegociosFragment();
                    getSupportActionBar().setTitle(getString(R.string.str_misnegocios));
                }
                break;


            case R.id.menuitem_configuracion:
                mIntent = new Intent(this, ConfiguracionActivity.class);
                startActivity(mIntent);
                break;

        }
        if (null != mFragment) {
            mFragmentManager.beginTransaction().replace(R.id.contenedor_activityhome, mFragment).commit();
        }

    }

    private void showToastMensajeErrorInicioSesion() {
        Toast.makeText(InicioActivity.this, R.string.str_debesiniciarsesion,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        navigationView.getMenu().clear();
        settearMenuNavigationView();
    }

    private void settearMenuXPerfilUsuario() {
        if (null != firebaseUser) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(firebaseUser.getUid()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    PerfilesXUsuario perfilEmpleado = null != usuario ? usuario.getPerfilEmpleado() : null;
                    if (null != perfilEmpleado && "S".equals(perfilEmpleado.getActivo())) {
                        navigationView.inflateMenu(R.menu.menu_drawer_administrador);
                    } else {
                        navigationView.inflateMenu(R.menu.menu_drawer_cliente);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            navigationView.inflateMenu(R.menu.menu_drawer_cliente);
        }
    }

    private class InicializarTrueTimeTask extends AsyncTask<Void, Void,Void>{

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
}
