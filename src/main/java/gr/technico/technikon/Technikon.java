package gr.technico.technikon;

import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairType;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Technikon {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Technikon");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        // Initialize a new owner object to get the unique id
        Owner owner1 = new Owner();
        owner1.setVat("123456789");
        owner1.setAddress("Kavala");
        owner1.setName("Stavroula");
        owner1.setSurname("Bakogianni");
        owner1.setPhoneNumber("6999999876");
        owner1.setUsername("bakostav");
        owner1.setPassword("123123123");
        owner1.setEmail("asss@example.com");

        entityManager.persist(owner1);

        Property property1 = new Property();
        property1.setOwner(owner1);
        property1.setE9("GR345678901234567890");
        property1.setPropertyAddress("Kavala");
        property1.setConstructionYear(1980);
        property1.setPropertyType(PropertyType.MAISONETTE);

        entityManager.persist(property1);

        Repair repair1 = new Repair();
        repair1.setOwner(owner1);
        repair1.setProperty(property1);
        repair1.setRepairType(RepairType.ELECTRICALWORK);
        repair1.setShortDescription("elctric work");
        repair1.setSubmissionDate(LocalDateTime.now());

        entityManager.persist(repair1);

        entityManager.getTransaction().commit();

        entityManager.close();
        entityManagerFactory.close();
    }
}
