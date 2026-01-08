package fr.java.frontend.util; // Adapte selon ton package

import java.time.LocalTime;

public class StoreStatus {
    // Définir les horaires (11h00 à 22h30)
    private static final LocalTime OPEN_TIME = LocalTime.of(11, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 30);
    
    //  Le code secret pour le gérant
    public static final String ADMIN_CODE = "1234";

    // Vérifier l'heure actuelle
    public static boolean isCurrentlyOpen() {
        LocalTime now = LocalTime.now();
        return now.isAfter(OPEN_TIME) && now.isBefore(CLOSE_TIME);
    }
}