package gr.technico.technikon.ui;

import gr.technico.technikon.services.OwnerService;
import gr.technico.technikon.services.OwnerServiceImpl;
import gr.technico.technikon.repositories.OwnerRepository;
import gr.technico.technikon.jpa.JpaUtil;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterface implements UserSelection {

    private static final Scanner scanner = new Scanner(System.in);
    private final OwnerInterface ownerInterface;
    private final AdminInterface adminInterface;

    public UserInterface() {
        // Set up the necessary service and repository
        OwnerRepository ownerRepository = new OwnerRepository(JpaUtil.getEntityManager());
        OwnerService ownerService = new OwnerServiceImpl(ownerRepository);

        // Initialize OwnerInterface with required service
        this.ownerInterface = new OwnerInterface(ownerService);
        this.adminInterface = new AdminInterface();
    }

    public void run() {
        while (true) {
            displayMainMenu();
            int role = getUserChoice();

            switch (role) {
                case 1:
                    ownerInterface.manageOwner();
                    break;
                case 2:
                    adminInterface.manageAdmin();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    @Override
    public void displayMainMenu() {
        System.out.println("\nWelcome to Technico");
        System.out.println("1. Property Owner");
        System.out.println("2. Admin");
        System.out.println("3. Exit");
        System.out.print("Select role: ");
    }

    @Override
    public int getUserChoice() {
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }
}
