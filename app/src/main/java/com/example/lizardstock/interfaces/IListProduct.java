package com.example.lizardstock.interfaces;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface IListProduct {

    interface View{
    }

    interface Presenter{
        void fillRecyclerView(final RecyclerView recyclerView, final String categoria);
    }

    interface Interactor{
        void fillRecyclerView(final RecyclerView recyclerView, final String categoria);
    }
}
