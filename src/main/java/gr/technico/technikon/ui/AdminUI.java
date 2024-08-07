package gr.technico.technikon.ui;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.services.OwnerServiceImpl;
import gr.technico.technikon.services.PropertyService;
import gr.technico.technikon.services.PropertyServiceImpl;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AdminUI implements AdminSelection {

    private static final Scanner scanner = new Scanner(System.in);
    private final OwnerServiceImpl ownerServiceImpl;
    private final PropertyService propertyService;

    public AdminUI(OwnerServiceImpl ownerServiceImpl, PropertyServiceImpl propertyServiceImpl) {
        this.ownerServiceImpl = ownerServiceImpl;
        this.propertyService = propertyServiceImpl;
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
                    deleteOwner();
                    break;
                case 3:
                    searchProperty();
                    break;
                case 4:
                    deleteProperty();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void showAdminMenu() {
        System.out.println("\nAdmin Menu:");
        System.out.println("1. Search Owner");
        System.out.println("2. Delete Owner");
        System.out.println("3. Search Property");
        System.out.println("4. Delete Property");
        System.out.println("5. Back to Main Menu");
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
        System.out.println("3. Go Back");
        System.out.print("Select an option by typing the corresponding number and pressing enter: ");

        int searchOption = getAdminAction();

        switch (searchOption) {
            case 1:
                searchOwnerByVat();
                break;
            case 2:
                searchOwnerByEmail();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void searchOwnerByVat() {
        System.out.print("Enter VAT number: ");
        String vat = scanner.nextLine().trim();

        Optional<Owner> owner = ownerServiceImpl.searchOwnerByVat(vat);
        if (owner.isPresent()) {
            if (owner.get().isDeleted()) {
                System.out.println("\nThis owner is marked as deleted");
            }
            System.out.println("\nOwner found:");
            System.out.println(owner.get().toString());

        } else {
            System.out.println("\nNo owner found with the given VAT number.");
        }
    }

    private void searchOwnerByEmail() {
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();

        Optional<Owner> owner = ownerServiceImpl.searchOwnerByEmail(email);
        if (owner.isPresent()) {
            if (owner.get().isDeleted()) {
                System.out.println("\nThis owner is marked as deleted");
            }
            System.out.println("\nOwner found:");
            System.out.println(owner.get().toString());

        } else {
            System.out.println("\nNo owner found with the given email.");
        }
    }

    @Override
    public void deleteOwner() {
        System.out.print("Enter the VAT number of the owner you want to delete: ");
        String vatNumber = scanner.nextLine().trim();

        try {
            Optional<Owner> optionalOwner = ownerServiceImpl.searchOwnerByVat(vatNumber);

            if (optionalOwner.isPresent()) {
                Owner ownerToDelete = optionalOwner.get();
                if (ownerToDelete.isDeleted()) {
                    System.out.println("\nThis owner is marked as deleted");
                }
                System.out.println("\nYou are about to delete the following owner and all associated properties and repairs:");
                System.out.println(ownerToDelete.toString());
                System.out.println("\nEnter 1 to confirm deletion or 2 to cancel: ");

                int userChoice = getAdminAction();

                switch (userChoice) {
                    case 1:
                        boolean deletionSuccessful = ownerServiceImpl.deleteOwnerPermanently(vatNumber);
                        if (deletionSuccessful) {
                            System.out.println("\nOwner and all associated properties and repairs have been successfully deleted.");
                        } else {
                            System.out.println("\nFailed to delete the owner. Please try again.");
                        }
                        break;
                    case 2:
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

    @Override
    public void searchProperty() {
        System.out.println("\nSearch Property by:");
        System.out.println("1. E9");
        System.out.println("2. VAT");
        System.out.println("3. Go Back");
        System.out.print("Select an option by typing the corresponding number and pressing enter: ");

        int searchOption = getAdminAction();

        switch (searchOption) {
            case 1:
                searchPropertyByE9();
                break;
            case 2:
                searchPropertyByVAT();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void searchPropertyByE9() {
        System.out.print("Insert E9: ");
        String e9 = scanner.nextLine().trim();

        try {
            System.out.println(propertyService.findByE9(e9));
        } catch (CustomException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void searchPropertyByVAT() {
        System.out.print("Insert VAT: ");
        String vat = scanner.nextLine().trim();

        try {
            List<Property> properties = propertyService.findByVAT(vat);
            for (Property property : properties) {
                System.out.println(property);
            }
        } catch (CustomException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void deleteProperty() {
        System.out.println("List of properties: ");
        List<Property> properties = propertyService.findAllProperties();
            for (Property property : properties) {
                System.out.println(property);
            }
        System.out.println("Insert property id: ");
        Long propertyId = scanner.nextLong();

        try {
            System.out.println("You are about to delete the following property and its repairs: " + propertyService.findByID(propertyId));
            System.out.println("Enter 1 to confirm deletion or 2 to cancel: ");

            int userChoice = getAdminAction();

            switch (userChoice) {
                case 1:
                    propertyService.permenantlyDeleteByID(propertyId);
                    break;
                case 2:
                    System.out.println("Deletion operation has been cancelled.");
                    break;
                default:
                    System.out.println("Invalid input. Deletion operation has been cancelled.");
                    break;
            }
        } catch (CustomException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
