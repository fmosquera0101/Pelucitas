package co.com.fredymosqueralemus.pelucitas;

import android.content.Intent;
import android.content.res.Configuration;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.usuario.Usuario;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;
import co.com.fredymosqueralemus.pelucitas.utilidades.UtilidadesFirebaseBD;


public class InicioActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private SharedPreferencesSeguro sharedPreferencesSeguro;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Intent mItent = getIntent();
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(this, Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        mAuth = FirebaseAuth.getInstance();
        if (!LoginActivity.class.getName().equals(mItent.getStringExtra(Constantes.CALL_FROM_ACTIVITY_HOME)) && Constantes.SI.equals(sharedPreferencesSeguro.getString(Constantes.ISLOGGED))) {
            mAuth.signInWithEmailAndPassword(sharedPreferencesSeguro.getString(Constantes.CORREO), sharedPreferencesSeguro.getString(Constantes.CONTRASENA)).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(InicioActivity.this, R.string.str_nohasiniciadosesion, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layouthome);
        actionBarDrawerToggle = getActionBarDrawerToggle();
        actionBarDrawerToggle.syncState();
        navigationView = getNavigationView();

        if (sharedPreferencesSeguro.containsKey(Constantes.ISLOGGED)) {
            settearMenuXPerfilUsuario();
            FragmentManager mFragmentManager = getSupportFragmentManager();
            Fragment mFragment = new InicioTiposDeNegociosFragment();
            getSupportActionBar().setTitle(getString(R.string.str_inicio));
            mFragmentManager.beginTransaction().replace(R.id.contenedor_activityhome, mFragment).commit();
        } else {
            Toast.makeText(InicioActivity.this, R.string.str_debesiniciarsesion,
                    Toast.LENGTH_SHORT).show();
        }

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
                    Toast.makeText(InicioActivity.this, R.string.str_debesiniciarsesion,
                            Toast.LENGTH_SHORT).show();
                } else {
                    mFragment = new InicioTiposDeNegociosFragment();
                    getSupportActionBar().setTitle(getString(R.string.str_inicio));
                }
                break;

            case R.id.menuitem_micuenta:
                Intent intent = new Intent(this, AdministrarMiPerfilActivity.class);
                intent.putExtra(Constantes.CALL_FROM_ACTIVITY_CONFIGURACIONACTIVITY, ConfiguracionActivity.class.getName());
                startActivity(intent);
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
                    Toast.makeText(InicioActivity.this, R.string.str_debesiniciarsesion,
                            Toast.LENGTH_SHORT).show();
                } else {
                    mIntent = new Intent(this, RegistrarMiNegocioActivity.class);
                    startActivity(mIntent);
                }
                break;

            case R.id.menuitem_perfiladministrador_administrarnegocios:
                if (!sharedPreferencesSeguro.containsKey(Constantes.ISLOGGED)) {
                    Toast.makeText(InicioActivity.this, R.string.str_debesiniciarsesion,
                            Toast.LENGTH_SHORT).show();
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

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void settearMenuXPerfilUsuario() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(UtilidadesFirebaseBD.getUrlInserccionUsuario(sharedPreferencesSeguro.getString(Constantes.USERUID)));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                if ("S".equals(usuario.getPerfilEmpleado().getActivo())) {
                    navigationView.inflateMenu(R.menu.menu_drawer_administrador);
                } else {
                    navigationView.inflateMenu(R.menu.menu_drawer_cliente);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
