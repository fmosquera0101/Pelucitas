package co.com.fredymosqueralemus.pelucitas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterMisNegocios;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.MiNegocio;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguro;
import co.com.fredymosqueralemus.pelucitas.sharedpreference.SharedPreferencesSeguroSingleton;


public class FragmentListviewMisnegocios extends Fragment {

    private ListView listView;
    private List<MiNegocio> lstMisNegocios;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreferencesSeguro sharedPreferencesSeguro;
    public void FragmentListviewMisnegocios(){

    }
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        View view =  layoutInflater.inflate(R.layout.fragment_listview_misnegocios, container, false);
        listView = (ListView) view.findViewById(R.id.listview_fragment_listview_misnegocios);
        sharedPreferencesSeguro = SharedPreferencesSeguroSingleton.getInstance(getContext(), Constantes.SHARED_PREFERENCES_INFOUSUARIO, Constantes.SECURE_KEY_SHARED_PREFERENCES);
        lstMisNegocios = new ArrayList<MiNegocio>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child(Constantes.MINEGOCIO_FIREBASE_BD).child(sharedPreferencesSeguro.getString(Constantes.USERUID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    MiNegocio miNegocio = child.getValue(MiNegocio.class);
                    lstMisNegocios.add(miNegocio);
                }
                AdapterMisNegocios adapterMisNegocios = new AdapterMisNegocios(getContext(),R.layout.layout_listview_misnegocios, lstMisNegocios);
                listView.setAdapter(adapterMisNegocios);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }
}
