package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.OwnerException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.repositories.OwnerRepository;

public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public String createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String username, String password) throws OwnerException {
        Owner owner = new Owner(vat, name, surname, address, phoneNumber, email, username, password);
        try {
            ownerRepository.save(owner);
            return owner.getVat();
        } catch (Exception e) {
            throw new OwnerException("Error creating owner: " + e.getMessage());
        }
    }
}
