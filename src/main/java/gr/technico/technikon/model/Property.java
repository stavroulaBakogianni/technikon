package gr.technico.technikon.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Column(name = "property_type", nullable = false)
    private PropertyType propertyType;

    @ManyToOne
    private Owner ownerId;
}
