package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.example.entity.ImportHistory;
import org.example.entity.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Stateless
public class ImportService {

    @PersistenceContext(unitName = "ticketPU")
    private EntityManager entityManager;

    private Validator validator;

    @jakarta.annotation.Resource
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * Import tickets with SERIALIZABLE isolation level to prevent concurrent conflicts
     * Rolls back everything if any error occurs
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ImportHistory importTickets(List<Ticket> tickets, String userId) {
        ImportHistory history = new ImportHistory();
        history.setUserId(userId);
        history.setCreatedAt(LocalDateTime.now());

        try {
            // Validate all tickets first
            for (Ticket ticket : tickets) {
                validateTicket(ticket);
                checkUniqueness(ticket);
            }

            // Persist all tickets
            for (Ticket ticket : tickets) {
                ticket.setId(null); // Ensure new entities
                entityManager.persist(ticket);
            }

            // Flush to trigger any DB constraints
            entityManager.flush();

            history.setStatus("SUCCESS");
            history.setObjectCount(tickets.size());
        } catch (Exception e) {
            history.setStatus("FAILED");
            // Get the root cause message for better error reporting
            String errorMsg = getRootCauseMessage(e);
            history.setErrorMessage(errorMsg);
            // Transaction will be rolled back automatically due to RuntimeException
            // but we catch it here to prevent EJBException wrapping
        } finally {
            // Persist history in a separate transaction
            persistHistory(history);
        }

        return history;
    }

    private void validateTicket(Ticket ticket) {
        if (validator != null) {
            Set<ConstraintViolation<Ticket>> violations = validator.validate(ticket);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder("Validation failed: ");
                for (ConstraintViolation<Ticket> violation : violations) {
                    sb.append(violation.getPropertyPath()).append(" ")
                      .append(violation.getMessage()).append("; ");
                }
                throw new IllegalArgumentException(sb.toString());
            }
        }

        // Manual validation for required fields
        if (ticket.getName() == null || ticket.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket name is required");
        }
        if (ticket.getCoordinates() == null) {
            throw new IllegalArgumentException("Coordinates are required");
        }
        if (ticket.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (ticket.getDiscount() == null || ticket.getDiscount() <= 0 || ticket.getDiscount() > 100) {
            throw new IllegalArgumentException("Discount must be between 1 and 100");
        }
        if (ticket.getNumber() <= 0) {
            throw new IllegalArgumentException("Number must be positive");
        }
    }

    /**
     * Check programmatic uniqueness constraint: ticket number must be unique
     * Uses pessimistic locking to prevent concurrent violations
     */
    private void checkUniqueness(Ticket ticket) {
        List<Ticket> existing = entityManager.createQuery(
                "SELECT t FROM Ticket t WHERE t.number = :number", Ticket.class)
                .setParameter("number", ticket.getNumber())
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();
        
        if (!existing.isEmpty()) {
            throw new IllegalArgumentException(
                "Ticket with number " + ticket.getNumber() + " already exists");
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void persistHistory(ImportHistory history) {
        entityManager.persist(history);
    }

    /**
     * Extract the most meaningful error message from the exception chain
     */
    private String getRootCauseMessage(Exception e) {
        Throwable current = e;
        String message = e.getMessage();
        
        // Traverse to find the root cause
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
            if (current.getMessage() != null && !current.getMessage().isEmpty()) {
                message = current.getMessage();
            }
        }
        
        return message != null ? message : "Unknown error occurred";
    }

    public List<ImportHistory> getImportHistory(String userId, boolean isAdmin) {
        if (isAdmin) {
            return entityManager.createQuery(
                    "SELECT h FROM ImportHistory h ORDER BY h.createdAt DESC", ImportHistory.class)
                    .getResultList();
        } else {
            return entityManager.createQuery(
                    "SELECT h FROM ImportHistory h WHERE h.userId = :userId ORDER BY h.createdAt DESC",
                    ImportHistory.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }
}
