package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.example.entity.Dummy;

import java.util.List;

@Stateless
public class DummyService {

    @PersistenceContext(unitName = "dummyPU")
    private EntityManager entityManager;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Dummy save(Dummy dummy) {
        entityManager.persist(dummy);
        return dummy;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Dummy update(Long id, Dummy updated) {
        Dummy existing = findById(id);
        if (existing == null) {
            return null;
        }
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        // createdAt remains unchanged
        return entityManager.merge(existing);
    }
}