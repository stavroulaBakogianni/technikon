package gr.technico.technikon.repositories;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairStatus;
import gr.technico.technikon.model.RepairType;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RepairRepositoryIT {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private RepairRepository repairRepository;

    private Owner testOwner;
    private Property testProperty;

    @BeforeEach
    public void setUp() {
        entityManagerFactory = JpaUtil.getEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
        repairRepository = new RepairRepository(entityManager);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // Create test Owner and Property
        testOwner = new Owner();
        testOwner.setVat("100000000");
        testOwner.setName("TestName");
        testOwner.setSurname("TestSurname");
        testOwner.setAddress("Test an Adress");
        testOwner.setPhoneNumber("6999999999");
        testOwner.setEmail("testemail@example.com");
        testOwner.setUsername("testusername");
        testOwner.setPassword("testpassword");

        entityManager.persist(testOwner);

        testProperty = new Property();
        testProperty.setOwner(testOwner);
        testProperty.setConstructionYear(1990);
        testProperty.setE9("PROP0000000000000001");
        testProperty.setPropertyAddress("A test address");
        testProperty.setPropertyType(PropertyType.MAISONETTE);

        entityManager.persist(testProperty);

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
    public void testSave() {
        Repair repair = new Repair();
        repair.setOwner(testOwner);
        repair.setProperty(testProperty);
        repair.setSubmissionDate(LocalDateTime.now());
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setRepairType(RepairType.PAINTING);

        Optional<Repair> savedRepair = repairRepository.save(repair);
        assertTrue(savedRepair.isPresent());
        assertEquals(repair.getOwner(), savedRepair.get().getOwner());
    }

    @Test
    public void testFindById() {
        Repair repair = new Repair();
        repair.setOwner(testOwner);
        repair.setProperty(testProperty);
        repair.setSubmissionDate(LocalDateTime.now());
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setRepairType(RepairType.PAINTING);

        entityManager.getTransaction().begin();
        entityManager.persist(repair);
        entityManager.getTransaction().commit();

        Optional<Repair> foundRepair = repairRepository.findById(repair.getId());
        assertTrue(foundRepair.isPresent());
        assertEquals(repair.getId(), foundRepair.get().getId());
    }

    @Test
    public void testFindAll() {
        Repair repair1 = new Repair();
        repair1.setOwner(testOwner);
        repair1.setProperty(testProperty);
        repair1.setSubmissionDate(LocalDateTime.now());
        repair1.setRepairStatus(RepairStatus.PENDING);
        repair1.setRepairType(RepairType.FRAMES);

        Repair repair2 = new Repair();
        repair2.setOwner(testOwner);
        repair2.setProperty(testProperty);
        repair2.setSubmissionDate(LocalDateTime.now());
        repair2.setRepairStatus(RepairStatus.PENDING);
        repair2.setRepairType(RepairType.PAINTING);

        entityManager.getTransaction().begin();
        entityManager.persist(repair1);
        entityManager.persist(repair2);
        entityManager.getTransaction().commit();

        List<Repair> repairs = repairRepository.findAll();
        assertEquals(2, repairs.size());
    }

    @Test
    public void testDeleteById() {
        Repair repair = new Repair();
        repair.setOwner(testOwner);
        repair.setProperty(testProperty);
        repair.setSubmissionDate(LocalDateTime.now());
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setRepairType(RepairType.PAINTING);

        entityManager.getTransaction().begin();
        entityManager.persist(repair);
        entityManager.getTransaction().commit();

        boolean isDeleted = repairRepository.deleteById(repair.getId());
        assertTrue(isDeleted);

        Optional<Repair> foundRepair = repairRepository.findById(repair.getId());
        assertFalse(foundRepair.isPresent());
    }

    @Test
    public void testSafeDelete() {
        Repair repair = new Repair();
        repair.setOwner(testOwner);
        repair.setProperty(testProperty);
        repair.setSubmissionDate(LocalDateTime.now());
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setRepairType(RepairType.PAINTING);

        entityManager.getTransaction().begin();
        entityManager.persist(repair);
        entityManager.getTransaction().commit();

        boolean isSafelyDeleted = repairRepository.safeDelete(repair);
        assertTrue(isSafelyDeleted);

        Optional<Repair> foundRepair = repairRepository.findById(repair.getId());
        assertTrue(foundRepair.isPresent());
        assertTrue(foundRepair.get().isDeleted());
    }

    @Test
    public void testFindRepairsByOwner() {
        Repair repair = new Repair();
        repair.setOwner(testOwner);
        repair.setProperty(testProperty);
        repair.setSubmissionDate(LocalDateTime.now());
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setRepairType(RepairType.PAINTING);

        entityManager.getTransaction().begin();
        entityManager.persist(repair);
        entityManager.getTransaction().commit();

        List<Repair> repairs = repairRepository.findRepairsByOwner(testOwner);
        assertEquals(1, repairs.size());
        assertEquals(testOwner, repairs.get(0).getOwner());
    }

    @Test
    public void testFindPendingRepairs() {
        Repair repair = new Repair();
        repair.setOwner(testOwner);
        repair.setProperty(testProperty);
        repair.setSubmissionDate(LocalDateTime.now());
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setRepairType(RepairType.PAINTING);

        entityManager.getTransaction().begin();
        entityManager.persist(repair);
        entityManager.getTransaction().commit();

        List<Repair> pendingRepairs = repairRepository.findPendingRepairs();
        assertEquals(1, pendingRepairs.size());
        assertEquals(RepairStatus.PENDING, pendingRepairs.get(0).getRepairStatus());
    }

    @Test
    public void testFindPendingRepairsByOwner() {
        Repair repair = new Repair();
        repair.setOwner(testOwner);
        repair.setProperty(testProperty);
        repair.setSubmissionDate(LocalDateTime.now());
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setRepairType(RepairType.PAINTING);
        repair.setProposedCost(BigDecimal.TEN);

        entityManager.getTransaction().begin();
        entityManager.persist(repair);
        entityManager.getTransaction().commit();

        List<Repair> pendingRepairs = repairRepository.findPendingRepairsByOwner(testOwner);
        assertEquals(1, pendingRepairs.size());
        assertEquals(RepairStatus.PENDING, pendingRepairs.get(0).getRepairStatus());
        assertEquals(testOwner, pendingRepairs.get(0).getOwner());
    }

    @Test
    public void testFindRepairsByPropertyId() {
        Repair repair = new Repair();
        repair.setOwner(testOwner);
        repair.setProperty(testProperty);
        repair.setSubmissionDate(LocalDateTime.now());
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setRepairType(RepairType.PAINTING);

        entityManager.getTransaction().begin();
        entityManager.persist(repair);
        entityManager.getTransaction().commit();

        List<Repair> repairs = repairRepository.findRepairsByPropertyId(testProperty);
        assertEquals(1, repairs.size());
        assertEquals(testProperty, repairs.get(0).getProperty());
    }

    @Test
    public void testFindInProgressRepairs() {
        Repair repair = new Repair();
        repair.setOwner(testOwner);
        repair.setProperty(testProperty);
        repair.setSubmissionDate(LocalDateTime.now());
        repair.setRepairStatus(RepairStatus.INPROGRESS);
        repair.setRepairType(RepairType.PAINTING);
        

        entityManager.getTransaction().begin();
        entityManager.persist(repair);
        entityManager.getTransaction().commit();

        List<Repair> inProgressRepairs = repairRepository.findInProgressRepairs();
        assertEquals(1, inProgressRepairs.size());
        assertEquals(RepairStatus.INPROGRESS, inProgressRepairs.get(0).getRepairStatus());
    }

    @Test
    public void testFindAcceptedRepairs() {
        Repair repair = new Repair();
        repair.setOwner(testOwner);
        repair.setProperty(testProperty);
        repair.setSubmissionDate(LocalDateTime.now());
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setAcceptanceStatus(true);
        repair.setRepairType(RepairType.PAINTING);

        entityManager.getTransaction().begin();
        entityManager.persist(repair);
        entityManager.getTransaction().commit();

        List<Repair> acceptedRepairs = repairRepository.findAcceptedRepairs();
        assertEquals(1, acceptedRepairs.size());
        assertTrue(acceptedRepairs.get(0).getAcceptanceStatus());
    }

    @Test
    public void testFindRepairsByDates() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        Repair repair = new Repair();
        repair.setOwner(testOwner);
        repair.setProperty(testProperty);
        repair.setSubmissionDate(LocalDateTime.of(2023, 6, 15, 12, 0));
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setRepairType(RepairType.PAINTING);

        entityManager.getTransaction().begin();
        entityManager.persist(repair);
        entityManager.getTransaction().commit();

        List<Repair> repairs = repairRepository.findRepairsByDates(startDate, endDate, testOwner);
        assertEquals(1, repairs.size());
        assertEquals(testOwner, repairs.get(0).getOwner());
    }
}
