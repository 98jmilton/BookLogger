package com.example.ezmilja.booklogger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static com.example.ezmilja.booklogger.ContentsActivity.currentIsbn;
import static com.example.ezmilja.booklogger.ContentsActivity.detailscurrentPage;
import static com.example.ezmilja.booklogger.ContentsActivity.listcurrentPage;
import static com.example.ezmilja.booklogger.SplashScreen.BookRef;


public class BookList extends AppCompatActivity {




    public static Book book;
    SearchView searchView;
    public  ArrayList<Book> listViewList =new ArrayList<Book>();
    private CustomAdapter customAdapter;
    int welp = 0;
    Boolean reload;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        reload=true;

        //Pull books from database
        BookRef.child("/Books/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                int k;
                if(listcurrentPage){

                    listViewList.clear();
                    for (DataSnapshot BookSnapshot : dataSnapshot.getChildren()) {
                        k = (int) dataSnapshot.getChildrenCount();

                        String isbn         = (String) BookSnapshot.child("ISBN").getValue();
                        String bookName     = (String) BookSnapshot.child("BookName").getValue();
                        String author       = (String) BookSnapshot.child("Author").getValue();
                        String imageAddress = (String) BookSnapshot.child("ImageAddress").getValue();
                        String genre        = (String) BookSnapshot.child("Genre").getValue();

                        try{
                            if(isbn!=null && bookName!=null && author!=null && imageAddress!=null && genre!=null){
                                listViewList.add(book= new Book(isbn,bookName,author,imageAddress,genre));
                                if(i==k-1){
                                    makeListView();
                                }
                                i++;
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            return;
                        }
                    }
                    welp++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Search for books
        searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customAdapter.getFilter().filter(s);
                return false;
            }
        });

        searchView.setIconifiedByDefault(true);
    }

    private void makeListView(){

        sortlist(listViewList);
        customAdapter = new BookList.CustomAdapter(BookList.this, listViewList);
        final ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(customAdapter);

    }

    private void sortlist(List list) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o, Object t1) {
                Book b1 = (Book) o;
                Book b2 = (Book) t1;
                return b1.getName().compareTo(b2.getName());
            }
        });
    }

    class CustomAdapter extends BaseAdapter implements Filterable {

        BookFilter bookFilter;
        Context context;
        List<Book> showList;

        CustomAdapter(Context context, List<Book> items) {
            this.context = context;
            this.showList = items;
        }

        private class ViewHolder {
            TextView bookDetails;
            ImageView image;
        }

        @Override
        public int getCount() {return showList.size();}

        @Override
        public Object getItem(int position) {return showList.get(position);}

        @Override
        public long getItemId(int position) {return showList.get(position).hashCode();}

        //Fill layout with image, name and author
        @Override
        public View getView (final int position, final View view, ViewGroup parent) {

            View vi = view;
            final ViewHolder holder;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final Book myBook = showList.get(position);

            if (view == null) {

                vi = inflater.inflate(R.layout.custom_layout, null);
                holder = new ViewHolder();
                holder.bookDetails = vi.findViewById(R.id.bookDetails);
                holder.image = vi.findViewById(R.id.imageViewCustom);
                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            holder.bookDetails.setText(myBook.getName()+"\n\n"+myBook.getAuthor());

            if (myBook.imageAddressX != null) {
                Glide.with(context).load(myBook.imageAddressX).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);
            }

            //go to each book's own details page when clicked
                vi.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {

                        currentIsbn = myBook.isbnX;
                        Context context = v.getContext();
                        Intent intent = new Intent(context, BookDetailsPage.class);
                        listcurrentPage = false;
                        detailscurrentPage=true;
                        context.startActivity(intent);
                        BookList.this.finish();
                    }
                });
                holder.bookDetails.setText(myBook.getName()+"\n\n"+myBook.getAuthor()+"\n\n"+myBook.getGenre());

                if (myBook.imageAddressX != null) {
                    Glide.with(context).load(myBook.imageAddressX).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);
                }
                return vi;

        }

        //Filter from search method
        @Override
        public Filter getFilter() {
            if (bookFilter == null)
                bookFilter = new BookFilter();
            return bookFilter;
        }

        class BookFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // We implement here the filter logic
                if (constraint == null || constraint.length() < 1) {
                    // No filter implemented we return all the list
                    showList = listViewList;
                    results.values = showList;
                    results.count = showList.size();
                } else {
                    // We perform filtering operation
                    List<Book> nBookList = new ArrayList<>();

                    for (Book b : listViewList) {
                        if (b.getName().toUpperCase()
                                .contains(constraint.toString().toUpperCase())) {
                            nBookList.add(b);
                        } else if (b.getAuthor().toUpperCase()
                                .contains(constraint.toString().toUpperCase())) {
                            nBookList.add(b);

                        }
                    }
                    showList = nBookList;
                    results.values = nBookList;
                    results.count = nBookList.size();
                }
                return results;
            }

            // Now we have to inform the adapter about the new list filtered
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                if (results.count == 0) {
                    notifyDataSetInvalidated();
                } else {
                    showList = (List<Book>) results.values;
                    notifyDataSetChanged();
                }
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent = new Intent( this, ContentsActivity.class);
        startActivity(intent);
    }
}
