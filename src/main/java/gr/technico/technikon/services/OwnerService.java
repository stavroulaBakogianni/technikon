package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.repositories.OwnerRepository;

import java.util.Optional;
import java.util.regex.Pattern;

public class OwnerService implements OwnerServiceInterface {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    // Create Owner
    @Override
    public void createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String username, String password)
            throws CustomException {
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

    // Search Owner
    @Override
    public Optional<Owner> searchOwnerByVat(String vat) {
        return ownerRepository.findByVat(vat);
    }

    @Override
    public Optional<Owner> searchOwnerByEmail(String email) {
        return ownerRepository.findByEmail(email);
    }

    // Update Owner
    @Override
    public void updateOwner(String vat, String address, String email, String password)
            throws CustomException {

        Owner owner = ownerRepository.findByVat(vat)
                .orElseThrow(() -> new CustomException("Owner with the given VAT number not found."));

        if (address != null && !address.isBlank()) {
            owner.setAddress(address);
        }

        if (email != null && !email.isBlank()) {
            validateEmail(email);
            if (!email.equals(owner.getEmail())) {
                try {
                    checkEmail(email);
                } catch (CustomException ex) {
                    throw new CustomException("Email already exists.");
                }
                owner.setEmail(email);
            }
        }

        if (password != null && !password.isBlank()) {
            validatePassword(password);
            owner.setPassword(password);
        }

        save(owner);
    }

    // Delete Owner
    public boolean deleteOwnerPermanently(String vat) {
        return ownerRepository.deletePermanentlyByVat(vat);
    }

    public boolean deleteOwnerSafely(String vat) {
        return ownerRepository.deleteSafelyByVat(vat);
    }

    // Authenticate Owner
    public Optional<String> authenticateOwner(String username, String password) throws CustomException {
        if (username == null || username.isBlank()) {
            throw new CustomException("Username cannot be null or blank.");
        }
        if (password == null || password.isBlank()) {
            throw new CustomException("Password cannot be null or blank.");
        }

        // Find owner by username
        Owner owner = ownerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Owner not found."));

        if (owner.isDeleted()) {
            throw new CustomException("Owner not found.");
        }

        if (!owner.getPassword().equals(password)) {
            throw new CustomException("Invalid password.");
        }

        return Optional.of(owner.getVat());
    }

    // Validations
    public void validateVat(String vat) throws CustomException {
        if (vat == null || vat.length() != 9) {
            throw new CustomException("VAT must be exactly 9 characters.");
        }
    }

    public void validateName(String name) throws CustomException {
        if (name == null || name.isBlank()) {
            throw new CustomException("Name cannot be null or blank.");
        }
    }

    public void validateSurname(String surname) throws CustomException {
        if (surname == null || surname.isBlank()) {
            throw new CustomException("Surname cannot be null or blank.");
        }
    }

    public void validatePassword(String password) throws CustomException {
        if (password.length() < 8) {
            throw new CustomException("Password must be at least 8 characters.");
        }
    }

    public void validatePhone(String phone) throws CustomException {
        if (phone.length() > 14) {
            throw new CustomException("Phone number must be at most 14 characters.");
        }
        if (!phone.matches("\\d+")) {
            throw new CustomException("Phone number must contain only numeric characters.");
        }
    }

    public void validateEmail(String email) throws CustomException {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        if (email == null || !emailPattern.matcher(email).matches()) {
            throw new CustomException("Invalid email format.");
        }
    }

    public void checkVat(String vat) throws CustomException {
        if (ownerRepository.findByVat(vat).isPresent()) {
            throw new CustomException("VAT already exists. If you have deleted your account contant the admin! ");
        }
    }

    public void checkEmail(String email) throws CustomException {
        if (ownerRepository.findByEmail(email).isPresent()) {
            throw new CustomException("Email already exists.");
        }
    }

    public void checkUsername(String username) throws CustomException {
        if (ownerRepository.findByUsername(username).isPresent()) {
            throw new CustomException("Username already exists.");
        }
    }

    // Functionalities
    private void save(Owner owner) throws CustomException {
        try {
            ownerRepository.save(owner);
        } catch (Exception e) {
            throw new CustomException("Failed to save owner details: " + e.getMessage());
        }
    }
}
