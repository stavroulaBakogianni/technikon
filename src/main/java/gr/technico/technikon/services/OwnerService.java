package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.repositories.OwnerRepository;

public class OwnerService implements OwnerServiceInterface {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public void createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String username, String password) throws CustomException {
        Owner owner = new Owner(null, vat, name, surname, address, phoneNumber, email, username, password);
        
        try {
            ownerRepository.save(owner);
        } catch (Exception e) {
            throw new CustomException("Failed to create owner: " + e.getMessage());
        }
    }
}
