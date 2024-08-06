package gr.technico.technikon.repositories;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Repair;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class RepairRepository implements Repository<Repair, Long> {

    private final EntityManager entityManager;

    public RepairRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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
    //maybe we remove this method if we dont need it

    @Override
    public List<Repair> findAll() {
        TypedQuery<Repair> query = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

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

    //Method called by owner and set isDeleted column true 
    public boolean safeDelete(Repair repair) {
        repair.setDeleted(true);
        Optional<Repair> safelyDeletedRepair = save(repair);
        if (safelyDeletedRepair.isPresent()) {
            return true;
        }
        return false;
    }

    public List<Repair> findRepairsByOwner(Owner owner) {
        javax.persistence.TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where ownerId  like :owner ",
                        getEntityClass())
                        .setParameter("owner", owner);
        return query.getResultList();
    }

    public List<Repair> findPendingRepairs() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where repair_status  like :repair_status ",
                        getEntityClass())
                        .setParameter("repair_status", "PENDING");
        return query.getResultList();
    }

    //Method to search by dates
    public List<Repair> findRepairsByDates(LocalDateTime startDate, LocalDateTime endDate) {
        TypedQuery<Repair> query = entityManager.createQuery("from " + getEntityClassName() + " where submission_date  between " + startDate + " and " + endDate, getEntityClass());
        return query.getResultList();
    }

    private Class<Repair> getEntityClass() {
        return Repair.class;
    }

    private String getEntityClassName() {
        return Repair.class.getName();
    }

}
