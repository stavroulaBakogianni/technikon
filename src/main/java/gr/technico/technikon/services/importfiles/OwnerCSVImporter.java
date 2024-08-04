package gr.technico.technikon.services.importfiles;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.repositories.OwnerRepository;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Optional;

public class OwnerCSVImporter implements FilesImporter {

    @Override
    public void importFile(String filePath) throws IOException, OutOfMemoryError, FileNotFoundException {

        OwnerRepository ownerRepository = new OwnerRepository(JpaUtil.getEntityManager());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                OwnerCSVImporter.class.getClassLoader().getResourceAsStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 8) {
                    throw new IOException("Invalid CSV format: incorrect number of fields");
                }

                String vat = fields[0];
                String name = fields[1];
                String surname = fields[2];
                String address = fields[3];
                String phoneNumber = fields[4];
                String email = fields[5];
                String username = fields[6];
                String password = fields[7];

                Owner owner = new Owner();
                
                owner.setVat(vat);
                owner.setName(name);
                owner.setSurname(surname);
                owner.setAddress(address);
                owner.setPhoneNumber(phoneNumber);
                owner.setEmail(email);
                owner.setUsername(username);
                owner.setPassword(password);

                
                Optional<Owner> savedOwner = ownerRepository.save(owner);
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
