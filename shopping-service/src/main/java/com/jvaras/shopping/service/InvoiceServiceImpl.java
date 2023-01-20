package com.jvaras.shopping.service;

import com.jvaras.shopping.client.CustomerClient;
import com.jvaras.shopping.client.ProductClient;
import com.jvaras.shopping.entity.Invoice;
import com.jvaras.shopping.entity.InvoiceItem;
import com.jvaras.shopping.model.Customer;
import com.jvaras.shopping.model.Product;
import com.jvaras.shopping.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoideService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    ProductClient productClient;

    @Override
    public List<Invoice> findInvoiceAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice getInvoice(Long id) {
        Invoice invoicedb = invoiceRepository.findById(id).orElse(null);
        if (invoicedb != null) {
            Customer customer = customerClient.gerCustomer(invoicedb.getCustomerId()).getBody();
            invoicedb.setCustomer(customer);
            List<InvoiceItem> listItem = invoicedb.getItems().stream().map(invoiceItem -> {
                Product product = productClient.getProduct(invoiceItem.getProductId()).getBody();
                invoiceItem.setProduct(product);
                return  invoiceItem;
            }).collect(Collectors.toList());
            invoicedb.setItems(listItem);
        }
        return invoicedb;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoicedb = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
        if (invoicedb != null) {
            return invoicedb;
        }

        invoice.setState("CREATED");
        invoicedb = invoiceRepository.save(invoice);
        invoicedb.getItems().forEach(invoiceItem -> {
            productClient.updateStockProduct(invoiceItem.getProductId(), invoiceItem.getQuantity() * -1);
        });
        return invoicedb;
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
