package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RepairService {

    Repair createRepair(RepairType repairType, String shortDescription,
            String description, Owner owner, Property property);

    void updateRepairType(Long id, RepairType repairType);

    void updshortDesc(Long id, String shortDescription);

    void updDesc(Long id, String description);

    void updCostDates(Long id, BigDecimal proposedCost, LocalDateTime proposedStartDate, LocalDateTime proposedEndDateTime);

    void updAcceptance(Long id, int response) throws Exception;

    void updComplete(Long id);

    void updateStatus(Long id);

    Long saveRepair(Repair repair) throws CustomException;

    List<Repair> getRepairs() throws CustomException;

    public List<Repair> getPendingRepairs() throws CustomException;

    public List<Repair> getPendingRepairsByOwner(Owner owner) throws CustomException;

    public List<Repair> getInProgressRepairs() throws CustomException;

    List<Repair> findRepairsByOwner(Owner owner) throws CustomException;

    public List<Repair> getRepairByPropertyId(Property property)throws CustomException;

    public List<Repair> getAcceptedRepairs() throws CustomException;

    List<Repair> findRepairsByDate(String date, Owner owner);

    List<Repair> findRepairsByRangeOfDates(String startDate, String endDate, Owner owner);

    boolean deletePermantlyById(Long id);

    boolean deleteSafely(Long id);
    
    Optional<Repair> findRepairById(Long id);

    void validateType(int repairType) throws CustomException;

    public void validateDesc(String description) throws CustomException;

    public void validateShortDesc(String shortDescription) throws CustomException;

    public RepairType checkType(int repairType) throws CustomException;

}
