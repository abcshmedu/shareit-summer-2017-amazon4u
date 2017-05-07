package edu.hm.shareit.test;

import edu.hm.shareit.media.Book;
import edu.hm.shareit.media.Disc;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest {

    @Test
    public void equalsTest() throws Exception {
        Book sut1 = new Book("title","author","978-3551551672");
        Book sut2 = new Book("title2","author2","978-3551551672");
        assertEquals(sut1,sut2);
    }


    @Test
    public void equalsTestNull() throws Exception {
        Book sut = new Book("title","author","978-3551551672");
        assertFalse(sut.equals(null));
    }

    @Test
    public void equalsTestDisc() throws Exception {
        Book sut = new Book("title","author","978-3551551672");
        Disc disc = new Disc("title","director","1231231231231",0);
        assertFalse(sut.equals(disc));
    }
}