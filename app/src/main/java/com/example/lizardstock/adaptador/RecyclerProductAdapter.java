package com.example.lizardstock.adaptador;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.lizardstock.R;
import com.example.lizardstock.interfaces.IListProduct;
import com.example.lizardstock.modelo.Product;
import com.example.lizardstock.utilidades.Utilidades;
import com.example.lizardstock.vista.activities.DetailProductView;

import java.util.List;

public class RecyclerProductAdapter extends RecyclerView.Adapter<RecyclerProductAdapter.viewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<Product> listProductos;
    private View.OnClickListener listener;
    private View.OnLongClickListener onLongClickListener;

    public RecyclerProductAdapter(List<Product> listProductos) {
        this.listProductos = listProductos;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        int layout = 0;
        if(Utilidades.visualizacion==Utilidades.LIST){
            layout=R.layout.item_list;
        }else{
            layout=R.layout.item_grid;
        }

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout,null,false);
        view.setOnClickListener(listener);
        view.setOnLongClickListener(onLongClickListener);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {

        Product productModel = listProductos.get(position);
        if(Utilidades.visualizacion==Utilidades.LIST){
            try {
                holder.txtCodigo.setText(productModel.getCodigo());
                holder.txtNombre.setText(productModel.getNombre());
                holder.txtPrecio.setText(productModel.getPrecio());
                holder.txtCantidad.setText(productModel.getCantidad());
            }catch (NullPointerException npe){
                npe.printStackTrace();
                if(productModel == null){
                    Toast.makeText(holder.imgArticulo.getContext(), npe.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }
        //Carga la imagen en el imageView usando Glide
        Glide.with(holder.itemView.getContext())
                .load(productModel.getImagenUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.imgArticulo.setVisibility(View.VISIBLE);
                        holder.imgArticulo.setImageResource(R.drawable.ic_error_black_24dp);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.imgArticulo.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(holder.imgArticulo);
    }  //nullpointex

    @Override
    public int getItemCount() {return Math.max(listProductos.size(), 0);}

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
        this.onLongClickListener=onLongClickListener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(onLongClickListener!=null){
            onLongClickListener.onLongClick(v);
        }
        return true;
    }

    //Referencia a item_list.xml o item_grid.xml
    static class viewHolder extends RecyclerView.ViewHolder{

        TextView txtCodigo, txtNombre, txtCantidad, txtPrecio;
        ImageView imgArticulo;
        ProgressBar progressBar;

        viewHolder(@NonNull View itemView) {
            super(itemView);

           if(Utilidades.visualizacion==Utilidades.LIST){
            txtCodigo=itemView.findViewById(R.id.txtCodigo);
            txtNombre=itemView.findViewById(R.id.txtNombre);
            txtCantidad=itemView.findViewById(R.id.txtCantidad);
            txtPrecio=itemView.findViewById(R.id.txtPrecio);
           }
            imgArticulo=itemView.findViewById(R.id.imgArticulo);
            progressBar = itemView.findViewById(R.id.progressList);
        }
    }
}
