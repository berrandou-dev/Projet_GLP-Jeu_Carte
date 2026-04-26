package test.demo;

import gui.screens.MenuGUI;

public class DemoLauncher {
    public static void main(String[] args) {
        // Active le mode démonstration
        System.setProperty("demo.mode", "true");
        
        System.out.println("=== TU N'Y PEUX RIEN - DÉMONSTRATION ===");
        System.out.println("Mode démo activé - Les cartes sont préparées");
        System.out.println();
        
        new MenuGUI();
    }
}
