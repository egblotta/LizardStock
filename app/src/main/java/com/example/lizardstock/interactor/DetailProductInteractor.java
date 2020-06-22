package com.example.lizardstock.interactor;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lizardstock.interfaces.IDetailProduct;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
                               final String precio, final String categoria) {

        /*if (imagenUri != null) {

            final StorageReference mStorageRef = mStorage.child(categoria).child(nombre);   //Referencia al storage de firebase

            UploadTask uploadTask = mStorageRef.putFile(imagenUri);
            uploadTask.continueWithTask(task -> {

                if(!task.isSuccessful()){
                    throw Objects.requireNonNull(task.getException());
                }
                return mStorageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if(task.isSuccessful()){*/

                    //Uri downloadUri = task.getResult();
                    //String urlFoto = downloadUri.toString();
                    Map<String, Object> product = new HashMap<>();

                    product.put("nombre", nombre);
                    product.put("cantidad", cantidad);
                    product.put("codigo", codigo);
                    product.put("precio", precio);
                    //product.put("imagenUrl", downloadUri);

                    DatabaseReference postRef = mDatabase.child("Articulos").child(categoria).child(nombre);
                    postRef.updateChildren(product)
                            .addOnSuccessListener(task1 -> presenter.successMessage(true))        //true
                            .addOnFailureListener(e -> presenter.successMessage(false));
                }
            }


