package gr.technico.technikon.services;

import gr.technico.technikon.exceptions.CustomException;

public interface OwnerServiceInterface {
    void createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String username, String password) throws CustomException;
}