package model;

public class Book {
  private static long LAST_ID;
  private final long id;
  private String title;
  private String author;
  private String publishingHouse;
  private Integer year;
  private Reader borrowedBy;

  static {
    LAST_ID = 1;
  }

  public Book(String title, String author, String publishingHouse, Integer year) {
    this.id = LAST_ID++;
    this.title = title;
    this.author = author;
    this.publishingHouse = publishingHouse;
    this.year = year;
    this.borrowedBy = null;
  }

  public Book(long id, String title, String author, String publishingHouse, Integer year){
    this.id = id;
    this.title = title;
    this.author = author;
    this.publishingHouse = publishingHouse;
    this.year = year;
    this.borrowedBy = null;
  }

  public long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public String getPublishingHouse() {
    return publishingHouse;
  }

  public Integer getYear() {
    return year;
  }

  public Reader getBorrowedBy() {
    return borrowedBy;
  }

  public static void setLastId(long lastId) {
    LAST_ID = lastId;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setPublishingHouse(String publishingHouse) {
    this.publishingHouse = publishingHouse;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public void setBorrowedBy(Reader borrowedBy) {
    this.borrowedBy = borrowedBy;
  }

  @Override
  public String toString() {
    return "Book{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", publishingHouse='" + publishingHouse + '\'' +
            ", year=" + year +
            ", borrowed by=" + (borrowedBy == null ? "anybody (available)" : "Reader ID:" + borrowedBy.getId()) +
            '}';
  }
}
