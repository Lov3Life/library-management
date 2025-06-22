package app;

import data.LibraryDataManager;
import model.Book;
import model.Library;
import model.Reader;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.stream.Stream;

public class LibraryApp {
  private final Library library;
  private final Scanner userInputScanner;
  private boolean globalFlag;

  public LibraryApp() {
    this.library = new Library(LibraryDataManager.loadLibraryName());
    this.userInputScanner = new Scanner(System.in);
    this.globalFlag = true;
  }

  public void startApp() {
    LibraryDataManager.loadReadersAndBooksData(library);
    System.out.printf("\n\n\nWelcome in Library ('%s') management System\n\n", library.getName());
    String options = """
            1. Register new Reader
            2. Change existing Reader data (need Reader ID)
            3. Remove Reader (need Reader ID)
            4. Print borrowed books by reader (need Reader ID)
            5. Add new book
            6. Change existing book data (need Book ID)
            7. Remove Book (need Book ID)
            8. Borrow Book (need Reader and Book ID)
            9. Return Book (need Reader and Book ID)
            10. Print Readers (optional options)
            11. Print Books (optional options)
            12. Change Library name
            0. Close App
            """;
    while(globalFlag) {
      System.out.println("\nAvailable options:\n");
      System.out.println(options);
      int output = getIntInputMenuOptions("Select option: ", "\nInvalid value. Select option: ", 0, 12);
      switch(output) {
        case 0 -> closeApp();
        case 1 -> addReaderToLibrary();
        case 2 -> changeReaderData();
        case 3 -> removeReaderFromLibrary();
        case 4 -> readerBorrowedBooks();
        case 5 -> addBookToLibrary();
        case 6 -> changeBookData();
        case 7 -> removeBookFromLibrary();
        case 8 -> borrowBook();
        case 9 -> returnBook();
        case 10 -> printReaders();
        case 11 -> printBooks();
        case 12 -> changeLibraryName();
        default -> System.out.println("\nInvalid input. Try again");
      }
    }
  }

  private String getStringInput(String message, boolean canBeEmpty) {
    while(true) {
      System.out.print(message);
      String input = userInputScanner.nextLine();
      if(canBeEmpty) {
        return input;
      } else {
        if(input.isEmpty()) {
          System.out.println("This value can not be empty");
        } else {
          return input;
        }
      }
    }
  }

  private int getIntInputMenuOptions(String messageFirst, String messageInvalid, int minVal, int maxVal) {
    System.out.print(messageFirst);
    int value;
    while(true) {
      try {
        value = Integer.parseInt(userInputScanner.nextLine());
        if(value >= minVal && value <= maxVal) {
          break;
        } else {
          throw new NumberFormatException("Number out of range");
        }
      } catch(NumberFormatException e) {
        System.out.print(messageInvalid);
      }
    }
    return value;
  }

  @SuppressWarnings("SameParameterValue")
  private Integer getIntegerInput(String message) {
    System.out.print(message);
    try {
      return Integer.parseInt(userInputScanner.nextLine());
    } catch(NumberFormatException e) {
      return null;
    }
  }

  private long getLongInput(String messageFirst, String messageInvalid) {
    System.out.print(messageFirst);
    long value;
    while(true) {
      try {
        value = Long.parseLong(userInputScanner.nextLine());
        break;
      } catch(NumberFormatException e) {
        System.out.print(messageInvalid);
      }
    }
    return value;
  }

  private LocalDate getDateInput(LocalDate now) {
    LocalDate bthDate;
    int year, month, day;
    while(true) {
      try {
        while(true) {
          try {
            System.out.print("Birth year: ");
            year = Integer.parseInt(userInputScanner.nextLine());
            if(year > 1000) {
              break;
            } else {
              throw new NumberFormatException("Invalid Year");
            }
          } catch(NumberFormatException e) {
            System.out.println("Invalid Year. Try again");
          }
        }
        while(true) {
          try {
            System.out.print("Birth month: ");
            month = Integer.parseInt(userInputScanner.nextLine());
            if(month > 0 && month <= 12) {
              break;
            } else {
              throw new NumberFormatException("Invalid month");
            }
          } catch(NumberFormatException e) {
            System.out.println("Invalid month. Try again");
          }
        }
        while(true) {
          try {
            System.out.print("Birth day: ");
            day = Integer.parseInt(userInputScanner.nextLine());
            if(day > 0 && day <= 31) {
              break;
            } else {
              throw new NumberFormatException("Invalid day. Try again");
            }
          } catch(NumberFormatException e) {
            System.out.println("Invalid day. Try again");
          }
        }
        bthDate = LocalDate.of(year, month, day);
        if(bthDate.isBefore(now) || bthDate.isEqual(now)) {
          break;
        } else {
          throw new DateTimeException("Invalid date");
        }
      } catch(DateTimeException e) {
        System.out.println("Invalid date. Try again");
      }
    }
    return bthDate;
  }

