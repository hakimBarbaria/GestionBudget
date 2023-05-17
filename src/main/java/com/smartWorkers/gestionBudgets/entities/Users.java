package com.smartWorkers.gestionBudgets.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Users {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long user_id;
  private String name;
  @Column(unique=true)
  private String email;
  private String password;
  private Double balance;
  private String role;
  @OneToMany(mappedBy = "user")
  private List<Transactions> transactions;

  public Long getUser_id() {
    return user_id;
  }
  public void setUser_id(Long user_id) {
    this.user_id = user_id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public Double getBalance() {
    return balance;
  }
  public void setBalance(Double balance) {
    this.balance = balance;
  }
  public List<Transactions> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<Transactions> transactions) {
    this.transactions = transactions;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
