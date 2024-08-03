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
public class Owner implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 9, max = 9, message = "Vat must contain 9 characters.")
    @Column(nullable = false, unique = true)
    private String vat;

    @Size(min = 1, max = 50)
    @NotNull
    private String name;

    @Size(min = 1, max = 50)
    @NotNull
    private String surname;

    @Size(min = 1, max = 50)
    private String address;

    @Size(max = 14)
    private String phoneNumber;

    @Email
    @NotNull
    @Column(unique = true)
//    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Size(min = 1, max = 50)
    @NotNull
    private String password;

    @OneToMany(mappedBy = "owner")
    private List<Property> propertyList;
}
