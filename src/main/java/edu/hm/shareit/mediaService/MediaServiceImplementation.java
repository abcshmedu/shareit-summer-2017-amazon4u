package edu.hm.shareit.mediaService;

import edu.hm.shareit.media.Book;
import edu.hm.shareit.media.Disc;
import edu.hm.shareit.media.Medium;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * The implementation of the MediaService interface.
 */
public class MediaServiceImplementation implements MediaService {
    private final Collection<Book> books = new HashSet<>();
    private final Collection<Disc> discs = new HashSet<>();

    private static final SessionFactory session = new Configuration().configure().buildSessionFactory();

    private Session getSession() {
        return this.session.getCurrentSession();
    }

    private void insert(Object toBeInserted) {
        try (Session entityManager = getSession()) {
            final Transaction transaction = entityManager.beginTransaction();
            System.out.println(toBeInserted.toString());
            entityManager.save(toBeInserted);
            //entityManager.persist(toBeInserted);
            transaction.commit();
            entityManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(Object toBeUpdated) {
        try (final Session entityManager = getSession()) {
            Transaction transaction = entityManager.beginTransaction();
            entityManager.merge(toBeUpdated);
            transaction.commit();
        }
    }

    private boolean contains(Object obj) {
        final boolean contains;
        final Session entityManager = getSession();
        final Transaction transaction = entityManager.beginTransaction();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        Class<?> clazz;

        if(obj instanceof Book) clazz = Book.class;
        else if(obj instanceof Disc) clazz = Disc.class;
        else throw new IllegalArgumentException();

        CriteriaQuery<?> query = builder.createQuery(clazz);
        Root<Person> root = query.from(Person.class);
        query.where(builder.equal(root.get("firstName"), "Neville"));
        Query<Person> q = entityManager.createQuery(query);
        List<Person> persons = q.getResultList();
    }

    @Override
    public MediaServiceResult addBook(Book book) {

        if (book == null) {
            return MediaServiceResult.FORBIDDEN;
        }
        Book b = new Book(book);
        if (b.getAuthor().equals("") || b.getTitle().equals("")) {
            System.out.println("MediaServiceResult >>> addBook() -> author or title missing");
            return MediaServiceResult.MISSING_ARG;
        }

        if (!isValidISBN(b.getIsbn())) {
            System.out.println("MediaServiceResult >>> addBook() -> Illegal ISBN");
            return MediaServiceResult.ILLEGAL_ISBN;
        }

        if (contains(b)) {
            System.out.println("MediaServiceResult >>> addBook() -> Duplicate found");
            return MediaServiceResult.ALREADY_EXISTS;
        }

        //getBooksCollection().add(book);
        insert(b);
        System.out.println("MediaServiceResult >>> addBook() -> book has been added");
        System.out.println("MediaServiceResult >>> addBook() -> current size "
                + getBooksCollection().size());

        return MediaServiceResult.OK;
    }


    @Override
    public MediaServiceResult addDisc(Disc disc) {
        if (disc == null) {
            return MediaServiceResult.FORBIDDEN;
        }
        Disc d = new Disc(disc);
        if (d.getDirector().equals("") || d.getTitle().equals("")) {
            System.out.println("MediaServiceResult >>> addDisc() -> director or title missing");
            return MediaServiceResult.MISSING_ARG;
        }

        if (!isValidBarcode(d.getBarcode())) {
            System.out.println("MediaServiceResult >>> addDisc() -> Illegal Barcode");
            return MediaServiceResult.ILLEGAL_BARCODE;
        }

        if (contains(d)) {
            return MediaServiceResult.ALREADY_EXISTS;
        }
        insert(d);
        //getDiscsCollection().add(disc);
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
        Session entityManager = getSession();
        entityManager.beginTransaction();
        List<Book> bookQuery;

        String queryString = "FROM Book";

        Query<Book> query = entityManager.createQuery(queryString);
        bookQuery = query.getResultList();

        return bookQuery.toArray(new Book[1]);
    }

    @Override
    public Medium[] getDiscs() {
        List<Disc> discQuery;
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Disc> criteriaQuery = criteriaBuilder.createQuery(Disc.class);

        Query<Disc> query = getSession().createQuery(criteriaQuery);
        discQuery = query.getResultList();

        return discQuery.toArray(new Disc[0]);
    }

    @Override
    public Medium getBook(String isbn) {
        final Book[] books = (Book[]) getBooks();
        Book result = null;
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                result = book;
                break;
            }
        }
        return result;
    }

    @Override
    public Medium getDisc(String barcode) {
        final Disc[] discs = (Disc[]) getDiscs();
        Disc result = null;
        for (Disc disc : discs) {
            if (disc.getBarcode().equals(barcode)) {
                result = disc;
                break;
            }
        }
        return result;
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
