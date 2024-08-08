package gr.technico.technikon;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.services.OwnerServiceImpl;
import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.services.importfiles.OwnerCSVImporter;
import gr.technico.technikon.services.importfiles.PropertyCSVImporter;
import gr.technico.technikon.services.importfiles.RepairCSVImporter;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.repositories.PropertyRepository;
import gr.technico.technikon.repositories.RepairRepository;
import gr.technico.technikon.services.PropertyServiceImpl;
import gr.technico.technikon.services.RepairServiceImpl;
import gr.technico.technikon.ui.MainMenuUI;
import java.io.IOException;

public class Technikon {

    public static void main(String[] args) throws CustomException, IOException {

        OwnerCSVImporter ownerCSVImporter = new OwnerCSVImporter();
        ownerCSVImporter.importFile("csv_files/owners.csv");
        PropertyCSVImporter propertyCSVImporter = new PropertyCSVImporter();
        propertyCSVImporter.importFile("csv_files/properties.csv");
        RepairCSVImporter repairCSVImporter = new RepairCSVImporter();
        repairCSVImporter.importFile("csv_files/repairs.csv");

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
