package com.bill.billing.controller;

import com.bill.billing.model.entity.SwapPackage;
import com.bill.billing.model.response.ResponseData;
import com.bill.billing.service.SwapPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packages")
public class SwapPackageController {

    @Autowired
    private SwapPackageService swapPackageService;

    @PostMapping("/create")
    public ResponseEntity<ResponseData<SwapPackage>> create(@RequestBody SwapPackage swapPackage) {
        return swapPackageService.createPackage(swapPackage);
    }

    @PutMapping("/extend/{id}")
    public ResponseEntity<ResponseData<SwapPackage>> extend(@PathVariable Long id, @RequestParam Integer extraDays) {
        return swapPackageService.extendPackage(id, extraDays);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<String>> delete(@PathVariable Long id) {
        return swapPackageService.deletePackage(id);
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAll() {
        return swapPackageService.getAllPackages();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return swapPackageService.getPackageById(id);
    }
}
