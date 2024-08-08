package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.repositories.PropertyRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class PropertyServiceIT {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private PropertyRepository propertyRepository;
    private OwnerRepository ownerRepository;
    private PropertyServiceImpl propertyServiceImpl;
    private OwnerServiceImpl ownerServiceImpl;

    private final String vat = "100000000";
    private final String e9 = "12345678901234567897";
    private final String address = "Test Address";
    private final int year = 2024;
    private final PropertyType propertyType = PropertyType.APARTMENTBUILDING;
    private final String newAddress = "Updated Address";

    @BeforeEach
    public void setUp() throws CustomException {
        entityManagerFactory = JpaUtil.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
        propertyRepository = new PropertyRepository(entityManager);
        ownerRepository = new OwnerRepository(entityManager);

        ownerServiceImpl = new OwnerServiceImpl(ownerRepository);
        propertyServiceImpl = new PropertyServiceImpl(propertyRepository, ownerServiceImpl);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // Create an Owner to associate with the Property
        ownerServiceImpl.createOwner(vat, "TestName", "TestSurname", "Test Address", "6999999999", "testemail@example.com", "testusername", "testpassword");

        // Create a Property
        propertyServiceImpl.createProperty(e9, address, year, propertyType, vat);

        transaction.commit();
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
    public void testCreateProperty() throws CustomException {
        Property property = propertyRepository.findPropertyByE9(e9).orElse(null);
        assertNotNull(property);
        assertEquals(e9, property.getE9());
        assertEquals(address, property.getPropertyAddress());
        assertEquals(year, property.getConstructionYear());
        assertEquals(propertyType, property.getPropertyType());
        assertFalse(property.isDeleted());
    }

    @Test
    public void testUpdatePropertyAddress() throws CustomException {
        Property property = propertyRepository.findPropertyByE9(e9).orElse(null);
        assertNotNull(property);

        propertyServiceImpl.updatePropertyAddress(property, newAddress);
        Property updatedProperty = propertyRepository.findPropertyByE9(e9).orElse(null);
        assertNotNull(updatedProperty);
        assertEquals(newAddress, updatedProperty.getPropertyAddress());
    }

    @Test
    public void testUpdatePropertyConstructionYear() throws CustomException {
        Property property = propertyRepository.findPropertyByE9(e9).orElse(null);
        assertNotNull(property);

        int newYear = 2025;
        propertyServiceImpl.updatePropertyConstructionYear(property, newYear);
        Property updatedProperty = propertyRepository.findPropertyByE9(e9).orElse(null);
        assertNotNull(updatedProperty);
        assertEquals(newYear, updatedProperty.getConstructionYear());
    }

    @Test
    public void testUpdatePropertyType() throws CustomException {
        Property property = propertyRepository.findPropertyByE9(e9).orElse(null);
        assertNotNull(property);

        PropertyType newType = PropertyType.DETACHEDHOUSE;
        propertyServiceImpl.updatePropertyType(property, newType);
        Property updatedProperty = propertyRepository.findPropertyByE9(e9).orElse(null);
        assertNotNull(updatedProperty);
        assertEquals(newType, updatedProperty.getPropertyType());
    }

    @Test
    public void testFindByE9() throws CustomException {
        Property property = propertyServiceImpl.findByE9(e9);
        assertNotNull(property);
        assertEquals(e9, property.getE9());
    }

    @Test
    public void testFindByID() throws CustomException {
        Property property = propertyRepository.findPropertyByE9(e9).orElse(null);
        assertNotNull(property);

        Property foundProperty = propertyServiceImpl.findByID(property.getId());
        assertNotNull(foundProperty);
        assertEquals(property.getId(), foundProperty.getId());
    }

    @Test
    public void testSafelyDeleteByID() throws CustomException {
        Property property = propertyRepository.findPropertyByE9(e9).orElse(null);
        assertNotNull(property);

        boolean deleted = propertyServiceImpl.safelyDeleteByID(property.getId());
        assertTrue(deleted);
        Property deletedProperty = propertyRepository.findPropertyByE9(e9).orElse(null);
        assertNotNull(deletedProperty);
        assertTrue(deletedProperty.isDeleted());
    }

    @Test
    public void testPermanentlyDeleteByID() throws CustomException {
        Property property = propertyRepository.findPropertyByE9(e9).orElse(null);
        assertNotNull(property);

        propertyServiceImpl.safelyDeleteByID(property.getId());
        boolean permanentlyDeleted = propertyServiceImpl.permenantlyDeleteByID(property.getId());
        assertTrue(permanentlyDeleted);
    }

    @Test
    public void testFindByVAT() throws CustomException {
        List<Property> properties = propertyServiceImpl.findByVAT(vat);
        assertFalse(properties.isEmpty());
    }

    @Test
    public void testFindByVATOwner() throws CustomException {
        List<Property> properties = propertyServiceImpl.findByVATOwner(vat);
        assertFalse(properties.isEmpty());
    }

    @Test
    public void testValidateE9() {
        assertThrows(CustomException.class, () -> propertyServiceImpl.validateE9("short"));
        assertDoesNotThrow(() -> propertyServiceImpl.validateE9("12345678901234567890"));
    }

    @Test
    public void testValidateConstructionYear() {
        assertThrows(CustomException.class, () -> propertyServiceImpl.validateConstructionYear("999"));
        assertDoesNotThrow(() -> propertyServiceImpl.validateConstructionYear("2024"));
    }

    @Test
    public void testValidatePropertyType() {
        assertThrows(CustomException.class, () -> propertyServiceImpl.validatePropertyType(null));
        assertDoesNotThrow(() -> propertyServiceImpl.validatePropertyType(PropertyType.MAISONETTE));
    }

}
