package gr.technico.technikon.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Property implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 20, max = 20, message = "E9 must contain 20 characters.")
    @NotNull
    @Column(unique = true)
    private String e9;

    @Column(name = "property_address", length = 50)
    private String propertyAddress;

    @Digits(integer = 4, fraction = 0)
    @Column(name = "construction_year")
    private int constructionYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false)
    private PropertyType propertyType;

    @ManyToOne
    @JoinColumn(name = "owner_vat", referencedColumnName = "vat", nullable = false)
    private Owner owner;

    @OneToMany(mappedBy = "property")
    private List<Repair> repairs;
}
