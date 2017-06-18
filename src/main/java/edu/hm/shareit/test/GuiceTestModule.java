package edu.hm.shareit.test;

import com.google.inject.AbstractModule;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by jupiter on 6/18/17.
 */
public class GuiceTestModule extends AbstractModule {
    protected void configure() {
        bind(SessionFactory.class).toInstance(new Configuration().configure().buildSessionFactory());
        // asdf
    }

}
