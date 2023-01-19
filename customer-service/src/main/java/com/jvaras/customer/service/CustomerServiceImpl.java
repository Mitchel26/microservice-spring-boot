package com.jvaras.customer.service;

import com.jvaras.customer.entity.Customer;
import com.jvaras.customer.entity.Region;
import com.jvaras.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public List<Customer> findCustomerAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findCustomersByRegion(Region region) {
        return customerRepository.findByRegion(region);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer customerdb = customerRepository.findByNumberID(customer.getNumberID());
        if (customerdb != null) {
            return customerdb;
        }
        customer.setState("CREATED");
        customerdb = customerRepository.save(customer);
        return customerdb;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer customerdb = getCustomer(customer.getId());
        if (customerdb == null) {
            return null;
        }

        customerdb.setFirstName(customer.getFirstName());
        customerdb.setLastName(customer.getLastName());
        customerdb.setEmail(customer.getEmail());
        customerdb.setPhotoUrl(customer.getPhotoUrl());

        return customerRepository.save(customerdb);
    }

    @Override
    public Customer deleteCustomer(Customer customer) {
        Customer customerdb = getCustomer(customer.getId());
        if (customerdb == null) {
            return null;
        }
        customer.setState("DELETED");
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
}
