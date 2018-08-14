package com.example.ezmilja.booklogger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ImageViewHolder> {

    private Book[] books;
    private ArrayList bookList;
    private Context context;
    private ArrayList filteredData;
    private String currentIsbn;
    private ItemFilter mFilter = new ItemFilter();

    BookAdapter(Context context_, ArrayList bookList) {

        this.context  = context_;
        this.bookList = bookList;
        this.filteredData = bookList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder,final int position) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference BookRef = database.getReference("/Books/");
        BookRef.addValueEventListener(new ValueEventListener() {
            int i = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot BookSnapshot : dataSnapshot.getChildren()) {

                    String isbn         = (String) BookSnapshot.child("ISBN").getValue();
                    String bookName     = (String) BookSnapshot.child("BookName").getValue();
                    String author       = (String) BookSnapshot.child("Author").getValue();
                    String imageAddress = (String) BookSnapshot.child("ImageAddress").getValue();
                    String genre        = (String) BookSnapshot.child("Genre").getValue();

                    try {
                        books[i] = new Book(isbn, bookName, author, imageAddress, genre);
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        return;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        try {
            if (books[position].imageAddressX != null) {
                Glide.with(this.context)
                        .load(books[position].imageAddressX)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.getImage());

                holder.BookDetails.setText(books[position].bookNameX + "\n\n" + books[position].authorX + "\n\n" + books[position].genreX);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentIsbn = books[position].isbnX;
                        Context context = view.getContext();
                        Intent intent = new Intent(context, BookDetailsPage.class);
                        context.startActivity(intent);
                    }
                });
            }
        }

        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return books.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

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
        return mFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference BookRef = database.getReference("/Books/");
            bookList.clear();
            BookRef.addValueEventListener(new ValueEventListener() {
                int i = 0;

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot BookSnapshot : dataSnapshot.getChildren()) {

                        String bookName = (String) BookSnapshot.child("BookName").getValue();
                        String author   = (String) BookSnapshot.child("Author").getValue();
                        String genre    = (String) BookSnapshot.child("Genre").getValue();

                        try{
                            bookList.addAll(Arrays.asList(bookName,author,genre));
                            System.out.println(bookList);
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            return;
                        }
                        i++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<String> list = bookList;

            int count = list.size();
            final ArrayList<String> nList = new ArrayList<>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nList.add(filterableString);
                }
            }

            results.values = nList;
            results.count  = nList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}