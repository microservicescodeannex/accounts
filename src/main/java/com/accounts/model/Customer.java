package com.accounts.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @ToString
public class Customer {

  @Column(name = "customer_id") @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int customerId;

  private String name;

  private String email;

  @Column(name = "mobile_number")
  private String mobileNumber;

  @Column(name = "create_dt")
  private LocalDate createDt;
}
