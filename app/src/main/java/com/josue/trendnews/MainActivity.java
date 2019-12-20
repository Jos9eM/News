package com.josue.trendnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.josue.trendnews.adapters.NewsAdapter;
import com.josue.trendnews.api.Client;
import com.josue.trendnews.api.NewsInterface;
import com.josue.trendnews.models.Article;
import com.josue.trendnews.models.Item;
import com.josue.trendnews.models.News;
import com.josue.trendnews.utils.DialogoCategoria;
import com.josue.trendnews.utils.DialogoPais;
import com.josue.trendnews.utils.Utils;
import com.josue.trendnews.views.Login;
import com.josue.trendnews.views.NewsDetail;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        DialogoCategoria.CategoryDialogListener, DialogoPais.CountryDialogListener {

    private static final String API_KEY = "18434d8a06324694abbfb28c794292d1";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private TextView topHead;
    private List<Article> articles = new ArrayList<>();
    private NewsAdapter adapter;
    private String TAG = MainActivity.class.getSimpleName();
    private String categoria = "", pais = Utils.getCountry();
    DialogoPais dialogoPais = new DialogoPais();
    private Menu menu;
    DialogoCategoria dialogoCategoria = new DialogoCategoria();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = findViewById(R.id.refesh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        recyclerView = findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        topHead = findViewById(R.id.head);
        loadingRefresh("");
    }

    public void loadJson (final String keyword, final String country, final String category){

        refreshLayout.setRefreshing(true);

        NewsInterface newsInterface = Client.getApi().create(NewsInterface.class);
        String language = Utils.getLanguage();

        Call<News> call;

        if (keyword.length() > 0){
            call = newsInterface.getNewsSearch(keyword, language, "publishedAt", API_KEY);
            topHead.setVisibility(View.INVISIBLE);
        } else {
            call = newsInterface.getNews(country, category,  API_KEY);
            topHead.setVisibility(View.VISIBLE); }

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticles() != null){

                    if (!articles.isEmpty()){
                        articles.clear();   }

                    articles = response.body().getArticles();
                    adapter = new NewsAdapter(articles, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    initListener();

                    refreshLayout.setRefreshing(false);

                }else{
                    refreshLayout.setRefreshing(false);
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage("No hay resultados que mostrar...");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        MenuItem menuItem = menu.findItem(R.id.search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2){
                    loadingRefresh(query);  }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        menuItem.getIcon().setVisible(false, false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.close:
                endSesion();
                return true;
            case R.id.country:
                openCountrys();
                return true;
            case R.id.filter:
                openCategorys();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openCategorys() {
        dialogoCategoria.show(getSupportFragmentManager(), "Dialogo Categorias");
    }

    private void openCountrys() {
        dialogoPais.show(getSupportFragmentManager(), "Dialogo Paises");
    }

    @Override
    public void onRefresh() {
        loadJson("", pais, categoria);
    }

    public void loadingRefresh(final String keyword){
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadJson(keyword, pais, categoria);
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Salir");
        alertDialog.setMessage(getString(R.string.salirApp));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss(); }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(homeIntent);  }
                });
        alertDialog.show();
    }

    private void initListener() {
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(MainActivity.this, NewsDetail.class);

                Article article = articles.get(position);
                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img", article.getUrlToImage());
                intent.putExtra("date", article.getPublishedAt());
                intent.putExtra("source", article.getSource().getName());
                intent.putExtra("author", article.getAuthor());

                startActivity(intent);
            }
        });
    }

    private void endSesion(){
        FirebaseAuth.getInstance().signOut();
        if (LoginManager.getInstance() != null){
            LoginManager.getInstance().logOut(); }
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);      }

    @Override
    public void applyCategory(String category, String code, int image) {
        categoria = code;
        Toast.makeText(getApplicationContext(), "Noticias de: " + category, Toast.LENGTH_SHORT).show();
        dialogoCategoria.dismiss();
        loadJson("", pais, categoria);
    }

    @Override
    public void applyCountry(String country, String code, int image) {
        pais = code;
        Toast.makeText(getApplicationContext(), "Noticias en: " + country, Toast.LENGTH_SHORT).show();
        dialogoPais.dismiss();
        loadJson("", pais, categoria);
        menu.getItem(2).setIcon(ContextCompat.getDrawable(this, image));
    }
}
