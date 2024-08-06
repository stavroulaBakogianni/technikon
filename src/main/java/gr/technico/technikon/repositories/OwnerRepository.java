package gr.technico.technikon.repositories;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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

    public Optional<Owner> findByVat(String vat) {
        TypedQuery<Owner> query = entityManager.createQuery("FROM Owner WHERE vat = :vat", Owner.class);
        query.setParameter("vat", vat);
        return query.getResultStream().findFirst();
    }

    public Optional<Owner> findByEmail(String email) {
        TypedQuery<Owner> query = entityManager.createQuery("FROM Owner WHERE email = :email", Owner.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst();
    }

    public Optional<Owner> findByUsername(String username) {
        TypedQuery<Owner> query = entityManager.createQuery("FROM Owner WHERE username = :username", Owner.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }

    public Optional<Owner> findByUsernameAndPassword(String username, String password) {
        TypedQuery<Owner> query = entityManager.createQuery(
                "FROM Owner WHERE username = :username AND password = :password AND isDeleted = false",
                Owner.class
        );
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.getResultStream().findFirst();
    }

    public boolean deletePermanentlyByVat(String vat) {
        try {
            JpaUtil.beginTransaction();
            Optional<Owner> optionalOwner = findByVat(vat);

            if (optionalOwner.isPresent()) {
                Owner owner = optionalOwner.get();
                // Remove Owner and its Properties and Repairs.
                entityManager.remove(owner);
                JpaUtil.commitTransaction();
                return true;
            } else {
                JpaUtil.rollbackTransaction();
                return false;
            }
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            return false;
        }
    }

    @Override
    public Optional<Owner> findById(Long id) {
        Owner owner;
        try {
            owner = entityManager.find(getEntityClass(), id);
            return Optional.of(owner);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Owner> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Class<Owner> getEntityClass() {
        return Owner.class;
    }
}
