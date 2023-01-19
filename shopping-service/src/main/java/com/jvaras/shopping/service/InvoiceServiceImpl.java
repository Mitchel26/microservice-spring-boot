package com.jvaras.shopping.service;

import com.jvaras.shopping.entity.Invoice;
import com.jvaras.shopping.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoideService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Override
    public List<Invoice> findInvoiceAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice getInvoice(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoicedb = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
        if (invoicedb != null) {
            return invoicedb;
        }

        invoice.setState("CREATED");
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        Invoice invoicedb = getInvoice(invoice.getId());
        if (invoicedb == null) {
            return null;
        }
        invoicedb.setCustomerId(invoice.getCustomerId());
        invoicedb.setDescription(invoice.getDescription());
        invoicedb.setNumberInvoice(invoice.getNumberInvoice());
        invoicedb.getItems().clear();
        invoicedb.setItems(invoice.getItems());
        return invoiceRepository.save(invoicedb);
    }

    @Override
    public Invoice deleteInvoice(Invoice invoice) {
        Invoice invoicedb = getInvoice(invoice.getId());
        if (invoicedb == null) {
            return null;
        }
        invoice.setState("DELETED");
        return invoiceRepository.save(invoice);
    }
}