  private Reader getReaderByID(String messageFirst, String messageInvalid) {
    long readerID = getLongInput(messageFirst, messageInvalid);
    return library.getReaders().stream()
            .filter(reader -> reader.getId() == readerID)
            .findFirst()
            .orElse(null);
  }

  private Book getBookById(String messageFirst, String messageInvalid) {
    long bookID = getLongInput(messageFirst, messageInvalid);
    return library.getBooks().stream()
            .filter(book -> book.getId() == bookID)
            .findFirst()
            .orElse(null);
  }

  public void addReaderToLibrary() {
    System.out.println("\nRegister new reader\n");
    LocalDate now = LocalDate.now();
    String firstName, lastName;
    firstName = getStringInput("Name: ", false);
    lastName = getStringInput("Surname: ", false);
    LocalDate bthDate = getDateInput(now);
    Reader userPretender = new Reader(firstName, lastName, bthDate);
    changeReaderData(userPretender);
    library.registerUser(userPretender);
    System.out.println("\nReader successfully added");
  }


  public void changeReaderData() {
    System.out.println("\nChange reader data\n");
    Reader userToChange = getReaderByID("Enter reader ID to change reader data: ", "Invalid ID, try again: ");
    if(userToChange != null) {
      changeReaderData(userToChange);
    } else {
      System.out.println("Reader not exists in the database");
    }
  }

