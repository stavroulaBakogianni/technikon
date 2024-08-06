package gr.technico.technikon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Repair implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "repair_type", nullable = false)
    private RepairType repairType;

    @Column(name = "short_description", length = 100)
    private String shortDescription;

    @PastOrPresent
    @NotNull
    @Column(name = "submission_date")
    private LocalDateTime submissionDate;

    @Size(max = 400)
    private String description;

    @FutureOrPresent
    @Column(name = "proposed_start_date")
    private LocalDateTime proposedStartDate;

    @FutureOrPresent
    @Column(name = "proposed_end_date")
    private LocalDateTime proposedEndDate;

    @DecimalMin(value = "0.0")
    @Column(name = "proposed_cost")
    private BigDecimal proposedCost;

    @Column(name = "acceptance_status")
    private Boolean acceptanceStatus;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "repair_status")
    private RepairStatus repairStatus = RepairStatus.PENDING;

    @FutureOrPresent
    @Column(name = "actual_start_date")
    private LocalDateTime actualStartDate;

    @FutureOrPresent
    @Column(name = "actual_end_date")
    private LocalDateTime actualEndDate;
    
    @NotNull
    private boolean isDeleted = false;
    
    @ManyToOne
    private Owner owner;

    @ManyToOne
    private Property property;
}
