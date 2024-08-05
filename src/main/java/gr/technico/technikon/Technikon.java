package gr.technico.technikon;

import gr.technico.technikon.services.OwnerService;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.services.importfiles.OwnerCSVImporter;
import gr.technico.technikon.services.importfiles.PropertyCSVImporter;
import gr.technico.technikon.services.importfiles.RepairCSVImporter;
import gr.technico.technikon.ui.MainMenuUI;
import java.io.IOException;

public class Technikon {

    public static void main(String[] args) throws IOException{

        OwnerCSVImporter ownerCSVImporter = new OwnerCSVImporter();
        ownerCSVImporter.importFile("csv_files/owners.csv");
        PropertyCSVImporter propertyCSVImporter = new PropertyCSVImporter();
        propertyCSVImporter.importFile("csv_files/properties.csv");
        RepairCSVImporter repairCSVImporter = new RepairCSVImporter();
        repairCSVImporter.importFile("csv_files/repairs.csv");

        OwnerRepository ownerRepository = new OwnerRepository(JpaUtil.getEntityManager());
        OwnerService ownerService = new OwnerService(ownerRepository);
        MainMenuUI mainMenuUI = new MainMenuUI(ownerService);
 
        mainMenuUI.run();
    }
}
