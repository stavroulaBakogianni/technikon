package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.jpa.JpaUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OwnerServiceIT {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private OwnerRepository ownerRepository;
    private OwnerService ownerService;

    private final String vat = "100000000";
    private final String name = "TestName";
    private final String surname = "TestSurname";
    private final String address = "Test an Adress";
    private final String phoneNumber = "6999999999";
    private final String email = "testemail@example.com";
    private final String username = "testusername";
    private final String password = "testpassword";

    @BeforeEach
    public void setUp() throws CustomException {
        entityManagerFactory = JpaUtil.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
        ownerRepository = new OwnerRepository(entityManager);
        ownerService = new OwnerService(ownerRepository);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        ownerService.createOwner(vat, name, surname, address, phoneNumber, email, username, password);
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
    public void testCreateOwner() throws CustomException {
        Owner owner = ownerRepository.findByVat(vat).orElse(null);
        assertNotNull(owner);
        assertEquals(name, owner.getName());
        assertEquals(surname, owner.getSurname());
        assertEquals(address, owner.getAddress());
        assertEquals(phoneNumber, owner.getPhoneNumber());
        assertEquals(email, owner.getEmail());
        assertEquals(username, owner.getUsername());
        assertEquals(password, owner.getPassword());
    }

    @Test
    public void testSearchOwnerByVat() throws CustomException {
        Optional<Owner> foundOwner = ownerService.searchOwnerByVat(vat);
        assertTrue(foundOwner.isPresent());
        assertEquals(vat, foundOwner.get().getVat());
        assertEquals(name, foundOwner.get().getName());
    }

    @Test
    public void testSearchOwnerByEmail() throws CustomException {
        Optional<Owner> foundOwner = ownerService.searchOwnerByEmail(email);
        assertTrue(foundOwner.isPresent());
        assertEquals(email, foundOwner.get().getEmail());
        assertEquals(name, foundOwner.get().getName());
    }

    @Test
    public void testUpdateOwnerAddress() throws CustomException {
        String newAddress = "New Address";
        ownerService.updateOwnerAddress(vat, newAddress);
        Owner owner = ownerRepository.findByVat(vat).orElse(null);
        assertNotNull(owner);
        assertEquals(newAddress, owner.getAddress());
    }

    @Test
    public void testUpdateOwnerEmail() throws CustomException {
        String newEmail = "new.email@gmail.com";
        ownerService.updateOwnerEmail(vat, newEmail);
        Owner owner = ownerRepository.findByVat(vat).orElse(null);
        assertNotNull(owner);
        assertEquals(newEmail, owner.getEmail());
    }

    @Test
    public void testUpdateOwnerPassword() throws CustomException {
        String newPassword = "newpassword";
        ownerService.updateOwnerPassword(vat, newPassword);
        Owner owner = ownerRepository.findByVat(vat).orElse(null);
        assertNotNull(owner);
        assertEquals(newPassword, owner.getPassword());
    }

    @Test
    public void testDeleteOwnerPermanently() throws CustomException {
        boolean deleted = ownerService.deleteOwnerPermanently(vat);
        assertTrue(deleted);
        Optional<Owner> foundOwner = ownerService.searchOwnerByVat(vat);
        assertFalse(foundOwner.isPresent());
    }

    @Test
    public void testDeleteOwnerSafely() throws CustomException {
        boolean deleted = ownerService.deleteOwnerSafely(vat);
        assertTrue(deleted);
        Owner owner = ownerRepository.findByVat(vat).orElse(null);
        assertNotNull(owner);
        assertTrue(owner.isDeleted());
    }

    @Test
    public void testVerifyOwner() throws CustomException {
        assertDoesNotThrow(() -> {
            Optional<String> result = ownerService.verifyOwner(username, password);
            assertTrue(result.isPresent());
            assertEquals(vat, result.get());
        });

        assertThrows(CustomException.class, () -> ownerService.verifyOwner(username, "wrongpassword"));
        assertThrows(CustomException.class, () -> ownerService.verifyOwner("wrongusername", password));
    }

    @Test
    public void testValidateVat() {
        assertThrows(CustomException.class, () -> ownerService.validateVat("123"));
        assertDoesNotThrow(() -> ownerService.validateVat("123456789"));
    }

    @Test
    public void testValidateName() {
        assertThrows(CustomException.class, () -> ownerService.validateName(""));
        assertDoesNotThrow(() -> ownerService.validateName("Nikos"));
    }

    @Test
    public void testValidateSurname() {
        assertThrows(CustomException.class, () -> ownerService.validateSurname(""));
        assertDoesNotThrow(() -> ownerService.validateSurname("Aygoustakis"));
    }

    @Test
    public void testValidatePassword() {
        assertThrows(CustomException.class, () -> ownerService.validatePassword("123"));
        assertDoesNotThrow(() -> ownerService.validatePassword("12345678"));
    }

    @Test
    public void testValidatePhone() {
        assertThrows(CustomException.class, () -> ownerService.validatePhone("123e45"));
        assertThrows(CustomException.class, () -> ownerService.validatePhone("123334456766656766"));
        assertDoesNotThrow(() -> ownerService.validatePhone("69996886775"));
    }

    @Test
    public void testValidateEmail() {
        assertThrows(CustomException.class, () -> ownerService.validateEmail("email"));
        assertDoesNotThrow(() -> ownerService.validateEmail("valid.email@example.com"));
    }

    @Test
    public void testCheckVat() throws CustomException {
        assertThrows(CustomException.class, () -> ownerService.checkVat(vat));
    }

    @Test
    public void testCheckEmail() throws CustomException {
        assertThrows(CustomException.class, () -> ownerService.checkEmail(email));
    }

    @Test
    public void testCheckUsername() throws CustomException {
        assertThrows(CustomException.class, () -> ownerService.checkVat(vat));

    }
}
