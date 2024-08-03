/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairType;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface RepairService {

    Repair createRepair(RepairType repairType, String shortDescription,
//            LocalDateTime submissionDate, 
            String description,
//            RepairStatus repairStatus,
            Owner owner,
            Property property);

    Long saveRepair(Repair repair) throws CustomException;

    List<Repair> getRepairs();

    Repair findRepairByUserId();

    //By Date(submissionDate) or Range of dates (proposedStart..propposedEnd//actualStart..actualEnd
    Repair findRepairByDate();

}