  private void changeReaderData(Reader reader) {
    boolean wantChangeFlag = true;
    while(wantChangeFlag) {
      System.out.printf("\nSummary:\n %s\n\n", reader.toString());
      String changes = """
              1. Name
              2. Surname
              3. Birth date
              0. save as is (Summary)
              """;
      System.out.println(changes);
      int output = getIntInputMenuOptions("Make any changes: ", "\nIncorrect option. Make any changes: ", 0, 3);
      switch(output) {
        case 1 -> {
          String name = getStringInput("Name: ", false);
          reader.setFirstName(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
          System.out.println("\nReader name changes");
        }
        case 2 -> {
          String surname = getStringInput("Surname: ", false);
          reader.setLastName(surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase());
          System.out.println("\nReader surname changes");
        }
        case 3 -> {
          reader.setBirthDate(getDateInput(LocalDate.now()));
          System.out.println("\nReader birth date changes");
        }
        case 0 -> wantChangeFlag = false;
        default -> System.out.println("\nIncorrect option selected");
      }
    }
  }

  public void removeReaderFromLibrary() {
    System.out.println("\nRemove existing reader\n");
    Reader userToRemove = getReaderByID("Enter reader ID to remove: ", "Invalid ID. Try again: ");
    if(userToRemove != null) {
      if(userToRemove.getBorrowedBooks().isEmpty()) {
        library.getReaders().remove(userToRemove);
        System.out.println("\nReader successfully removed");
      } else {
        System.out.println("\nReader has borrowed books. Can not remove");
      }
    } else {
      System.out.println("\nNo reader with this ID found");
    }
  }

  public void readerBorrowedBooks() {
    System.out.println("\nPrint reader borrowed books\n");
    Reader readerToCheck = getReaderByID("Reader ID: ", "Invalid reader ID, try again: ");
    if(readerToCheck == null) {
      System.out.println("\nInvalid Reader ID");
    } else {
      if(readerToCheck.getBorrowedBooks().isEmpty()) {
        System.out.println("\nReader borrowed any books");
        return;
      }
      System.out.println();
      for(Book book : readerToCheck.getBorrowedBooks()) {
        System.out.println(book);
      }
    }
  }

  public void addBookToLibrary() {
    System.out.println("\nAdd new book to library\n");
    String title, author, publishingHouse;
    Integer year;
    title = getStringInput("Title: ", false);
    author = getStringInput("Author: ", true);
    publishingHouse = getStringInput("Publishing house: ", true);
    year = getIntegerInput("Publishing year: ");
    Book book = new Book(title, author, publishingHouse, year);
    changeBookData(book);
    library.addBock(book);
    System.out.println("\nBook successfully added");
  }

  public void changeBookData() {
    System.out.println("\nChange book data\n");
    Book bookToChange = getBookById("Enter Book ID to change data: ", "Invalid ID, try again: ");
    if(bookToChange != null) {
      changeBookData(bookToChange);
    } else {
      System.out.println("Book not exists in the database");
    }
  }

  private void changeBookData(Book book) {
    boolean wantChangeFlag = true;
    while(wantChangeFlag) {
      System.out.printf("\nSummary:\n %s \n\n", book.toString());
      String changes = """
              1. Title
              2. Author
              3. Publishing house
              4. Publishing year
              0. save as is (Summary)
              """;
      System.out.println(changes);
      int output = getIntInputMenuOptions("Make any changes: ", "\nIncorrect option. Make any changes: ", 0, 4);
      switch(output) {
        case 1 -> {
          book.setTitle(getStringInput("Title: ", false));
          System.out.println("Title change");
        }
        case 2 -> {
          book.setAuthor(getStringInput("Author: ", true));
          System.out.println("Author change");
        }
        case 3 -> {
          book.setPublishingHouse(getStringInput("Publishing house: ", true));
          System.out.println("Publishing house change");
        }
        case 4 -> {
          book.setYear(getIntegerInput("Publishing year: "));
          System.out.println("Publishing year change");
        }
        case 0 -> wantChangeFlag = false;
        default -> System.out.println("Incorrect option selected");
      }
    }
  }

  public void removeBookFromLibrary() {
    System.out.println("\nRemove existing book from library\n");
    System.out.print("Enter book ID to remove: ");
    Book bookToRemove = getBookById("Enter book ID to remove: ", "Invalid ID, try again: ");
    if(bookToRemove != null) {
      if(bookToRemove.getBorrowedBy() == null) {
        library.getBooks().remove(bookToRemove);
        System.out.println("\nBook successfully removed");
      } else {
        System.out.println("\nThe book is borrowed. Cannot be removed");
      }
    } else {
      System.out.println("No Book with this ID found");
    }
  }

  public void borrowBook() {
    System.out.println("\nBorrow book by reader\n");
    Reader userToBorrow = getReaderByID("Reader ID: ", "Invalid reader ID, try again: ");
    Book bookToBorrow = getBookById("Book ID: ", "Invalid book, try again: ");
    if(userToBorrow == null) {
      System.out.println("Invalid reader ID");
      return;
    }
    if(bookToBorrow == null) {
      System.out.println("Invalid book ID");
      return;
    }
    for(Book book : userToBorrow.getBorrowedBooks()) {
      if(bookToBorrow.getId() == book.getId()) {
        System.out.println("Reader has already borrowed this book");
        return;
      }
    }
    if(bookToBorrow.getBorrowedBy() != null) {
      System.out.println("Book is currently being borrowed by someone");
      return;
    }
    userToBorrow.getBorrowedBooks().add(bookToBorrow);
    bookToBorrow.setBorrowedBy(userToBorrow);
    System.out.println("\nBook borrowed successfully");
  }

  public void returnBook() {
    System.out.println("\nReturn book by reader\n");
    Reader userToReturn = getReaderByID("Reader ID: ", "Invalid reader ID, try again: ");
    Book bookToReturn = getBookById("Book ID: ", "Invalid book, try again: ");
    if(userToReturn == null) {
      System.out.println("Invalid reader ID");
      return;
    }
    if(bookToReturn == null) {
      System.out.println("Invalid book ID");
      return;
    }
    if(userToReturn.getBorrowedBooks().isEmpty()) {
      System.out.println("User borrowed any books");
      return;
    }
    for(Book book : userToReturn.getBorrowedBooks()) {
      if(book.getId() == bookToReturn.getId()) {
        break;
      }
      System.out.println("Reader not borrowed this book");
      return;
    }
    bookToReturn.setBorrowedBy(null);
    userToReturn.getBorrowedBooks().remove(bookToReturn);
    System.out.println("\nBook Returned successfully");
  }

  public void printReaders() {
    System.out.println("\nPrint readers info\n");
    Stream<Reader> readerStream = null;
    String filterOptions = """
            1. ID
            2. Name
            3. Surname
            4. Name + Surname
            5. Name + Surname + birthDate
            6. Any borrowed books
            7. No borrowed books
            8. all readers
            """;
    System.out.println("Filter options:");
    System.out.println(filterOptions);
    int input = getIntInputMenuOptions("Pick option: ", "\nInvalid value. Pick option: ", 1, 8);
    switch(input) {
      case 1 -> {
        long id = getLongInput("Enter reader ID:", "Invalid ID, try again: ");
        readerStream = library.getReaders().stream()
                .filter(reader -> reader.getId() == id);
      }
      case 2 -> {
        String name = getStringInput("Name: ", false);
        readerStream = library.getReaders().stream()
                .filter(reader -> reader.getFirstName().equalsIgnoreCase(name));
      }
      case 3 -> {
        String surname = getStringInput("Surname: ", false);
        readerStream = library.getReaders().stream()
                .filter(reader -> reader.getLastName().equalsIgnoreCase(surname));
      }
      case 4 -> {
        String name = getStringInput("Name: ", false);
        String surname = getStringInput("Surname: ", false);
        readerStream = library.getReaders().stream()
                .filter(reader -> reader.getFirstName().equalsIgnoreCase(name))
                .filter(reader -> reader.getLastName().equalsIgnoreCase(surname));
      }
      case 5 -> {
        String name = getStringInput("Name: ", false);
        String surname = getStringInput("Surname: ", false);
        LocalDate date = getDateInput(LocalDate.now());
        readerStream = library.getReaders().stream()
                .filter(reader -> reader.getFirstName().equalsIgnoreCase(name))
                .filter(reader -> reader.getLastName().equalsIgnoreCase(surname))
                .filter(reader -> reader.getBirthDate().isEqual(date));
      }
      case 6 -> readerStream = library.getReaders().stream()
              .filter(reader -> !reader.getBorrowedBooks().isEmpty());
      case 7 -> readerStream = library.getReaders().stream()
              .filter(reader -> reader.getBorrowedBooks().isEmpty());
      case 8 -> readerStream = library.getReaders().stream();
      default -> System.out.println("\nInvalid input");
    }
    if(readerStream != null) {
      System.out.println("\nMatching readers:\n");
      readerStream
              .forEach(System.out::println);
    }
  }

  public void printBooks() {
    System.out.println("\nPrint books info\n");
    Stream<Book> bookStream = null;
    String filterOptions = """
            1. ID
            2. Title
            3. Author
            4. Title + Author
            5. Publishing house
            6. Title + Author + publishing house
            7. Year
            8. Borrowed books
            9. No borrowed books
            10. all books
            """;
    System.out.println("Filter options:");
    System.out.println(filterOptions);
    int input = getIntInputMenuOptions("Pick option: ", "\nInvalid value. Pick option: ", 1, 10);
    switch(input) {
      case 1 -> {
        long id = getLongInput("Enter book ID:", "Invalid ID, try again: ");
        bookStream = library.getBooks().stream()
                .filter(book -> book.getId() == id);
      }
      case 2 -> {
        String title = getStringInput("Title: ", false);
        bookStream = library.getBooks().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title));
      }
      case 3 -> {
        String author = getStringInput("Author: ", true);
        bookStream = library.getBooks().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author));
      }
      case 4 -> {
        String title = getStringInput("Title: ", false);
        String author = getStringInput("Author: ", true);
        bookStream = library.getBooks().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .filter(book -> book.getAuthor().equalsIgnoreCase(author));
      }
      case 5 -> {
        String house = getStringInput("Publishing house: ", true);
        bookStream = library.getBooks().stream()
                .filter(book -> book.getPublishingHouse().equalsIgnoreCase(house));
      }
      case 6 -> {
        String title = getStringInput("Title: ", false);
        String author = getStringInput("Author: ", true);
        String house = getStringInput("Publishing house: ", true);
        bookStream = library.getBooks().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .filter(book -> book.getPublishingHouse().equalsIgnoreCase(house));
      }
      case 7 -> {
        Integer year = getIntegerInput("Publishing year: ");
        if(year != null) {
          bookStream = library.getBooks().stream()
                  .filter(book -> book.getYear().equals(year));
        } else {
          bookStream = library.getBooks().stream()
                  .filter(book -> book.getYear() == null);
        }
      }
      case 8 -> bookStream = library.getBooks().stream()
              .filter(book -> book.getBorrowedBy() != null);
      case 9 -> bookStream = library.getBooks().stream()
              .filter(book -> book.getBorrowedBy() == null);
      case 10 -> bookStream = library.getBooks().stream();
      default -> System.out.println("\nInvalid input");
    }
    if(bookStream != null) {
      System.out.println("\nMatching books:\n");
      bookStream
              .forEach(System.out::println);
    }
  }

  public void changeLibraryName() {
    System.out.println("\nChange Library name (current name: '" + library.getName() + "')\n");
    String newName = getStringInput("New library name: ", false);
    library.setName(newName);
    System.out.println("\nLibrary name changed");
  }

  public void closeApp() {
    String options = """
            1. Yes
            0. No
            """;
    System.out.println("\n" + options);
    int validOutput = getIntInputMenuOptions("Save the entered data: ", "\nInvalid input. Save the entered data: ", 0, 1);
    switch(validOutput) {
      case 1 -> {
        LibraryDataManager.saveLibraryName(library.getName());
        LibraryDataManager.saveReaders(library.getReaders());
        LibraryDataManager.saveBooks(library.getBooks());
        globalFlag = false;
      }
      case 0 -> globalFlag = false;
      default -> System.out.println("\nInvalid input.");
    }
  }
}
