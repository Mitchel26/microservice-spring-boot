package com.jvaras.shopping.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvaras.shopping.entity.Invoice;
import com.jvaras.shopping.service.InvoideService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoideService invoideService;

    @GetMapping
    public ResponseEntity<List<Invoice>> listAllInvoices() {
        List<Invoice> invoices = invoideService.findInvoiceAll();
        if (invoices == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable("id") Long id) {
        log.info("Fetching Invoice with id {}", id);
        Invoice invoice = invoideService.getInvoice(id);
        if (invoice == null) {
            log.info("Invoice with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice);
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice, BindingResult result) {
        log.info("Creating Invoice: {}", invoice);
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Invoice invoicedb = invoideService.createInvoice(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoicedb);

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable("id") Long id, @RequestBody Invoice invoice) {
        log.info("Updating Invoice with id {}", id);
        Invoice invoicedb = invoideService.getInvoice(id);
        if (invoicedb == null) {
            log.info("Unable to enable. Invoice with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        invoice.setId(id);
        invoicedb = invoideService.updateInvoice(invoice);
        return ResponseEntity.ok(invoicedb);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Invoice> deleteInvoice(@PathVariable("id") Long id) {
        Invoice invoice = invoideService.getInvoice(id);
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }
        invoice = invoideService.deleteInvoice(invoice);
        return ResponseEntity.ok(invoice);
    }


    private String formatMessage(BindingResult result) {
        List<Map<String, String>> errors = result.getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
