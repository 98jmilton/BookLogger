package com.example.ezmilja.booklogger;

public class Book {

    public String isbnX;
    public String bookNameX;
    public String imageAddressX;
    public String authorX;
    public String genreX;

    Book(String isbn,String bookName, String author, String imageAddress,String genre) {

        this.isbnX = isbn;
        this.bookNameX = bookName;
        this.imageAddressX = imageAddress;
        this.authorX = author;
        this.genreX = genre;
    }

    public String getName() {
        return bookNameX;
    }

    public String getAuthor() {
        return bookNameX;
    }

    public String getGenre() {
        return bookNameX;
    }
}