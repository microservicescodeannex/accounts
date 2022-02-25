package com.accounts.controller;

import com.accounts.config.AccountsServiceConfig;
import com.accounts.model.Accounts;
import com.accounts.model.Customer;
import com.accounts.model.Properties;
import com.accounts.repository.AccountsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

  private final AccountsRepository accountsRepository;
  private final AccountsServiceConfig accountsServiceConfig;

  public AccountsController(AccountsRepository accountsRepository, AccountsServiceConfig accountsServiceConfig) {

    this.accountsRepository = accountsRepository;
    this.accountsServiceConfig = accountsServiceConfig;
  }

  @PostMapping("/accounts")
  public Accounts getAccountDetails(@RequestBody Customer customer) {
    return accountsRepository.findByCustomerId(customer.getCustomerId());
  }

  @GetMapping("/account/properties")
  public String getPropertyDetails() throws JsonProcessingException {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    Properties properties = new Properties(
        accountsServiceConfig.getMsg(),
        accountsServiceConfig.getBuildVersion(),
        accountsServiceConfig.getMailDetails(),
        accountsServiceConfig.getActiveBranches()
    );

    return ow.writeValueAsString(properties);
  }
}
