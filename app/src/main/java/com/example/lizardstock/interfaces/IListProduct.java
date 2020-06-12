package com.example.lizardstock.interfaces;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface IListProduct {

    interface View{
        void successMessage(Boolean success);
    }

    interface Presenter{
        void fillRecyclerView(final RecyclerView recyclerView, final String categoria);
        void successMessage(Boolean success);
    }

    interface Interactor{
        void fillRecyclerView(final RecyclerView recyclerView, final String categoria);
    }
}
