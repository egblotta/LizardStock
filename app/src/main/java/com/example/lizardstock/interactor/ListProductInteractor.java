package com.example.lizardstock.interactor;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.R;
import com.example.lizardstock.interfaces.IListProduct;
import com.example.lizardstock.modelo.Product;
import com.example.lizardstock.adaptador.RecyclerProductAdapter;
import com.example.lizardstock.presentador.ListPresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ListProductInteractor implements IListProduct.Interactor{

    private DatabaseReference mDatabase;
    private List<Product> mProducts;
    private RecyclerProductAdapter mAdapter;
    private IListProduct.Presenter presenter;
    private Context mContext;

    public ListProductInteractor(IListProduct.Presenter presenter, Context mContext) {
        this.presenter = presenter;
        this.mContext = mContext;
        mDatabase=FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void fillRecyclerView(final RecyclerView recyclerView, final String categoria) {
        mDatabase = mDatabase.child("Articulos").child(categoria);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mProducts = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product product = postSnapshot.getValue(Product.class);
                    mProducts.add(product);
                }
                mAdapter = new RecyclerProductAdapter(mContext, mProducts, R.layout.item_list);
                recyclerView.setAdapter(mAdapter);
                presenter.addSuccess(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                presenter.addSuccess(false);
                Toast.makeText(mContext, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

