package edu.hm.shareit.mediaService;

import edu.hm.shareit.media.Book;
import edu.hm.shareit.media.Disc;
import edu.hm.shareit.media.Medium;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * The implementation of the MediaService interface.
 */
public class MediaServiceImplementation implements MediaService {
    private final Collection<Book> books = new HashSet<>();
    private final Collection<Disc> discs = new HashSet<>();

    @Override
    public MediaServiceResult addBook(Book b) {
        Book book = new Book(b);
        if (book == null) {
            return MediaServiceResult.FORBIDDEN;
        }

        if (book.getAuthor().equals("") || book.getTitle().equals("")) {
            System.out.println("MediaServiceResult >>> addBook() -> author or title missing");
            return MediaServiceResult.MISSING_ARG;
        }

        if (!isValidISBN(book.getIsbn())) {
            System.out.println("MediaServiceResult >>> addBook() -> Illegal ISBN");
            return MediaServiceResult.ILLEGAL_ISBN;
        }

        if (getBooksCollection().contains(book)) {
            System.out.println("MediaServiceResult >>> addBook() -> Duplicate found");
            return MediaServiceResult.ALREADY_EXISTS;
        }

        getBooksCollection().add(book);
        System.out.println("MediaServiceResult >>> addBook() -> book has been added");
        System.out.println("MediaServiceResult >>> addBook() -> current size "
                + getBooksCollection().size());

        return MediaServiceResult.OK;
    }

    @Override
    public MediaServiceResult addDisc(Disc d) {
        Disc disc = new Disc(d);
        if (disc == null) {
            return MediaServiceResult.FORBIDDEN;
        }

        if (disc.getDirector().equals("") || disc.getTitle().equals("")) {
            System.out.println("MediaServiceResult >>> addDisc() -> director or title missing");
            return MediaServiceResult.MISSING_ARG;
        }

        if (!isValidBarcode(disc.getBarcode())) {
            System.out.println("MediaServiceResult >>> addDisc() -> Illegal Barcode");
            return MediaServiceResult.ILLEGAL_BARCODE;
        }

        if (getDiscsCollection().contains(disc)) {
            return MediaServiceResult.ALREADY_EXISTS;
        }

        getDiscsCollection().add(disc);
        return MediaServiceResult.OK;
    }

    @Override
    public MediaServiceResult updateBook(Book b) {
        Book book = new Book(b);
        if (book == null) {
            return MediaServiceResult.FORBIDDEN;
        }
        Book toBeUpdated = (Book) getBook(book.getIsbn());

        if (toBeUpdated == null) {
            return MediaServiceResult.UNMATCHING_ISBN;
        }
        toBeUpdated.setAuthor(book.getAuthor());
        toBeUpdated.setTitle(book.getTitle());
        return MediaServiceResult.OK;
    }

    @Override
    public MediaServiceResult updateDisc(Disc d) {
        Disc disc = new Disc(d);
        if (disc == null) {
            return MediaServiceResult.FORBIDDEN;
        }
        Disc toBeUpdated = (Disc) getDisc(disc.getBarcode());
        if (toBeUpdated == null) {
            return MediaServiceResult.UNMATCHING_BARCODE;
        }
        toBeUpdated.setTitle(disc.getTitle());
        toBeUpdated.setDirector(disc.getDirector());
        toBeUpdated.setFsk(disc.getFsk());

        return MediaServiceResult.OK;
    }

    @Override
    public Medium[] getBooks() {
        return getBooksCollection().toArray(new Medium[0]);
    }

    @Override
    public Medium[] getDiscs() {
        return getDiscsCollection().toArray(new Medium[0]);
    }

    @Override
    public Medium getBook(String isbn) {
        Supplier<Optional<Book>> supplier = () -> getBooksCollection()
                .parallelStream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst();

        if (supplier.get().isPresent()) {
            return supplier.get().get();
        }
        return null;
    }

    @Override
    public Medium getDisc(String barcode) {
        Supplier<Optional<Disc>> supplier = () -> getDiscsCollection()
                .parallelStream()
                .filter(disc -> disc.getBarcode().equals(barcode))
                .findFirst();
        if (supplier.get().isPresent()) {
            return supplier.get().get();
        }
        return null;
    }

    /**
     * Getter for the collection version of books.
     *
     * @return The books.
     */
    protected Collection<Book> getBooksCollection() {
        return books;
    }

    /**
     * Getter for the collection version of discs.
     *
     * @return The discs
     */
    protected Collection<Disc> getDiscsCollection() {
        return discs;
    }

    /**
     * Checks if an isbn is written correctly.
     *
     * @param isbn The isbn code
     * @return true when correct else false
     */
    private boolean isValidISBN(String isbn) {
        if (isbn == null) {
            return false;
        }



        final int isbnLength = 13;
        if (isbn.length() != isbnLength) {
            return false;
        }

        int sum = 0;
        for (int index = 0; index < isbnLength - 1; index++) {
            int digit = Integer.parseInt(isbn.substring(index, index + 1));
            final int three = 3;
            sum += (index % 2 == 0) ? digit : digit * three;
        }

        final int ten = 10;
        int checksum = ten - (sum % ten);
        if (checksum == ten) {
            checksum = 0;
        }

        return checksum == Integer.parseInt(isbn.substring(isbnLength - 1));
    }

    /**
     * Checks if an barcode is written correctly.
     *
     * @param barcode The barcode
     * @return true when correct else false
     */
    private boolean isValidBarcode(String barcode) {
        final int barcodeLength = 13;
        return barcode.length() == barcodeLength && barcode.matches("[0-9]+");
    }
}
