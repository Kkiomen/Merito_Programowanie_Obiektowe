package pl.wsb.domain.warehouse;

import com.capgemini.programowanie.obiektowe.SupportedMetalType;

import java.util.*;

public class Warehouse {
    private Map<String, Map<SupportedMetalType, Double>> inventory; // Przechowuje informacje o metalach przechowywanych przez klientów.
    public static final double MAX_VOLUME = 10000.0; // Przykładowa maksymalna dostępna objętość magazynu.

    public Warehouse() {
        this.inventory = new HashMap<>();
    }

    public Map<String, Map<SupportedMetalType, Double>> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Map<SupportedMetalType, Double>> inventory) {
        this.inventory = inventory;
    }
}
