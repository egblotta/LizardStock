package com.example.lizardstock.interfaces;

import android.net.Uri;

public interface IDetailProduct{

    interface View{
        void addSuccess(boolean success);
    }

    interface Presenter{
        void addSuccess(boolean success);
        void firebaseUpdate(final String categoria, final String nombre, final String cantidad, final String codigo,
                            final String precio, final Uri imagenUri);
        void firebaseDelete(final String nombre, final String categoria);
    }

    interface Interactor{
        void firebaseUpdate(final String categoria, final String nombre, final String cantidad, final String codigo,
                            final String precio, final Uri imagenUri);
        void firebaseDelete(final String nombre, final String categoria);
    }
}
