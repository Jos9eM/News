package com.josue.trendnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.josue.trendnews.R;
import com.josue.trendnews.models.Item;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private List<Item> items;
    private Context contex;
    private OnItemClickListener onItemClickListener;


    public CountryAdapter(List<Item> items, Context contex) {
        this.items = items;
        this.contex = contex;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        ImageView image;
        OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.texto);
            image = itemView.findViewById(R.id.imagen);

            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(CountryAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick (View view, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(contex).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Item item = items.get(position);
        holder.title.setText(item.getNombre());
        holder.image.setBackgroundResource(item.getImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}