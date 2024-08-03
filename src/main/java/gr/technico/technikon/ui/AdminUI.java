package gr.technico.technikon.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminUI implements AdminSelection {

    private static final Scanner scanner = new Scanner(System.in);

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
    }

    @Override
    public void updateOwner() {
    }

    @Override
    public void deleteOwner() {
    }
}
