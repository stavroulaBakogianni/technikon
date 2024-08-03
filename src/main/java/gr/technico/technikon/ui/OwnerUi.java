package gr.technico.technikon.ui;

import gr.technico.technikon.exceptions.CustomException;
import gr.technico.technikon.services.OwnerService;
import lombok.extern.slf4j.Slf4j;

import java.util.InputMismatchException;
import java.util.Scanner;

@Slf4j
public class OwnerUi implements OwnerSelection {

    private static final Scanner scanner = new Scanner(System.in);
    private final OwnerService ownerService;
    
    // Initialize ownerService
    public OwnerUi(OwnerService ownerService) {
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
        System.out.print("Select action: ");
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

    @Override
    public void createOwner() {
        try {
            log.info("Starting the creation of a new owner.");
            
            System.out.print("Enter VAT: ");
            String vat = scanner.nextLine().trim();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Surname: ");
            String surname = scanner.nextLine();
            System.out.print("Enter Address: ");
            String address = scanner.nextLine();
            System.out.print("Enter Phone Number: ");
            String phoneNumber = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Enter Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            ownerService.createOwner(vat, name, surname, address, phoneNumber, email, username, password);
            log.info("Owner created successfully.");
        } catch (CustomException e) {
            log.error("Error creating owner:", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while creating owner: ", e);
        }
    }

    @Override
    public void updateOwner() {
    }

    @Override
    public void deleteOwner() {
    }
}
