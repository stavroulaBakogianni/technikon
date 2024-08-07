package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;

import java.util.Optional;

public interface OwnerService {
    
    // Create Owner
    void createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String username, String password)
        throws CustomException;

    // Search Owner
    Optional<Owner> searchOwnerByVat(String vat);

    Optional<Owner> searchOwnerByEmail(String email);

    // Update Owner
    void updateOwnerAddress(String vat, String address) throws CustomException;

    void updateOwnerEmail(String vat, String email) throws CustomException;

    void updateOwnerPassword(String vat, String password) throws CustomException;

    // Delete Owner
    boolean deleteOwnerPermanently(String vat);

    boolean deleteOwnerSafely(String vat);
    
    // Verify Owner
    Optional<String> verifyOwner(String username, String password) throws CustomException;

    // Validations
    void validateVat(String vat) throws CustomException;

    void validateName(String name) throws CustomException;

    void validateSurname(String surname) throws CustomException;

    void validatePassword(String password) throws CustomException;

    void validatePhone(String phone) throws CustomException;

    void validateEmail(String email) throws CustomException;

    void checkVat(String vat) throws CustomException;

    void checkEmail(String email) throws CustomException;

    void checkUsername(String username) throws CustomException;
}
