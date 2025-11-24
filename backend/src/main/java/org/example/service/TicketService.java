package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

import org.example.entity.Ticket;

import java.util.List;

@Stateless
public class TicketService {

    @PersistenceContext(unitName = "ticketPU")
    private EntityManager entityManager;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Ticket save(Ticket ticket) {
        checkTicketNumberUniqueness(ticket.getNumber(), null);
        entityManager.persist(ticket);
        return ticket;
    }
    
    private void checkTicketNumberUniqueness(long number, Long excludeId) {
        String query = excludeId == null 
            ? "SELECT t FROM Ticket t WHERE t.number = :number"
            : "SELECT t FROM Ticket t WHERE t.number = :number AND t.id != :excludeId";
        
        var q = entityManager.createQuery(query, Ticket.class)
                .setParameter("number", number)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE);
        
        if (excludeId != null) {
            q.setParameter("excludeId", excludeId);
        }
        
        List<Ticket> existing = q.getResultList();
        
        if (!existing.isEmpty()) {
            throw new IllegalArgumentException(
                "Ticket with number " + number + " already exists");
        }
    }

    public Ticket findById(Long id) {
        return entityManager.find(Ticket.class, id);
    }

    public Ticket findByIdForUpdate(Long id) {
        return entityManager.find(Ticket.class, id, LockModeType.PESSIMISTIC_WRITE);
    }

    public List<Ticket> findAll() {
        return entityManager.createQuery("SELECT t FROM Ticket t", Ticket.class)
                .getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteById(Long id) {
        Ticket ticket = findByIdForUpdate(id);
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
        Ticket existing = findByIdForUpdate(id);
        if (existing == null)
            return null;
        
        if (existing.getNumber() != updated.getNumber()) {
            checkTicketNumberUniqueness(updated.getNumber(), id);
        }
        
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

    @SuppressWarnings("null")
    public Ticket findMinByNumber() {
        return entityManager.createQuery(
                "SELECT t FROM Ticket t ORDER BY t.number ASC", Ticket.class)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null); // Can return null if no tickets exist
    }

    public long countByNumberLessThan(long number) {
        return entityManager.createQuery(
                "SELECT COUNT(t) FROM Ticket t WHERE t.number < :number", Long.class)
                .setParameter("number", number)
                .getSingleResult();
    }

    public List<Ticket> findByCommentStartsWith(String prefix) {
        return entityManager.createQuery(
                "SELECT t FROM Ticket t WHERE t.comment LIKE :prefix", Ticket.class)
                .setParameter("prefix", prefix + "%")
                .getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Ticket sellTicket(Long ticketId, float price, org.example.entity.Person person) {
        Ticket ticket = findByIdForUpdate(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket not found");
        }
        ticket.setPrice(price);
        ticket.setPerson(person);
        return entityManager.merge(ticket);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int cancelEvent(Long eventId) {
        return entityManager.createQuery(
                "DELETE FROM Ticket t WHERE t.event.id = :eventId")
                .setParameter("eventId", eventId)
                .executeUpdate();
    }
}
