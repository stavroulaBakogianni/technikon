package gr.technico.technikon.repositories;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class PropertyRepositoryIT {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private PropertyRepository propertyRepository;
    private OwnerRepository ownerRepository;

    @BeforeEach
    public void setUp() {
        entityManagerFactory = JpaUtil.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
        propertyRepository = new PropertyRepository(entityManager);
        ownerRepository = new OwnerRepository(entityManager);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
    }

    @AfterEach
    public void tearDown() {
        EntityTransaction transaction = entityManager.getTransaction();
        if (transaction.isActive()) {
            transaction.rollback();
        }
        if (entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    @Test
    public void testSave() {
        Owner owner = new Owner();
        owner.setName("Apostolis");
        owner.setSurname("Tourlidas");
        owner.setAddress("Agrinio");
        owner.setPhoneNumber("6978456123");
        owner.setEmail("apo@gmail.com");
        owner.setUsername("apostolis2");
        owner.setPassword("password");owner.setVat("123456785");
        owner.setName("Apostolis");
        owner.setSurname("Tourlidas");
        owner.setAddress("Agrinio");
        owner.setPhoneNumber("6978456123");
        owner.setEmail("apo@gmail.com");
        owner.setUsername("apostolis2");
        owner.setPassword("password");

        ownerRepository.save(owner);

        Property property = new Property();
        property.setE9("12345678901234567898");
        property.setPropertyAddress("Patra");
        property.setConstructionYear(2024);
        property.setPropertyType(PropertyType.DETACHEDHOUSE);
        property.setDeleted(false);
        property.setOwner(owner);

        Optional<Property> result = propertyRepository.save(property);

        assertTrue(result.isPresent());
        assertEquals(property.getE9(), result.get().getE9());
        assertEquals(owner, result.get().getOwner());
    }

    @Test
    public void testFindById() {
        Owner owner = new Owner();
        owner.setVat("123456786");
        owner.setName("Apostolis");
        owner.setSurname("Tourlidas");
        owner.setAddress("Agrinio");
        owner.setPhoneNumber("6978456123");
        owner.setEmail("apos@gmail.com");
        owner.setUsername("apostolis3");
        owner.setPassword("password");

        ownerRepository.save(owner);

        Property property = new Property();
        property.setE9("1234567890123456785");
        property.setPropertyAddress("Patra");
        property.setConstructionYear(2024);
        property.setPropertyType(PropertyType.DETACHEDHOUSE);
        property.setDeleted(false);
        property.setOwner(owner);

        propertyRepository.save(property);

        Optional<Property> foundProperty = propertyRepository.findById(property.getId());
        assertTrue(foundProperty.isPresent());
        assertEquals(property.getE9(), foundProperty.get().getE9());
        assertEquals(owner, foundProperty.get().getOwner());
    }

    @Test
    public void testFindAll() {
        Owner owner1 = new Owner();
        owner1.setVat("123456787");
        owner1.setName("Apostolis");
        owner1.setSurname("Tourlidas");
        owner1.setAddress("Agrinio");
        owner1.setPhoneNumber("6978456123");
        owner1.setEmail("ap@gmail.com");
        owner1.setUsername("apostolis1");
        owner1.setPassword("password");

        Owner owner2 = new Owner();
        owner2.setVat("123456786");
        owner2.setName("Maria");
        owner2.setSurname("Papadopoulou");
        owner2.setAddress("Athens");
        owner2.setPhoneNumber("6978456123");
        owner2.setEmail("m@gmail.com");
        owner2.setUsername("mariapap");
        owner2.setPassword("password");

        ownerRepository.save(owner1);
        ownerRepository.save(owner2);

        Property property1 = new Property();
        property1.setE9("12345678901234567899");
        property1.setPropertyAddress("");
        property1.setConstructionYear(2024);
        property1.setPropertyType(PropertyType.DETACHEDHOUSE);
        property1.setDeleted(false);
        property1.setOwner(owner1);

        Property property2 = new Property();
        property2.setE9("098765432109876543210");
        property2.setPropertyAddress("Patra");
        property2.setConstructionYear(2025);
        property2.setPropertyType(PropertyType.DETACHEDHOUSE);
        property2.setDeleted(false);
        property2.setOwner(owner2);

        propertyRepository.save(property1);
        propertyRepository.save(property2);

        List<Property> properties = propertyRepository.findAll();
        assertEquals(2, properties.size());
    }

    @Test
    public void testDeleteById() {
        Owner owner = new Owner();
        owner.setVat("123456784");
        owner.setName("Apostolis");
        owner.setSurname("Tourlidas");
        owner.setAddress("Agrinio");
        owner.setPhoneNumber("6978456123");
        owner.setEmail("apost@gmail.com");
        owner.setUsername("apostolis5");
        owner.setPassword("password");

        ownerRepository.save(owner);

        Property property = new Property();
        property.setE9("12345678901234567894");
        property.setPropertyAddress("Patra");
        property.setConstructionYear(2024);
        property.setPropertyType(PropertyType.DETACHEDHOUSE);
        property.setDeleted(false);
        property.setOwner(owner);

        propertyRepository.save(property);

        boolean result = propertyRepository.deleteById(property.getId());
        assertTrue(result);

        Optional<Property> foundProperty = propertyRepository.findById(property.getId());
        assertFalse(foundProperty.isPresent());
    }

    @Test
    public void testFindPropertyByE9() {
        Owner owner = new Owner();
        owner.setVat("123456789");
        owner.setName("Apostolis");
        owner.setSurname("Tourlidas");
        owner.setAddress("Agrinio");
        owner.setPhoneNumber("6978456123");
        owner.setEmail("a@gmail.com");
        owner.setUsername("apostolis");
        owner.setPassword("password");

        ownerRepository.save(owner);;

        Property property = new Property();
        property.setE9("12345678901234567890");
        property.setPropertyAddress("Patra");
        property.setConstructionYear(2024);
        property.setPropertyType(PropertyType.DETACHEDHOUSE);
        property.setDeleted(false);
        property.setOwner(owner);

        propertyRepository.save(property);

        Optional<Property> foundProperty = propertyRepository.findPropertyByE9("12345678901234567890");
        assertTrue(foundProperty.isPresent());
        assertEquals(property.getE9(), foundProperty.get().getE9());
        assertEquals(owner, foundProperty.get().getOwner());
    }

    @Test
    public void testFindPropertyByVAT() {
        Owner owner1 = new Owner();
        owner1.setVat("123456789");
        owner1.setName("Apostolis");
        owner1.setSurname("Tourlidas");
        owner1.setAddress("Agrinio");
        owner1.setPhoneNumber("6978456123");
        owner1.setEmail("a@gmail.com");
        owner1.setUsername("apostolis");
        owner1.setPassword("password");

        Owner owner2 = new Owner();
        owner2.setVat("123456788");
        owner2.setName("Maria");
        owner2.setSurname("Papadopoulou");
        owner2.setAddress("Athens");
        owner2.setPhoneNumber("6978456123");
        owner2.setEmail("m@gmail.com");
        owner2.setUsername("mariapap");
        owner2.setPassword("password");

        ownerRepository.save(owner1);
        ownerRepository.save(owner2);

        Property property1 = new Property();
        property1.setE9("12345678901234567890");
        property1.setPropertyAddress("Thessaloniki");
        property1.setConstructionYear(2024);
        property1.setPropertyType(PropertyType.DETACHEDHOUSE);
        property1.setDeleted(false);
        property1.setOwner(owner1);

        Property property2 = new Property();
        property2.setE9("09876543210987654321");
        property2.setPropertyAddress("Ioannina");
        property2.setConstructionYear(2025);
        property2.setPropertyType(PropertyType.MAISONETTE);
        property2.setDeleted(false);
        property2.setOwner(owner1);

        Property property3 = new Property();
        property3.setE9("54321098765432109876");
        property3.setPropertyAddress("Patra");
        property3.setConstructionYear(2026);
        property3.setPropertyType(PropertyType.APARTMENTBUILDING);
        property3.setDeleted(false);
        property3.setOwner(owner2);

        propertyRepository.save(property1);
        propertyRepository.save(property2);
        propertyRepository.save(property3);

        List<Property> properties = propertyRepository.findPropertyByVAT("123456789");
        assertEquals(2, properties.size());
        assertTrue(properties.stream().anyMatch(p -> p.getE9().equals("12345678901234567890")));
        assertTrue(properties.stream().anyMatch(p -> p.getE9().equals("09876543210987654321")));
    }

}
