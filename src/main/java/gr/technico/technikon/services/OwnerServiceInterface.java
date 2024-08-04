package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;

import java.util.Optional;

public interface OwnerServiceInterface {
    void createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String username, String password)
        throws CustomException;

    Optional<Owner> searchOwnerByVat(String vat);

    Optional<Owner> searchOwnerByEmail(String email);
    
    void updateOwner(String vat, String address, String email, String password)
        throws CustomException;

    boolean deleteOwnerPermanently(String vat);

    boolean deleteOwnerSafely(String vat);
}