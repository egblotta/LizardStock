package com.example.lizardstock.presentador;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.R;
import com.example.lizardstock.adaptador.RecyclerProductAdapter;
import com.example.lizardstock.modelo.Product;
import com.example.lizardstock.vista.AddProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ListPresenter{

    private Context mContext;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private List<Product> mProducts;
    private RecyclerProductAdapter mAdapter;

    public ListPresenter(Context mContext, StorageReference mStorage, DatabaseReference mDatabase) {
        this.mContext = mContext;
        this.mStorage = mStorage;
        this.mDatabase = mDatabase;
    }

    public ListPresenter() {}

    public void fillRecyclerView(final RecyclerView recyclerView, final String categoria) {

        mDatabase = FirebaseDatabase.getInstance().getReference("Articulos").child(categoria);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkInternetConection(){
        ConnectivityManager con=(ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

}
