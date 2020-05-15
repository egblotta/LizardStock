package com.example.lizardstock.presentador;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lizardstock.modelo.Product;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPresenter {

    private Context mContext;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;

    public AddPresenter(Context mContext, StorageReference mStorage, DatabaseReference mDatabase) {
        this.mContext = mContext;
        this.mStorage = mStorage;
        this.mDatabase = mDatabase;
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = mContext.getContentResolver();
        MimeTypeMap mime =  MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void firebaseUpload(final String categoria, final String nombre, final String cantidad, final String codigo,
                               final String precio, final Uri imagenUri){

        if(imagenUri!=null) {
            final StorageReference imageRef = mStorage.child(categoria).child("Imagenes").child(codigo.trim() + "." + getFileExtension(imagenUri));
            imageRef.putFile(imagenUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw new Exception();
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {

                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){

                        Uri downloadLink = task.getResult();
                        Map<String,Object> product = new HashMap<>();
                        product.put("nombre",nombre);
                        product.put("cantidad",cantidad);
                        product.put("codigo",codigo);
                        product.put("precio",precio);
                        product.put("categoria",categoria);

                        assert downloadLink != null;
                        product.put("imagenUrl", downloadLink.toString());

                        DatabaseReference postRef = mDatabase.child("Articulos").child(categoria).child(nombre);

                        postRef.updateChildren(product)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(mContext,"Articulo cargado.",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext,"Error al cargar el articulo. "+ e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        }
    }
}
