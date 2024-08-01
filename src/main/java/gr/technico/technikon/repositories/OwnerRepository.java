package gr.technico.technikon.repositories;

import gr.technico.technikon.model.Owner;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OwnerRepository implements Repository<Owner, String> {

    private final EntityManager entityManager;

    public OwnerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Owner> save(Owner owner) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(owner);
            entityManager.getTransaction().commit();
            return Optional.of(owner);
        } catch (Exception e) {
            log.debug("An exception occurred while saving owner", e);
            entityManager.getTransaction().rollback();
        }
        return Optional.empty();
    }
}
