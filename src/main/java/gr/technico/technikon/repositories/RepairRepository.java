package gr.technico.technikon.repositories;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Repair;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

public class RepairRepository implements Repository<Repair, Long> {

    private final EntityManager entityManager;

    public RepairRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Repair> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Repair> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
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

}
