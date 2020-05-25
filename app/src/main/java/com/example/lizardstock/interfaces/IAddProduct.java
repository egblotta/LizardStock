package com.example.lizardstock.interfaces;

import android.net.Uri;

public interface IAddProduct {

     interface View{
         void addSuccess(boolean success);
    }

    interface Presenter{
        void addSuccess(boolean success);
        void firebaseUpload(final String categoria, final String nombre, final String cantidad, final String codigo,
                            final String precio, final Uri imagenUri);
    }

    interface Interactor{
        void firebaseUpload(final String categoria, final String nombre, final String cantidad, final String codigo,
                            final String precio, final Uri imagenUri);
    }
}
