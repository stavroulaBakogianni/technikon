package gr.technico.technikon.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

public class OwnerInterface implements OwnerSelection {

    private static final Scanner scanner = new Scanner(System.in);

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
    }

    @Override
    public void updateOwner() {
    }

    @Override
    public void deleteOwner() {
    }
}
