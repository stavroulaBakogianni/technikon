package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.repositories.OwnerRepository;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Service that manages owner functionalities.
 */
public class OwnerService implements OwnerServiceInterface {

    private final OwnerRepository ownerRepository;

    /**
     * Constructs a new OwnerService with the given OwnerRepository
     *
     * @param ownerRepository
     */
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    /**
     * Creates a new owner with the given details
     *
     * @param vat
     * @param name
     * @param surname
     * @param address
     * @param phoneNumber
     * @param email
     * @param username
     * @param password
     * @throws CustomException if any validation fails
     */
    @Override
    public void createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String username, String password)
            throws CustomException {
        validateVat(vat);
        validateName(name);
        validateSurname(surname);
        validatePhone(phoneNumber);
        validateEmail(email);
        validatePassword(password);

        checkVat(vat);
        checkEmail(email);
        checkUsername(username);

        Owner owner = new Owner();
        owner.setVat(vat);
        owner.setAddress(address);
        owner.setName(name);
        owner.setSurname(surname);
        owner.setPhoneNumber(phoneNumber);
        owner.setUsername(username);
        owner.setPassword(password);
        owner.setEmail(email);

        save(owner);
    }

    /**
     * Searches Owner by its VAT
     *
     * @param vat
     * @return an Optional containing the found Owner, or an empty Optional if
     * no Owner was found
     */
    @Override
    public Optional<Owner> searchOwnerByVat(String vat) {
        return ownerRepository.findByVat(vat);
    }

    /**
     * Searches Owner by its email
     *
     * @param email
     * @return an Optional containing the found Owner, or an empty Optional if
     * no Owner was found
     */
    @Override
    public Optional<Owner> searchOwnerByEmail(String email) {
        return ownerRepository.findByEmail(email);
    }

    /**
     * Updates the address of an owner identified by VAT
     *
     * @param vat
     * @param address
     * @throws CustomException if the owner is not found or the update fails
     */
    @Override
    public void updateOwnerAddress(String vat, String address) throws CustomException {
        Owner owner = getOwnerByVat(vat);
        owner.setAddress(address);
        save(owner);
    }

    /**
     * Updates the email of an owner identified by VAT
     *
     * @param vat
     * @param email
     * @throws CustomException if the email is invalid, already exists, or the
     * update fails
     */
    @Override
    public void updateOwnerEmail(String vat, String email) throws CustomException {
        Owner owner = getOwnerByVat(vat);
        validateEmail(email);
        if (!email.equals(owner.getEmail())) {
            checkEmail(email);
            owner.setEmail(email);
        }
        save(owner);
    }

    /**
     * Updates the password of an owner identified by VAT
     *
     * @param vat
     * @param password
     * @throws CustomException if the password is invalid or the update fails
     */
    @Override
    public void updateOwnerPassword(String vat, String password) throws CustomException {
        Owner owner = getOwnerByVat(vat);
        validatePassword(password);
        owner.setPassword(password);
        save(owner);
    }

    /**
     * Permanently deletes an owner by VAT
     *
     * @param vat
     * @return true if the owner was deleted, false otherwise
     */
    @Override
    public boolean deleteOwnerPermanently(String vat) {
        return ownerRepository.deletePermanentlyByVat(vat);
    }

    /**
     * Soft deletes an owner by VAT, marking the owner as deleted
     *
     * @param vat
     * @return true if the owner was marked as deleted, false otherwise
     */
    @Override
    public boolean deleteOwnerSafely(String vat) {
        try {
            Owner owner = getOwnerByVat(vat);
            owner.setDeleted(true);
            save(owner);

            return true;
        } catch (CustomException e) {
            return false;
        }
    }

    /**
     * Verifies the owner's credentials
     *
     * @param username
     * @param password
     * @return an Optional containing the VAT of the verified owner
     * @throws CustomException if the username or password is null/blank or
     * invalid
     */
    @Override
    public Optional<String> verifyOwner(String username, String password) throws CustomException {
        if (username == null || username.isBlank()) {
            throw new CustomException("Username cannot be null or blank.");
        }
        if (password == null || password.isBlank()) {
            throw new CustomException("Password cannot be null or blank.");
        }

        Owner owner = ownerRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new CustomException("Invalid username or password."));
        return Optional.of(owner.getVat());
    }

    /**
     * Validates the VAT.
     *
     * @param vat
     * @throws CustomException if the VAT is null or not exactly 9 characters
     *
     */
    @Override
    public void validateVat(String vat) throws CustomException {
        if (vat == null || vat.length() != 9) {
            throw new CustomException("VAT must be exactly 9 characters.");
        }
    }

    /**
     * Validates the name
     *
     * @param name
     * @throws CustomException if the name is null or blank
     */
    @Override
    public void validateName(String name) throws CustomException {
        if (name == null || name.isBlank()) {
            throw new CustomException("Name cannot be null or blank.");
        }
    }

    /**
     * Validates the surname
     *
     * @param surname
     * @throws CustomException if the surname is null or blank
     */
    @Override
    public void validateSurname(String surname) throws CustomException {
        if (surname == null || surname.isBlank()) {
            throw new CustomException("Surname cannot be null or blank.");
        }
    }

    /**
     * Validates the password
     *
     * @param password
     * @throws CustomException if the password is less than 8 characters long
     */
    @Override
    public void validatePassword(String password) throws CustomException {
        if (password.length() < 8) {
            throw new CustomException("Password must be at least 8 characters.");
        }
    }

    /**
     * Validates the phone number
     *
     * @param phone
     * @throws CustomException if the phone number is more than 14 characters
     * long or contains non-numeric characters
     */
    @Override
    public void validatePhone(String phone) throws CustomException {
        if (phone.length() > 14) {
            throw new CustomException("Phone number must be at most 14 characters.");
        }
        if (!phone.matches("\\d+")) {
            throw new CustomException("Phone number must contain only numeric characters.");
        }
    }

    /**
     * Validates the email
     *
     * @param email
     * @throws CustomException if the email is invalid
     */
    @Override
    public void validateEmail(String email) throws CustomException {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        if (email == null || !emailPattern.matcher(email).matches()) {
            throw new CustomException("Invalid email format.");
        }
    }

    /**
     * Checks if the VAT already exists
     *
     * @param vat
     * @throws CustomException if the VAT already exists
     */
    @Override
    public void checkVat(String vat) throws CustomException {
        if (ownerRepository.findByVat(vat).isPresent()) {
            throw new CustomException("VAT already exists. If you have deleted your account, contact the admin!");
        }
    }

    /**
     * Checks if the email already exists
     *
     * @param email
     * @throws CustomException if the email already exists
     */
    @Override
    public void checkEmail(String email) throws CustomException {
        if (ownerRepository.findByEmail(email).isPresent()) {
            throw new CustomException("Email already exists.");
        }
    }

    /**
     * Checks if the username already exists
     *
     * @param username
     * @throws CustomException if the username already exists
     */
    @Override
    public void checkUsername(String username) throws CustomException {
        if (ownerRepository.findByUsername(username).isPresent()) {
            throw new CustomException("Username already exists.");
        }
    }

    /**
     * Saves the given Owner
     *
     * @param owner
     * @throws CustomException if the save operation fails
     */
    private void save(Owner owner) throws CustomException {
        try {
            ownerRepository.save(owner);
        } catch (Exception e) {
            throw new CustomException("Failed to save owner details: " + e.getMessage());
        }
    }

    /**
     * Retrieves the Owner by VAT
     *
     * @param vat
     * @return the Owner
     * @throws CustomException if the Owner with the given VAT is not found
     */
    private Owner getOwnerByVat(String vat) throws CustomException {
        return ownerRepository.findByVat(vat)
                .orElseThrow(() -> new CustomException("Owner with the given VAT number not found."));
    }
}
