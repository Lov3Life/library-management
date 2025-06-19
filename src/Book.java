public class Book {
  private static long LAST_ID;
  private final long id;
  private String title;
  private String author;
  private String publishingHouse;
  private int year;
  private boolean isAvailable;

  static {
    LAST_ID = 1;
  }

  public Book(String title, String author, String publishingHouse, int year) {
    this.id = LAST_ID++;
    this.title = title;
    this.author = author;
    this.publishingHouse = publishingHouse;
    this.year = year;
    this.isAvailable = true;
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

  public int getYear() {
    return year;
  }

  public boolean isAvailable() {
    return isAvailable;
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

  public void setYear(int year) {
    this.year = year;
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
  }

  @Override
  public String toString() {
    return "Book{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", year=" + year +
            '}';
  }
}
