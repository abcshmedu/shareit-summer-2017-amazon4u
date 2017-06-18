package edu.hm.shareit.test;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.shareit.media.Book;
import edu.hm.shareit.media.Disc;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import static org.junit.Assert.*;


public class MediaResourceTest {

    static final Book BOOK = new Book("title","author","9783551551672");
    static final Book ANOTHER_BOOK = new Book("first","author2","9783551551689");
    static final Disc DISC = new Disc("title","director","1111111111111",0);
    static final Disc ANOTHER_DISC = new Disc("anotherTitle", "anotherAuthor", "2222222222222", 6);
    static final String APP_URL = "/";
    static final int PORT = 8082;
    static final String WEBAPP_DIR = "./src/main/webapp/";
    Server jetty;
    static final JSONObject BOOK_JSON = new JSONObject();
    static final Client CLIENT = ClientBuilder.newClient();
    static final WebTarget BOOK_TARGET = CLIENT.target("http://localhost:8082").path("shareit/media/books");
    static final WebTarget DISC_TARGET = CLIENT.target("http://localhost:8082").path("shareit/media/discs");

    @Before
    public void setUp() throws Exception {
        BOOK_JSON.put("title", BOOK.getTitle());
        BOOK_JSON.put("author", BOOK.getAuthor());
        BOOK_JSON.put("isbn", BOOK.getIsbn());
        jetty = new Server(PORT);
        jetty.setHandler(new WebAppContext(WEBAPP_DIR, APP_URL));
        jetty.start();
        System.out.println("Jetty listening on port " + PORT);
        //jetty.join();
    }

    @After
    public void tearDown() throws Exception {
        jetty.stop();
        jetty.join();
        //jetty.destroy();
    }


    private void reset() throws Exception {
        WebTarget target = CLIENT.target("http://localhost:8082").path("shareit/media");
        target.request().header("Token","DebugToken").delete();
    }


