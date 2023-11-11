package pl.wsb.domain.warehouse.repository;

import pl.wsb.domain.warehouse.Warehouse;
import pl.wsb.domain.warehouse.exception.WarehouseNotFoundException;

public class WarehouseRepository {

    private Warehouse warehouse;

    public WarehouseRepository() {
        this.warehouse = new Warehouse();
    }

    public Warehouse getWarehouse() {
        if(this.warehouse == null){
            throw new WarehouseNotFoundException();
        }

        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
