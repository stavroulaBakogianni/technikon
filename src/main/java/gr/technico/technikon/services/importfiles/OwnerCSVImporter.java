package gr.technico.technikon.services.importfiles;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.services.OwnerService;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class OwnerCSVImporter implements FilesImporter {

    @Override
    public void importFile(String filePath) throws IOException, OutOfMemoryError, FileNotFoundException {

        OwnerRepository ownerRepository = new OwnerRepository(JpaUtil.getEntityManager());
        OwnerService ownerService = new OwnerService(ownerRepository);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                OwnerCSVImporter.class.getClassLoader().getResourceAsStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] fields = line.split(",");

                    if (fields.length != 8) {
                        //The line is malformed, skip it
                        continue;
                    }

                    String vat = fields[0];
                    String name = fields[1];
                    String surname = fields[2];
                    String address = fields[3];
                    String phoneNumber = fields[4];
                    String email = fields[5];
                    String username = fields[6];
                    String password = fields[7];

                    ownerService.createOwner(vat, name, surname, address, phoneNumber, email, username, password);
                } catch (CustomException e) {
                    System.out.println("Owner doesn't pass validations, skip this line" + e.getMessage());
                }
            }
        } catch (OutOfMemoryError e) {
            System.out.println("Java run out of memory: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Filepath not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
