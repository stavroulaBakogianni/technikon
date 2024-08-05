package gr.technico.technikon.repositories;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Property;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertyRepository implements Repository<Property, Long> {

    private final EntityManager entityManager;

    public PropertyRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private Class<Property> getEntityClass() {
        return Property.class;
    }

    private String getEntityClassName() {
        return Property.class.getName();
    }

    /**
     * Saves the given Property entity to the database.
     *
     * This method starts a transaction, persists the Property entity, and
     * commits the transaction. If an exception occurs during the process, the
     * transaction is rolled back and an exception is thrown.
     *
     * @param property the Property entity to be saved
     * @return an Optional containing the saved Property entity if the operation
     * is successful
     * @throws Exception if an exception occurs during the save operation
     */
    @Override
    public Optional<Property> save(Property property) {
        try {
            JpaUtil.beginTransaction();
            entityManager.persist(property);
            JpaUtil.commitTransaction();
            return Optional.of(property);
        } catch (Exception e) {
            JpaUtil.rollbackTransaction();
            log.error("Exception: ", e);
            throw e;
        }
    }

    /**
     * Finds the Property entity with the specified ID.
     *
     * This method attempts to find the Property entity with the given ID. If
     * the entity is not found, an exception is thrown.
     *
     * @param id the ID of the Property entity to be found
     * @return an Optional containing the found Property entity if the operation
     * is successful
     * @throws Exception if an exception occurs during the search operation
     */
    @Override
    public Optional<Property> findById(Long id) {
        Property property;
        try {
            property = entityManager.find(getEntityClass(), id);
            return Optional.of(property);
        } catch (Exception e) {
            log.error("Exception: ", e);
            throw e;
        }
    }

    /**
     * Retrieves all Property entities from the database.
     *
     * This method creates and executes a TypedQuery to retrieve all Property
     * entities.
     *
     * @return a List containing all Property entities found in the database
     */
    @Override
    public List<Property> findAll() {
        TypedQuery<Property> query
                = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

    /**
     * Deletes a Property entity by its ID.
     *
     * This method finds the Property entity with the specified ID. If the
     * entity is found, it starts a transaction, removes the entity from the
     * database, and commits the transaction. If an exception occurs during this
     * process, the transaction is rolled back and method returns false.
     *
     * @param id the ID of the Property entity to be deleted
     * @return true if the Property entity was found and successfully deleted;
     * false otherwise
     */
    @Override
    public boolean deleteById(Long id) {
        Property property = entityManager.find(getEntityClass(), id);
        if (property != null) {

            try {
                JpaUtil.beginTransaction();
                entityManager.remove(property);
                JpaUtil.commitTransaction();
            } catch (Exception e) {
                JpaUtil.rollbackTransaction();
                log.error("Exception: ", e);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Finds a Property entity by its E9 identifier.
     *
     * This method creates a TypedQuery to search for a Property entity with the
     * specified E9 identifier. If the entity is found, it is returned wrapped
     * in an Optional. If not found, an empty Optional is returned.
     *
     * @param e9 the E9 identifier of the Property entity to be found
     * @return an Optional containing the found Property entity if the operation
     * is successful; otherwise, an empty Optional
     */
    public Optional<Property> findPropertyByE9(String e9) {
        TypedQuery<Property> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where e9 = :e9 ",
                        getEntityClass())
                        .setParameter("e9", e9);
        return Optional.of(query.getSingleResult());
    }

    /**
     * Finds a list of Property entities by the owner's VAT identifier.
     *
     * This method creates a TypedQuery to search for Property} entities
     * associated with the specified owner's VAT identifier. The query returns a
     * list of properties that belong to the owner with the given VAT.
     *
     * @param vat the VAT identifier of the owner whose properties are to be
     * found
     * @return a List of Property entities associated with the specified owner's
     * VAT
     */
    public List<Property> findPropertyByVAT(String vat) {
        TypedQuery<Property> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where owner_vat = :vat ",
                        getEntityClass())
                        .setParameter("vat", vat);
        return query.getResultList();
    }
}
