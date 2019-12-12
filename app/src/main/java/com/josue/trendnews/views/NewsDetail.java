package com.josue.trendnews.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.josue.trendnews.R;
import com.josue.trendnews.utils.Utils;

public class NewsDetail extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private ImageView imageView;
    private TextView barTitle, barSubTitle, date, title;
    private boolean isHideToolBar = false;
    private LinearLayout tittleAppBar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String mUrl, mImg, mTitle, mDate, mSource, mAuthor, mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);

        toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);

        tittleAppBar = findViewById(R.id.title_appbar);
        imageView = findViewById(R.id.backdrop);
        barTitle = findViewById(R.id.title_on_appbar);
        barSubTitle = findViewById(R.id.subtitle_on_appbar);
        date = findViewById(R.id.date);
        title = findViewById(R.id.title);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mImg = intent.getStringExtra("img");
        mTitle = intent.getStringExtra("title");
        mDate = intent.getStringExtra("date");
        mSource = intent.getStringExtra("source");
        mAuthor = intent.getStringExtra("author");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this).load(mImg).apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).
                into(imageView);

        barTitle.setText(mSource);
        barSubTitle.setText(mUrl);
        title.setText(mTitle);

        String autor = null;
        String fecha = Utils.DateFormat(mDate);
        if (mAuthor != null || mAuthor != ""){
            autor = " \u2022 " + mAuthor;
        } else {
            autor = "";   }

        date.setText(autor + " " + fecha);

        initWebView(mUrl);

    }

    private void initWebView(String url){

        WebView wb = findViewById(R.id.webView);
         wb.getSettings().setLoadsImagesAutomatically(true);
         wb.getSettings().setJavaScriptEnabled(true);
         wb.getSettings().setDomStorageEnabled(true);
         wb.getSettings().setSupportZoom(true);
         wb.getSettings().setBuiltInZoomControls(true);
         wb.getSettings().setDisplayZoomControls(true);
         wb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
         wb.setWebViewClient(new WebViewClient());

         wb.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;

        if (percentage == 1f && isHideToolBar){
            tittleAppBar.setVisibility(View.VISIBLE);
            isHideToolBar = !isHideToolBar;
        }else if (percentage < 1f && !isHideToolBar){
            tittleAppBar.setVisibility(View.GONE);
            isHideToolBar = !isHideToolBar;
        }

    }
}
