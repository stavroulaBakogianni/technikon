package gr.technico.technikon;

import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairStatus;
import gr.technico.technikon.model.RepairType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Technikon {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Technikon");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

//        Owner owner = new Owner("123457", "Apostolis", "Tourlidas", "Agrinio", "6999999999", "a@example.com", "apostolis", "apostolis");
        Owner owner1 = new Owner("123456788", "Apostolis", "Tourlidas", "Agrinio", "6999999999", "asss@example.com", "apostolissss", "apostolis");
//        entityManager.persist(owner1);
        
        Property property = new Property("1233", "Athens", 2000, PropertyType.DETACHEDHOUSE, owner1);
//        entityManager.persist(property);

        Repair repair = new Repair(owner1, property, RepairType.ELECTRICALWORK, "aaa", LocalDateTime.now(), "aaaa", LocalDateTime.now(), LocalDateTime.now(), BigDecimal.valueOf(150.0), true, RepairStatus.PENDING, LocalDateTime.now(), LocalDateTime.now());
//        entityManager.persist(repair);
        
        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }
}
