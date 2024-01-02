package pl.gienius.pisarz;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Book implements Serializable {

    private Long id;
    private String title;
    private Writer writer;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private String description;

    public Book() {
    }

    public Book(String title, Writer writer, LocalDate releaseDate, String description) {
        this.title = title;
        this.writer = writer;
        //this.author = author;
        this.releaseDate = releaseDate;
        this.description = description;
    }

    public Book(Long id, String title, Writer writer, LocalDate releaseDate, String description) {
        this.title = title;
        this.writer = writer;
        //this.author = author;
        this.releaseDate = releaseDate;
        this.description = description;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", writer=" + writer +
                ", releaseDate=" + releaseDate +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(getId(), book.getId()) && Objects.equals(getTitle(), book.getTitle()) && Objects.equals(writer, book.writer) && Objects.equals(getReleaseDate(), book.getReleaseDate()) && Objects.equals(getDescription(), book.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), writer, getReleaseDate(), getDescription());
    }
}
