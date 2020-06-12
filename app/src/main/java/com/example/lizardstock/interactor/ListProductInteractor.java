package com.example.lizardstock.interactor;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.R;
import com.example.lizardstock.interfaces.IListProduct;
import com.example.lizardstock.modelo.Product;
import com.example.lizardstock.adaptador.RecyclerProductAdapter;
import com.example.lizardstock.vista.fragments.ListProductView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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
                    mAdapter.setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(final View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Eliminar articulo?");
                            final DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){

                                        case DialogInterface.BUTTON_POSITIVE:
                                            deleteItem(categoria,mProducts.get(recyclerView.getChildAdapterPosition(v)).getNombre());
                                            mAdapter.notifyDataSetChanged();
                                            break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                dialog.dismiss();
                                                break;
                                    }
                                }
                            };
                            builder.setPositiveButton(R.string.Si,dialogListener);
                            builder.setNegativeButton(R.string.No, dialogListener);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return true;
                        }
                    });
                    recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(recyclerView.getContext(), "Error al cargar la lista.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItem(String categoria, String nombre){
        StorageReference imageRef = mStorage.child(categoria).child(nombre);
        imageRef.delete();
        mDatabase.child(nombre).removeValue();
    }
}
