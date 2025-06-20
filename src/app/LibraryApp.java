package app;

import data.LibraryDataManager;
import model.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.stream.Stream;

public class LibraryApp {
  private final Library library;
  private final Scanner userInputScanner;

  public LibraryApp() {
    this.library = new Library(LibraryDataManager.loadLibraryName());
    this.userInputScanner = new Scanner(System.in);
  }

  public void startApp() {
    LibraryDataManager.loadReadersAndBooksData(library);
    System.out.printf("\n\n\nWelcome in Library ('%s') management System\n\n", library.getName());
    boolean globalFlag = true;
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
            
            Select option:\s""";
    while(globalFlag) {
      System.out.println("\nAvailable options:\n");
      System.out.print(options);
      String output = userInputScanner.nextLine();
      int validOutput;
      try {
        validOutput = Integer.parseInt(output);
      } catch(NumberFormatException e) {
        System.out.println("\nInvalid input. Try again");
        continue;
      }
      switch(validOutput) {
        case 0 -> {
          closeApp();
          globalFlag = false;
        }
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

  public void addReaderToLibrary() {
    System.out.println("\nRegister new reader\n");
    LocalDate now = LocalDate.now();
    String firstName, lastName;
    firstName = getStringInput("Name: ");
    lastName = getStringInput("Surname: ");
    LocalDate bthDate = getDateInput(now);
    Reader userPretender = new Reader(firstName, lastName, bthDate);
    changeReaderData(userPretender);
    library.registerUser(userPretender);
    System.out.println("\nReader successfully added");
  }

  public void removeReaderFromLibrary() {
    System.out.println("\nRemove existing reader\n");
    long userID = getLongInput("Enter reader ID to remove: ", "Invalid ID. Try again: ");
    var userToRemove = library.getReaders().stream()
            .filter(reader -> reader.getId() == userID)
            .findFirst()
            .orElse(null);
    if(userToRemove != null) {
      library.getReaders().remove(userToRemove);
      System.out.println("\nReader successfully removed");
    } else {
      System.out.println("\nNo reader with this ID found");
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
      System.out.print("Make any changes: ");
      int output;
      while(true) {
        try {
          output = Integer.parseInt(userInputScanner.nextLine());
          break;
        } catch(NumberFormatException e) {
          System.out.println("Incorrect option selected");
        }
      }
      switch(output) {
        case 1 -> {
          String name = getStringInput("Name: ");
          reader.setFirstName(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
          System.out.println("\nReader name changes");
        }
        case 2 -> {
          String surname = getStringInput("Surname: ");
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

  private String getStringInput(String message) {
    System.out.print(message);
    return userInputScanner.nextLine();
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

  public void changeReaderData() {
    System.out.println("\nChange reader data\n");
    long userID = getLongInput("Enter reader ID to change reader data: ", "Invalid ID, try again: ");
    var userToChange = library.getReaders().stream()
            .filter(reader -> reader.getId() == userID)
            .findFirst()
            .orElse(null);
    if(userToChange != null) {
      changeReaderData(userToChange);
    } else {
      System.out.println("Reader not exists in the database");
    }
  }

  public void readerBorrowedBooks() {
    System.out.println("\nPrint reader borrowed books\n");
    Reader readerToCheck = getReaderByID();
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
    title = getStringInput("Title: ");
    author = getStringInput("Author: ");
    publishingHouse = getStringInput("Publishing house: ");
    year = getIntegerInput("Publishing year: ");
    Book book = new Book(title, author, publishingHouse, year);
    changeBookData(book);
    library.addBock(book);
    System.out.println("\nBook successfully added");
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
      System.out.print("Make any changes: ");
      int output;
      while(true) {
        try {
          output = Integer.parseInt(userInputScanner.nextLine());
          break;
        } catch(NumberFormatException e) {
          System.out.println("Incorrect option selected");
        }
      }
      switch(output) {
        case 1 -> {
          book.setTitle(getStringInput("Title: "));
          System.out.println("Title change");
        }
        case 2 -> {
          book.setAuthor(getStringInput("Author: "));
          System.out.println("Author change");
        }
        case 3 -> {
          book.setPublishingHouse(getStringInput("Publishing house: "));
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

  public void changeBookData() {
    System.out.println("\nChange book data\n");
    long bookID = getLongInput("Enter Book ID to change data: ", "Invalid ID, try again: ");
    var bookToChange = library.getBooks().stream()
            .filter(book -> book.getId() == bookID)
            .findFirst()
            .orElse(null);
    if(bookToChange != null) {
      changeBookData(bookToChange);
    } else {
      System.out.println("Book not exists in the database");
    }
  }

  public void removeBookFromLibrary() {
    System.out.println("\nRemove existing book from library\n");
    System.out.print("Enter book ID to remove: ");
    long bookID = getLongInput("Enter book ID to remove: ", "Invalid ID, try again: ");
    var bookToRemove = library.getBooks().stream()
            .filter(book -> book.getId() == bookID)
            .findFirst()
            .orElse(null);
    if(bookToRemove != null) {
      library.getBooks().remove(bookToRemove);
      System.out.println("\nBook successfully removed");
    } else {
      System.out.println("No Book with this ID found");
    }
  }

  public void borrowBook() {
    System.out.println("\nBorrow book by reader\n");
    Reader userToBorrow = getReaderByID();
    Book bookToBorrow = getBookById();
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
    Reader userToReturn = getReaderByID();
    Book bookToReturn = getBookById();
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

  private Reader getReaderByID() {
    long readerID = getLongInput("Reader ID: ", "Invalid reader ID, try again: ");
    return library.getReaders().stream()
            .filter(reader -> reader.getId() == readerID)
            .findFirst()
            .orElse(null);
  }

  private Book getBookById() {
    long bookID = getLongInput("Book ID: ", "Invalid book, try again: ");
    return library.getBooks().stream()
            .filter(book -> book.getId() == bookID)
            .findFirst()
            .orElse(null);
  }

  public void printReaders() {
    System.out.println("\nPrint readers info\n");
    Stream<Reader> readerStream = library.getReaders().stream();
    String filterOptions = """
            1. Name
            2. Surname
            3. Name + Surname
            4. Name + Surname + birthDate
            5. Any borrowed books
            6. No borrowed books
            7. all readers
            """;
    System.out.println(filterOptions);
    int input;
    while(true) {
      System.out.print("Pick option: ");
      try {
        input = Integer.parseInt(userInputScanner.nextLine());
        break;
      } catch(NumberFormatException e) {
        System.out.print("Invalid input. ");
      }
    }
    switch(input) {
      case 1 -> {
        String name = getStringInput("Name: ");
        System.out.println("\nMatching readers:\n");
        readerStream
                .filter(reader -> reader.getFirstName().equalsIgnoreCase(name))
                .forEach(System.out::println);
      }
      case 2 -> {
        String surname = getStringInput("Surname: ");
        System.out.println("\nMatching readers:\n");
        readerStream
                .filter(reader -> reader.getLastName().equalsIgnoreCase(surname))
                .forEach(System.out::println);
      }
      case 3 -> {
        String name = getStringInput("Name: ");
        String surname = getStringInput("Surname: ");
        System.out.println("\nMatching readers:\n");
        readerStream
                .filter(reader -> reader.getFirstName().equalsIgnoreCase(name))
                .filter(reader -> reader.getLastName().equalsIgnoreCase(surname))
                .forEach(System.out::println);
      }
      case 4 -> {
        String name = getStringInput("Name: ");
        String surname = getStringInput("Surname: ");
        LocalDate date = getDateInput(LocalDate.now());
        System.out.println("\nMatching readers:\n");
        readerStream
                .filter(reader -> reader.getFirstName().equalsIgnoreCase(name))
                .filter(reader -> reader.getLastName().equalsIgnoreCase(surname))
                .filter(reader -> reader.getBirthDate().isEqual(date))
                .forEach(System.out::println);
      }
      case 5 -> {
        System.out.println("\nMatching readers:\n");
        readerStream
              .filter(reader -> !reader.getBorrowedBooks().isEmpty())
              .forEach(System.out::println);
      }
      case 6 -> {
        System.out.println("\nMatching readers:\n");
        readerStream
              .filter(reader -> reader.getBorrowedBooks().isEmpty())
              .forEach(System.out::println);
      }
      case 7 -> {
        System.out.println("\nMatching readers:\n");
        readerStream
              .forEach(System.out::println);
      }
      default -> System.out.println("\nInvalid input");
    }
  }

  public void printBooks() {
    System.out.println("\nPrint books info\n");
    Stream<Book> bookStream = library.getBooks().stream();
    String filterOptions = """
            1. Title
            2. Author
            3. Title + Author
            4. Publishing house
            5. Title + Author + publishing house
            6. Year
            7. Borrowed books
            8. No borrowed books
            9. all books
            """;
    System.out.println(filterOptions);
    int input;
    while(true) {
      System.out.print("Pick option: ");
      try {
        input = Integer.parseInt(userInputScanner.nextLine());
        break;
      } catch(NumberFormatException e) {
        System.out.print("Invalid input. ");
      }
    }
    switch(input) {
      case 1 -> {
        String title = getStringInput("Title: ");
        System.out.println("\nMatching books:\n");
        bookStream
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .forEach(System.out::println);
      }
      case 2 -> {
        String author = getStringInput("Author: ");
        System.out.println("\nMatching books:\n");
        bookStream
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .forEach(System.out::println);
      }
      case 3 -> {
        String title = getStringInput("Title: ");
        String author = getStringInput("Author: ");
        System.out.println("\nMatching books:\n");
        bookStream
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .forEach(System.out::println);
      }
      case 4 -> {
        String house = getStringInput("Publishing house: ");
        System.out.println("\nMatching books:\n");
        bookStream
                .filter(book -> book.getPublishingHouse().equalsIgnoreCase(house))
                .forEach(System.out::println);
      }
      case 5 -> {
        String title = getStringInput("Title: ");
        String author = getStringInput("Author: ");
        String house = getStringInput("Publishing house: ");
        System.out.println("\nMatching books:\n");
        bookStream
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .filter(book -> book.getPublishingHouse().equalsIgnoreCase(house))
                .forEach(System.out::println);
      }
      case 6 -> {
        Integer year = getIntegerInput("Publishing year: ");
        System.out.println("\nMatching books:\n");
        if(year != null){
          bookStream
                  .filter(book -> book.getYear().equals(year))
                  .forEach(System.out::println);
        }else{
         bookStream
                 .filter(book -> book.getYear() == null)
                 .forEach(System.out::println);
        }
      }
      case 7 ->{
        System.out.println("\nMatching books:\n");
        bookStream
              .filter(book -> book.getBorrowedBy() != null)
              .forEach(System.out::println);
      }
      case 8 ->{
        System.out.println("\nMatching books:\n");
        bookStream
              .filter(book -> book.getBorrowedBy() == null)
              .forEach(System.out::println);
      }
      case 9 ->{
        System.out.println("\nMatching books:\n");
        bookStream
              .forEach(System.out::println);
      }
      default -> System.out.println("\nInvalid input");
    }
  }

  public void changeLibraryName() {
    System.out.println("\nChange Library name (current name: '" + library.getName() + "')\n");
    String newName = getStringInput("New library name: ");
    library.setName(newName);
    System.out.println("\nLibrary name changed");
  }

  public void closeApp() {
    String options = """
            1. Yes
            0. No
            """;
    System.out.println("\n" + options);
    System.out.print("Save the entered data: ");
    int validOutput = 0;
    try {
      validOutput = Integer.parseInt(userInputScanner.nextLine());
    } catch(NumberFormatException e) {
      System.out.println("\nInvalid input.");
      closeApp();
    }
    switch(validOutput){
      case 1 -> {
        LibraryDataManager.saveLibraryName(library.getName());
        LibraryDataManager.saveReaders(library.getReaders());
        LibraryDataManager.saveBooks(library.getBooks());
      }
      case 0 -> {
      }
      default -> {
        System.out.println("\nInvalid input.");
        closeApp();
      }
    }
  }
}
