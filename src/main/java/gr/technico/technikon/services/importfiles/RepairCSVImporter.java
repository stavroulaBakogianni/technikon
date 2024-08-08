package gr.technico.technikon.services.importfiles;

import gr.technico.technikon.jpa.JpaUtil;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairStatus;
import gr.technico.technikon.model.RepairType;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.repositories.PropertyRepository;
import gr.technico.technikon.repositories.RepairRepository;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class RepairCSVImporter implements FilesImporter {

    @Override
    public void importFile(String filePath) throws IOException, OutOfMemoryError, FileNotFoundException {

        PropertyRepository propertyRepository = new PropertyRepository(JpaUtil.getEntityManager());
        OwnerRepository ownerRepository = new OwnerRepository(JpaUtil.getEntityManager());
        RepairRepository repairRepository = new RepairRepository(JpaUtil.getEntityManager());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                PropertyCSVImporter.class.getClassLoader().getResourceAsStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (!(fields.length >= 11 && fields.length <= 13)) {
                    //The line is malformed, skip it
                    continue;
                }

                Long ownerId;
                try {
                    ownerId = Long.parseLong(fields[0]);
                } catch (NumberFormatException e) {
                    // Invalid CSV format: ownerId must be Long, skip line
                    continue;
                }

                Long propertyId;
                try {
                    propertyId = Long.parseLong(fields[1]);
                } catch (NumberFormatException e) {
                    // Invalid CSV format: propertyId must be Long, skip line
                    continue;
                }

                RepairType repairType;
                try {
                    repairType = RepairType.valueOf(fields[2]);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid repair type, skip line
                    continue;
                }

                String shortDiscription = fields[3];

                LocalDateTime submitionDate;
                try {
                    submitionDate = LocalDateTime.parse(fields[4], formatter);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid date format, skip line
                    continue;
                }

                String description = fields[5];
                LocalDateTime proposedStartDate;
                try {
                    proposedStartDate = LocalDateTime.parse(fields[6], formatter);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid date format, skip line
                    continue;
                }

                LocalDateTime proposedEndDate;
                try {
                    proposedEndDate = LocalDateTime.parse(fields[7], formatter);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid date format, skip line
                    continue;
                }

                BigDecimal proposedCost;
                try {
                    proposedCost = new BigDecimal(fields[8]);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid cost format, skip line
                    continue;
                }

                boolean acceptanceStatus;
                try {
                    acceptanceStatus = Boolean.parseBoolean(fields[9]);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid status format, skip line
                    continue;
                }

                RepairStatus repairStatus;
                try {
                    repairStatus = RepairStatus.valueOf(fields[10]);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid repair status, skip line
                    continue;
                }

                LocalDateTime actualStartDate = null;
                if (fields.length >= 12) {
                    try {
                        actualStartDate = LocalDateTime.parse(fields[11], formatter);
                    } catch (IllegalArgumentException e) {
                        // Invalid CSV format: invalid date format, skip line
                        continue;
                    }
                }

                LocalDateTime actualEndDate = null;
                if (fields.length == 13) {
                    try {
                        actualEndDate = LocalDateTime.parse(fields[12], formatter);
                    } catch (IllegalArgumentException e) {
                        // Invalid CSV format: invalid date format, skip line
                        continue;
                    }
                }

                Optional<Owner> ownerOptional = ownerRepository.findById(ownerId);
                if (!ownerOptional.isPresent()) {
                    // Owner not found, skip line
                    continue;
                }
                Owner owner = ownerOptional.get();

                Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
                if (!propertyOptional.isPresent()) {
                    // Property not found, skip line
                    continue;
                }
                Property property = propertyOptional.get();

                Repair repair = new Repair();
                repair.setOwner(owner);
                repair.setProperty(property);
                repair.setRepairType(repairType);
                repair.setShortDescription(shortDiscription);
                repair.setSubmissionDate(submitionDate);
                repair.setDescription(description);
                repair.setProposedStartDate(proposedStartDate);
                repair.setProposedEndDate(proposedEndDate);
                repair.setProposedCost(proposedCost);
                repair.setAcceptanceStatus(acceptanceStatus);
                repair.setRepairStatus(repairStatus);
                repair.setActualStartDate(actualStartDate);
                repair.setActualEndDate(actualEndDate);

                repairRepository.save(repair);
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
