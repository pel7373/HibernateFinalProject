package com.javarush.dao;

import com.javarush.config.SessionFactoryProvider;
import com.javarush.entity.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class CityDAO implements CrudDAO<City, Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            CityDAO.class);
    private final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactoryRelationalDb();
    private static final CityDAO INSTANCE = new CityDAO();

    private CityDAO() {
    }

    public void shutdown() {
        if (nonNull(sessionFactory)) {
            sessionFactory.close();
            LOGGER.info("shutdown: sessionFactory closed");
        }
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
        LOGGER.info("findAll works successfully");
        return allCities;
    }

    @Override
    public Optional<City> findById(Integer id)  {
        Session session = sessionFactory.openSession();
        Query<City> query = session.createQuery("from City c join fetch c.country where c.id = :ID");
        query.setParameter("ID", id);
        Optional<City> city = query.uniqueResultOptional();
        session.close();
        LOGGER.info(String.format("findById works successfully. id: %d", id));
        return city;
    }

    public List<City> findByName(String name)  {
        Session session = sessionFactory.openSession();
        Query<City> query = session.createQuery("from City c join fetch c.country where c.name = :NAME");
        query.setParameter("NAME", name);
        List<City> list = query.getResultList();
        session.close();
        LOGGER.info(String.format("findByName works successfully. name: %s", name));
        return list;
    }


    @Override
    public void save(City city) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(city);
        transaction.commit();
        session.close();
        LOGGER.info(String.format("save works successfully. id: %d", city.getId()));
    }

    @Override
    public void update(City city) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(city);
        transaction.commit();
        session.close();
        LOGGER.info(String.format("update works successfully. id: %d", city.getId()));
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
        LOGGER.info(String.format("deleteById works successfully. id: %d", id));
    }

    public static CityDAO getInstance() {
        return INSTANCE;
    }
}