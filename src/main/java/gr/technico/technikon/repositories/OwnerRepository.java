package gr.technico.technikon.repositories;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

public class OwnerRepository implements Repository<Owner, Long> {

    private final EntityManager entityManager;

    public OwnerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Owner> save(Owner owner) {
        try {
            JpaUtil.beginTransaction();
            entityManager.persist(owner);
            JpaUtil.commitTransaction(); 
            return Optional.of(owner);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction(); 
            return Optional.empty();
        }
    }

    @Override
    public List<Owner> findAll() {
        return List.of();
    }


    @Override
    public Optional<Owner> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
