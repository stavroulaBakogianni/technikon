package gr.technico.technikon.ui;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.services.OwnerService;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class AdminUI implements AdminSelection {

    private static final Scanner scanner = new Scanner(System.in);
    private final OwnerService ownerService;

    public AdminUI(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    public void manageAdmin() {
        while (true) {
            showAdminMenu();
            int action = getAdminAction();

            switch (action) {
                case 1:
                    searchOwner();
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
    public void showAdminMenu() {
        System.out.println("\nAdmin Menu:");
        System.out.println("1. Search Owner");
        System.out.println("2. Update Owner");
        System.out.println("3. Delete Owner");
        System.out.println("4. Back to Main Menu");
        System.out.print("Select an action by typing the corresponding number and pressing enter: ");
    }

    private int getAdminAction() {
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

    @Override
    public void searchOwner() {
        System.out.println("\nSearch Owner by:");
        System.out.println("1. VAT Number");
        System.out.println("2. Email");
        System.out.print("Select an option by typing the corresponding number and pressing enter: ");

        int searchOption = getAdminAction();

        switch (searchOption) {
            case 1:
                searchOwnerByVat();
                break;
            case 2:
                searchOwnerByEmail();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void searchOwnerByVat() {
        System.out.print("Enter VAT number: ");
        String vat = scanner.nextLine().trim();

        Optional<Owner> owner = ownerService.searchOwnerByVat(vat);
        if (owner.isPresent()) {
            System.out.println("\nOwner found:");
            showFoundOwner(owner.get());
        } else {
            System.out.println("\nNo owner found with the given VAT number.");
        }
    }

    private void searchOwnerByEmail() {
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();

        Optional<Owner> owner = ownerService.searchOwnerByEmail(email);
        if (owner.isPresent()) {
            System.out.println("\nOwner found:");
            showFoundOwner(owner.get());
        } else {
            System.out.println("\nNo owner found with the given email.");
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
                System.out.println("\nYou are about to delete the following owner and all associated properties and repairs:");
                showFoundOwner(ownerToDelete);
                System.out.print("\nEnter 1 to confirm deletion or 2 to cancel: ");

                String userChoice = scanner.nextLine().trim();

                switch (userChoice) {
                    case "1":
                        boolean deletionSuccessful = ownerService.deleteOwnerPermanently(vatNumber);
                        if (deletionSuccessful) {
                            System.out.println("\nOwner and all associated properties and repairs have been successfully deleted.");
                        } else {
                            System.out.println("\nFailed to delete the owner. Please try again.");
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
