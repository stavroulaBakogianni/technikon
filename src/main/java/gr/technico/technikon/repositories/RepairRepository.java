package gr.technico.technikon.repositories;

import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Repair;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepairRepository implements Repository<Repair, Long>{
    private final EntityManager entityManager;
    public RepairRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
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

    @Override
    public String toString() {
        return "RepairRepository{" + "entityManager=" + entityManager + '}';
    }

    @Override
    public List<Repair> findAll() {
        TypedQuery<Repair> query = 
               entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();    
    }
    
    public List<Repair> findRepairsByUserId(Owner owner) {
       TypedQuery<Repair> query = 
               entityManager.createQuery("from " + getEntityClassName()
                       + " where owner  like :owner "
                       , getEntityClass())
               .setParameter("owner", owner);
        return query.getResultList();    
    }

    @Override
    public Optional<Repair> save(Repair t) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(t);
            entityManager.getTransaction().commit();
            return Optional.of(t);
        } catch (Exception e) {
            log.debug("An exception occured");
        }
        return Optional.empty(); 
    }

    @Override
    public boolean deleteById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
     private Class<Repair> getEntityClass() {
        return Repair.class;
    }

    private String getEntityClassName() {
        return Repair.class.getName();
    }
    
}
