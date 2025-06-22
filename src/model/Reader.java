package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reader {
  private static long LAST_ID;
  private final long id;
  private String firstName;
  private String lastName;
  private LocalDate birthDate;
  private final List<Book> borrowedBooks;

  static {
    LAST_ID = 1;
  }

  public Reader(String firstName, String lastName, LocalDate date) {
    this.id = LAST_ID++;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = date;
    this.borrowedBooks = new ArrayList<>();
  }

  public Reader(long id, String firstName, String lastName, LocalDate date) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = date;
    this.borrowedBooks = new ArrayList<>();
  }

  public long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public List<Book> getBorrowedBooks() {
    return borrowedBooks;
  }

  public static void setLastId(long lastId) {
    LAST_ID = lastId;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  @Override
  public String toString() {
    StringBuilder borrowedBooksString = new StringBuilder();
    if(borrowedBooks.isEmpty()) {
      borrowedBooksString.append("any books borrowed");
    } else {
      String prefix = "";
      for(Book book : borrowedBooks) {
        borrowedBooksString.append(prefix).append(book.getId());
        prefix = ",";
      }
    }
    return "id: " + id +
            ", firstName: '" + firstName + '\'' +
            ", lastName: '" + lastName + '\'' +
            ", birthDate: " + birthDate +
            ", borrowedBooks (ID's): " + borrowedBooksString;
  }
}
