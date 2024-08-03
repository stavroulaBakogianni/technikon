package gr.technico.technikon;
import gr.technico.technikon.ui.UserUI;


import lombok.extern.slf4j.Slf4j;

@Slf4j

public class Technikon {
    public static void main(String[] args) {
        // The Menu of the app, it cotrols the flow
        UserUI userInterface = new UserUI();
        userInterface.run();
        
    }
}
