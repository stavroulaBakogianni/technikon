package gr.technico.technikon.repositories;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.Repair;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Repository for managing Repairs.
 *
 */
public class RepairRepository implements Repository<Repair, Long> {

    private final EntityManager entityManager;

    public RepairRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Saves the provided Repair entity to the database.
     *
     * @param repair The repair entity to be saved. Must not be null.
     * @return An Optional containing the saved Repair if the save was
     * successful, or Optional.empty() if it was not.
     */
    @Override
    public Optional<Repair> save(Repair repair) {
        try {
            JpaUtil.beginTransaction();
            entityManager.persist(repair);
            JpaUtil.commitTransaction();
            return Optional.of(repair);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            return Optional.empty();
        }
    }

    /**
     * Retrieves all Repair entities from the database.
     *
     * @return A List of all Repair entities.
     */
    @Override
    public List<Repair> findAll() {
        TypedQuery<Repair> query = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

    /**
     * Finds a Repair entity by its ID.
     *
     * @param id The ID of the repair to be found. Must not be null.
     * @return An Optional containing the found Repair, or Optional.empty() if
     * no repair was found.
     */
    @Override
    public Optional<Repair> findById(Long id) {
        try {
            entityManager.getTransaction().begin();
            Repair t = entityManager.find(getEntityClass(), id);
            entityManager.getTransaction().commit();
            return Optional.of(t);
        } catch (Exception e) {
            System.out.println("An exception occured");
        }
        return Optional.empty();
    }

    /**
     * Deletes a Repair entity by its ID.
     *
     * @param id The ID of the repair to be deleted. Must not be null.
     * @return true if the repair was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteById(Long id) {
        Repair repair = entityManager.find(getEntityClass(), id);
        if (repair != null) {

            try {
                JpaUtil.beginTransaction();
                entityManager.remove(repair);
                JpaUtil.commitTransaction();
            } catch (Exception e) {
                JpaUtil.rollbackTransaction();
                System.out.println("Exception: " + e);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Marks a Repair entity as deleted by setting its deleted flag to true and
     * saving the updated entity to the database.
     *
     * @param repair The repair entity to be safely deleted. Must not be null.
     * @return true if the repair was successfully marked as deleted, false
     * otherwise.
     */
    public boolean safeDelete(Repair repair) {
        repair.setDeleted(true);
        Optional<Repair> safelyDeletedRepair = save(repair);
        if (safelyDeletedRepair.isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * Finds all Repair entities associated with the given owner.
     *
     * @param owner The owner whose repairs are to be retrieved. Must not be
     * null.
     * @return A List of Repair entities associated with the specified owner.
     */
    public List<Repair> findRepairsByOwner(Owner owner) {
        javax.persistence.TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where owner  like :owner ",
                        getEntityClass())
                        .setParameter("owner", owner);
        return query.getResultList();
    }

    /**
     * Finds all Repair entities with a status of "PENDING".
     *
     * @return A List of pending Repair entities.
     */
    public List<Repair> findPendingRepairs() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where repair_status  like :repair_status ",
                        getEntityClass())
                        .setParameter("repair_status", "PENDING");
        return query.getResultList();
    }

    /**
     *
     * Finds all Repair entities with a status of "PENDING" for a specific
     * owner.
     *
     * @param owner The owner whose pending repairs are to be retrieved. Must
     * not be null.
     * @return A List of pending Repair entities associated with the specified
     * owner.
     */
    public List<Repair> findPendingRepairsByOwner(Owner owner) {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where repair_status  like :repair_status "
                        + " and owner like :owner "
                        + " and proposed_cost != null",
                        getEntityClass())
                        .setParameter("owner", owner)
                        .setParameter("repair_status", "PENDING");
        return query.getResultList();
    }

    /**
     * Finds all Repair entities associated with a specific property.
     *
     * @param property The property whose repairs are to be retrieved. Must not
     * be null.
     * @return A List of Repair entities associated with the specified property.
     */
    public List<Repair> findRepairsByPropertyId(Property property) {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where property  like :property ",
                        getEntityClass())
                        .setParameter("property", property);
        return query.getResultList();
    }

    /**
     * Finds all Repair entities with a status of "INPROGRESS".
     *
     * @return A List of in-progress Repair entities.
     */
    public List<Repair> findInProgressRepairs() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where repair_status  like :repair_status ",
                        getEntityClass())
                        .setParameter("repair_status", "INPROGRESS");
        return query.getResultList();
    }

    /**
     * Finds all Repair entities that have been accepted.
     *
     * @return A List of accepted Repair entities.
     */
    public List<Repair> findAcceptedRepairs() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where acceptance_status  = 1 ",
                        getEntityClass());
        return query.getResultList();
    }

    /**
     * Finds all Repair entities within a specific date range. Optionally
     * filters by owner.
     *
     * @param startDate The start date of the range. Must not be null.
     * @param endDate The end date of the range. Must not be null.
     * @param owner The owner to filter the repairs by. Can be null if no
     * filtering by owner is needed.
     */
    public List<Repair> findRepairsByDates(LocalDateTime startDate, LocalDateTime endDate, Owner owner) {
        if (owner == null) {
            TypedQuery<Repair> query
                    = entityManager.createQuery("from " + getEntityClassName()
                            + " where submission_date between :startDate and :endDate",
                            getEntityClass())
                            .setParameter("startDate", startDate)
                            .setParameter("endDate", endDate);

            return query.getResultList();
        } else {
            TypedQuery<Repair> query
                    = entityManager.createQuery("from " + getEntityClassName()
                            + " where submission_date between :startDate and :endDate"
                            + " and owner like :owner ",
                            getEntityClass())
                            .setParameter("startDate", startDate)
                            .setParameter("endDate", endDate)
                            .setParameter("owner", owner);
            return query.getResultList();
        }

    }

    /**
     * Returns the Class object representing the Repair entity.
     *
     */
    private Class<Repair> getEntityClass() {
        return Repair.class;
    }

    /**
     * Returns the name of the Repair entity class.
     *
     * @return The name of the Repair entity class.
     */
    private String getEntityClassName() {
        return Repair.class.getName();
    }
}
