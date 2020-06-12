package com.example.lizardstock.vista.activities;

import android.os.Bundle;

import com.example.lizardstock.R;
import com.example.lizardstock.interfaces.IListProduct;
import com.example.lizardstock.utilidades.Utilidades;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            //Para ir desde el fab al segundo fragment
            @Override
            public void onClick(View view) {
                fabClick();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==item.getItemId()){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fabClick(){
        Fragment navHost = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHost != null;
        NavController nav = NavHostFragment.findNavController(navHost);
        nav.navigate(R.id.addProduct);
    }

}
