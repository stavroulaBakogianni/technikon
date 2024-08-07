package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    List<Repair> getRepairs();

    public List<Repair> getPendingRepairs();

    public List<Repair> getPendingRepairsByOwner(Owner owner);

    public List<Repair> getInProgressRepairs();

    List<Repair> findRepairByUserId(Owner owner);

    public List<Repair> getRepairByPropertyId(Property property);

    public List<Repair> getAcceptedRepairs();

    List<Repair> findRepairsByDate(String date);

    List<Repair> findRepairsByRangeOfDates(String startDate, String endDate);

    boolean deletePermantlyById(Long id);

    boolean deleteSafely(Long id);

    void validateType(int repairType) throws CustomException;

    public void validateDesc(String description) throws CustomException;

    public void validateShortDesc(String shortDescription) throws CustomException;

    public RepairType checkType(int repairType) throws CustomException;

}
