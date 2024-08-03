package gr.technico.technikon.model;

import java.util.List;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Owner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 9, max = 9)
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

    @Column(name = "phone_number", length = 14)
    private String phoneNumber;

    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Size(min = 1, max = 50)
    @NotNull
    private String password;

}