package com.smartWorkers.gestionBudgets.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.smartWorkers.gestionBudgets.entities.Categories;
import com.smartWorkers.gestionBudgets.entities.Transactions;
import com.smartWorkers.gestionBudgets.entities.Users;
import com.smartWorkers.gestionBudgets.services.CategoriesService;
import com.smartWorkers.gestionBudgets.services.TransactionsService;
import com.smartWorkers.gestionBudgets.services.UsersService;

@Controller
public class transactionConrtoller {
  boolean ChangingTypeOfPresentation = false;

  @Autowired
  TransactionsService transactionsService;

  @Autowired
  CategoriesService categoriesService;

  @Autowired
  UsersService usersService;

  @RequestMapping("/searchAdvanced")
	  public String searchAdvanced(ModelMap modelMap, Authentication authentication, 
			  @RequestParam("amountMin") Double amountMin,
			  @RequestParam("amountMax") Double amountMax,
			  @RequestParam("type") String type,
			  @RequestParam("dateCreation") Date dateCreation,
			  @RequestParam("dateUpdate") Date dateUpdate,
			  @RequestParam("categorie") Long categorie) {
	  Users currentUser = usersService.getUserById(authentication);
	  List<Transactions> transactions = transactionsService.advancedSearch(amountMin,
			  amountMax, type, dateCreation, dateUpdate, categorie, currentUser.getUser_id());
	  
	    modelMap.addAttribute("transactions", transactions);
	    if (transactions.isEmpty()) {
	      modelMap.addAttribute("message",
	          "If you see this message that's mean you don't creat any transaction in this date yet, try to creat one\r\n" +
	              "  	Click on \"add\" to add one or click \"Exist\" to go back to the original list");
	    }
	    if (this.ChangingTypeOfPresentation == false) {
	      return "listeTransactionsUsingCards";
	    } else {
	      return "listeTransactions";
	    }
	  }
  @RequestMapping("/redirectionToOriginalList")
  public String redirectionToOriginalList() {
    return "redirect:/Transactions";
  }

  @RequestMapping("/AddTransactions")
  public String RedirectToAddTransaction(ModelMap modelMap) {
    List<Categories> categories = categoriesService.getCategories();
    modelMap.addAttribute("categories", categories);
    return "AddTransactions";
  }

  @RequestMapping("/ChangingType")
  public String changingType() {
    if (this.ChangingTypeOfPresentation == false) {
      this.ChangingTypeOfPresentation = true;

    } else {
      this.ChangingTypeOfPresentation = false;
    }
    return "redirect:/Transactions";
  }

