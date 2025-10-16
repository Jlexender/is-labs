package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.example.entity.Ticket;

import java.util.List;

@Stateless
public class TicketService {

    @PersistenceContext(unitName = "ticketPU")
    private EntityManager entityManager;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Ticket save(Ticket ticket) {
        entityManager.persist(ticket);
        return ticket;
    }

    public Ticket findById(Long id) {
        return entityManager.find(Ticket.class, id);
    }

    public List<Ticket> findAll() {
        return entityManager.createQuery("SELECT t FROM Ticket t", Ticket.class)
                .getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteById(Long id) {
        Ticket ticket = findById(id);
        if (ticket != null) {
            entityManager.remove(ticket);
        }
    }

    public long count() {
        return entityManager.createQuery("SELECT COUNT(t) FROM Ticket t", Long.class)
                .getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Ticket update(Long id, Ticket updated) {
        Ticket existing = findById(id);
        if (existing == null)
            return null;
        existing.setName(updated.getName());
        existing.setCoordinates(updated.getCoordinates());
        existing.setPerson(updated.getPerson());
        existing.setEvent(updated.getEvent());
        existing.setPrice(updated.getPrice());
        existing.setType(updated.getType());
        existing.setDiscount(updated.getDiscount());
        existing.setNumber(updated.getNumber());
        existing.setComment(updated.getComment());
        existing.setVenue(updated.getVenue());
        return entityManager.merge(existing);
    }
}
