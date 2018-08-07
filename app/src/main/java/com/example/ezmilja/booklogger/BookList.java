package com.example.ezmilja.booklogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import static com.example.ezmilja.booklogger.ContentsActivity.books;

public class BookList extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        RecyclerView rv = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);

        BookAdapter adapter;
        adapter = new BookAdapter(books,this);
        rv.setAdapter(adapter);
    }
}
