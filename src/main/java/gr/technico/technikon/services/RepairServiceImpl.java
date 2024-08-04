package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairStatus;
import gr.technico.technikon.model.RepairType;
import gr.technico.technikon.repositories.RepairRepository;
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
//            LocalDateTime submissionDate, 
            String description,
//            RepairStatus repairStatus,
            Owner owner, Property property) {
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
    public Repair deleteUserSafely(Long id) {
        //Repair repair2 = null ;
        Optional<Repair> repair1 = repairRepository.findById(id);
        Repair repair2 = repair1.get();
        System.out.println(repair2.getId());
        repair2.setIsDeleted(true);
        repairRepository.save(repair2);
        return repair2;
    }



}
