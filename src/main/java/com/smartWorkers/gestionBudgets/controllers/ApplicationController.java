package com.smartWorkers.gestionBudgets.controllers;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartWorkers.gestionBudgets.entities.Transactions;
import com.smartWorkers.gestionBudgets.services.TransactionsService;

@Controller
public class ApplicationController {
	@Autowired
	 TransactionsService transactionsService;
	
	
	@RequestMapping("/Transactions")
	public String Transitions(
	ModelMap modelMap,
	@RequestParam(name = "page", defaultValue = "0") int page,
	@RequestParam(name = "size", defaultValue = "1") int size)
	{
	 Page<Transactions> transactions = transactionsService.getTransactionsInPages(page, size);
	modelMap.addAttribute("transactions", transactions);
	modelMap.addAttribute("pages", new int[transactions.getTotalPages()]);
	modelMap.addAttribute("currentPage", page);
	return "listeTransactions";
	}
	
	@PostMapping("/filteringWithDate")
	public String filterTransactionsByMonth(@RequestParam("month") int month, Model model) {
	    // Get the transactions from your service layer
		List<Transactions> transactions = transactionsService.getTransactions();

	    // Filter the transactions based on the selected month
	    List<Transactions> filteredTransactions = transactions.stream()
	        .filter(transaction -> {
	            // Get the month of the transaction date
	            LocalDate transactionDate = transaction.getCreated_at().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	            int transactionMonth = transactionDate.getMonthValue();

	            // Compare the month with the selected month
	            return transactionMonth == month;
	        })
	        .collect(Collectors.toList());

	    // Add the filtered transactions to the model
	    model.addAttribute("transactions", filteredTransactions);
	    if (filteredTransactions.isEmpty()) {
	    	model.addAttribute("message","You don't have any transactions this mounth !");
	
	    }
	    return "listeTransactions"; // Return the name of the view to render the filtered transaction

}
}
