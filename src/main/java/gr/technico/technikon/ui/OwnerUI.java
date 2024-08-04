package gr.technico.technikon.ui;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.services.OwnerService;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class OwnerUI implements OwnerSelection {

    private static final Scanner scanner = new Scanner(System.in);
    private final OwnerService ownerService;

    // Initialize ownerService
    public OwnerUI(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    public void manageOwner() {
        while (true) {
            showOwnerMenu();
            int action = getOwnerAction();

            switch (action) {
                case 1:
                    createOwner();
                    break;
                case 2:
                    updateOwner();
                    break;
                case 3:
                    deleteOwner();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    @Override
    public void showOwnerMenu() {
        System.out.println("\nOwner Menu:");
        System.out.println("1. Create Owner");
        System.out.println("2. Update Profile");
        System.out.println("3. Delete Account");
        System.out.println("4. Back to Main Menu");
        System.out.print("Select an action by typing the corresponding number and pressing enter: ");
    }

    private int getOwnerAction() {
        while (true) {
            try {
                int action = scanner.nextInt();
                scanner.nextLine();
                return action;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    public void createOwner() {
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("\nStarting the creation of a new owner");
                
                String vat;
                while (true) {
                    System.out.print("Enter VAT (9 characters): ");
                    vat = scanner.nextLine().trim();
                    try {
                        ownerService.checkVat(vat);
                        ownerService.validateVat(vat);
                        break;
                    } catch (CustomException e) {
                        System.out.println(e.getMessage());
                    }
                }

                String name;
                while (true) {
                    System.out.print("Enter Name: ");
                    name = scanner.nextLine().trim();
                    try {
                        ownerService.validateName(name);
                        break;
                    } catch (CustomException e) {
                        System.out.println(e.getMessage());
                    }
                }

                String surname;
                while (true) {
                    System.out.print("Enter Surname: ");
                    surname = scanner.nextLine().trim();
                    try {
                        ownerService.validateSurname(surname);
                        break;
                    } catch (CustomException e) {
                        System.out.println(e.getMessage());
                    }
                }

                String address;
                while (true) {
                    System.out.print("Enter Address: ");
                    address = scanner.nextLine().trim();
                    try {
                        ownerService.validateAddress(address);
                        break;
                    } catch (CustomException e) {
                        System.out.println(e.getMessage());
                    }
                }

                String phoneNumber;
                while (true) {
                    System.out.print("Enter Phone Number (max 14 characters): ");
                    phoneNumber = scanner.nextLine().trim();
                    try {
                        ownerService.validatePhone(phoneNumber);
                        break;
                    } catch (CustomException e) {
                        System.out.println(e.getMessage());
                    }
                }

                String email;
                while (true) {
                    System.out.print("Enter Email: ");
                    email = scanner.nextLine().trim();
                    try {
                        ownerService.validateEmail(email);
                        ownerService.checkEmail(email);
                        break;
                    } catch (CustomException e) {
                        System.out.println(e.getMessage());
                    }
                }

                String username;
                while (true) {
                    System.out.print("Enter Username: ");
                    username = scanner.nextLine().trim();
                    try {
                        ownerService.checkUsername(username);
                        break;
                    } catch (CustomException e) {
                        System.out.println(e.getMessage());
                    }
                }

                String password;
                while (true) {
                    System.out.print("Enter Password (at least 8 characters): ");
                    password = scanner.nextLine().trim();
                    try {
                        ownerService.validatePassword(password);
                        break;
                    } catch (CustomException e) {
                        System.out.println(e.getMessage());
                    }
                }

                // Create Owner
                ownerService.createOwner(vat, name, surname, address, phoneNumber, email, username, password);
                System.out.println("Owner created successfully.");
                validInput = true;

            } catch (CustomException e) {
                System.out.println("Error creating owner: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error while creating owner: " + e);
            }
        }
    }

    @Override
    public void updateOwner() {
        try {
            System.out.print("\nEnter the VAT number of the owner to update: ");
            String vat = scanner.nextLine().trim();

            // Find the owner by VAT
            Owner owner = ownerService.searchOwnerByVat(vat)
                    .orElseThrow(() -> new CustomException("Owner with the given VAT number not found."));

            System.out.println("\nPress Enter to skip updating a field.");

            System.out.print("Enter new Address (current: " + owner.getAddress() + "): ");
            String newAddress = scanner.nextLine().trim();

            String newEmail;
            while (true) {
                System.out.print("Enter new Email (current: " + owner.getEmail() + "): ");
                newEmail = scanner.nextLine().trim();
                if (newEmail.isEmpty()) {
                    newEmail = owner.getEmail();
                    break;
                }
                try {
                    ownerService.validateEmail(newEmail);
                    ownerService.checkEmail(newEmail); 
                    break; 
                } catch (CustomException e) {
                    System.out.println(e.getMessage());
                }
            }

            String newPassword;
            while (true) {
                System.out.print("Enter new Password (at least 8 characters): ");
                newPassword = scanner.nextLine().trim();
                if (newPassword.isEmpty()) {
                    newPassword = null; 
                    break;
                }
                try {
                    ownerService.validatePassword(newPassword);
                    break;
                } catch (CustomException e) {
                    System.out.println(e.getMessage());
                }
            }

            // Update owner details
            ownerService.updateOwner(vat,
                    newAddress.isEmpty() ? null : newAddress,
                    newEmail.isEmpty() ? null : newEmail,
                    newPassword);

            System.out.println("\nOwner details updated successfully.");
            Owner updatedOwner = ownerService.searchOwnerByVat(vat)
                    .orElseThrow(() -> new CustomException("Owner with the given VAT number not found."));

            System.out.println("\nUpdated Details:");
            showFoundOwner(updatedOwner);

        } catch (CustomException e) {
            System.out.println("Error updating owner: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e);
        }
    }

    private void showFoundOwner(Owner owner) {
        System.out.println("VAT: " + owner.getVat());
        System.out.println("Name: " + owner.getName());
        System.out.println("Surname: " + owner.getSurname());
        System.out.println("Address: " + owner.getAddress());
        System.out.println("Phone Number: " + owner.getPhoneNumber());
        System.out.println("Email: " + owner.getEmail());
        System.out.println("Username: " + owner.getUsername());
    }

    @Override
    public void deleteOwner() {
        System.out.print("Enter the VAT number of the owner you want to delete: ");
        String vatNumber = scanner.nextLine().trim();

        try {
            Optional<Owner> optionalOwner = ownerService.searchOwnerByVat(vatNumber);

            if (optionalOwner.isPresent()) {
                Owner ownerToDelete = optionalOwner.get();
                System.out.println("\nYou are about to delete the following owner!");
                showFoundOwner(ownerToDelete);
                System.out.print("\nEnter 1 to confirm deletion or 2 to cancel: ");

                String userChoice = scanner.nextLine().trim();

                switch (userChoice) {
                    case "1":
                        boolean deletionSuccessful = ownerService.deleteOwnerSafely(vatNumber);
                        if (deletionSuccessful) {
                            System.out.println("\nOwner has been successfully marked as deleted.");
                        } else {
                            System.out.println("\nFailed to mark the owner as deleted. Please try again.");
                        }
                        break;
                    case "2":
                        System.out.println("\nDeletion operation has been cancelled.");
                        break;
                    default:
                        System.out.println("Invalid input. Deletion operation has been cancelled.");
                        break;
                }
            } else {
                System.out.println("\nNo owner found with the given VAT number.");
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}