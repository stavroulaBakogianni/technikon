package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairStatus;
import gr.technico.technikon.model.RepairType;
import gr.technico.technikon.repositories.PropertyRepository;
import gr.technico.technikon.repositories.RepairRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepairServiceImpl implements RepairService {

    private final RepairRepository repairRepository;
    private final OwnerService ownerServiceInterface;
    private final PropertyRepository propertyRepository;

    public RepairServiceImpl(RepairRepository repairRepository, PropertyRepository propertyRepository, OwnerServiceImpl ownerService) {
        this.repairRepository = repairRepository;
                this.propertyRepository = propertyRepository;
        this.ownerServiceInterface = ownerService;
    }

    /**
     * Initializes a new Repair with the provided details, sets its submission
     * date to the current time, and assigns its status as PENDING.
     *
     * @param repairType
     * @param shortDescription
     * @param description
     * @param owner
     * @param property
     * @return The created Repair instance, which has been saved to the
     * repository.
     */
    @Override
    public Repair createRepair(RepairType repairType, String shortDescription,
            String description, Owner owner, Property property) throws CustomException{
        
        if (owner == null) {
            throw new CustomException("Owner id not inserted.");
        }
        if (property == null) {
            throw new CustomException("Property id not inserted.");
        }
        validateRepairType(repairType);
        validateShortDesc(shortDescription);
        validateDesc(description);
        Repair repair = new Repair();
        repair.setRepairType(repairType);
        repair.setShortDescription(shortDescription);
        repair.setDescription(description);
        repair.setSubmissionDate(LocalDateTime.now());
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setOwner(owner);
        repair.setProperty(property);
        repairRepository.save(repair);
        return repair;
    }

    /**
     * Updates the type of the repair identified by the given ID.
     *
     * @param id
     * @param repairType
     */
    @Override
    public void updateRepairType(Long id, RepairType repairType) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (repairType != null) {
            repairFound.setRepairType(repairType);
        }
        repairRepository.save(repairFound);
    }

    /**
     * Updates the short description of the repair identified by the given ID.
     *
     * @param id
     * @param shortDescription
     */
    @Override
    public void updshortDesc(Long id, String shortDescription) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (shortDescription != null) {
            repairFound.setShortDescription(shortDescription);
        }
        repairRepository.save(repairFound);
    }

    /**
     * Updates the detailed description of the repair identified by the given
     * ID.
     *
     * @param id
     * @param description
     */
    @Override
    public void updDesc(Long id, String description) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (description != null) {
            repairFound.setDescription(description);
        }
        repairRepository.save(repairFound);
    }

    /**
     * Updates the proposed cost, start date, and end date of the repair
     * identified by the given ID.
     *
     * @param id
     * @param proposedCost
     * @param proposedStartDate
     * @param proposedEndDateTime
     */
    @Override
    public void updCostDates(Long id, BigDecimal proposedCost, LocalDateTime proposedStartDate, LocalDateTime proposedEndDateTime) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (proposedCost != null) {
            repairFound.setProposedCost(proposedCost);
        }
        repairFound.setProposedStartDate(proposedStartDate);
        repairFound.setProposedEndDate(proposedEndDateTime);
        repairRepository.save(repairFound);
    }

    /**
     * Updates the acceptance status of the repair identified by the given ID.
     *
     * @param id
     * @param response
     */
    @Override
    public void updAcceptance(Long id, int response) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (response == 1) {
            repairFound.setAcceptanceStatus(Boolean.TRUE);
            //repairFound.setRepairStatus(RepairStatus.INPROGRESS);
            //repairFound.setActualStartDate(LocalDateTime.now());
        } else {
            repairFound.setAcceptanceStatus(Boolean.FALSE);
            repairFound.setRepairStatus(RepairStatus.DECLINED);
        }
        repairRepository.save(repairFound);
    }

    /**
     * Updates the status of the repair identified by the given ID to INPROGRESS
     * and sets the actual start date to the current time.
     *
     * @param id
     */
    @Override
    public void updateStatus(Long id) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();

        repairFound.setRepairStatus(RepairStatus.INPROGRESS);
        repairFound.setActualStartDate(LocalDateTime.now());

        repairRepository.save(repairFound);
    }

    /**
     * Marks the repair identified by the given ID as complete by setting its
     * status to COMPLETE and its actual end date to the current time.
     *
     * @param id
     */
    @Override
    public void updComplete(Long id) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        repairFound.setActualEndDate(LocalDateTime.now());
        repairFound.setRepairStatus(RepairStatus.COMPLETE);
        repairRepository.save(repairFound);
    }

    /**
     * Saves the given Repair to the repository.
     *
     * @param repair
     * @return
     * @throws CustomException
     */
    @Override
    public Long saveRepair(Repair repair) throws CustomException {
        repairRepository.save(repair);
        return repair.getId();
    }

    /**
     * Retrieves all repairs from the repository.
     *
     * @return A list of all Repair instances.
     */
    @Override
    public List<Repair> getRepairs() throws CustomException{
        List<Repair> repairs = repairRepository.findAll();
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found");
        } else {
            return repairs;
        }
    }

    /**
     * Retrieves all repairs with a status of PENDING from the repository.
     *
     * @return
     */
    @Override
    public List<Repair> getPendingRepairs() throws CustomException {
        List<Repair> repairs = repairRepository.findPendingRepairs();
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found.");
        } else {
            return repairs;
        }
    }

    /**
     * Retrieves all pending repairs for a given owner.
     *
     * @param owner
     * @return A list of all pending Repair instances associated with the given
     * owner.
     * @thows CustomExcepion.
     */
    @Override
    public List<Repair> getPendingRepairsByOwner(Owner owner) throws CustomException {
        List<Repair> repairs = repairRepository.findPendingRepairsByOwner(owner).stream()
                .filter(repair -> !repair.isDeleted())
                .collect(Collectors.toList());
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found");
        } else {
            return repairs;
        }
    }

    /**
     * Retrieves all repairs with a status of INPROGRESS from the repository.
     *
     * @return A list of all in-progress Repair instances.
     */
    @Override
    public List<Repair> getInProgressRepairs() throws CustomException {
        List<Repair> repairs = repairRepository.findInProgressRepairs();
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found");
        } else {
            return repairs;
        }
        
    }

    /**
     * Retrieves all repairs that have been accepted.
     *
     * @return A list of all accepted Repair instances.
     */
    @Override
    public List<Repair> getAcceptedRepairs() throws CustomException {
        List<Repair> repairs = repairRepository.findAcceptedRepairs();
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found");
        } else {
            return repairs;
        }
    }

    /**
     * Retrieves all repairs associated with a given owner.
     *
     * @param owner
     * @return A list of all Repair instances associated with the given owner.
     */
    @Override
    public List<Repair> findRepairsByOwner(Owner owner) throws CustomException{
        List<Repair> repairs = repairRepository.findRepairsByOwner(owner).stream()
                .filter(property -> !property.isDeleted())
                .collect(Collectors.toList());
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found");
        } else {
            return repairs;
        }
    }

    /**
     * Retrieves all repairs for a given owner on a specific date.
     *
     * @param date
     * @param owner
     *
     * @return A list of all {@link Repair} instances associated with the given
     * owner on the specified date. Returns an empty list if the date format is
     * invalid.
     */
    @Override
    public List<Repair> findRepairsByDate(String date, Owner owner) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + date);
            return Collections.emptyList();
        }

        LocalDateTime localDateTimeStart = localDate.atStartOfDay();
        LocalDateTime localDateTimeEnd = localDate.atTime(LocalTime.MAX);
        return repairRepository.findRepairsByDates(localDateTimeStart, localDateTimeEnd, owner);
    }

    /**
     * Retrieves all repairs for a given owner within a specified range of
     * dates.
     *
     * @param startDate
     * @param endDate
     * @param owner
     *
     * @return A list of all Repair instances associated with the given owner
     * within the specified date range. Returns an empty list if any of the date
     * formats are invalid
     */
    @Override
    public List<Repair> findRepairsByRangeOfDates(String startDate, String endDate, Owner owner) {
        LocalDate startLocalDate;
        LocalDate endLocalDate;

        try {
            startLocalDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
            endLocalDate = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format");
            return Collections.emptyList();
        }

        LocalDateTime localDateTimeStart = startLocalDate.atStartOfDay();
        LocalDateTime localDateTimeEnd = endLocalDate.atTime(LocalTime.MAX);
        return repairRepository.findRepairsByDates(localDateTimeStart, localDateTimeEnd, owner);
    }

    /**
     * Retrieves a repair by its ID.
     *
     * @param id
     *
     * @return An Optional containing the found Repair, or an empty Optional if
     * no repair is found.
     */
    @Override
    public Optional<Repair> findRepairById(Long id) {

        return repairRepository.findById(id);
    }

    /**
     * Permanently deletes a repair by its ID.
     *
     * @param id
     *
     * @return true if the repair was successfully deleted, false otherwise.
     */
    @Override
    public boolean deletePermantlyById(Long id) {
        return repairRepository.deleteById(id);
    }

    /**
     * Safely deletes a repair by its ID, checking if it exists first.
     *
     * @param id
     *
     * @return true if the repair was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteSafely(Long id) {
        Optional<Repair> repair = repairRepository.findById(id);
        if (repair.isEmpty()) {
            System.out.println("Repair no found");
            return false;
        }
        Repair repairFound = repair.get();
        return repairRepository.safeDelete(repairFound);
    }

    /**
     * Retrieves all repairs associated with a specific property.
     *
     * @param property
     *
     * @return A list of all Repair instances associated with the specified
     * property.
     */
    @Override
    public List<Repair> getRepairByPropertyId(Property property) throws CustomException{
        List<Repair> repairs = repairRepository.findRepairsByPropertyId(property).stream()
                .filter(repair -> !repair.isDeleted())
                .collect(Collectors.toList());
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found");
        } else {
            return repairs;
        }
    }

    /**
     * Validates the description of a repair. The description must not be null,
     * blank, or exceed 400 characters.
     *
     * @param description
     * @throws CustomException
     */
    @Override
    public void validateDesc(String description) throws CustomException {
        if (description == null || description.length() > 400 || description.isBlank()) {
            throw new CustomException("Description cannot be empty nor exceed 400 characters.");
        }
    }

    /**
     * Validates the short description of a repair. The short description must
     * not be null, blank, or exceed 100 characters.
     *
     * @param shortDescription
     * @throws CustomException
     */
    @Override
    public void validateShortDesc(String shortDescription) throws CustomException {
        if (shortDescription == null || shortDescription.length() > 100 || shortDescription.isBlank()) {
            throw new CustomException("Short Description cannot be empty nor exceed 100 characters.");
        }
    }

    /**
     * Validates the repair type based on an integer value. The value must be
     * between 1 and 5.
     *
     * @param repairType
     * @throws CustomException
     */
    @Override
    public void validateType(int repairType) throws CustomException {
        if (repairType < 1 || repairType > 5) {
            throw new CustomException("Invalid input. Please enter a number between 1-5.");
        }
    }

    /**
     * Determines the RepairType based on an integer value.
     *
     * @param repairType
     *
     * @return The corresponding RepairType.
     * @throws CustomException
     */
    @Override
    public RepairType checkType(int repairType) throws CustomException {
        switch (repairType) {
            case 1:
                return RepairType.PAINTING;
            case 2:
                return RepairType.INSULATION;
            case 3:
                return RepairType.FRAMES;
            case 4:
                return RepairType.PLUMBING;
            case 5:
                return RepairType.ELECTRICALWORK;
            default:
                throw new CustomException("Invalid input.");
        }
    }
     public void validateRepairType(RepairType repairType) throws CustomException {
        
         if (repairType != RepairType.valueOf("PAINTING") 
                 && repairType != RepairType.valueOf("INSULATION") 
                 && repairType != RepairType.valueOf("FRAMES") 
                 && repairType != RepairType.valueOf("PLUMBING") 
                 && repairType != RepairType.valueOf("ELECTRICALWORK") ){
            throw new CustomException("Invalid input.");
        }
            
            
    }

}
