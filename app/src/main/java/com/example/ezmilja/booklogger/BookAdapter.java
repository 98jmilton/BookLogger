package com.example.ezmilja.booklogger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import static com.example.ezmilja.booklogger.ContentsActivity.books;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ImageViewHolder> {


    private Context context;

    BookAdapter(Context context_) {

        this.context = context_;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        //BookAdapter.bind(items.get(position), clickListener);
        try {
            if (books[position].imageAddress != null)
            {
                Glide.with(this.context)
                        .load(books[position].imageAddress)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.getImage());

                holder.BookDetails.setText(books[position].bookName + "\n" + books[position].author + "\n" + books[position].genre);
                }
                }catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("Null Error = " + e );
        }
    }

    @Override
    public int getItemCount() {
        return books.length;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView BookImage;
        TextView BookDetails;

        ImageViewHolder(View itemView) {
            super(itemView);
            BookImage = itemView.findViewById(R.id.imageViewCustom);
            BookDetails = itemView.findViewById(R.id.bookDetails);
        }

        public ImageView getImage(){
            return this.BookImage;
        }
    }
}