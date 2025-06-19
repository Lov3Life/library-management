public class Main {
  public static void main(String[] args) {
    Library library = new Library("Library");
    library.addBock("1984", "George Orwell", 1949);
    library.addBock("Animal Farm", "George Orwell", 1945);
    library.registerUser("Jan", "Kowalski", 1988, 12, 12);
    System.out.println(library.listAvailableBooks());
    System.out.println(library.borrowBook(0, 0));
    System.out.println(library.listAvailableBooks());
    System.out.println(library.borrowBook(0, 0));
    System.out.println(library.returnBook(0, 0));
    System.out.println(library.listAvailableBooks());
    System.out.println(library.returnBook(0, 0));
  }
}
