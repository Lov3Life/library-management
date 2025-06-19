import java.time.LocalDate;

public class Main {
  public static void main(String[] args) {
    Library library = new Library("Poland Library");
//    library.addBock("1984", "George Orwell", 1949);
//    library.addBock("Animal Farm", "George Orwell", 1945);
//    Reader reader1 = new Reader("Jan", "Kowalski", LocalDate.of(1988, 12, 12));
//    Reader reader2 = new Reader("Jan", "Kowalski", LocalDate.of(1988, 12, 12));
//    library.registerUser(reader1);
//    library.registerUser(reader2);
//    System.out.println(library.listAvailableBooks());
//    System.out.println(library.borrowBook(0, 0));
//    System.out.println(library.listAvailableBooks());
//    System.out.println(library.borrowBook(0, 0));
//    System.out.println(library.returnBook(0, 0));
//    System.out.println(library.listAvailableBooks());
//    System.out.println(library.returnBook(0, 0));
    LibraryApp libraryApp = new LibraryApp(library);
    libraryApp.startApp();
  }
}
