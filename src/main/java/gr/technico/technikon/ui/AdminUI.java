package gr.technico.technikon.ui;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.model.Owner;
import gr.technico.technikon.model.Property;
import gr.technico.technikon.model.Repair;
import gr.technico.technikon.services.OwnerServiceImpl;
import gr.technico.technikon.services.PropertyService;
import gr.technico.technikon.services.PropertyServiceImpl;
import gr.technico.technikon.services.RepairServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.MatchResult;

public class AdminUI implements AdminSelection {

    private static final Scanner scanner = new Scanner(System.in);
    private final OwnerServiceImpl ownerServiceImpl;
    private final PropertyService propertyService;
    private final RepairServiceImpl repairServiceImpl;

    public AdminUI(OwnerServiceImpl ownerServiceImpl, PropertyServiceImpl propertyServiceImpl, RepairServiceImpl repairServiceImpl) {
        this.ownerServiceImpl = ownerServiceImpl;
        this.propertyService = propertyServiceImpl;
        this.repairServiceImpl = repairServiceImpl;
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
                    getPending();
                    break;
                case 6:
                    proposeCostDates();
                    break;
                case 7:
                    updateStatus();
                    break;
                case 8:
                    markComplete();
                    break;
                case 9:
                    deleteRepair();
                    break;
                case 10:
                    searchRepairsByDate();
                    break;
                case 11:
                    searchRepairsByRangeOfDates();
                    break;
                case 12:
                    adminReport();
                    break;
                case 13:
                    searchRepairsByOwnerVat();
                    break;
                case 14:
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
        System.out.println("5. Find Pending Repairs");
        System.out.println("6. Propose cost and dates for a repair");
        System.out.println("7. Update status and actual start date of a repair");
        System.out.println("8. Mark complete");
        System.out.println("9. Delete repair");
        System.out.println("10. Search repairs by date");
        System.out.println("11. Search repairs by range of dates");
        System.out.println("12. Full Reports");
        System.out.println("13. Search all repairs by owner vat");
        System.out.println("14. Back to Main Menu");
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
            Long e9Number = Long.parseLong(e9);
            System.out.println(propertyService.findByE9(e9Number.toString()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid numeric E9.");
        } catch (CustomException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void searchPropertyByVAT() {
        System.out.print("Insert VAT: ");
        String vat = scanner.nextLine().trim();

        try {
            Long vatNumber = Long.parseLong(vat);
            List<Property> properties = propertyService.findByVAT(vatNumber.toString());
            if (properties.isEmpty()) {
                System.out.println("No properties found for the given VAT.");
            } else {
                for (Property property : properties) {
                    System.out.println(property);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid numeric VAT.");
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

        Long propertyId = null;
        while (propertyId == null) {
            System.out.print("Insert property id: ");
            String input = scanner.nextLine().trim();

            try {
                propertyId = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid numeric property ID.");
            }
        }

        try {
            System.out.println("You are about to delete the following property and its repairs: " + propertyService.findByID(propertyId));
            System.out.println("Enter 1 to confirm deletion or 2 to cancel: ");

            int userChoice = getAdminAction();

            switch (userChoice) {
                case 1:
                    propertyService.permenantlyDeleteByID(propertyId);
                    System.out.println("Property and its repairs have been successfully deleted.");
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

    //Repair
    public void getPending() {
        List<Repair> adminPendingRepairs = repairServiceImpl.getPendingRepairs();
        for (Repair r : adminPendingRepairs) {
            System.out.println("\n" + r.getId() + " " + r.getDescription() + " " + r.getShortDescription() + " " + r.getRepairType());
        }
    }

    public void proposeCostDates() {
        try {
            List<Repair> repairs = repairServiceImpl.getPendingRepairs();
            for (Repair r : repairs) {
                System.out.println("\n" + r.getId() + " " + r.getDescription() + " " + r.getShortDescription() + " " + r.getRepairType());
            }
            System.out.print("Enter the Repair Id for update ");
            Long id = scanner.nextLong();
            System.out.println("Enter proposed start date (dd/MM/yyyy HH:mm)");
            LocalDateTime proposedStart = dateInput();
            System.out.println("Enter proposed end date (dd/MM/yyyy HH:mm)");
            LocalDateTime proposedEnd = dateInput();
            System.out.print("Enter cost: ");
            BigDecimal cost = scanner.nextBigDecimal();

            repairServiceImpl.updCostDates(id, cost, proposedStart, proposedEnd);
            System.out.println("\nProposed cost and proposed dates updated successfully.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public static LocalDateTime dateInput() throws NumberFormatException {
        LocalDateTime date;
        try {

            Scanner scanner = new Scanner(System.in);
            scanner.findInLine("(\\d\\d)\\/(\\d\\d)\\/(\\d\\d\\d\\d)\\ (\\d\\d):(\\d\\d)");
            MatchResult mr = scanner.match();
            int year = Integer.parseInt(mr.group(3));
            int month = Integer.parseInt(mr.group(2));
            int day = Integer.parseInt(mr.group(1));
            int hour = Integer.parseInt(mr.group(4));
            int minute = Integer.parseInt(mr.group(5));
            date = LocalDateTime.of(year, month, day, hour, minute);
            return date;

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return null;
        }
    }

    public void updateStatus() {
        try {
            List<Repair> acceptedRepairs = repairServiceImpl.getAcceptedRepairs();
            for (Repair r : acceptedRepairs) {
                System.out.println("\n" + r.getId() + " " + r.getRepairStatus() + " " + r.getRepairType());
            }
            System.out.print("Enter the Repair Id for update ");
            Long id = scanner.nextLong();
            repairServiceImpl.updateStatus(id);
            System.out.println("\nRepair status and actual start date updated successfully.");

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void markComplete() {
        try {
            List<Repair> adminInProgressRepairs = repairServiceImpl.getInProgressRepairs();
            for (Repair r : adminInProgressRepairs) {
                System.out.println("\n" + r.getId() + " " + r.getRepairStatus() + " " + r.getRepairType());
            }
            System.out.print("Enter the Repair Id for update ");
            Long id = scanner.nextLong();
            repairServiceImpl.updComplete(id);
            System.out.println("\nRepair completed successfully.");

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void adminReport() {
        try {
            List<Repair> adminRepairs = repairServiceImpl.getRepairs();
            for (Repair r : adminRepairs) {
                System.out.println(r.toString());
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void searchRepairsByDate() {

        System.out.println("Please type the date you want to retrieve repairs for. Follow the 2024-08-20 format and press enter:");
        String date = scanner.nextLine();

        List<Repair> repairs = repairServiceImpl.findRepairsByDate(date, null);

        System.out.println("List of repairs for: " + date);
        for (Repair r : repairs) {
            System.out.println(r.toString());
        }

    }

    public void searchRepairsByRangeOfDates() {

        System.out.println("Please type the start date you want to retrieve repairs for. Follow the 2024-08-20 format and press enter:");
        String startDate = scanner.nextLine();

        System.out.println("Please type the end date you want to retrieve repairs for. Follow the 2024-09-22 format and press enter:");
        String endDate = scanner.nextLine();

        List<Repair> repairs = repairServiceImpl.findRepairsByRangeOfDates(startDate, endDate, null);

        System.out.println("List of repairs for range: " + startDate + " - " + endDate);
        for (Repair r : repairs) {
            System.out.println(r.toString());
        }
    }

    public void searchRepairsByOwnerVat() {
        System.out.println("Please type the owner's VAT you want to see repairs for and press enter:");
        String ownerVat = scanner.nextLine();

        Optional<Owner> owner = ownerServiceImpl.searchOwnerByVat(ownerVat);

        if (owner.isEmpty()) {
            System.out.println("Owner not found.");
            return;
        }

        Owner foundOwner = owner.get();

        List<Repair> repairs = repairServiceImpl.findRepairsByOwner(foundOwner);

        System.out.println("List of repairs for owner with VAT: " + ownerVat);
        for (Repair r : repairs) {
            System.out.println(r.toString());
        }
    }

    public void deleteRepair() {

        System.out.println("Please type the owner's VAT you want to see the list of repairs for deleteion and press enter:");
        String ownerVat = scanner.nextLine();

        Optional<Owner> owner = ownerServiceImpl.searchOwnerByVat(ownerVat);

        if (owner.isEmpty()) {
            System.out.println("Owner not found.");
            return;
        }

        Owner foundOwner = owner.get();

        List<Repair> repairs = repairServiceImpl.findRepairsByOwner(foundOwner);

        System.out.println("List of repairs for owner with VAT: " + ownerVat);
        for (Repair r : repairs) {
            System.out.println(r.getId() + " " + r.toString());
        }

        System.out.println("Please type the repair id you want to delete");

        Long repairId = scanner.nextLong();

        System.out.println("You are about to delete the following repair: " + repairServiceImpl.findRepairById(repairId));
        System.out.println("Enter 1 to confirm deletion or 2 to cancel: ");

        int userChoice = getAdminAction();

        switch (userChoice) {
            case 1:
                boolean success = repairServiceImpl.deletePermantlyById(repairId);

                if (success) {
                    System.out.println("The repair has been successfully deleted.");
                } else {
                    System.out.println("The repair has not been deleted.");
                }
                break;
            case 2:
                System.out.println("Deletion operation has been cancelled.");
                break;
            default:
                System.out.println("Invalid input. Deletion operation has been cancelled.");
                break;
        }

    }
}
