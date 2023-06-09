package com.smartWorkers.gestionBudgets.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smartWorkers.gestionBudgets.entities.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

  @Query("SELECT t FROM Transactions t WHERE t.user.id = :userId")
  List<Transactions> findAllByUserId(long userId);

  @Query("SELECT t FROM Transactions t WHERE t.user.id = :userId")
  Page<Transactions> findByUserId(long userId, Pageable pageable);

  @Query("select t from Transactions t where t.categorie.categorie_id = :categorie_id and t.user.id = :userId")
  List<Transactions> findByCategorieId(Long categorie_id, long userId);

  @Query("SELECT t FROM Transactions t WHERE t.user.id = :userId AND t.type = :type")
  Page<Transactions> findByType(String type, long userId, Pageable pageable);

  @Query(value = "SELECT COALESCE(SUM(amount), 0) as count FROM (SELECT 1 as month_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) m LEFT JOIN transactions ON MONTH(created_at) = m.month_num and type = 'EXPENSE' AND user_id = ?1 GROUP BY m.month_num ORDER BY m.month_num", nativeQuery = true)
  List<Float> getExpensesAmountForEveryMonth(long user_id);

  @Query(value = "SELECT COALESCE(SUM(amount), 0) as count FROM (SELECT 1 as month_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) m LEFT JOIN transactions ON MONTH(created_at) = m.month_num and type = 'INCOME' AND user_id = ?1 GROUP BY m.month_num ORDER BY m.month_num", nativeQuery = true)
  List<Float> getIncomeAmountForEveryMonth(long user_id);

  @Query("SELECT COUNT(*) FROM Transactions t WHERE t.type = 'INCOME' AND t.user.id = ?1")

  int getCountIncomes(long user_id);

  @Query("SELECT COUNT(*) FROM Transactions t WHERE t.type = 'EXPENSE' AND t.user.id = ?1")
  int getCountExpenses(long user_id);

  @Query("select t from Transactions t where t.type= ?1 and t.user.id = ?2 order by t.transaction_id DESC limit 3")
  List<Transactions> getTransactionsInType(String type, long userId);

  @Query("SELECT COUNT(t) FROM Transactions t WHERE t.user.id = :userId")
  Long countByUserId(long userId);
  
  @Query("SELECT t FROM Transactions t WHERE t.amount BETWEEN ?1 AND ?2 AND t.type = ?3 AND t.created_at = ?4 "
  		+ "AND t.updated_at = ?5 AND t.categorie.id = ?6 AND t.user.id = ?7")
  List<Transactions> advancedSearch(double amountMin, double amountMax, String type, Date DateCreation, Date dateUpdate, 
		  Long categorie, long userId);


}