    private String convertToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            System.out.println("Error");
            return "";
        }
    }


    @Test
    public void createBookStatus() throws Exception {
        reset();
        Response want = Response.ok().build();
        Response have = BOOK_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(BOOK,MediaType.APPLICATION_JSON_TYPE));
        assertEquals(want.getStatus(),have.getStatus());
    }

    @Test
    public void getBookCompareStatus() throws Exception {
        reset();
        BOOK_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(BOOK,MediaType.APPLICATION_JSON_TYPE));
        Response want = Response.ok().entity(BOOK).build();
        Response have = BOOK_TARGET.path("978-3551551672").request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").get();
        assertEquals(want.getStatus(),have.getStatus());
    }

    @Test
    public void getBookCompareJson() throws Exception {
        reset();
        BOOK_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(BOOK,MediaType.APPLICATION_JSON_TYPE));
        String want = convertToJson(BOOK);
        Response response = BOOK_TARGET.path(BOOK.getIsbn()).request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").get();
        String have = response.readEntity(String.class);
        assertEquals(want,have);
    }

    @Test
    public void getBooksCompareStatus() throws Exception {
        reset();
        BOOK_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(BOOK,MediaType.APPLICATION_JSON_TYPE));
        BOOK_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(ANOTHER_BOOK,MediaType.APPLICATION_JSON_TYPE));
        Response want = Response.ok().entity(BOOK).build();
        Response have = BOOK_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").get();
        assertEquals(want.getStatus(),have.getStatus());
    }

    @Test
    public void updateBookCompareStatus() throws Exception {
        reset();
        BOOK_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(BOOK,MediaType.APPLICATION_JSON_TYPE));
        BOOK_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(ANOTHER_BOOK,MediaType.APPLICATION_JSON_TYPE));
        Response want = Response.ok().entity(BOOK).build();
        Response have = BOOK_TARGET.path(BOOK.getIsbn()).request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken")
                .put(Entity.entity(new Book("Rick","Roll",BOOK.getIsbn()),MediaType.APPLICATION_JSON_TYPE));
        assertEquals(want.getStatus(),have.getStatus());
    }

    @Test
    public void updateBookCompareJson() throws Exception {
        reset();
        BOOK_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(BOOK,MediaType.APPLICATION_JSON_TYPE));
        BOOK_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(ANOTHER_BOOK,MediaType.APPLICATION_JSON_TYPE));
        Book toBeUpdated = new Book("Rick","Roll",BOOK.getIsbn());
        String want = convertToJson(new Book[]{toBeUpdated,ANOTHER_BOOK});
        BOOK_TARGET.path(BOOK.getIsbn()).request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken")
                .put(Entity.entity(toBeUpdated,MediaType.APPLICATION_JSON_TYPE));
        Response response = BOOK_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").get();
        String have = response.readEntity(String.class);
        assertEquals(want,have);
    }

    // ----------------------------------------------------------------------


    @Test
    public void createDiscStatus() throws Exception {
        reset();
        Response want = Response.ok().build();
        Response have = DISC_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(DISC,MediaType.APPLICATION_JSON_TYPE));
        assertEquals(want.getStatus(),have.getStatus());
    }

    @Test
    public void getDiscCompareStatus() throws Exception {
        reset();
        DISC_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(DISC,MediaType.APPLICATION_JSON_TYPE));
        Response want = Response.ok().entity(DISC).build();
        Response have = DISC_TARGET.path(DISC.getBarcode()).request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").get();
        assertEquals(want.getStatus(),have.getStatus());
    }

    @Test
    public void getDiscCompareJson() throws Exception {
        reset();
        DISC_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(DISC,MediaType.APPLICATION_JSON_TYPE));
        String want = convertToJson(DISC);
        Response response = DISC_TARGET.path(DISC.getBarcode()).request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").get();
        String have = response.readEntity(String.class);
        assertEquals(want,have);
    }

    @Test
    public void getDiscsCompareJson() throws Exception {
        reset();
        DISC_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(DISC,MediaType.APPLICATION_JSON_TYPE));
        DISC_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(ANOTHER_DISC,MediaType.APPLICATION_JSON_TYPE));
        String want = convertToJson(new Disc[]{ANOTHER_DISC,DISC});
        Response response = DISC_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").get();
        String have = response.readEntity(String.class);
        assertEquals(want,have);
    }

    @Test
    public void updateDiscCompareStatus() throws Exception {
        reset();
        DISC_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(DISC,MediaType.APPLICATION_JSON_TYPE));
        DISC_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(ANOTHER_DISC,MediaType.APPLICATION_JSON_TYPE));
        Response want = Response.ok().entity(DISC).build();
        Response have = DISC_TARGET.path(DISC.getBarcode()).request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken")
                .put(Entity.entity(new Disc("Rick","Roll",DISC.getBarcode(),0),MediaType.APPLICATION_JSON_TYPE));
        assertEquals(want.getStatus(),have.getStatus());
    }

    @Test
    public void updateDiscCompareJson() throws Exception {
        reset();
        DISC_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(DISC,MediaType.APPLICATION_JSON_TYPE));
        DISC_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").post(Entity.entity(ANOTHER_DISC,MediaType.APPLICATION_JSON_TYPE));
        Disc toBeUpdated = new Disc("Rick","Roll",DISC.getBarcode(), 0);
        String want = convertToJson(new Disc[]{toBeUpdated,ANOTHER_DISC});
        DISC_TARGET.path(DISC.getBarcode()).request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken")
                .put(Entity.entity(toBeUpdated,MediaType.APPLICATION_JSON_TYPE));
        Response response = DISC_TARGET.request(MediaType.APPLICATION_JSON_TYPE).header("Token","DebugToken").get();
        String have = response.readEntity(String.class);
        System.out.println(have);
        assertEquals(want,have);
    }
}