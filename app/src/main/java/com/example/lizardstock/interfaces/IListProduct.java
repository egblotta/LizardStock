package com.example.lizardstock.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lizardstock.modelo.Product;

import java.util.ArrayList;

public interface IListProduct {

    interface View{
        void openDeleteDialog(android.view.View v, ArrayList<Product> mProducts , RecyclerView recyclerView);
        void errorMessage(Boolean error);
        void openUpdateDialog(android.view.View v, ArrayList<Product> mProducts , RecyclerView recyclerView);
        void successMessage(Boolean success);
    }

    interface Presenter{
        void fillRecyclerView(final RecyclerView recyclerView, final String categoria);
        void deleteItem(String categoria, String nombre);
        void openDeleteDialog(android.view.View v, ArrayList<Product> mProducts , RecyclerView recyclerView);
        void errorMessage(Boolean error);
        void openUpdateDialog(android.view.View v, ArrayList<Product> mProducts , RecyclerView recyclerView);
        void successMessage(Boolean success);
    }

    interface Interactor{
        void fillRecyclerView(final RecyclerView recyclerView, final String categoria);
        void deleteItem(String categoria, String nombre);
    }
}
