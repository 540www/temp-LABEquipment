package com.example.equipment.web;

import com.example.equipment.model.Consumable;
import com.example.equipment.model.Inventory;
import com.example.equipment.repository.ConsumableRepository;
import com.example.equipment.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/consumables")
public class ConsumablesController {

    private final ConsumableRepository consumableRepository;
    private final InventoryRepository inventoryRepository;

    public ConsumablesController(ConsumableRepository consumableRepository, InventoryRepository inventoryRepository) {
        this.consumableRepository = consumableRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping
    public List<Consumable> list() {
        return consumableRepository.findAll();
    }

    @PostMapping
    public Consumable create(@RequestBody Consumable c) {
        return consumableRepository.save(c);
    }

    @GetMapping("/inventory")
    public Inventory getInventory(@RequestParam Long itemId, @RequestParam(required = false) Long labId) {
        return inventoryRepository.findByItemIdAndLaboratoryId(itemId, labId).orElseGet(() -> {
            Inventory i = new Inventory();
            i.setItemId(itemId);
            i.setLaboratoryId(labId);
            i.setQuantity(0);
            i.setSafetyStock(0);
            return i;
        });
    }

    @PostMapping("/inventory/adjust")
    @Transactional
    public Inventory adjust(@RequestParam Long itemId, @RequestParam(required = false) Long labId, @RequestParam Integer delta) {
        Inventory inv = inventoryRepository.findByItemIdAndLaboratoryId(itemId, labId)
                .orElseGet(() -> {
                    Inventory i = new Inventory();
                    i.setItemId(itemId);
                    i.setLaboratoryId(labId);
                    i.setQuantity(0);
                    i.setSafetyStock(0);
                    return i;
                });
        int q = (inv.getQuantity() == null ? 0 : inv.getQuantity()) + delta;
        if (q < 0) throw new IllegalArgumentException("库存不足");
        inv.setQuantity(q);
        return inventoryRepository.save(inv);
    }

    @PostMapping("/issue")
    @Transactional
    public Map<String, Object> issue(@RequestParam @NotNull Long itemId,
                                     @RequestParam(required = false) Long labId,
                                     @RequestParam @NotNull Integer qty) {
        adjust(itemId, labId, -qty);
        return Map.of("ok", true, "itemId", itemId, "labId", labId, "qty", qty);
    }
}


