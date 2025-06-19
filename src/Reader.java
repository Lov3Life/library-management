import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reader {
  private static long LAST_ID;
  private long id;
  private String firstName;
  private String lastName;
  private LocalDate birthDate;
  private List<Book> borrowedBooks;

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
    return "Reader{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", birthDate=" + birthDate +
            '}';
  }
}
