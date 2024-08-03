package gr.technico.technikon.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.*;

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
    private String e9;
    
    @Size(max = 50)
    private String propertyAddress;
    
    @Digits(integer = 4, fraction = 0)
    private int constructionYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false)
    private PropertyType propertyType;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "vat")
    private Owner owner;

    @OneToMany(mappedBy = "property")
    private List<Repair> repairs;
}