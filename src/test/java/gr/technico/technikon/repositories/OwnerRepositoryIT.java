package gr.technico.technikon.repositories;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OwnerRepositoryIT {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private OwnerRepository ownerRepository;

    private final String vat = "100000000";
    private final String name = "TestName";
    private final String surname = "TestSurname";
    private final String address = "Test an Adress";
    private final String phoneNumber = "6999999999";
    private final String email = "testemail@example.com";
    private final String username = "testusername";
    private final String password = "testpassword";

    @BeforeEach
    public void setUp() {
        entityManagerFactory = JpaUtil.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
        ownerRepository = new OwnerRepository(entityManager);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // create Test Owner
        Owner owner = createTestOwner(vat, name, surname, address, phoneNumber, email, username, password);
        ownerRepository.save(owner);
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

    // create Test Owner
    private Owner createTestOwner(String vat, String name, String surname, String address,String phoneNumber, String email, String username, String password) {
        Owner owner = new Owner();
        owner.setVat(vat);
        owner.setName(name);
        owner.setSurname(surname);
        owner.setAddress(address);
        owner.setPhoneNumber(phoneNumber);
        owner.setEmail(email);
        owner.setUsername(username);
        owner.setPassword(password);
        return owner;
    }

    @Test
    public void testSave() {
        Owner owner = createTestOwner("992882834", "Test2Name", "Test2Surname", "Test2 Adress", "69995888574", "test2@example.com", "test2username", "test2password");

        Optional<Owner> savedOwner = ownerRepository.save(owner);
        assertTrue(savedOwner.isPresent());
        assertEquals("992882834", savedOwner.get().getVat());
    }

    @Test
    public void testFindByVat() {
        Optional<Owner> foundOwner = ownerRepository.findByVat(vat);
        assertTrue(foundOwner.isPresent());
        assertEquals(vat, foundOwner.get().getVat());
    }

    @Test
    public void testFindByEmail() {
        Optional<Owner> foundOwner = ownerRepository.findByEmail(email);
        assertTrue(foundOwner.isPresent());
        assertEquals(name, foundOwner.get().getName());
        assertEquals(vat, foundOwner.get().getVat());
    }

    @Test
    public void testFindByUsername() {
        Optional<Owner> foundOwner = ownerRepository.findByUsername(username);
        assertTrue(foundOwner.isPresent());
        assertEquals(vat, foundOwner.get().getVat());
    }

    @Test
    public void testFindByUsernameAndPassword() {
        Optional<Owner> foundOwner = ownerRepository.findByUsernameAndPassword(username, password);
        assertTrue(foundOwner.isPresent());
        assertEquals(vat, foundOwner.get().getVat());
    }

    @Test
    public void testDeletePermanentlyByVatFound() {
        boolean isDeleted = ownerRepository.deletePermanentlyByVat(vat);
        assertTrue(isDeleted);

        Optional<Owner> foundOwner = ownerRepository.findByVat(vat);
        assertFalse(foundOwner.isPresent());
    }

    @Test
    public void testDeletePermanentlyByVatNotFound() {
        boolean isDeleted = ownerRepository.deletePermanentlyByVat("000");
        assertFalse(isDeleted);
    }
}
