package gr.technico.technikon.ui;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import gr.technico.technikon.services.OwnerServiceImpl;
import gr.technico.technikon.services.PropertyService;
import gr.technico.technikon.services.PropertyServiceImpl;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class OwnerUI implements OwnerSelection {

    private static final Scanner scanner = new Scanner(System.in);
    private final OwnerServiceImpl ownerServiceImpl;
    private final PropertyService propertyService;

    public OwnerUI(OwnerServiceImpl ownerServiceImpl, PropertyServiceImpl propertyServiceImpl) {
        this.ownerServiceImpl = ownerServiceImpl;
        this.propertyService = propertyServiceImpl;
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
                        createProperty();
                        break;
                    case 4:
                        updateProperty();
                        break;
                    case 5:
                        deleteProperty();
                        break;
                    case 6:
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
        System.out.println("3. Create Property");
        System.out.println("4. Update Property");
        System.out.println("5. Delete Property");
        System.out.println("6. Logout");
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
            Optional<String> vat = ownerServiceImpl.verifyOwner(username, password);
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
                        ownerServiceImpl.checkVat(vat);
                        ownerServiceImpl.validateVat(vat);
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
                        ownerServiceImpl.validateName(name);
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
                        ownerServiceImpl.validateSurname(surname);
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
                        ownerServiceImpl.validatePhone(phoneNumber);
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
                        ownerServiceImpl.validateEmail(email);
                        ownerServiceImpl.checkEmail(email);
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
                        ownerServiceImpl.checkUsername(username);
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
                        ownerServiceImpl.validatePassword(password);
                        break;
                    } catch (CustomException e) {
                        System.out.println(e.getMessage());
                    }
                }

                ownerServiceImpl.createOwner(vat, name, surname, address, phoneNumber, email, username, password);
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
        ownerServiceImpl.updateOwnerAddress(loggedInOwnerVat, newAddress);
        System.out.println("\nAddress updated successfully.");
    }

    private void updateEmail() throws CustomException {
        String newEmail;
        while (true) {
            System.out.print("Enter new Email: ");
            newEmail = scanner.nextLine().trim();
            try {
                ownerServiceImpl.validateEmail(newEmail);
                ownerServiceImpl.updateOwnerEmail(loggedInOwnerVat, newEmail);
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
                ownerServiceImpl.validatePassword(newPassword);
                ownerServiceImpl.updateOwnerPassword(loggedInOwnerVat, newPassword);
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

            Optional<Owner> ownerOpt = ownerServiceImpl.searchOwnerByVat(loggedInOwnerVat);

            if (ownerOpt.isPresent()) {
                Owner ownerToDelete = ownerOpt.get();
                System.out.println("\nYou are about to delete the following owner!");
                System.out.println(ownerToDelete.toString());
                System.out.print("\nEnter 1 to confirm deletion or 2 to cancel: ");

                String userChoice = scanner.nextLine().trim();

                switch (userChoice) {
                    case "1":
                        boolean deletionSuccessful = ownerServiceImpl.deleteOwnerSafely(loggedInOwnerVat);
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

    @Override
    public void createProperty() {
        try {
            if (loggedInOwnerVat == null) {
                System.out.println("You must be logged in to create a property.");
                return;
            }

            System.out.println("Please insert e9 (20 characters):");
            String e9 = scanner.nextLine().trim();
            propertyService.validateE9(e9);
            Property property = propertyService.findByE9ForCreate(e9);
            if (property != null) {
                throw new CustomException("Property with e9 " + e9 + " already exists");
            }

            System.out.println("Please insert address:");
            String address = scanner.nextLine().trim();

            System.out.println("Please insert year of construction:");
            String yearInput = scanner.nextLine();
            propertyService.validateConstructionYear(yearInput);
            int year = Integer.parseInt(yearInput);

            System.out.println("Please select the Property Type:");
            System.out.println("1. Detached house");
            System.out.println("2. Maisonette");
            System.out.println("3. Apartment building");
            System.out.print("Select an option by entering the corresponding number: ");
            int propertyTypeNumber = getOwnerAction();

            PropertyType propertyType = getPropertyTypeFromNumber(propertyTypeNumber);

            propertyService.createProperty(e9, address, year, propertyType, loggedInOwnerVat);
            System.out.println("Property created successfully.");
        } catch (CustomException ex) {
            System.out.println(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid property type number.");
        }
    }
   
    private PropertyType getPropertyTypeFromNumber(int number) throws CustomException {
        switch (number) {
            case 1:
                return PropertyType.DETACHEDHOUSE;
            case 2:
                return PropertyType.MAISONETTE;
            case 3:
                return PropertyType.APARTMENTBUILDING;
            default:
                throw new CustomException("Invalid property type number.");
        }
    }

    @Override
    public void updateProperty() {
        if (loggedInOwnerVat == null) {
            System.out.println("You must be logged in to update your profile.");
            return;
        }

        int choice;
        do {
            System.out.println("\nWhich field would you like to update?");
            System.out.println("1. E9");
            System.out.println("2. Address");
            System.out.println("3. Construction year");
            System.out.println("4. Property type");
            System.out.println("5. Go back");
            System.out.print("Enter the number of the field you want to update: ");

            choice = getOwnerAction();

            try {
                switch (choice) {
                    case 1:
                        updatePropertyE9();
                        break;
                    case 2:
                        updatePropertyAddress();
                        break;
                    case 3:
                        updatePropertyConstructionYear();
                        break;
                    case 4:
                        updatePropertyType();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (CustomException ex) {
                ex.printStackTrace();
            }
        } while (choice != 5);
    }

    private void updatePropertyE9() throws CustomException {
        List<Property> properties = propertyService.findByVATOwner(loggedInOwnerVat);

        System.out.println("Properties associated with VAT " + loggedInOwnerVat + ":");
        for (Property property : properties) {
            System.out.println(property);
        }

        System.out.println("\nPlease insert the ID of the property you want to update:");
        Long propertyId = scanner.nextLong();
        scanner.nextLine();

        Property property = propertyService.findByID(propertyId);
        if (property == null) {
            throw new CustomException("Property with ID " + propertyId + " not found.");
        }

        System.out.println("\nPlease insert the new E9 (20 characters):");
        String newE9 = scanner.nextLine().trim();
        propertyService.validateE9(newE9);

        propertyService.updatePropertyE9(property, newE9);

        System.out.println("Property updated successfully.");
    }

    private void updatePropertyAddress() throws CustomException {
        List<Property> properties = propertyService.findByVATOwner(loggedInOwnerVat);

        System.out.println("Properties associated with VAT " + loggedInOwnerVat + ":");
        for (Property property : properties) {
            System.out.println(property);
        }

        System.out.println("\nPlease insert the ID of the property you want to update:");
        Long propertyId = scanner.nextLong();
        scanner.nextLine();

        Property property = propertyService.findByID(propertyId);
        if (property == null) {
            throw new CustomException("Property with ID " + propertyId + " not found.");
        }

        System.out.println("\nPlease insert the new Address:");
        String newAddress = scanner.nextLine().trim();

        propertyService.updatePropertyAddress(property, newAddress);

        System.out.println("Property updated successfully.");
    }

    public void updatePropertyConstructionYear() throws CustomException {
        List<Property> properties = propertyService.findByVATOwner(loggedInOwnerVat);

        System.out.println("\nProperties associated with VAT " + loggedInOwnerVat + ":");
        for (Property property : properties) {
            System.out.println(property);
        }

        System.out.println("\nPlease insert the ID of the property you want to update:");
        Long propertyId = scanner.nextLong();
        scanner.nextLine();

        Property property = propertyService.findByID(propertyId);
        if (property == null) {
            throw new CustomException("Property with ID " + propertyId + " not found.");
        }

        String yearInput;
        while (true) {
            System.out.println("Please insert the new Construction Year (4 digits):");
            yearInput = scanner.nextLine().trim();
            try {
                propertyService.validateConstructionYear(yearInput);
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        }

        int newYear = Integer.parseInt(yearInput);
        propertyService.updatePropertyConstructionYear(property, newYear);
        System.out.println("Property updated successfully.");
    }

    public void updatePropertyType() throws CustomException {
        List<Property> properties = propertyService.findByVATOwner(loggedInOwnerVat);

        System.out.println("\nProperties associated with VAT " + loggedInOwnerVat + ":");
        for (Property property : properties) {
            System.out.println(property);
        }

        System.out.println("\nPlease insert the ID of the property you want to update:");
        Long propertyId = scanner.nextLong();
        scanner.nextLine();

        Property property = propertyService.findByID(propertyId);
        if (property == null) {
            throw new CustomException("Property with ID " + propertyId + " not found.");
        }

        System.out.println("Please select the new Property Type:");
        System.out.println("1. Detached house");
        System.out.println("2. Maisonette");
        System.out.println("3. Apartment building");
        System.out.print("Select an option by entering the corresponding number: ");
        int propertyTypeNumber = getOwnerAction();

        PropertyType newPropertyType = getPropertyTypeFromNumber(propertyTypeNumber);

        propertyService.updatePropertyType(property, newPropertyType);
        System.out.println("Property updated successfully.");
    }

    @Override
    public void deleteProperty() {
        try {
            List<Property> properties = propertyService.findByVATOwner(loggedInOwnerVat);

            System.out.println("\nProperties associated with VAT " + loggedInOwnerVat + ":");
            for (Property property : properties) {
                System.out.println(property);
            }

            System.out.println("\nPlease insert the ID of the property you want to delte:");
            Long propertyId = scanner.nextLong();
            scanner.nextLine();

            try {
                System.out.println("\nYou are about to delete the following property and its repairs:" + propertyService.findByID(propertyId));
                System.out.println("Enter 1 to confirm deletion or 2 to cancel: ");

                int choice = getOwnerAction();

                switch (choice) {
                    case 1:
                        propertyService.safelyDeleteByID(propertyId);
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
        } catch (CustomException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
