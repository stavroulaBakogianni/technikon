package gr.technico.technikon;

import gr.technico.technikon.services.OwnerService;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.ui.MainMenuUI;

public class Technikon {

    public static void main(String[] args) {
        OwnerRepository ownerRepository = new OwnerRepository(JpaUtil.getEntityManager());
        OwnerService ownerService = new OwnerService(ownerRepository);
        MainMenuUI mainMenuUI = new MainMenuUI(ownerService);

        mainMenuUI.run();
    }
}
