package com.javarush.dao;

import com.javarush.config.SessionFactoryProvider;
import com.javarush.domain.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class CityDAO implements CrudDAO<City, Integer> {
    private final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactoryRelationalDb();
    private static final CityDAO INSTANCE = new CityDAO();

    private CityDAO() {
    }

    @Override
    public List<City> findAll() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from City");
        session.close();
        return query.getResultList();
    }

    @Override
    public Optional<City> findById(Integer id) {
        Query<City> query = sessionFactory.openSession().createQuery("from City c join fetch c.country where c.id = :ID");
        query.setParameter("ID", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public void deleteById(Integer id) {

    }

    public Optional<City> findByName(String name) {
        Query<City> query = sessionFactory.openSession().createQuery("from City c where c.name = :NAME");
        query.setParameter("NAME", name);
        return Optional.ofNullable((City) query.getSingleResult());

    }

    public int getTotalCount() {
        Query<Long> query = sessionFactory.getCurrentSession().createQuery("select count(c) from City c", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

    public static CityDAO getInstance() {
        return INSTANCE;
    }
}