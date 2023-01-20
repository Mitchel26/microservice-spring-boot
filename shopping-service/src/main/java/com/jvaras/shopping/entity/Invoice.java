package com.jvaras.shopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jvaras.shopping.model.Customer;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_invoice")
    private String numberInvoice;

    private String description;

    @Column(name = "customer_id")
    private Long customerId;

    @Transient
    private Customer customer;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @Valid
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id")
    private List<InvoiceItem> items;



    private String state;

    public Invoice() {
        this.items = new ArrayList<>();
    }

    @PrePersist
    public void prePersist() {
        this.createAt = new Date();
    }
}
