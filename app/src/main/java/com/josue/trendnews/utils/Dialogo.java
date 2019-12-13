package com.josue.trendnews.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josue.trendnews.R;
import com.josue.trendnews.adapters.CountryAdapter;
import com.josue.trendnews.models.Item;

import java.util.ArrayList;
import java.util.List;

public class Dialogo extends DialogFragment {

    private TextView title;
    private RecyclerView recyclerView;

    public Dialogo(Context context){
        final Dialog dialog = new Dialog(context);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        CountryAdapter countryAdapter;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_rv);

        title = dialog.findViewById(R.id.title);
        recyclerView = dialog.findViewById(R.id.recycler);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        title.setText("Seleccione un Pais");

        String[] countrysName = {"Emiratos Arabes" , "Argentina", "Austria" , "Australia",
                "Belgica" , "Bulgaria", "Brazil" , "Canada", "Suiza" , "China",
                "Colombia" , "Republica Checa", "Alemania" , "Egipto", "Francia" , "Reino Unido",
                "Grecia" , "Honk Kong", "Hungria" , "Indonesia", "Irlanda" , "Israel",
                "India" , "Italia", "Japon" , "Corea", "Lituania" , "Latvia",
                "Marruecos" , "Mexico", "Malasia" , "Nigeria", "Paises Bajos" , "Noruega",
                "Nueva Zelanda" , "Filipinas", "Polonia" , "Portugal", "Rumania" , "Serbia",
                "Rusia" , "Arabia Saudita", "Suecia" , "Singapur", "Eslovenia" , "Eslovaquia",
                "Tailandia" , "Turquia", "Taiwan" , "Ucrania", "Estados Unidos" , "Venezuela", "Sudafrica"};


        int[] countryImg = {R.mipmap.ae, R.mipmap.ar, R.mipmap.at, R.mipmap.au, R.mipmap.be,
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

        countryAdapter = new CountryAdapter(paises, context);
        recyclerView.setAdapter(countryAdapter);
        countryAdapter.notifyDataSetChanged();
    }
}
