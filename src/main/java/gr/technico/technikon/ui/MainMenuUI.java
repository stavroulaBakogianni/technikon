package gr.technico.technikon.ui;

import gr.technico.technikon.services.OwnerService;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenuUI implements MainMenuSelection {

    private static final Scanner scanner = new Scanner(System.in);
    private final OwnerUI OwnerUI;
    private final AdminUI adminUI;

    // Constructor accepting parameters
    public MainMenuUI(OwnerService ownerService) {
        this.OwnerUI = new OwnerUI(ownerService);
        this.adminUI = new AdminUI(ownerService);
    }

    public void run() {
        while (true) {
            displayMainMenu();
            int role = getUserChoice();

            switch (role) {
                case 1:
                    OwnerUI.manageOwner();
                    break;
                case 2:
                    adminUI.manageAdmin();
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
        System.out.print("Please type a number for your role and press enter: ");
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
