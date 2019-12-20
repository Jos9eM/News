package com.josue.trendnews.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josue.trendnews.R;
import com.josue.trendnews.adapters.CategoryAdapter;
import com.josue.trendnews.adapters.CountryAdapter;
import com.josue.trendnews.models.Item;

import java.util.ArrayList;
import java.util.List;

public class DialogoCategoria extends AppCompatDialogFragment {

    private RecyclerView recyclerView;
    private CategoryDialogListener categoryDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rv, null);
        builder.setView(view).setTitle("Selecciona una Categoria");

        recyclerView = view.findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        CategoryAdapter categoryAdapter;

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final String[] categorysName = {"Todos" , "Entretenimiento", "General", "Salud", "Ciencia",
                "Deportes", "Tecnologia" };

        final String[] categoysCode = {"", "entertainment", "general", "health", "science",
                "sports", "technology" };

        final int[] caterogyImg = {R.mipmap.todas, R.mipmap.entertainment, R.mipmap.general,
                R.mipmap.health, R.mipmap.science, R.mipmap.sports, R.mipmap.technology };

        List <Item> categorias = new ArrayList<>();

        for (int i = 0 ; i < categorysName.length ; i++){
            Item categoria = new Item(categorysName[i], caterogyImg[i]);
            categorias.add(categoria); }

        categoryAdapter = new CategoryAdapter(categorias, getActivity().getApplicationContext());
        recyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String category = categorysName[position];
                String code = categoysCode[position];
                int image = caterogyImg[position];

                categoryDialogListener.applyCategory(category,code,image);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            categoryDialogListener = (CategoryDialogListener) context;
        }catch (ClassCastException e){
           throw new ClassCastException(context.toString() +
                   "Implementar Listener"); }
    }

    public interface CategoryDialogListener{
        void applyCategory(String category, String code, int image);
    }
}
