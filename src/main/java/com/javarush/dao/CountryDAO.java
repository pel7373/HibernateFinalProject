package com.javarush.dao;

import com.javarush.config.SessionFactoryProvider;
import com.javarush.domain.City;
import com.javarush.domain.Country;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CountryDAO implements CrudDAO<Country, Integer> {
    private final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactoryRelationalDb();
    private static final CountryDAO INSTANCE = new CountryDAO();

    private CountryDAO() {
    }

    @Override
    public List<Country> findAll() {
        Session session = sessionFactory.openSession();
        Query<Country> query = session.createQuery("select c from Country c join fetch c.languages", Country.class);
        List<Country> list = query.list();
        session.close();
        return list;
    }

    @Override
    public Optional<Country> findById(Integer id) {
        Session session = sessionFactory.openSession();
        Query<Country> query = session.createQuery("from Country c join fetch c.languages where c.id = :ID");
        query.setParameter("ID", id);
        Optional<Country> country = query.uniqueResultOptional();
        session.close();
        return country;
    }

    @Override
    public void save(Country country) {

    }

    @Override
    public void update(Country country) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    public static CountryDAO getInstance() {
        return INSTANCE;
    }
}