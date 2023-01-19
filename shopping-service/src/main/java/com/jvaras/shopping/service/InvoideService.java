package com.jvaras.shopping.service;

import com.jvaras.shopping.entity.Invoice;

import java.util.List;

public interface InvoideService {
    public List<Invoice> findInvoiceAll();

    public Invoice getInvoice(Long id);

    public Invoice createInvoice(Invoice invoice);

    public Invoice updateInvoice(Invoice invoice);

    public Invoice deleteInvoice(Invoice invoice);
}
