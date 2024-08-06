package gr.technico.technikon.repositories;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Repair;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepairRepository implements Repository<Repair, Long> {

    private final EntityManager entityManager;

    public RepairRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Repair> findAll() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

    public List<Repair> findRepairsByUserId(Owner owner) {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where owner  like :owner ",
                         getEntityClass())
                        .setParameter("owner", owner);
        return query.getResultList();
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

    @Override
    public boolean deleteById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Class<Repair> getEntityClass() {
        return Repair.class;
    }

    private String getEntityClassName() {
        return Repair.class.getName();
    }

    @Override
    public Optional<Repair> findById(Long id) {
        try {
            entityManager.getTransaction().begin();
            Repair t = entityManager.find(getEntityClass(), id);
            entityManager.getTransaction().commit();
            return Optional.of(t);
        } catch (Exception e) {
            log.debug("An exception occured");
        }
        return Optional.empty();
    }

    public List<Repair> findAllByOwnerId(Owner owner1) {

        javax.persistence.TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where ownerId  like :owner1 ",
                         getEntityClass())
                        .setParameter("owner1", owner1);
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
}
