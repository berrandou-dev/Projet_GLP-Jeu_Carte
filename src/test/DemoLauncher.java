package test;

import gui.screens.MenuGUI;

public class DemoLauncher {
    public static void main(String[] args) {
        // Active le mode démonstration
        System.setProperty("demo.mode", "true");
        
        System.out.println("=== TU N'Y PEUX RIEN - DÉMONSTRATION ===");
        System.out.println("Mode démo activé - Les cartes sont préparées");
        System.out.println();
        System.out.println("DÉROULEMENT DE LA DÉMO :");
        System.out.println("1. Menu → Règles (popup) → Fermer");
        System.out.println("2. Nouvelle partie → Options");
        System.out.println("3. Montrer +/- joueurs et difficultés");
        System.out.println("4. Démarrer → Partie avec cartes préparées");
        System.out.println();
        
        new MenuGUI();
    }
}
