package co.com.fredymosqueralemus.pelucitas;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import co.com.fredymosqueralemus.pelucitas.adapters.AdapterTiposDeNegocios;
import co.com.fredymosqueralemus.pelucitas.modelo.minegocio.TipoNegocio;



public class InicioFragmentTiposDeNegocios extends Fragment {

    private Context context;

    private ListView listView;
    private List<TipoNegocio> tiposDeNegocios;

    public InicioFragmentTiposDeNegocios() {
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


    private List<TipoNegocio> getTiposDeNegocios(){
        List<TipoNegocio> mLstTipoEntidades = new ArrayList<>();
        TipoNegocio mTipoEntidades;
        mTipoEntidades = new TipoNegocio();

        //mTipoEntidades.setImagenTipoEntidad(R.drawable.salondebelleza);
        mTipoEntidades.setTipoNegocio(getString(R.string.str_tiponegocio_salonbelleza));
        mLstTipoEntidades.add(mTipoEntidades);
        mTipoEntidades = new TipoNegocio();
       // mTipoEntidades.setImagenTipoEntidad(R.drawable.peluqueria);
        mTipoEntidades.setTipoNegocio(getString(R.string.str_tiponegocio_peluqueria));
        mLstTipoEntidades.add(mTipoEntidades);
        mTipoEntidades = new TipoNegocio();
        //mTipoEntidades.setImagenTipoEntidad(R.drawable.barberia);
        mTipoEntidades.setTipoNegocio(getString(R.string.str_tiponegocio_barberia));
        mLstTipoEntidades.add(mTipoEntidades);

        return  mLstTipoEntidades;

    }

    private void addClickListenerListView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*TipoEntidades mTipoEntidades = tiposDeNegocios.get(position);
                Intent mIntent = new Intent(context, MisEntidadConfiguracionActivity.class);
                mIntent.putExtra(Constantes.TIPO_ENTIDAD, mTipoEntidades.getTipoEntidad());
                mIntent.putExtra(Constantes.PEDIR_CITA, "S");
                startActivity(mIntent);*/
            }
        });
    }

}
