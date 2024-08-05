package gr.technico.technikon.services.importfiles;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.repositories.PropertyRepository;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class PropertyCSVImporter implements FilesImporter {

    @Override
    public void importFile(String filePath) throws IOException, OutOfMemoryError, FileNotFoundException {

        PropertyRepository propertyRepository = new PropertyRepository(JpaUtil.getEntityManager());
        OwnerRepository ownerRepository = new OwnerRepository(JpaUtil.getEntityManager());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                PropertyCSVImporter.class.getClassLoader().getResourceAsStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length != 5) {
                    //The line is malformed, skip it
                    continue;
                }

                String e9 = fields[0];
                String propertyAddress = fields[1];

                int constructionYear;
                try {
                    constructionYear = Integer.parseInt(fields[2]);
                } catch (NumberFormatException e) {
                    // Invalid CSV format: construction year must be an integer, skip line
                    continue;
                }
                PropertyType propertyType;
                try {
                    propertyType = PropertyType.valueOf(fields[3]);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid property type, skip line
                    continue;
                }
                String ownerVat = fields[4];

                Optional<Owner> ownerOptional = ownerRepository.findByVat(ownerVat);
                if (!ownerOptional.isPresent()) {
                    // Owner not found, skip line
                    continue;
                }
                Owner owner = ownerOptional.get();

                Property property = new Property();
                property.setE9(e9);
                property.setPropertyAddress(propertyAddress);
                property.setConstructionYear(constructionYear);
                property.setPropertyType(propertyType);
                property.setOwner(owner);

                propertyRepository.save(property);
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
