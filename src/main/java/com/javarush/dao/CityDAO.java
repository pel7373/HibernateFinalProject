package com.javarush.dao;

import com.javarush.config.SessionFactoryProvider;
import com.javarush.domain.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CityDAO implements CrudDAO<City, Integer> {
    private final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactoryRelationalDb();
    private static final CityDAO INSTANCE = new CityDAO();

    private CityDAO() {
    }

    @Override
    public List<City> findAll() {
        List<City> allCities = new ArrayList<>();
        int step = 500;

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query<Long> query = session.createQuery("select count(c) from City c", Long.class);
        int totalCount = Math.toIntExact(query.uniqueResult());
        for (int offset = 0; offset < totalCount; offset += step) {
            Query<City> query2 = session.createQuery("from City", City.class);
            query2.setFirstResult(offset);
            query2.setMaxResults(step);
            allCities.addAll(query2.list());
        }
        session.getTransaction().commit();
        session.close();
        return allCities;
    }

    @Override
    public Optional<City> findById(Integer id)  {
        Session session = sessionFactory.openSession();
        Query<City> query = session.createQuery("from City c join fetch c.country where c.id = :ID");
        query.setParameter("ID", id);
        Optional<City> city = query.uniqueResultOptional();
        session.close();
        return city;
    }

    public List<City> findByName(String name)  {
        Session session = sessionFactory.openSession();
        Query<City> query = session.createQuery("from City c join fetch c.country where c.name = :NAME");
        query.setParameter("NAME", name);
        List<City> list = query.getResultList();
        session.close();
        return list;
    }


    @Override
    public void save(City city) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(city);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(City city) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(city);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteById(Integer id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("delete City c where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        transaction.commit();
        session.close();
    }

    public static CityDAO getInstance() {
        return INSTANCE;
    }
}