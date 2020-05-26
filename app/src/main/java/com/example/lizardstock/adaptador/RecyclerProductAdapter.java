package com.example.lizardstock.adaptador;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.lizardstock.R;
import com.example.lizardstock.modelo.Product;
import com.example.lizardstock.vista.Detail.DetailProductView;

import java.util.List;

public class RecyclerProductAdapter extends RecyclerView.Adapter<RecyclerProductAdapter.viewHolder> {

    private List<Product> listProductos;

    public RecyclerProductAdapter(List<Product> listProductos) {
        this.listProductos = listProductos;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list,viewGroup,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {

        Product productModel = listProductos.get(position);
        //traemos la lista de articulos desde la posicion id, nombre, cantidad y lo asignamos a los campos definidos
        holder.txtCodigo.setText(productModel.getCodigo());
        holder.txtNombre.setText(productModel.getNombre());
        holder.txtPrecio.setText(productModel.getPrecio());
        holder.txtCantidad.setText(productModel.getCantidad());

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
                })
                .into(holder.imgArticulo);
    }

    @Override
    public int getItemCount() {
        return Math.max(listProductos.size(), 0);
    }

    //Referencia a item_list.xml
    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtCodigo, txtNombre, txtCantidad, txtPrecio;
        ImageView imgArticulo;
        ProgressBar progressBar;

        viewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            txtCodigo=itemView.findViewById(R.id.txtCodigo);
            txtNombre=itemView.findViewById(R.id.txtNombre);
            txtCantidad=itemView.findViewById(R.id.txtCantidad);
            txtPrecio=itemView.findViewById(R.id.txtPrecio);
            imgArticulo=itemView.findViewById(R.id.imgArticulo);
            progressBar = itemView.findViewById(R.id.progressList);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), DetailProductView.class);
            v.getContext().startActivity(intent);
        }
    }
}
