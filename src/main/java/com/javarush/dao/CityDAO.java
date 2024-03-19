package com.javarush.dao;

import com.javarush.config.SessionFactoryProvider;
import com.javarush.domain.City;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class CityDAO implements CrudDAO<City, Long> {
    private final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactoryRelationalDb();
    private static final CityDAO INSTANCE = new CityDAO();

    private CityDAO() {
    }

    @Override
    public List<City> findAll() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from City");
        return query.getResultList();
        /*
        try (Session session = sessionFactory.getCurrentSession()) {
            List<City> allCities = new ArrayList<>();
            session.beginTransaction();

            List<Country> countries = countryDAO.getAll();
            int totalCount = cityDAO.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                Query<City> query = sessionFactory.getCurrentSession().createQuery("select c from City c", City.class);
                query.setFirstResult(i);
                query.setMaxResults(step);
                allCities.addAll(query.list());
            }
            List<CityCountry> preparedData = main.transformData(allCities);
            session.getTransaction().commit();
            return allCities;
        }
        */
    }

    @Override
    public Optional<City> findById(Long id) {
        Query<City> query = sessionFactory.openSession().createQuery("from City c join fetch c.country where c.id = :ID");
        query.setParameter("ID", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public void deleteById(Long id) {

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