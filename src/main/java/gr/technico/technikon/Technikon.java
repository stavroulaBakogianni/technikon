package gr.technico.technikon;
import gr.technico.technikon.ui.UserUI;


public class Technikon {
    public static void main(String[] args) {
        // The Menu of the app, it cotrols the flow
        UserUI userInterface = new UserUI();
        userInterface.run();
        
    }
}