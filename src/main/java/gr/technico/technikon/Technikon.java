package gr.technico.technikon;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.services.OwnerServiceImpl;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.repositories.PropertyRepository;
import gr.technico.technikon.repositories.RepairRepository;
import gr.technico.technikon.services.PropertyServiceImpl;
import gr.technico.technikon.services.RepairServiceImpl;
import gr.technico.technikon.ui.MainMenuUI;

public class Technikon {

    public static void main(String[] args) throws CustomException {
        OwnerRepository ownerRepository = new OwnerRepository(JpaUtil.getEntityManager());
        OwnerServiceImpl ownerService = new OwnerServiceImpl(ownerRepository);
        
        PropertyRepository propertyRepository = new PropertyRepository(JpaUtil.getEntityManager());
        PropertyServiceImpl propertyService = new PropertyServiceImpl(propertyRepository, ownerService);
        
        RepairRepository repairRepository = new RepairRepository(JpaUtil.getEntityManager());
        RepairServiceImpl repairService = new RepairServiceImpl(repairRepository, propertyRepository, ownerService);
        
        MainMenuUI mainMenuUI = new MainMenuUI(ownerService, propertyService, repairService);
        

        mainMenuUI.run();
    }
}
