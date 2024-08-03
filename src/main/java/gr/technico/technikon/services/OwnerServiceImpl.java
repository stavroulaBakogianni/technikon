package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.repositories.OwnerRepository;

public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public String createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String username, String password) throws CustomException {
        //Owner owner = new Owner(vat, name, surname, address, phoneNumber, email, username, password);
        Owner owner = new Owner();
        owner.setVat(vat);
        owner.setName(name);
        owner.setSurname(surname);
        owner.setAddress(address);
        owner.setEmail(email);
        owner.setPhoneNumber(phoneNumber);
        owner.setUsername(username);
        owner.setPassword(password);
        try {
            ownerRepository.save(owner);
            return owner.getVat();
        } catch (Exception e) {
            throw new CustomException("Error creating owner: " + e.getMessage());
        }
    }
}
