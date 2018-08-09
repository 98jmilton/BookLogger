package com.example.ezmilja.booklogger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import static com.example.ezmilja.booklogger.ContentsActivity.books;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ImageViewHolder> {
    private Book[] images;
    private Context context;
    private BooksAdapterListener listener;
    private List<Book> bookList;
    private List<Book> bookListFiltered;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.bookDetails);
            thumbnail = view.findViewById(R.id.imageViewCustom);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // send selected book row in callback
                    listener.onBookSelected(bookListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    BookAdapter(Book[] books, Context context_, List<Book> bookList) {

        this.images = books;
        this.context = context_;
        this.listener = listener;
        this.bookList = bookList;
        this.bookListFiltered = bookList;
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
         {
            try {
                if (books[position].imageAddress != null)
                {
                    Glide.with(this.context)
                            .load(books[position].imageAddress)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.getImage());

                    holder.BookDetails.setText(books[position].bookName + "\n" + books[position].author + "\n" + books[position].genre);
                }

                if (books[position].imageAddress == null){
                    System.out.println("fu");
                }

            }catch (NullPointerException e){
                e.printStackTrace();
                System.out.println("REEEEEEEEEEEEEEEEEEEEEeeeeee" + e );

            }
        }
    }

    @Override
    public int getItemCount() {
        return images.length;
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

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    bookListFiltered = bookList;
                } else {
                    List<Book> filteredList = new ArrayList<>();
                    for (Book row : bookList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    bookListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = bookListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                bookListFiltered = (ArrayList<Book>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface BooksAdapterListener {
        void onBookSelected(Book bookRow);
    }
}