package com.example.ezmilja.booklogger;

import java.util.ArrayList;

import static com.example.ezmilja.booklogger.SplashScreen.j;

public class BooksArray {

    static int i = (int) j;
    public static Book[] books = new Book[i];
    public static ArrayList<Book> createBookList(int j) {
        ArrayList<Book> booklist = new ArrayList<Book>();

        for (int i = 0; i < j; i++) {
            booklist.add(new Book(books[i].isbn, books[i].author, books[i].imageAddress, books[i].description, books[i].bookName, books[i].publisher, books[i].max_copys, books[i].numberOfCopys, books[i].page, books[i].rating, books[i].num_rating));
        }
        return booklist;
    }

}
