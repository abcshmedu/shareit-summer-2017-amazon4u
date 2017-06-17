package edu.hm.shareit.media;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * A class for books.
 */
@Entity
//@Table(name="TBook")
public class Book extends Medium {

    @Column(name ="Author")
    private String author;

    @Id
    private final String isbn;



    /**
     * Default constructor for Jackson.
     */
    public Book() {
        this("empty", "empty", "empty");
    }

    /**
     * Creates a representation of a book.
     * Not to be mistaken as an book exemplar.
     *
     * @param title  Title of the book
     * @param author Author(s) of the book
     * @param isbn   ISBN of the book
     */
    public Book(String title, String author, String isbn) {
        super(title);
        if (author == null || isbn == null) {
            throw new NullPointerException();
        }
        this.author = author;
        this.isbn = isbn.replaceAll("-","");
    }

    /**
     * Copy ctor
     * @param book
     */
    public Book(Book book) {
        super(book.getTitle());
        if (book.getAuthor() == null || book.getIsbn() == null) {
            throw new NullPointerException();
        }

        this.author = book.getAuthor();
        this.isbn = book.getIsbn().replaceAll("-","");

    }

    /**
     * Setter for author.
     *
     * @param author The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Getter for author.
     *
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Getter for ISBN.
     *
     * @return isbn
     */
    public String getIsbn() {
        return isbn;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Book book = (Book) o;

        return this.getIsbn().equals(book.getIsbn());
    }

    @Override
    public int hashCode() {
        return getIsbn().hashCode();
    }

    @Override
    public String toString() {
        return "Book{"
                + "author='" + author + '\''
                + ", isbn='" + isbn + '\''
                + '}';
    }
}
