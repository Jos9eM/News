package com.josue.trendnews.adapters;

import android.content.Context;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.josue.trendnews.R;
import com.josue.trendnews.models.Article;
import com.josue.trendnews.utils.Utils;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.myViewHolder> {

    private List<Article> articles;
    private Context contex;
    private OnItemClickListener onItemClickListener;
    private int timeout = 3000;

    public NewsAdapter(List<Article> articles, Context contex) {
        this.articles = articles;
        this.contex = contex;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(contex).inflate(R.layout.item_news, parent, false);

        return new myViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, int position) {

        Article article = articles.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(contex).load(article.getUrlToImage()).apply(requestOptions).
                listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).transition(DrawableTransitionOptions.withCrossFade()).
                into(holder.image);

        holder.title.setText(article.getTitle());
        holder.author.setText(article.getAuthor());
        holder.published.setText(Utils.DateFormat(article.getPublishedAt()));
        holder.source.setText(article.getSource().getName());
        holder.description.setText(article.getDescription());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick (View view, int position);
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title, description, author, source, published;
        ImageView image;
        ProgressBar progressBar;

        OnItemClickListener onItemClickListener;

        public myViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            author = itemView.findViewById(R.id.author);
            source = itemView.findViewById(R.id.source);
            published = itemView.findViewById(R.id.published);

            image = itemView.findViewById(R.id.imageNew);
            progressBar = itemView.findViewById(R.id.progress);

            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());

        }
    }
}
