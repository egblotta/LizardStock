package com.example.lizardstock.interfaces;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface IListProduct {

    interface View{
        void addSuccess(boolean success);
    }

    interface Presenter{
        void fillRecyclerView(final RecyclerView recyclerView, final String categoria);
        void addSuccess(boolean success);
    }

    interface Interactor{
        void fillRecyclerView(final RecyclerView recyclerView, final String categoria);
    }
}
