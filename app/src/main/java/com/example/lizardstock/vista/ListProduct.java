package com.example.lizardstock.vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.R;
import com.example.lizardstock.adaptador.RecyclerProductAdapter;
import com.example.lizardstock.presentador.ListPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class ListProduct extends Fragment implements View.OnClickListener{

    private RecyclerProductAdapter mAdapter;
    private ListPresenter listPresenter;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private FloatingActionButton fab;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_product, container, false);
        listPresenter = new ListPresenter(getContext(),mStorage,mDatabase);

        ImageView imagensc=view.findViewById(R.id.imagen_sinconexion);
        imagensc.setVisibility(View.INVISIBLE);

        initRecycler(view);

        return view;
    }

    private void initRecycler(View view){
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerProducts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        listPresenter.fillRecyclerView(mRecyclerView);
    }

    @Override
    public void onClick(View v) {
        fab.setOnClickListener(this);

    }

    /*public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ListProduct.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }*/
}
