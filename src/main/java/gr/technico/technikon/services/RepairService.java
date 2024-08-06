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

public class RepairService implements RepairServiceInterface {

    private final RepairRepository repairRepository;

    public RepairService(RepairRepository repairRepository) {
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
    public void updGeneral(Long id, RepairType repairType, String shortDescription,
            String description) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (repairType != null) {
            repairFound.setRepairType(repairType);
        }
        if (shortDescription != null) {
            repairFound.setShortDescription(shortDescription);
        }
        if (description != null) {
            repairFound.setDescription(description);
        }
        repairFound.setSubmissionDate(LocalDateTime.now());
        repairFound.setRepairStatus(RepairStatus.PENDING);
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
        Repair repairFound = repair.get();
        if (response == 1) {
            repairFound.setAcceptanceStatus(Boolean.TRUE);
            repairFound.setRepairStatus(RepairStatus.INPROGRESS);
            repairFound.setActualStartDate(LocalDateTime.now());
        } else {
            repairFound.setAcceptanceStatus(Boolean.FALSE);
            repairFound.setRepairStatus(RepairStatus.DECLINED);
        }
        repairRepository.save(repairFound);
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
    public List<Repair> findRepairByUserId() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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

}
