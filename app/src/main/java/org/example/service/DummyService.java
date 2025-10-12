package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.example.domain.Dummy;

import java.util.List;

@Stateless
public class DummyService {

    @PersistenceContext(unitName = "dummyPU")
    private EntityManager entityManager;

    @Transactional
    public Dummy save(Dummy dummy) {
        entityManager.persist(dummy);
        return dummy;
    }

    @Transactional
    public Dummy save(String name) {
        Dummy dummy = new Dummy();
        dummy.setName(name);
        entityManager.persist(dummy);
        return dummy;
    }

    public Dummy findById(Long id) {
        return entityManager.find(Dummy.class, id);
    }

    public List<Dummy> findAll() {
        return entityManager.createQuery("SELECT d FROM Dummy d", Dummy.class)
                .getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        Dummy dummy = findById(id);
        if (dummy != null) {
            entityManager.remove(dummy);
        }
    }

    public long count() {
        return entityManager.createQuery("SELECT COUNT(d) FROM Dummy d", Long.class)
                .getSingleResult();
    }
}