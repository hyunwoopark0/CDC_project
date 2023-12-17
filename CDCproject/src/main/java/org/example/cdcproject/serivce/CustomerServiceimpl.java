package org.example.cdcproject.serivce;

import jakarta.transaction.Transactional;
import org.example.cdcproject.dao.Customer;
import org.example.cdcproject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceimpl implements CustomerSerivce {

    private CustomerRepository customerRepository;

    @Autowired
    public void CustomerServiceimpl(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }
    @Override
    public void register(Customer customer) {
        Customer saveCustomer = this.customerRepository.save(customer);
        System.out.println(saveCustomer);
    }

    @Override
    public void delete(Customer customer) {
        this.customerRepository.deleteByConditions(customer.getFirstName(), customer.getLastName());
    }

}
