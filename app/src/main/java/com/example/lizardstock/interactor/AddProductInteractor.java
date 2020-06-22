package com.example.lizardstock.interactor;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.lizardstock.interfaces.IAddProduct;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddProductInteractor implements IAddProduct.Interactor {

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private IAddProduct.Presenter presenter;


    public AddProductInteractor(IAddProduct.Presenter presenter) {
        this.presenter = presenter;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    public void firebaseUpload(final String codigo, final String nombre, final String cantidad,
                               final String precio, final String categoria, final Uri imagenUri) {

            if (imagenUri != null) {
                final StorageReference mStorageRef = mStorage.child(categoria).child(nombre);

                UploadTask uploadTask = mStorageRef.putFile(imagenUri);
                uploadTask.continueWithTask(task -> {

                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return mStorageRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();

                        assert downloadUri != null;
                        String urlFoto = downloadUri.toString();

                        Map<String, Object> product = new HashMap<>();

                        product.put("codigo", codigo);
                        product.put("nombre", nombre);
                        product.put("cantidad", cantidad);
                        product.put("precio", precio);
                        product.put("categoria", categoria);
                        product.put("imagenUrl", urlFoto);

                        DatabaseReference postRef = mDatabase.child("Articulos").child(categoria).child(nombre);
                        postRef.updateChildren(product)
                                .addOnSuccessListener(task1 -> presenter.addSuccess(true))        //true
                                .addOnFailureListener(e -> presenter.addSuccess(false));
                    }
                });
            }
    }
}
