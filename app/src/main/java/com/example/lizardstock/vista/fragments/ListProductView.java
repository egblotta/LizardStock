package com.example.lizardstock.vista.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.util.Util;
import com.example.lizardstock.R;
import com.example.lizardstock.interfaces.IListProduct;
import com.example.lizardstock.presentador.ListPresenter;
import com.example.lizardstock.utilidades.Utilidades;

public class ListProductView extends Fragment implements IListProduct.View{

    private Spinner spnCategoria;
    private Switch mSwitch;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list_product, container, false);

        mSwitch = view.findViewById(R.id.swLista);
        spnCategoria = view.findViewById(R.id.spnLista);

        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.categorias, android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategoria.setAdapter(spnAdapter);

        ImageView imagenSc = view.findViewById(R.id.imagen_sinconexion);
        imagenSc.setVisibility(View.INVISIBLE);

        switchOn(view);
        optionSpinner(view);
        return view;
    }

    private void switchOn(final View view){
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Utilidades.visualizacion=Utilidades.LIST;
                    mSwitch.setText("Lista");
                    Toast.makeText(getContext(), "Lista", Toast.LENGTH_SHORT).show();
                }else{
                    Utilidades.visualizacion=Utilidades.GRID;
                    mSwitch.setText("Grilla");
                    Toast.makeText(getContext(), "Grilla ", Toast.LENGTH_SHORT).show();
                }
                //optionSpinner(view);
            }
        });
    }

    private void optionSpinner(final View view){
        spnCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                initRecycler(view,spnCategoria.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecycler(View v, String categoria) {
        IListProduct.Presenter presenter = new ListPresenter(this);
        RecyclerView mRecyclerView = v.findViewById(R.id.recyclerProducts);
        mRecyclerView.setHasFixedSize(true);

        if(Utilidades.visualizacion==Utilidades.LIST){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }
        presenter.fillRecyclerView(mRecyclerView, categoria);
    }


    @Override
    public void successMessage(Boolean success) {
        if(success)
            Toast.makeText(getContext(), "Articulo eliminado.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Error al eliminar.", Toast.LENGTH_SHORT).show();
    }
}
