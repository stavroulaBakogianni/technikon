package gr.technico.technikon.ui;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.PropertyType;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.model.RepairType;
import gr.technico.technikon.services.OwnerServiceImpl;
import gr.technico.technikon.services.PropertyService;
import gr.technico.technikon.services.PropertyServiceImpl;
import gr.technico.technikon.services.RepairServiceImpl;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class OwnerUI implements OwnerSelection {

    private static final Scanner scanner = new Scanner(System.in);
    private final OwnerServiceImpl ownerServiceImpl;
    private final PropertyServiceImpl propertyService;
    private final RepairServiceImpl repairServiceImpl;

    public OwnerUI(OwnerServiceImpl ownerServiceImpl, PropertyServiceImpl propertyServiceImpl, RepairServiceImpl repairServiceImpl) {
        this.ownerServiceImpl = ownerServiceImpl;
        this.propertyService = propertyServiceImpl;
        this.repairServiceImpl = repairServiceImpl;
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
                        createRepair();
                        break;
                    case 7:
                        updateRepair();
                        break;
                    case 8:
                        updateAcceptance();
                        break;
                    case 9:
                        deleteRepair();
                        break;
                    case 10:   
                        getOwnerPropertiesStatuses();
                        break;
                    case 11:
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
        System.out.println("6. Create Repair");
        System.out.println("7. Update Repair");
        System.out.println("8. Accept/Decline Repair");
        System.out.println("9. Delete Repair");
        System.out.println("10. Full Reports of Properties and Statuses of Repairs");
        System.out.println("11. Logout");
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
                System.out.println("You must be logged in to update your profile.");
                return;
            }
            
            System.out.println("Please insert e9:");
            String e9 = scanner.nextLine().trim();
            propertyService.validateE9(e9);
            System.out.println("Please insert address:");
            String address = scanner.nextLine().trim();
            System.out.println("Please insert year of construction");
            String yearInput = scanner.nextLine();
            propertyService.validateConstructionYear(yearInput);
            int year = Integer.parseInt(yearInput);
            System.out.println("Please insert the type of property");
            String propertyTypeInput = scanner.nextLine();
            propertyService.validatePropertyType(propertyTypeInput);
            PropertyType propertyType = Arrays.stream(PropertyType.values())
                    .filter(type -> type.getCode().equals(propertyTypeInput))
                    .findFirst()
                    .orElseThrow(() -> new CustomException("Invalid property type"));
            
            propertyService.createProperty(e9, address, year, propertyType, loggedInOwnerVat);
            System.out.println("Property created successfully.");
        } catch (CustomException ex) {
            System.out.println(ex.getMessage());
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
        List<Property> properties = propertyService.findByVAT(loggedInOwnerVat);

        System.out.println("Properties associated with VAT " + loggedInOwnerVat + ":");
        for (Property property : properties) {
            System.out.println(property);
        }

        System.out.println("Please insert the ID of the property you want to update:");
        Long propertyId = scanner.nextLong();
        scanner.nextLine();

        System.out.println("Please insert the new E9:");
        String newE9 = scanner.nextLine().trim();
        propertyService.validateE9(newE9);        

        propertyService.updatePropertyE9(propertyId, newE9);

        System.out.println("Property updated successfully.");
    }

    private void updatePropertyAddress() throws CustomException {
        List<Property> properties = propertyService.findByVAT(loggedInOwnerVat);

        System.out.println("Properties associated with VAT " + loggedInOwnerVat + ":");
        for (Property property : properties) {
            System.out.println(property);
        }

        System.out.println("Please insert the ID of the property you want to update:");
        Long propertyId = scanner.nextLong();
        scanner.nextLine();

        System.out.println("Please insert the new Address:");
        String newAddress = scanner.nextLine().trim();

        propertyService.updatePropertyAddress(propertyId, newAddress);

        System.out.println("Property updated successfully.");
    }

    public void updatePropertyConstructionYear() throws CustomException {        
        List<Property> properties = propertyService.findByVAT(loggedInOwnerVat);

        System.out.println("Properties associated with VAT " + loggedInOwnerVat + ":");
        for (Property property : properties) {
            System.out.println(property);
        }

        System.out.println("Please insert the ID of the property you want to update:");
        Long propertyId = scanner.nextLong();
        scanner.nextLine();

        System.out.println("Please insert the new Construction Year:");
        int newYear = scanner.nextInt();
        propertyService.validateConstructionYear(String.valueOf(newYear));

        propertyService.updatePropertyConstructionYear(propertyId, newYear);

        System.out.println("Property updated successfully.");
    }

    public void updatePropertyType() throws CustomException {        
        List<Property> properties = propertyService.findByVAT(loggedInOwnerVat);

        System.out.println("Properties associated with VAT " + loggedInOwnerVat + ":");
        for (Property property : properties) {
            System.out.println(property);
        }

        System.out.println("Please insert the ID of the property you want to update:");
        Long propertyId = scanner.nextLong();
        scanner.nextLine();

        System.out.println("Please insert the new Property Type:");
        String propertyTypeInput = scanner.nextLine().trim();
        propertyService.validatePropertyType(propertyTypeInput);
        PropertyType newPropertyType = Arrays.stream(PropertyType.values())
                .filter(type -> type.getCode().equals(propertyTypeInput))
                .findFirst()
                .orElseThrow(() -> new CustomException("Invalid property type"));

        propertyService.updatePropertyType(propertyId, newPropertyType);

        System.out.println("Property updated successfully.");
    }

    @Override
    public void deleteProperty() {
        try {
            List<Property> properties = propertyService.findByVAT(loggedInOwnerVat);

            System.out.println("Properties associated with VAT " + loggedInOwnerVat + ":");
            for (Property property : properties) {
                System.out.println(property);
            }

            System.out.println("Please insert the ID of the property you want to update:");
            Long propertyId = scanner.nextLong();
            scanner.nextLine();

            try {
                System.out.println("You are about to delete the following property and its repairs:" + propertyService.findByID(propertyId));
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
    
    public void createRepair() {
        boolean validInput = false;

        if (loggedInOwnerVat == null) {
            System.out.println("You must be logged in to update your profile.");
            //return;
        } else {

            while (!validInput) {
                try {
                    System.out.println("\nLet's begin the registration process!");
                    Owner owner = null;
                    do {
                        try {
                            ownerServiceImpl.validateVat(loggedInOwnerVat);
                            owner = ownerServiceImpl.getOwnerByVat(loggedInOwnerVat);
                            break;
                        } catch (CustomException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (owner == null);
                    List<Property> properties = propertyService.findByVAT(loggedInOwnerVat);
                    for (Property p : properties) {
                        System.out.println(p.getId() + " " + p.getE9() + " " + p.getPropertyAddress() + " " + p.getPropertyType());
                    }

                    String e9;
                    Property property = null;
                    do {
                        System.out.print("Enter E9: ");
                        e9 = scanner.nextLine().trim();
                        try {
                            property = propertyService.findByE9(e9);
                            break;
                        } catch (CustomException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (property == null);

                    String shortDescription;
                    do {
                        System.out.print("Enter Short Description (up to 100 characters): ");
                        shortDescription = scanner.nextLine().trim();
                        try {
                            repairServiceImpl.validateShortDesc(shortDescription);
                            break;
                        } catch (CustomException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (shortDescription == null || shortDescription.isBlank());

                    String description;
                    do {
                        System.out.print("Enter Description (up to 400 characters): ");
                        description = scanner.nextLine().trim();
                        try {
                            repairServiceImpl.validateDesc(description);
                            break;
                        } catch (CustomException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (description == null || description.isBlank());

                    int repairType;
                    RepairType type = null;
                    do {
                        String SCAN_TYPE = """
                                         Please select 
                                         Repair Type
                                         --------------------
                                            1. Painting
                                            2. Insulation
                                            3. Frames
                                            4. Plumbing
                                            5. Electrical work

                                        Select an action by typing the corresponding number and pressing enter:                          
                                        """;
                        System.out.print(SCAN_TYPE);
                        repairType = scanner.nextInt();
                        try {
                            repairServiceImpl.validateType(repairType);
                            type = repairServiceImpl.checkType(repairType);

                            break;
                        } catch (CustomException e) {
                            System.out.println(e.getMessage());
                        }
                    } while (repairType < 1 || repairType > 5);

                    repairServiceImpl.createRepair(type, shortDescription, description, owner, property);
                    System.out.println("\nRepair created successfully.");
                    validInput = true;
                } catch (CustomException e) {
                    System.out.println("Error creating repair: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error while creating repair: " + e);
                }
            }
        }
    }

    public void updateRepair() {
        if (loggedInOwnerVat == null) {
            System.out.println("You must be logged in to update your profile.");
            return;
        } else {
            int choice;
            do {
                System.out.println("\nWhich field would you like to update?");
                System.out.println("1. Repair Type");
                System.out.println("2. Short Description");
                System.out.println("3. Description");
                System.out.println("4. Go back");
                System.out.print("Enter the number of the field you want to update: ");

                choice = getOwnerAction();

                try {
                    switch (choice) {
                        case 1:
                            updateRepairType();
                            break;
                        case 2:
                            updateShortDescription();
                            break;
                        case 3:
                            updateDescription();
                            break;
                        case 4:
                            return;
                        default:
                            System.out.println("Invalid option. Please try again.");
                    }
                } catch (CustomException ex) {
                    ex.printStackTrace();
                }
            } while (choice != 4);
        }

    }

    public Long checkRepairs() {
        if (loggedInOwnerVat == null) {
            System.out.println("You must be logged in to update your profile.");
            return null;
        } else {
            Owner owner = null;
            do {
                try {
                    ownerServiceImpl.validateVat(loggedInOwnerVat);
                    owner = ownerServiceImpl.getOwnerByVat(loggedInOwnerVat);
                    List<Repair> repairs = repairServiceImpl.findRepairByUserId(owner);
                    for (Repair r : repairs) {
                        System.out.println(r.getId() + " " + r.getDescription() + " " + r.getShortDescription() + " " + r.getRepairType());
                    }
                    System.out.print("Enter the Repair Id for update ");
                    Long id = scanner.nextLong();
                    return id;

                } catch (CustomException e) {
                    System.out.println(e.getMessage());
                    return null;
                }
            } while (owner == null);
        }
    }

    public void updateShortDescription() throws CustomException {

        Long id = checkRepairs();
        String shortDescription;
        do {
            System.out.print("Enter Short Description (up to 100 characters): ");
            shortDescription = scanner.nextLine().trim();
            try {
                repairServiceImpl.validateShortDesc(shortDescription);
                repairServiceImpl.updshortDesc(id, shortDescription);
                System.out.println("\nShort Description updated successfully.");
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        } while (shortDescription == null || shortDescription.isBlank());

    }

    public void updateDescription() {
        Long id = checkRepairs();
        String description;
        do {
            System.out.print("Enter Description (up to 400 characters): ");
            description = scanner.nextLine().trim();
            try {
                repairServiceImpl.validateDesc(description);
                repairServiceImpl.updDesc(id, description);
                System.out.println("\nDescription updated successfully.");
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        } while (description == null || description.isBlank());

    }

    public void updateRepairType() {
        Long id = checkRepairs();

        int repairType;
        RepairType type = null;
        do {
            String SCAN_TYPE = """
                                 Please select 
                                 Repair Type
                                 --------------------
                                    1. Painting
                                    2. Insulation
                                    3. Frames
                                    4. Plumbing
                                    5. Electrical work

                                Select an action by typing the corresponding number and pressing enter:                          
                                """;
            System.out.print(SCAN_TYPE);
            repairType = scanner.nextInt();
            try {
                repairServiceImpl.validateType(repairType);
                type = repairServiceImpl.checkType(repairType);
                repairServiceImpl.updType(id, type);
                System.out.println("\nRepair Type updated successfully.");
                break;
            } catch (CustomException e) {
                System.out.println(e.getMessage());
            }
        } while (repairType < 1 || repairType > 5);

    }

    public void updateAcceptance() {
        if (loggedInOwnerVat == null) {
            System.out.println("You must be logged in to update your profile.");

        } else {
            Owner owner = null;
            do {
                try {
                    ownerServiceImpl.validateVat(loggedInOwnerVat);
                    owner = ownerServiceImpl.getOwnerByVat(loggedInOwnerVat);
                    List<Repair> repairs = repairServiceImpl.getPendingRepairsByOwner(owner);
                    for (Repair r : repairs) {
                        System.out.println(r.getId() + " " + r.getDescription() + " " + r.getShortDescription() + " " + r.getRepairType());
                    }
                    System.out.print("Enter the Repair Id for update ");
                    Long id = scanner.nextLong();
                    System.out.print("Do you want to accept or decline the repair? (1 for accept / 2 for decline) ");
                    int response = scanner.nextInt();
                    repairServiceImpl.updAcceptance(id, response);
                    System.out.println("\nAcceptance updated successfully.");

                    break;
                } catch (CustomException e) {
                    System.out.println(e.getMessage());
                }
            } while (owner == null);

        }
    }

    public void deleteRepair() {
        if (loggedInOwnerVat == null) {
            System.out.println("You must be logged in to update your profile.");

        } else {
            Owner owner = null;
            do {
                try {
                    ownerServiceImpl.validateVat(loggedInOwnerVat);
                    owner = ownerServiceImpl.getOwnerByVat(loggedInOwnerVat);
                    List<Repair> repairs = repairServiceImpl.findRepairByUserId(owner);
                    for (Repair r : repairs) {
                        System.out.println(r.getId() + " " + r.getDescription() + " " + r.getShortDescription() + " " + r.getRepairType());
                    }
                    System.out.print("Enter the Repair Id for update ");
                    Long id = scanner.nextLong();

                    repairServiceImpl.deleteSafely(id);
                    System.out.println("\nRepair deleted successfully.");

                    break;
                } catch (CustomException e) {
                    System.out.println(e.getMessage());
                }
            } while (owner == null);

        }

    }

    public void getOwnerPropertiesStatuses() {
        if (loggedInOwnerVat == null) {
            System.out.println("You must be logged in to update your profile.");

        } else {
            Owner owner = null;
            do {
                try {
                    ownerServiceImpl.validateVat(loggedInOwnerVat);
                    owner = ownerServiceImpl.getOwnerByVat(loggedInOwnerVat);
                    List<Property> properties = propertyService.findByVAT(loggedInOwnerVat);
                    for (Property prop : properties) {
                        System.out.println(prop.getId() + " " + prop.getE9() + " " + prop.getPropertyAddress()+ " ");
                        List <Repair> repairsbyOwner = repairServiceImpl.getRepairByPropertyId(prop);
                        for (Repair r : repairsbyOwner) {
                            System.out.println(r.getRepairStatus() + " " + r.getRepairType());
                        }
                    }
                } catch (CustomException e) {
                    System.out.println(e.getMessage());
                }
            } while (owner == null);
        }
    }
}

