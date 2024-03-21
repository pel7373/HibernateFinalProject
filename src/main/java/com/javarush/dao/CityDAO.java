package com.javarush.dao;

import com.javarush.config.SessionFactoryProvider;
import com.javarush.domain.City;
import jakarta.persistence.NoResultException;
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
        return query.getResultList();
    }

    @Override
    public Optional<City> findById(Integer id) throws NoResultException {
        Query<City> query = sessionFactory.openSession().createQuery("from City c join fetch c.country where c.id = :ID");
        query.setParameter("ID", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    public Optional<City> findByName(String name) {
        Query<City> query = sessionFactory.openSession().createQuery("from City c where c.name = :NAME");
        query.setParameter("NAME", name);
        return Optional.ofNullable((City) query.getSingleResult());
    }

    @Override
    public void save(City city) {

    }

    @Override
    public void update(City city) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    public static CityDAO getInstance() {
        return INSTANCE;
    }
}