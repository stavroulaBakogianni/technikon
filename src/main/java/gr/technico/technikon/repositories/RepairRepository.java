package gr.technico.technikon.repositories;

import gr.technico.technikon.model.Repair;
import java.util.List;
import java.util.Optional;
//import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepairRepository implements Repository<Repair, Long>{
//    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    public RepairRepository(EntityManager entityManager) {
//    public RepairRepository(EntityManagerFactory entityManagerFactory) {
//        this.entityManagerFactory = entityManagerFactory;
        this.entityManager = entityManager;
    }
    
    @Override
    public Optional<Repair> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Repair> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<Repair> save(Repair t) {
        try {
            entityManager.getTransaction().begin();
//            entityManagerFactory.persist(t);
//            entityManagerFactory.getTransaction().commit();
            entityManager.persist(t);
            entityManager.getTransaction().commit();
            return Optional.of(t);
        } catch (Exception e) {
            log.debug("An exception occured");
        }
        return Optional.empty(); 
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
