import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

public class LibraryApp {
  private final Library library;
  private final Scanner userInputScanner;

  public LibraryApp(Library library) {
    this.library = library;
    this.userInputScanner = new Scanner(System.in);
  }

  public void startApp() {
    System.out.printf("\n\n\nWelcome in Library ('%s') management System\n\n\n", library.getName());
    boolean globalFlag = true;
    String options = """
            1. Register new Reader
            2. Change existing Reader data
            3. Add new book
            4. Change existing book data
            5. Borrow Book
            6. Return Book
            7. Print Users (optional options)
            8. Print Books
            0. Close App
            
            Select option:\s""";
    while(globalFlag) {
      System.out.println("Available options:");
      System.out.print(options);
      String output = userInputScanner.nextLine();
      int validOutput;
      try {
        validOutput = Integer.parseInt(output);
      } catch(NumberFormatException e) {
        System.out.println("Invalid input. Try again");
        continue;
      }
      switch(validOutput) {
        case 0 -> {
          closeApp();
          globalFlag = false;
        }
        case 1 -> addReaderToLibrary();
        case 2 -> changeReaderData();
        case 7 -> {
          for(Reader reader : library.getReaders()){
            System.out.println(reader);
          }
        }
        default -> System.out.println("Invalid input. Try again");
      }
    }
  }

  public void addReaderToLibrary() {
    LocalDate now = LocalDate.now();
    String firstName, lastName;
    System.out.print("Name: ");
    firstName = userInputScanner.nextLine();
    System.out.print("Surname: ");
    lastName = userInputScanner.nextLine();
    LocalDate bthDate = validDate(now);
    Reader userPretender = new Reader(firstName, lastName, bthDate);
    changeReaderData(userPretender);
    library.registerUser(userPretender);
    System.out.println("User successfully added");
  }

  private void changeReaderData(Reader reader) {
    boolean wantChangeFlag = true;
    while(wantChangeFlag) {
      System.out.printf("Summary: %s %s (ID: %d), Birth date: %s\n", reader.getFirstName(), reader.getLastName(), reader.getId(), reader.getBirthDate());
      String changes = """
              Make any changes:
              1. Name
              2. Surname
              3. Birth date
              0. Exit
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
          System.out.print("Name: ");
          String name = userInputScanner.nextLine();
          reader.setFirstName(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
          System.out.println("User name changes");
        }
        case 2 -> {
          System.out.print("Surname: ");
          String surname = userInputScanner.nextLine();
          reader.setLastName(surname.substring(0, 1).toUpperCase() + surname.substring(1).toLowerCase());
          System.out.println("User surname changes");
        }
        case 3 -> {
          reader.setBirthDate(validDate(LocalDate.now()));
          System.out.println("User data changes");
        }
        case 0 -> wantChangeFlag = false;
        default -> System.out.println("Incorrect option selected");
      }
    }
  }

  private LocalDate validDate(LocalDate now) {
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
    System.out.print("Enter user ID to change user data: ");
    long userID;
    while(true){
      try {
        userID = Long.parseLong(userInputScanner.nextLine());
        break;
      } catch(NumberFormatException e) {
        System.out.print("Invalid ID, try again: ");
      }
    }
    long finalUserID = userID;
    var userToChange = library.getReaders().stream()
            .filter(reader -> reader.getId() == finalUserID)
            .findFirst()
            .orElse(null);
    if(userToChange != null) {
      changeReaderData(userToChange);
    } else {
      System.out.println("User not exists in the database");
    }
  }

  public void closeApp() {

  }
}
