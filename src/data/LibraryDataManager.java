package data;

import model.Book;
import model.Library;
import model.Reader;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryDataManager {

  private static final String LIBRARY_NAME_FILE = "config/libraryName.txt";
  private static final String READERS_FILE = "config/readers.csv";
  private static final String BOOKS_FILE = "config/books.csv";

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void saveLibraryName(String name){
    File file = new File(LIBRARY_NAME_FILE);
    file.getParentFile().mkdir();
    try(BufferedWriter writer = new BufferedWriter(new FileWriter(LIBRARY_NAME_FILE))){
      writer.write(name);
    } catch(IOException e) {
      System.out.println("error while writing library name to file");
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void saveReaders(List<Reader> readers){
    File file = new File(READERS_FILE);
    file.getParentFile().mkdir();
    try(BufferedWriter writer = new BufferedWriter(new FileWriter(READERS_FILE))) {
      for(Reader reader : readers){
        StringBuilder stringWriter = new StringBuilder(reader.getId() + ";"
                + reader.getFirstName() + ";"
                + reader.getLastName() + ";"
                + reader.getBirthDate().getYear() + ";"
                + reader.getBirthDate().getMonthValue() + ";"
                + reader.getBirthDate().getDayOfMonth());
        String prefix = ";";
        for(Book book : reader.getBorrowedBooks()){
          stringWriter.append(prefix).append(book.getId());
          prefix = ",";
        }
        writer.write(stringWriter.toString());
        writer.newLine();
      }
    } catch(IOException e) {
      System.out.println("error while writing readers to file");
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void saveBooks(List<Book> books){
    File file = new File(BOOKS_FILE);
    file.getParentFile().mkdir();
    try(BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
      for(Book book : books){
        String stringWriter = book.getId() + ";"
                + book.getTitle() + ";"
                + book.getAuthor() + ";"
                + book.getPublishingHouse() + ";"
                + book.getYear() + ";"
                + (book.getBorrowedBy() != null ? book.getBorrowedBy().getId() : null);
        writer.write(stringWriter);
        writer.newLine();
      }
    } catch(IOException e) {
      System.out.println("error while writing books to file");
    }
  }

  public static String loadLibraryName(){
    try(BufferedReader bufferedReader = new BufferedReader(new FileReader(LIBRARY_NAME_FILE))){
      return bufferedReader.readLine();
    }catch(IOException e) {
      return "Default library";
    }
  }

  private static Map<Long, Book> loadMapBooks(Library library){
    try(BufferedReader bufferedReader = new BufferedReader(new FileReader(BOOKS_FILE))) {
      String line;
      long maxID = 0;
      Map<Long, Book> books = new HashMap<>();
      while((line = bufferedReader.readLine()) != null){
        String[] bookData = line.split(";");
        books.put(Long.parseLong(bookData[0]), new Book(Long.parseLong(bookData[0]),
                bookData[1],
                bookData[2],
                bookData[3],
                (bookData[4] == null ? null : Integer.valueOf(bookData[4]))));
        library.addBock(books.get(Long.parseLong(bookData[0])));
        maxID = Math.max(maxID, Long.parseLong(bookData[0]));
      }
      Book.setLastId(maxID + 1);
      return books;
    } catch(IOException e) {
      System.out.println("Failed load books");
      return new HashMap<>();
    }
  }

  private static void loadReaders(Library library){
    Map<Long, Book> books = loadMapBooks(library);
    try(BufferedReader bufferedReader = new BufferedReader(new FileReader(READERS_FILE))) {
      String line;
      long maxID = 0;
      while((line = bufferedReader.readLine()) != null){
        String[] readerData = line.split(";");
        Reader reader = new Reader(Long.parseLong(readerData[0]),
                readerData[1],
                readerData[2],
                LocalDate.of(Integer.parseInt(readerData[3]),
                        Integer.parseInt(readerData[4]),
                        Integer.parseInt(readerData[5])));
        if(readerData.length == 7){
          for(String bookID : readerData[6].split(",")){
            Book book = books.get(Long.parseLong(bookID));
            book.setBorrowedBy(reader);
            reader.getBorrowedBooks().add(book);
          }
        }
        maxID = Math.max(maxID, Long.parseLong(readerData[0]));
        library.registerUser(reader);
      }
      Reader.setLastId(maxID + 1);
    } catch(IOException e) {
      System.out.println("Failed load Readers");
    }
  }

  public static void loadReadersAndBooksData(Library library){
    loadReaders(library);
  }
}
