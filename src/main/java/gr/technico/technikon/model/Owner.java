package gr.technico.technikon.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "owner")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Owner implements Serializable {
    
    @Id
    @Column(name = "VAT")
    @Size(min = 9, max = 9, message = "Vat must contain 9 characters.")
    private String vat;
    
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    
    @Column(name = "surname", length = 50, nullable = false)
    private String surname;
    
    @Column(name = "address",length = 50, nullable = false)
    private String address;
    
    @Column(name = "phoneNumber", length = 20, nullable = false)    
    private String phoneNumber;
    
    @Column(name = "email", length = 100, nullable = false, unique = true)
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    private String email;
    
    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;
    
    @Column(name = "password", length = 50, nullable = false)
    private String password;    
}
