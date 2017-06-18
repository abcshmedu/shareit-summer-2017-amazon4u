package edu.hm.shareit.test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import edu.hm.shareit.media.Book;
import edu.hm.shareit.media.Disc;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by jupiter on 6/18/17.
 */
public class BasicTest {



    private static final Injector injector = Guice.createInjector(new GuiceTestModule());
    @Inject
    private SessionFactory sessionFactory;
    private Session entityManager;
    private Transaction tx;

    // dirty trick to store lecture's id for demonstration purposes
    private static long id;

    public BasicTest() {
        injector.injectMembers(this);
    }

    /**
     * Database is initialized with some data up-front.
     */
    @BeforeClass
    public static void initialize() {
        Session entityManager = injector.getInstance(SessionFactory.class).getCurrentSession();
        Transaction tx = entityManager.beginTransaction();


        Book BOOK = new Book("harry potter","j.k.Rowling","9783551551672");
        entityManager.persist(BOOK);

        Book ANOTHER_BOOK = new Book("hermine granger","j.k.Rowling","9783551551689");
        entityManager.persist(ANOTHER_BOOK);

        Disc DISC = new Disc("title","director","1111111111111",0);
        entityManager.persist(DISC);

        Disc ANOTHER_DISC = new Disc("anotherTitle", "anotherAuthor", "2222222222222", 6);
        entityManager.persist(ANOTHER_DISC);

        tx.commit();
    }

    /**
     * Shut down database after all tests have run.
     */
    @AfterClass
    public static void shutDown() {
        injector.getInstance(SessionFactory.class).close();
    }

    /**
     * Initializes a entityManager before each test.
     */
    @Before
    public void setUp() {
        entityManager = sessionFactory.getCurrentSession();
        tx = entityManager.beginTransaction();
    }

    /**
     * Close entityManager after a test.
     */
    @After
    public void tearDown() {
        // some tests might close entityManager for demo purposes
        if (entityManager.isOpen()) {
            tx.commit();
        }
    }

    /**
     * Demonstrates finding of a Person entity given the FirstName.
     */
    @Test
    public void testFind() {
        String title = "harry potter";
        //property does not need to be private but is case sensitive!
        String queryString = "from Book p where p.title ='" + title  + "'";
        List<Book> list = entityManager.createQuery(queryString).list();

        assertEquals(1, list.size()); // asdf
    }

    /**
     * Demonstrates another feature of how to find entities.
     */
    @Test
    public void testFindLike() {
        String queryString = "from Book where author like '%Rowling%'";
        org.hibernate.query.Query query = entityManager.createQuery(queryString);
        List<Book> list = query.list();
        assertEquals(1, list.size());
    }

    @Test
    public void testLoadDisc() {
        Disc disc = (Disc) entityManager.get(Disc.class, id);
        assertNotNull(disc);
    }


/*

    @Test(expected=org.hibernate.LazyInitializationException.class)
    public void testLazyLoading() {
        Lecture lecture = (Lecture) entityManager.get(Lecture.class, lectureId);
        assertNotNull(lecture);
        entityManager.close();
        List<Teacher> teachers = lecture.getTeachers();
        assertEquals(2, teachers.size());
    }

    @Test
    public void testAllPersonsWithCriteria() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = criteriaBuilder.createQuery(Person.class);
        CriteriaQuery<Person> root = query.select(query.from(Person.class));

        Query<Person> q = entityManager.createQuery(query);
        List<Person> persons = q.getResultList();
        assertEquals(3, persons.size());
    }

    @Test
    public void testPersonForNameCriteria() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = builder.createQuery(Person.class);
        Root<Person> root = query.from(Person.class);

        query.where(builder.equal(root.get("firstName"), "Neville"));

        Query<Person> q = entityManager.createQuery(query);
        List<Person> persons = q.getResultList();
        assertEquals(1, persons.size());
    }
*/



}
