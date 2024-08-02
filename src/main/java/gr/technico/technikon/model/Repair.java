package gr.technico.technikon.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @Size(min = 0, max = 100)
    private String shortDescription;

    @PastOrPresent
    @NotNull
    private LocalDateTime submissionDate;

    @Size(max = 400)
    private String description;

    @FutureOrPresent
    private LocalDateTime proposedStartDate;

    @FutureOrPresent
    private LocalDateTime proposedEndDate;

    @DecimalMin(value = "0.0")
    private BigDecimal proposedCost;

    private Boolean acceptanceStatus;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RepairStatus repairStatus = RepairStatus.PENDING;

    @FutureOrPresent
    private LocalDateTime actualStartDate;

    @FutureOrPresent
    private LocalDateTime actualEndDate;

    @ManyToOne
    private Owner owner;

    @ManyToOne
    private Property property;
}
