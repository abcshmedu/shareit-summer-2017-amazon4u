package edu.hm.shareit.test;

import edu.hm.shareit.media.Book;
import edu.hm.shareit.media.Disc;
import edu.hm.shareit.media.Medium;
import edu.hm.shareit.mediaService.MediaService;
import edu.hm.shareit.mediaService.MediaServiceImplementation;
import edu.hm.shareit.mediaService.MediaServiceResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for MediaServiceImplementation
 */
public class MediaServiceImplementationTest {
    private static final Book BOOK = new Book("title", "author", "978-3551551672");
    private static final Disc DISC = new Disc("Never gonna give you up", "Rick Astley", "1111111111111", 0);


    @Before
    public void reset() {
        new MediaServiceImplementation().purge();
    }

    @Test
    public void addNewBook() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult wanted = MediaServiceResult.OK;
        MediaServiceResult have = sut.addBook(BOOK);
        assertEquals(wanted, have);
    }

    @Test
    public void addExistingBook() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult wanted = MediaServiceResult.ALREADY_EXISTS;
        sut.addBook(BOOK);
        MediaServiceResult have = sut.addBook(BOOK);
        assertEquals(wanted, have);
    }

    @Test
    public void addBookInvalidISBN() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult wanted = MediaServiceResult.ILLEGAL_ISBN;
        MediaServiceResult have = sut.addBook(new Book("title", "author", "345345003"));
        assertEquals(wanted, have);
    }

    @Test
    public void getBook() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        Book wanted = BOOK;
        sut.addBook(BOOK);
        Book have = (Book) sut.getBook(BOOK.getIsbn());
        assertEquals(wanted, have);
    }

    @Test
    public void getBookNotFound() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        Book wanted = null;
        Book have = (Book) sut.getBook(BOOK.getIsbn());
        assertEquals(wanted, have);
    }

    @Test
    public void updateBook() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        sut.addBook(BOOK);
        Book wanted = new Book("newTitle", "newAuthor", BOOK.getIsbn());
        sut.updateBook(wanted);
        Book have = (Book) sut.getBook(BOOK.getIsbn());
        assertEquals(wanted, have);
    }


    @Test
    public void updateBookResultOK() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        sut.addBook(BOOK);
        Book newBook = new Book("newTitle", "newAuthor", BOOK.getIsbn());
        sut.updateBook(newBook);
        MediaServiceResult wanted = MediaServiceResult.OK;
        MediaServiceResult have = sut.updateBook(newBook);
        assertEquals(wanted, have);
    }

    @Test
    public void getBooks() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        Book anotherBook = new Book("anotherTitle", "anotherAuthor", "978-3-551-55900-5");
        sut.addBook(BOOK);
        sut.addBook(anotherBook);
        Medium[] wanted = {anotherBook, BOOK}; // in this order, because it is sorted
        Medium[] have = sut.getBooks();
        assertArrayEquals(wanted, have);
    }

    @Test
    public void addNewDisc() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult wanted = MediaServiceResult.OK;
        MediaServiceResult have = sut.addDisc(DISC);
        assertEquals(wanted, have);
    }

    @Test
    public void addExistingDisc() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult wanted = MediaServiceResult.ALREADY_EXISTS;
        sut.addDisc(DISC);
        MediaServiceResult have = sut.addDisc(DISC);
        assertEquals(wanted, have);
    }

    @Test
    public void addDiscInvalidBarcode() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult wanted = MediaServiceResult.ILLEGAL_BARCODE;
        MediaServiceResult have = sut.addDisc(new Disc("title", "Astley", "111", 0));
        assertEquals(wanted, have);
    }

    @Test
    public void getDisc() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        Disc wanted = DISC;
        sut.addDisc(DISC);
        Disc have = (Disc) sut.getDisc(DISC.getBarcode());
        assertEquals(wanted, have);
    }

    @Test
    public void getDiscNotFound() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        Disc wanted = null;
        Disc have = (Disc) sut.getDisc(DISC.getBarcode());
        assertEquals(wanted, have);
    }

    @Test
    public void updateDisc() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        sut.addDisc(DISC);
        Disc wanted = new Disc("newTitle", "Astley", DISC.getBarcode(), 0);
        sut.updateDisc(wanted);
        Disc have = (Disc) sut.getDisc(DISC.getBarcode());
        assertEquals(wanted, have);
    }

    @Test
    public void updateDiscResultOK() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        sut.addDisc(DISC);
        Disc newDisc = new Disc("newTitle", "Astley", DISC.getBarcode(), 18);
        sut.updateDisc(newDisc);
        MediaServiceResult wanted = MediaServiceResult.OK;
        MediaServiceResult have = sut.updateDisc(newDisc);
        assertEquals(wanted, have);
    }

    @Test
    public void getDiscs() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        Disc anotherDisc = new Disc("anotherTitle", "Astley", "2222222222222", 0);
        sut.addDisc(DISC);
        sut.addDisc(anotherDisc);
        Medium[] wanted = {anotherDisc, DISC}; // in this order, because it is sorted
        Medium[] have = sut.getDiscs();
        assertArrayEquals(wanted, have);
    }


    @Test
    public void addNullBook() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult have = sut.addBook(null);
        MediaServiceResult want = MediaServiceResult.FORBIDDEN;
        assertEquals(want,have);
    }

    @Test
    public void addNullDisc() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult have = sut.addDisc(null);
        MediaServiceResult want = MediaServiceResult.FORBIDDEN;
        assertEquals(want,have);
    }


    @Test
    public void addBookWithNoAuthor () throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult have = sut.addBook(new Book("asdf", "", BOOK.getIsbn()));
        MediaServiceResult want = MediaServiceResult.MISSING_ARG;
        assertEquals(want,have);
    }

    @Test
    public void addBookWithNoTitle () throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult have = sut.addBook(new Book("", "asdf", BOOK.getIsbn()));
        MediaServiceResult want = MediaServiceResult.MISSING_ARG;
        assertEquals(want,have);
    }

    @Test
    public void addDiscWithNoDirecotr() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult have = sut.addDisc(new Disc("asdf", "", DISC.getBarcode(), 0));
        MediaServiceResult want = MediaServiceResult.MISSING_ARG;
        assertEquals(want,have);
    }

    @Test
    public void addDiscWithNoTitle() throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult have = sut.addDisc(new Disc("", "asdf", DISC.getBarcode(), 0));
        MediaServiceResult want = MediaServiceResult.MISSING_ARG;
        assertEquals(want,have);
    }

    @Test
    public void addBookWithIllegalIsbn () throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult have = sut.addBook(new Book("fss", "asdf", "asdfasd"));
        MediaServiceResult want = MediaServiceResult.ILLEGAL_ISBN;
        assertEquals(want,have);
    }

    @Test
    public void addDiscWithIllegalBarcode () throws Exception {
        MediaService sut = new MediaServiceImplementation();
        MediaServiceResult have = sut.addDisc(new Disc("fss", "asdf", "asdfasd", 0));
        MediaServiceResult want = MediaServiceResult.ILLEGAL_BARCODE;
        assertEquals(want,have);
    }

}