package gr.technico.technikon;
import gr.technico.technikon.ui.UserInterface;


public class Technikon {
    public static void main(String[] args) {
        // The Menu of the app, it cotrols the flow
        UserInterface userInterface = new UserInterface();
        userInterface.run();
        
    }
}