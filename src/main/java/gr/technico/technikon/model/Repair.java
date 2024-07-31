package gr.technico.technikon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table (name = "repair")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Repair implements Serializable {

    public Repair(Owner ownerVat, Property propertyId, RepairType repairType, String shortDescription, LocalDateTime submissionDate, String description, LocalDateTime proposedStartDate, LocalDateTime proposedEndDate, BigDecimal proposedCost, Boolean acceptanceStatus, RepairStatus repairStatus, LocalDateTime actualStartDate, LocalDateTime actualEndDate) {
        this.ownerVat = ownerVat;
        this.propertyId = propertyId;
        this.repairType = repairType;
        this.shortDescription = shortDescription;
        this.submissionDate = submissionDate;
        this.description = description;
        this.proposedStartDate = proposedStartDate;
        this.proposedEndDate = proposedEndDate;
        this.proposedCost = proposedCost;
        this.acceptanceStatus = acceptanceStatus;
        this.repairStatus = repairStatus;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
    }
    
    
    
    @Id
    @Column(name = "repair_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairId;
    
    @ManyToOne
    @JoinColumn(name = "owner_vat", referencedColumnName = "vat", nullable = false)
    private Owner ownerVat;
    
    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "property_id", nullable = false)
    private Property propertyId;
    
    @Column(name = "repair_type", nullable = false)
    private RepairType repairType;
    
    @Column(name = "short_description", length = 100, nullable = false)
    private String shortDescription;
    
    @Column(name = "submission_date", nullable = false)    
    private LocalDateTime submissionDate;
    
    @Column(name = "description", length = 200, nullable = false)
    private String description;
    
    @Column(name = "proposed_start__date", nullable = false)    
    private LocalDateTime proposedStartDate;
    
    @Column(name = "proposed_end__date", nullable = false)    
    private LocalDateTime proposedEndDate;
    
    @Column(name = "proposed_cost", nullable = false)
    private BigDecimal proposedCost;
    
    @Column(name = "acceptance_status", nullable = false)
    private Boolean acceptanceStatus;
    
    @Column(name = "repair_status", nullable = false)
    private RepairStatus repairStatus;
    
    @Column(name = "actual_start__date", nullable = false)    
    private LocalDateTime actualStartDate;
    
    @Column(name = "actual_end__date", nullable = false)    
    private LocalDateTime actualEndDate;
}
