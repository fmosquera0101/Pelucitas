package co.com.fredymosqueralemus.pelucitas;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterTiposDeNegocios;
import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;
import co.com.fredymosqueralemus.pelucitas.imagenes.ImagenModelo;
import co.com.fredymosqueralemus.pelucitas.modelo.settings.TiposDeNegocio;


public class InicioTiposDeNegociosFragment extends Fragment {

    private Context context;

    private ListView listView;
    private List<TiposDeNegocio> tiposDeNegocios;
    private FragmentActivity fragmentActivity;

    public InicioTiposDeNegociosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_listview_inicio_tipos_de_negocios, container, false);
        context  = getContext();
        inicializarViews(view);
        setAdapterListView();
        addClickListenerListView();
        return  view;
    }
    private void inicializarViews(View view){
        listView = (ListView) view.findViewById(R.id.listview_tipos_de_negocios_InicioFragmentTiposDeNegocios);
    }
    private void setAdapterListView(){
        tiposDeNegocios = getTiposDeNegocios();
        AdapterTiposDeNegocios tipoEntidadesAdapter = new AdapterTiposDeNegocios(context, tiposDeNegocios);
        listView.setAdapter(tipoEntidadesAdapter);
    }


    private List<TiposDeNegocio> getTiposDeNegocios(){
        List<TiposDeNegocio> lstTiposDeNegocio = new ArrayList<>();
        TiposDeNegocio tiposDeNegocio= new TiposDeNegocio();

        tiposDeNegocio.setNombreTipoNegocio(getString(R.string.str_tiponegocio_barberia));
        ImagenModelo imagenModelo = new ImagenModelo();
        imagenModelo.setNombreImagen(Constantes.TIPOS_NEGOCIOS_BARBERIA_FIREBASE_BD);
        tiposDeNegocio.setImagenModelo(imagenModelo);
        lstTiposDeNegocio.add(tiposDeNegocio);

        tiposDeNegocio= new TiposDeNegocio();
        tiposDeNegocio.setNombreTipoNegocio(getString(R.string.str_tiponegocio_peluqueria));
        imagenModelo = new ImagenModelo();
        imagenModelo.setNombreImagen(Constantes.TIPOS_NEGOCIOS_PELUQUERIA_FIREBASE_BD);
        tiposDeNegocio.setImagenModelo(imagenModelo);
        lstTiposDeNegocio.add(tiposDeNegocio);

        tiposDeNegocio= new TiposDeNegocio();
        tiposDeNegocio.setNombreTipoNegocio(getString(R.string.str_tiponegocio_salonbelleza));
        imagenModelo = new ImagenModelo();
        imagenModelo.setNombreImagen(Constantes.TIPOS_NEGOCIOS_SALONESDEBELLEZA_FIREBASE_BD);
        tiposDeNegocio.setImagenModelo(imagenModelo);
        lstTiposDeNegocio.add(tiposDeNegocio);


        return  lstTiposDeNegocio;

    }

    @Override
    public void onAttach(Context activity) {
        fragmentActivity = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void addClickListenerListView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, VerNegociosXTipoParaReservarActivity.class);
                intent.putExtra(Constantes.TIPO_NEGOCIO_SELECCIONADO, tiposDeNegocios.get(position).getImagenModelo().getNombreImagen());
                startActivity(intent);
            }
        });
    }

}