  @RequestMapping("/Transactions")
  public String Transitions(
      ModelMap modelMap,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "3") int size, Authentication authentication) {
    Users currentUser = usersService.getUserById(authentication);
    Page<Transactions> transactions = transactionsService.getTransactionsInPages(page, size, currentUser.getUser_id());
    // List<Transactions> ALLtransactions = transactionsService.getTransactions();
    List<Categories> categories = categoriesService.getCategories();
    modelMap.addAttribute("transactions", transactions);
    modelMap.addAttribute("pages", new int[transactions.getTotalPages()]);
    modelMap.addAttribute("currentPage", page);
    // modelMap.addAttribute("ALLtransactions", ALLtransactions);
    modelMap.addAttribute("categories", categories);
    if (this.ChangingTypeOfPresentation == false) {
      return "listeTransactionsUsingCards";
    } else {
      return "listeTransactions";
    }
  }

  @PostMapping("/filteringWithDate")
  public String filterTransactionsByMonth(@RequestParam("month") int month, ModelMap modelMap,
      Authentication authentication) {
    Users currentUser = usersService.getUserById(authentication);
    // Get the transactions from your service layer
    List<Transactions> transactions = transactionsService.getTransactions(currentUser.getUser_id());
    List<Transactions> ALLtransactions = transactionsService.getTransactions(currentUser.getUser_id());
    // Filter the transactions based on the selected month
    List<Transactions> filteredTransactions = transactions.stream()
        .filter(transaction -> {
          // Get the month of the transaction date
          LocalDate transactionDate = transaction.getCreated_at().toInstant().atZone(ZoneId.systemDefault())
              .toLocalDate();
          int transactionMonth = transactionDate.getMonthValue();

          // Compare the month with the selected month
          return transactionMonth == month;
        })
        .collect(Collectors.toList());

    // Add the filtered transactions to the model
    modelMap.addAttribute("transactions", filteredTransactions);
    modelMap.addAttribute("ALLtransactions", ALLtransactions);
    if (filteredTransactions.isEmpty()) {
      modelMap.addAttribute("message",
          "If you see this message that's mean you don't creat any transaction in this date yet, try to creat one\r\n" +
              "  	Click on \"add\" to add one or click \"Exist\" to go back to the original list");
    }
    if (this.ChangingTypeOfPresentation == false) {
      return "listeTransactionsUsingCards";
    } else {
      return "listeTransactions";
    }
  }

  @PostMapping("/filterWithCategorie")
  public String filterWithCategory(@RequestParam("categorie_id") Long categorie_id, ModelMap modelMap,
      Authentication authentication) {
    Users currentUser = usersService.getUserById(authentication);

    List<Transactions> filteredTransactions = transactionsService.findByCategorie((Long) categorie_id,
        currentUser.getUser_id());
    List<Transactions> ALLtransactions = transactionsService.getTransactions(currentUser.getUser_id());
    List<Categories> categories = categoriesService.getCategories();
    modelMap.addAttribute("transactions", filteredTransactions);
    if (filteredTransactions.isEmpty()) {
      modelMap.addAttribute("message",
          "If you see this message that's mean you don't creat any transaction in this category yet, try to creat one\r\n"
              +
              "  	Click on \"add\" to add one or click \"Exist\" to go back to the original list ");
    }
    modelMap.addAttribute("ALLtransactions", ALLtransactions);
    modelMap.addAttribute("categories", categories);
    if (this.ChangingTypeOfPresentation == false) {
      return "listeTransactionsUsingCards";
    } else {
      return "listeTransactions";
    }
  }

  @PostMapping(path = "/delete")
  public String deleteTransaction(@RequestParam("id") Long id) {
    transactionsService.deleteTransaction(id);
    return "redirect:/Transactions";
  }

  @RequestMapping("/modifier_transaction")
  public String modifierTransaction(@RequestParam("id") Long transaction_id, ModelMap modelMap) {
    Transactions transaction = transactionsService.getTransactionById(transaction_id);
    List<Categories> categories = categoriesService.getCategories();
    modelMap.addAttribute("categories", categories);
    modelMap.addAttribute("transaction", transaction);
    return "editTransaction";
  }

  @RequestMapping("/update_transaction")
  public String updateTransaction(@ModelAttribute("transaction") Transactions new_transaction,
      @RequestParam("date") String date, ModelMap modelMap)
      throws ParseException {
    Long transaction_id = new_transaction.getTransaction_id();
    Transactions old_transaction = transactionsService.getTransactionById(transaction_id);

    if (date != "" && old_transaction.getCreated_at() != new_transaction.getCreated_at()) {
      // conversion de la date
      SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
      Date dateCreation = dateformat.parse(String.valueOf(date));
      old_transaction.setCreated_at(dateCreation);

      Date currentDate = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      old_transaction.setUpdated_at(dateFormat.parse(dateFormat.format(currentDate)));
    }

    old_transaction.setAmount(new_transaction.getAmount());
    old_transaction.setType(new_transaction.getType());
    old_transaction.setCategorie(new_transaction.getCategorie());
    old_transaction.setDescription(new_transaction.getDescription());

    // if (old_transaction.getDescription() != new_transaction.getDescription() &&
    // new_transaction.getDescription().length() > 0D) {
    // old_transaction.setDescription(new_transaction.getDescription());
    // }
    transactionsService.udpateTransaction(old_transaction);

    Transactions updated_Transaction = transactionsService.getTransactionById(transaction_id);
    List<Categories> categories = categoriesService.getCategories();

    modelMap.addAttribute("categories", categories);
    modelMap.addAttribute("message", "Transaction updated successfully !");
    modelMap.addAttribute("transaction", updated_Transaction);
    return "editTransaction";
  }

  @RequestMapping("/saveTransaction")
  public String saveTransaction(ModelMap modelMap,
      @ModelAttribute("transaction") Transactions new_transaction,
      @RequestParam("date") String date,
      @RequestParam("description") String description,
      Authentication authentication) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date dateCreation = dateFormat.parse(date);
    new_transaction.setCreated_at(dateCreation);
    LocalDateTime ldt = LocalDateTime.now();
    String today = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(ldt);
    Date tadayForReal = dateFormat.parse(today);
    new_transaction.setUpdated_at(tadayForReal);
    new_transaction.setDescription(description);

    Users currentUser = usersService.getUserById(authentication);
    new_transaction.setUser(currentUser);

    transactionsService.addTransaction(new_transaction);
    return "redirect:/Transactions";
  }

  @GetMapping("/showOUT")
  public String filterWithType(ModelMap modelMap,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "3") int size, Authentication authentication) {
    Users currentUser = usersService.getUserById(authentication);

    Page<Transactions> filteredTransactions = transactionsService.filterByType("EXPENSE", page, size,
        currentUser.getUser_id());
    Page<Transactions> transactions = transactionsService.getTransactionsInPages(page, size, currentUser.getUser_id());
    modelMap.addAttribute("transactions", filteredTransactions);
    if (filteredTransactions.isEmpty()) {
      modelMap.addAttribute("message",
          "If you see this message that's mean you don't creat any Out transaction yet, try to creat one\r\n" +
              "  	Click on \"add\" to add one or click \"Exist\" to go back to the original list");
    }
    modelMap.addAttribute("pages", new int[transactions.getTotalPages()]);
    modelMap.addAttribute("currentPage", page);
    if (this.ChangingTypeOfPresentation == false) {
      return "listeTransactionsUsingCards";
    } else {
      return "listeTransactions";
    }
  }

  @GetMapping("/showIN")
  public String filterWithTypeIN(ModelMap modelMap,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "3") int size, Authentication authentication) {
    Users currentUser = usersService.getUserById(authentication);

    Page<Transactions> filteredTransactions = transactionsService.filterByType("INCOME", page, size,
        currentUser.getUser_id());
    Page<Transactions> transactions = transactionsService.getTransactionsInPages(page, size, currentUser.getUser_id());
    modelMap.addAttribute("transactions", filteredTransactions);
    if (filteredTransactions.isEmpty()) {
      modelMap.addAttribute("message",
          "If you see this message that's mean you don't creat any Income transaction yet, try to creat one\r\n" +
              "  	Click on \"add\" to add one or click \"Exist\" to go back to the original list");
    }
    modelMap.addAttribute("pages", new int[transactions.getTotalPages()]);
    modelMap.addAttribute("currentPage", page);
    if (this.ChangingTypeOfPresentation == false) {
      return "listeTransactionsUsingCards";
    } else {
      return "listeTransactions";
    }
  }
}
