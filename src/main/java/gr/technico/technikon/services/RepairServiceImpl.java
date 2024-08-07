package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairStatus;
import gr.technico.technikon.model.RepairType;
import gr.technico.technikon.repositories.RepairRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RepairServiceImpl implements RepairService {

    private final RepairRepository repairRepository;

    public RepairServiceImpl(RepairRepository repairRepository) {
        this.repairRepository = repairRepository;
    }

    @Override
    public Repair createRepair(RepairType repairType, String shortDescription,
            String description, Owner owner, Property property) {
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

    @Override
    public void updateRepairType(Long id, RepairType repairType) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (repairType != null) {
            repairFound.setRepairType(repairType);
        }
        repairRepository.save(repairFound);
    }

    @Override
    public void updshortDesc(Long id, String shortDescription) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (shortDescription != null) {
            repairFound.setShortDescription(shortDescription);
        }
        repairRepository.save(repairFound);
    }

    @Override
    public void updDesc(Long id, String description) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (description != null) {
            repairFound.setDescription(description);
        }
        repairRepository.save(repairFound);
    }

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

    @Override
    public void updAcceptance(Long id, int response) {
        Optional<Repair> repair = repairRepository.findById(id);
        if (repair.isEmpty()) {
            System.out.println("Repair not found");
            return;
        }
        Repair repairFound = repair.get();
        if (response == 1) {
            repairFound.setAcceptanceStatus(Boolean.TRUE);
        } else {
            repairFound.setAcceptanceStatus(Boolean.FALSE);
            repairFound.setRepairStatus(RepairStatus.DECLINED);
        }
        try {
            repairRepository.save(repairFound);
        } catch (Exception e) {
            throw new Exception("Failed to update acceptance status");
        }
    }

    @Override
    public void updComplete(Long id) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        repairFound.setActualEndDate(LocalDateTime.now());
        repairFound.setRepairStatus(RepairStatus.COMPLETE);
        repairRepository.save(repairFound);
    }

    @Override
    public Long saveRepair(Repair repair) throws CustomException {
        repairRepository.save(repair);
        return repair.getId();
    }

    @Override
    public List<Repair> getRepairs() {
        return repairRepository.findAll();
    }

    @Override
    public List<Repair> getPendingRepairs() {
        return repairRepository.findPendingRepairs();
    }

    @Override
    public List<Repair> findRepairByUserId(Owner owner) {
        return repairRepository.findRepairsByUserId(owner);
    }

    @Override
    public Repair findRepairByDate() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteSafely(Long id) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        repairFound.setDeleted(true);
        repairRepository.save(repairFound);
    }

    // Validations
    @Override
    public void validateDesc(String description) throws CustomException {
        if (description == null || description.length() > 400 || description.isBlank()) {
            throw new CustomException("Description cannot be empty nor exceed 400 characters.");
        }
    }

    @Override
    public void validateShortDesc(String shortDescription) throws CustomException {
        if (shortDescription == null || shortDescription.length() > 100 || shortDescription.isBlank()) {
            throw new CustomException("Short Description cannot be empty nor exceed 100 characters.");
        }
    }

    public void validateType(int repairType) throws CustomException {
        if (repairType < 1 || repairType > 5) {
            throw new CustomException("Invalid input. Please enter a number between 1-5.");
        }
    }

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

}
