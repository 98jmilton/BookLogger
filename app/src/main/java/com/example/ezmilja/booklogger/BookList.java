package com.example.ezmilja.booklogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import static com.example.ezmilja.booklogger.ContentsActivity.books;

public class BookList extends AppCompatActivity {

    private RecyclerView Rv;

    private RecyclerView.LayoutManager layoutManager;

    private BookAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Rv = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Rv.setHasFixedSize(true);
        Rv.setLayoutManager(layoutManager);

        adapter=new BookAdapter(books,this);
        Rv.setAdapter(adapter);


    }
}
