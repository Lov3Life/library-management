package model;

import java.util.ArrayList;
import java.util.List;

public class Library {
  private String name;
  private final List<Book> books;
  private final List<Reader> readers;

  public Library(String name) {
    this.name = name;
    this.books = new ArrayList<>();
    this.readers = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Book> getBooks() {
    return books;
  }

  public List<Reader> getReaders() {
    return readers;
  }

  public void addBock(Book book) {
    books.add(book);
  }

  public void registerUser(Reader reader) {
    readers.add(reader);
  }

}
