package gr.technico.technikon.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "property")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Property implements Serializable {
    
    @Id
    @Column(name = "property_id")
    @Size(min = 20, max = 20, message = "Property id must contain 20 characters.")
    private String propertyId;
    
    @Column(name = "property_address", length = 50, nullable = false)
    private String propertyAddress;
    
    @Column(name = "construction_year", length = 4, nullable = false)
    private int constructionYear;
    
    @Column(name = "property_type", nullable = false)
    private PropertyType propertyType;
    
    @ManyToOne
    @JoinColumn(name = "owner_vat", referencedColumnName = "vat" ,nullable = false)
    private Owner ownerVat;
}
