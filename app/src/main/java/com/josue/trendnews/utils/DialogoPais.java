package com.josue.trendnews.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josue.trendnews.R;
import com.josue.trendnews.adapters.CountryAdapter;
import com.josue.trendnews.models.Item;

import java.util.ArrayList;
import java.util.List;

public class DialogoPais extends AppCompatDialogFragment {

    private RecyclerView recyclerView;
    private CountryDialogListener countryDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rv, null);
        builder.setView(view).setTitle("Selecciona un Pais");

        recyclerView = view.findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        final CountryAdapter countryAdapter;

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final String[] countrysName = {"Emiratos Arabes" , "Argentina", "Austria" , "Australia",
                "Belgica" , "Bulgaria", "Brazil" , "Canada", "Suiza" , "China",
                "Colombia" , "Republica Checa", "Alemania" , "Egipto", "Francia" , "Reino Unido",
                "Grecia" , "Honk Kong", "Hungria" , "Indonesia", "Irlanda" , "Israel",
                "India" , "Italia", "Japon" , "Corea", "Lituania" , "Latvia",
                "Marruecos" , "Mexico", "Malasia" , "Nigeria", "Paises Bajos" , "Noruega",
                "Nueva Zelanda" , "Filipinas", "Polonia" , "Portugal", "Rumania" , "Serbia",
                "Rusia" , "Arabia Saudita", "Suecia" , "Singapur", "Eslovenia" , "Eslovaquia",
                "Tailandia" , "Turquia", "Taiwan" , "Ucrania", "Estados Unidos" , "Venezuela", "Sudafrica"};

        final String[] countryCode = {"ae" , "ar", "at" , "au", "be" , "bg", "br" , "ca", "ch" , "cn",
                "co" , "cz", "de" , "eg", "fr" , "gb", "gr" , "hk", "hu" , "id", "ie" , "il",
                "in" , "it", "jp" , "kr", "lt" , "lv", "ma" , "mx", "my" , "ng", "nl" , "no",
                "nz" , "ph", "pl" , "pt", "ro" , "rs", "ru" , "sa", "se" , "sg", "si" , "sk",
                "th" , "tr", "tw" , "ua", "us" , "ve", "za"};

        final int[] countryImg = {R.mipmap.ae, R.mipmap.ar, R.mipmap.at, R.mipmap.au, R.mipmap.be,
                R.mipmap.bg, R.mipmap.br, R.mipmap.ca, R.mipmap.ch, R.mipmap.cn, R.mipmap.co,
                R.mipmap.cz, R.mipmap.de, R.mipmap.eg, R.mipmap.fr, R.mipmap.gb, R.mipmap.gr,
                R.mipmap.hk, R.mipmap.hu, R.mipmap.id, R.mipmap.ie, R.mipmap.il, R.mipmap.in,
                R.mipmap.it, R.mipmap.jp, R.mipmap.kr, R.mipmap.lt, R.mipmap.lv, R.mipmap.ma,
                R.mipmap.mx, R.mipmap.my, R.mipmap.ng, R.mipmap.nl, R.mipmap.no, R.mipmap.nz,
                R.mipmap.ph, R.mipmap.pl, R.mipmap.pt, R.mipmap.ro, R.mipmap.rs, R.mipmap.ru,
                R.mipmap.sa, R.mipmap.se, R.mipmap.sg, R.mipmap.si, R.mipmap.sk, R.mipmap.th,
                R.mipmap.tr, R.mipmap.tw, R.mipmap.ua, R.mipmap.us, R.mipmap.ve, R.mipmap.za};

        List<Item> paises = new ArrayList<>();

        for (int i = 0 ; i < countrysName.length ; i++){
            Item pais = new Item(countrysName[i], countryImg[i]);
            paises.add(pais); }

        countryAdapter = new CountryAdapter(paises, getActivity().getApplicationContext());
        recyclerView.setAdapter(countryAdapter);
        countryAdapter.notifyDataSetChanged();

        countryAdapter.setOnItemClickListener(new CountryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String country = countrysName[position];
                String code = countryCode[position];
                int image = countryImg[position];

                countryDialogListener.applyCountry(country, code, image);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            countryDialogListener = (CountryDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Implementar Listener"); }
    }

    public interface CountryDialogListener{
        void applyCountry(String country, String code, int image);
    }
}
