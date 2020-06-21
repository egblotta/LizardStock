package com.example.lizardstock.interfaces;

import android.net.Uri;

public interface IDetailProduct {

    interface View{
        void successMessage(Boolean success);
    }

    interface Presenter{
        void successMessage(Boolean success);
        void firebaseUpdate(final String codigo, final String nombre, final String cantidad,
                            final String precio, final String categoria, final Uri imagenUri);
    }

    interface Interactor{
        void firebaseUpdate(final String codigo, final String nombre, final String cantidad,
                            final String precio, final String categoria, final Uri imagenUri);
    }

}
