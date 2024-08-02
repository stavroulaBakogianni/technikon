package gr.technico.technikon.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.*;
import javax.validation.constraints.NegativeOrZero.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NotNull
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
    private String email;

    @Size(min = 1, max = 50)
    @NotNull
    private String username;

    @Size(min = 1, max = 50)
    @NotNull
    private String password;
}
