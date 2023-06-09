package com.smartWorkers.gestionBudgets.entities;

import java.util.Iterator;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Categories {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long categorie_id;
  private String name;
  private String description;

  @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
  private List<Budgets> budgets;

  @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
  private List<Transactions> transactions;

  public Categories() {
  }
  public double getAllAmounts(){
    Double amount = (double) 0;
    Iterator iterator = budgets.iterator();
    while (iterator.hasNext()){
      Budgets budgets1 = (Budgets) iterator.next();
      amount += budgets1.getbudgetLimit();
    }
    return amount;
  }
  public Categories(String name, String description, List<Budgets> budgets, List<Transactions> transactions) {
    super();
    this.name = name;
    this.description = description;
    this.budgets = budgets;
    this.transactions = transactions;
  }

  public List<Transactions> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<Transactions> transactions) {
    this.transactions = transactions;
  }

  public Long getCategorie_id() {
    return categorie_id;
  }

  public void setCategorie_id(Long categorie_id) {
    this.categorie_id = categorie_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Budgets> getBudgets() {
    return budgets;
  }

  public void setBudgets(List<Budgets> budgets) {
    this.budgets = budgets;
  }

}
