package gr.technico.technikon.services;

import gr.technico.technikon.model.Owner;

public interface OwnerService {
    Owner createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String username, String password);
}
