package com.smartWorkers.gestionBudgets.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.smartWorkers.gestionBudgets.entities.Transactions;

public interface TransactionsService {

  public List<Transactions> getTransactions();

  public Page<Transactions> getTransactionsInPages(int page, int size);

  public List<Transactions> findByCategorie(String categorie);

  public void deleteTransaction(Long id);

  public Transactions getTransactionById(Long transaction_id);

  public void udpateTransaction(Transactions transaction);
  public void addTransaction(Transactions transaction);
}
