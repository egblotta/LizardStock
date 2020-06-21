package com.example.lizardstock.interactor;

import android.net.Uri;

import com.example.lizardstock.interfaces.IDetailProduct;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class DetailProductInteractor implements IDetailProduct.Interactor {

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private IDetailProduct.Presenter presenter;

    public DetailProductInteractor(IDetailProduct.Presenter presenter) {
        this.presenter = presenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void firebaseUpdate(final String codigo, final String nombre, final String cantidad,
                               final String precio, final String categoria, final Uri imagenUri) {
        if (imagenUri != null) {
            final StorageReference imageRef = mStorage.child(categoria).child(nombre);
            imageRef.putFile(imagenUri).continueWithTask(task -> {

                if (!task.isSuccessful()) {
                    throw new Exception();
                }
                return imageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    Uri downloadLink = task.getResult();
                    Map<String, Object> product = new HashMap<>();

                    product.put("nombre", nombre);
                    product.put("cantidad", cantidad);
                    product.put("codigo", codigo);
                    product.put("precio", precio);
                    assert downloadLink != null;
                    product.put("imagenUrl", downloadLink.toString());

                    DatabaseReference postRef = mDatabase.child("Articulos").child(categoria).child(nombre);
                    postRef.updateChildren(product)
                            .addOnSuccessListener(task1 -> presenter.successMessage(true))        //true
                            .addOnFailureListener(e -> presenter.successMessage(false));           //false
                }
            });
        }
    }
}
