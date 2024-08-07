package gr.technico.technikon;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.services.OwnerServiceImpl;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.repositories.PropertyRepository;
import gr.technico.technikon.services.PropertyService;
import gr.technico.technikon.services.PropertyServiceImpl;
import gr.technico.technikon.ui.MainMenuUI;

public class Technikon {

    public static void main(String[] args) throws CustomException {
        OwnerRepository ownerRepository = new OwnerRepository(JpaUtil.getEntityManager());
        OwnerServiceImpl ownerService = new OwnerServiceImpl(ownerRepository);
        
        PropertyRepository propertyRepository = new PropertyRepository(JpaUtil.getEntityManager());
        PropertyServiceImpl propertyService = new PropertyServiceImpl(propertyRepository, ownerService);
        MainMenuUI mainMenuUI = new MainMenuUI(ownerService, propertyService);

        mainMenuUI.run();
    }
}
