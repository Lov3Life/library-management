import java.time.LocalDate;
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

  public List<Book> getBooks() {
    return books;
  }

  public List<Reader> getReaders() {
    return readers;
  }

  public boolean addBock(String title, String author, String publishingHouse, int year){
    Book book = new Book(title, author, publishingHouse, year);
    books.add(book);
    return true;
  }

  public void registerUser(Reader reader){
    readers.add(reader);
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
