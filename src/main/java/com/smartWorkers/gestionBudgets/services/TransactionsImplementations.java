package com.smartWorkers.gestionBudgets.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.smartWorkers.gestionBudgets.dao.TransactionsRepository;
import com.smartWorkers.gestionBudgets.entities.Transactions;

@Service
public class TransactionsImplementations implements TransactionsService {

  private TransactionsRepository transactionRepository;

  public TransactionsImplementations(TransactionsRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  @Override
  public List<Transactions> getTransactions(long user_id) {
    return transactionRepository.findAllByUserId(user_id);
  }

  @Override
  public Page<Transactions> getTransactionsInPages(int page, int size, long user_id) {
    return transactionRepository.findByUserId(user_id, PageRequest.of(page, size));
  }

  @Override
  public List<Transactions> findByCategorie(Long categorie_id, long user_id) {
    return transactionRepository.findByCategorieId(categorie_id, user_id);
  }

  @Override
  public Transactions getTransactionById(Long transaction_id) {
    return transactionRepository.findById(transaction_id).get();
  }

  @Override
  public void deleteTransaction(Long id) {
    transactionRepository.deleteById(id);
  }

  @Override
  public void udpateTransaction(Transactions transaction) {
    transactionRepository.save(transaction);
  }

  @Override
  public void addTransaction(Transactions transaction) {
    transactionRepository.save(transaction);
  }

  @Override
  public Page<Transactions> filterByType(String Type, int page, int size, long user_id) {
    return transactionRepository.findByType(Type, user_id, PageRequest.of(page, size));
  }

  @Override
  public List<Float> getExpensesCountsByMonth(long user_id) {
    return transactionRepository.getExpensesAmountForEveryMonth(user_id);
  }

  @Override
  public List<Float> getIncomeCountsByMonth(long user_id) {
    return transactionRepository.getIncomeAmountForEveryMonth(user_id);
  }

  @Override
  public int getCountIncomes(long user_id) {
    return transactionRepository.getCountIncomes(user_id);
  }

  @Override
  public int getCountExpenses(long user_id) {
    return transactionRepository.getCountExpenses(user_id);
  }

  @Override
  public Long numberTransactions(long userId) {
    return transactionRepository.countByUserId(userId);
  }

  @Override
  public List<Transactions> getLastTransactions(String Type, long idUser) {
    return this.transactionRepository.getTransactionsInType(Type, idUser);
  }

@Override
public List<Transactions> advancedSearch(Double amountMin, Double amountMax, String type, Date DateCreation,
		Date dateUpdate, Long categorie, long User_id) {
	// TODO Auto-generated method stub
	return this.transactionRepository.advancedSearch(amountMin, amountMax, type, DateCreation, dateUpdate, categorie, User_id);
}
}
