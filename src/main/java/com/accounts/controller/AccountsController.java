package com.accounts.controller;

import com.accounts.config.AccountsServiceConfig;
import com.accounts.model.*;
import com.accounts.repository.AccountsRepository;
import com.accounts.service.client.CardsFeignClient;
import com.accounts.service.client.LoansFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountsController {

  private final AccountsRepository accountsRepository;
  private final AccountsServiceConfig accountsServiceConfig;
  private final CardsFeignClient cardsFeignClient;
  private final LoansFeignClient loansFeignClient;

  public AccountsController(
      AccountsRepository accountsRepository,
      AccountsServiceConfig accountsServiceConfig,
      CardsFeignClient cardsFeignClient,
      LoansFeignClient loansFeignClient
  ) {

    this.accountsRepository = accountsRepository;
    this.accountsServiceConfig = accountsServiceConfig;
    this.cardsFeignClient = cardsFeignClient;
    this.loansFeignClient = loansFeignClient;
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

  @PostMapping("/customerDetails")
  public CustomerDetails customerDetails(@RequestBody Customer customer) {
    Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
    List<Cards> cards = cardsFeignClient.getCardDetails(customer);
    List<Loans> loans = loansFeignClient.getLoanDetails(customer);

    CustomerDetails customerDetails = new CustomerDetails();

    customerDetails.setAccounts(accounts);
    customerDetails.setCards(cards);
    customerDetails.setLoans(loans);

    return customerDetails;
  }
}
