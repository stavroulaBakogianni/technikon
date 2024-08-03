package gr.technico.technikon;

import javax.persistence.EntityManager;
import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairStatus;
import gr.technico.technikon.model.RepairType;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.repositories.PropertyRepository;
import gr.technico.technikon.repositories.RepairRepository;
import gr.technico.technikon.services.OwnerService;
import gr.technico.technikon.services.OwnerServiceImpl;
import gr.technico.technikon.services.PropertyService;
import gr.technico.technikon.services.PropertyServiceImpl;
import gr.technico.technikon.services.RepairService;
import gr.technico.technikon.services.RepairServiceImpl;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class Technikon {

    public static void main(String[] args) {

        log.info("CRM application starting...");
        
        EntityManager em = JpaUtil.getEntityManager();
        
        em.getTransaction().begin();
        
        OwnerRepository ownerRepository = new OwnerRepository(em);
        OwnerService ownerService = new OwnerServiceImpl(ownerRepository);
        
        PropertyRepository propertyRepository = new PropertyRepository(em);
        PropertyService propertyService = new PropertyServiceImpl(propertyRepository);
        
        RepairRepository repairRepository = new RepairRepository(em);
        RepairService repairService = new RepairServiceImpl(repairRepository);

        Owner owner1 = ownerService.createOwner("123456778", "Apostolis", "Tourlidas",
                "Agrinio", "6999999999", "asss@example.com", "apostolissss", "apostolis");
        Owner owner2 = ownerService.createOwner("123456779", "Apostolos", "Turlidas", 
                "Agrinio", "6989999999", "arss@example.com", "apostolisss", "apostoliis");
        em.persist(owner1);
        em.persist(owner2);

        Property property1 = propertyService.createProperty("54767446745476744674", "Agrinio", 1984, 
                PropertyType.MAISONETTE, owner1);
        Property property2 = propertyService.createProperty("54767346745476744674", "Agrinio", 1974, 
                PropertyType.APARTMENTBUILDING, owner2);
        em.persist(property1);
        em.persist(property2);
        
        Repair repair1 = repairService.createRepair(RepairType.PAINTING, "painting", 
                 "sdafasdkuHDIUQW", owner2, property2);
        em.persist(repair1);
        Repair repair2 = repairService.createRepair(RepairType.ELECTRICALWORK, "electricals", 
                 "sdafsdaedeasdkuHDIUQW", owner1, property1);
        em.persist(repair2);
        Repair repair3 = repairService.createRepair(RepairType.FRAMES, "frames", 
                 "sdafsdaedeasdkuHDIUQW", owner1, property1);
        em.persist(repair3);
        
        em.close();
    }
}
