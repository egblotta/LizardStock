package com.example.lizardstock.interactor;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.R;
import com.example.lizardstock.interfaces.IDetailProduct;
import com.example.lizardstock.interfaces.IListProduct;
import com.example.lizardstock.modelo.Product;
import com.example.lizardstock.adaptador.RecyclerProductAdapter;
import com.example.lizardstock.modelo.utilidades.Utilidades;
import com.example.lizardstock.presentador.DetailsPresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListProductInteractor implements IListProduct.Interactor{

    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private RecyclerProductAdapter mAdapter;
    private IListProduct.Presenter presenter;


    public ListProductInteractor(IListProduct.Presenter presenter) {
        this.presenter = presenter;
        mDatabase=FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void fillRecyclerView(final RecyclerView recyclerView, final String categoria) {

        mDatabase = mDatabase.child("Articulos").child(categoria);
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ArrayList<Product> mProducts = new ArrayList<>();
                mProducts.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Product product = postSnapshot.getValue(Product.class);
                        mProducts.add(product);
                    }
                    mAdapter = new RecyclerProductAdapter(mProducts);

                    mAdapter.setOnLongClickListener(v -> {
                        presenter.openDeleteDialog(v,mProducts,recyclerView);
                        return true;
                    });
                    mAdapter.setOnClickListener(v -> {
                        presenter.openUpdateDialog(v,mProducts,recyclerView);
                    });
                    recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.errorMessage(true);
            }
        });
    }

    @Override
    public void deleteItem(String categoria, String nombre) {
        StorageReference imageRef = mStorage.child(categoria).child(nombre);
        imageRef.delete();
        mDatabase.child(nombre).removeValue();
        presenter.successMessage(true);
    }

}
