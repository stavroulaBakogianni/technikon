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

    public OwnerUI(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    // Keep track of the logged in owner
    private String loggedInOwnerVat;

    public void manageOwner() {
        while (true) {
            if (loggedInOwnerVat == null) {
                // Owner is not logged in
                showOwnerSelectionMenu();

                int action = getOwnerAction();

                switch (action) {
                    case 1:
                        authenticateOwner();
                        break;
                    case 2:
                        createOwner();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } else {
                // Owner is logged in
                showOwnerMenu();
                int action = getOwnerAction();

                switch (action) {
                    case 1:
                        updateOwner();
                        break;
                    case 2:
                        deleteOwner();
                        break;
                    case 3:
                        loggedInOwnerVat = null;
                        System.out.println("You have been logged out.");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    public void showOwnerSelectionMenu() {
        System.out.println("\nProperty Owner Menu:");
        System.out.println("1. Existing Owner");
        System.out.println("2. Create Owner");
        System.out.println("3. Back to Main Menu");
        System.out.print("Select an action by typing the corresponding number and pressing enter: ");
    }

    public void showOwnerMenu() {
        System.out.println("\nOwner Menu:");
        System.out.println("1. Update Profile");
        System.out.println("2. Delete Account");
        System.out.println("3. Logout");
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

    private void authenticateOwner() {
        System.out.println("\nPlease enter your login details.");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            Optional<String> vat = ownerService.verifyOwner(username, password);
            if (vat.isPresent()) {
                loggedInOwnerVat = vat.get();
                System.out.println("Login successful.");
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (CustomException e) {
            System.out.println("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public void createOwner() {
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("\nLet's begin the registration process!");

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
                System.out.print("Enter Address: ");
                address = scanner.nextLine().trim();

                String phoneNumber;
                while (true) {
                    System.out.print("Enter Phone Number (max 14 digits): ");
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
        if (loggedInOwnerVat == null) {
            System.out.println("You must be logged in to update your profile.");
            return;
        }

        int choice;
        do {
            System.out.println("\nWhich field would you like to update?");
            System.out.println("1. Address");
            System.out.println("2. Email");
            System.out.println("3. Password");
            System.out.println("4. Go back");
            System.out.print("Enter the number of the field you want to update: ");

            choice = getOwnerAction();

            try {
                switch (choice) {
                    case 1:
                        updateAddress();
                        break;
                    case 2:
                        updateEmail();
                        break;
                    case 3:
                        updatePassword();
                        break;
                    case 4:
                        return; 
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (CustomException e) {
                System.out.println("Error updating owner: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e);
            }
        } while (choice != 4);
    }

    private void updateAddress() throws CustomException {
        System.out.print("Enter new Address: ");
        String newAddress = scanner.nextLine().trim();
        ownerService.updateOwnerAddress(loggedInOwnerVat, newAddress);
        System.out.println("\nAddress updated successfully.");
    }

    private void updateEmail() throws CustomException {
        String newEmail;
        while (true) {
            System.out.print("Enter new Email: ");
            newEmail = scanner.nextLine().trim();
            try {
                ownerService.validateEmail(newEmail);
                ownerService.updateOwnerEmail(loggedInOwnerVat, newEmail);
                System.out.println("\nEmail updated successfully.");
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void updatePassword() throws CustomException {
        String newPassword;
        while (true) {
            System.out.print("Enter new Password (at least 8 characters): ");
            newPassword = scanner.nextLine().trim();
            try {
                ownerService.validatePassword(newPassword);
                ownerService.updateOwnerPassword(loggedInOwnerVat, newPassword);
                System.out.println("\nPassword updated successfully.");
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void deleteOwner() {
        try {
            if (loggedInOwnerVat == null) {
                System.out.println("You must be logged in to delete your account.");
                return;
            }

            Optional<Owner> ownerOpt = ownerService.searchOwnerByVat(loggedInOwnerVat);

            if (ownerOpt.isPresent()) {
                Owner ownerToDelete = ownerOpt.get();
                System.out.println("\nYou are about to delete the following owner!");
                System.out.println(ownerToDelete.toString());
                System.out.print("\nEnter 1 to confirm deletion or 2 to cancel: ");

                String userChoice = scanner.nextLine().trim();

                switch (userChoice) {
                    case "1":
                        boolean deletionSuccessful = ownerService.deleteOwnerSafely(loggedInOwnerVat);
                        if (deletionSuccessful) {
                            System.out.println("\nOwner has been successfully marked as deleted.");
                            // Logout
                            loggedInOwnerVat = null;
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
