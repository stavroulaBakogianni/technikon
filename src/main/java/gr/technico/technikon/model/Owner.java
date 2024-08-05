package gr.technico.technikon.model;

import java.util.List;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @Size(max = 50)
    private String address;

    @Column(name = "phone_number", length = 14)
    private String phoneNumber;

    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Size(min = 8, max = 50)
    @NotNull
    private String password;

    @NotNull
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> propertyList;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Repair> repairList;

    @Override
    public String toString() {
        return "Owner {"
                + "\n    ID = " + id
                + "\n    VAT = " + vat
                + "\n    Name = " + name
                + "\n    Surname = " + surname
                + "\n    Address = " + address
                + "\n    Phone Number = " + phoneNumber
                + "\n    Email = " + email
                + "\n    Username = " + username
                + "\n}";
    }
}
