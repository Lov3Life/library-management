import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
  private String name;
  private List<Book> books;
  private List<Reader> readers;

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

  public boolean addBock(String title, String author, int year){
    Book book = new Book(title, author, year);
    books.add(book);
    return true;
  }

  public boolean registerUser(String firstName, String lastName, int year, int month, int day){
    Reader reader = new Reader(firstName, lastName, year, month, day);
    readers.add(reader);
    return true;
  }

  public boolean borrowBook(int bookIndex, int userIndex){
    Book bookToBorrow = books.get(bookIndex);
    Reader reader = readers.get(userIndex);
    if(!bookToBorrow.isAvailable()){
      return false;
    }
    bookToBorrow.setAvailable(false);
    return reader.getBorrowedBooks().add(bookToBorrow);
  }

  public boolean returnBook(int bookIndex, int userIndex){
    Book bookToReturn = books.get(bookIndex);
    Reader reader = readers.get(userIndex);
    if(bookToReturn.isAvailable()){
      return false;
    }
    bookToReturn.setAvailable(true);
    return reader.getBorrowedBooks().remove(bookToReturn);
  }

  public List<Book> listAvailableBooks(){
    return books.stream()
            .filter(Book::isAvailable)
            .collect(Collectors.toList());
  }

}
