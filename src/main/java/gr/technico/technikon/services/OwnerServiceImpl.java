package gr.technico.technikon.services;

import gr.technico.technikon.model.Owner;
import gr.technico.technikon.repositories.OwnerRepository;

public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Owner createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String username, String password) {
        Owner owner = new Owner();
        owner.setName(name);
        owner.setVat(vat);
        owner.setSurname(surname);
        owner.setAddress(address);
        owner.setEmail(email);
        owner.setPhoneNumber(phoneNumber);
        owner.setUsername(username);
        owner.setPassword(password);

        ownerRepository.save(owner);
//        return owner.getVat();
        return owner;

    }
}