package com.joonda.controller;

import com.joonda.model.AccountTransactions;
import com.joonda.repository.AccountTransActionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BalanceController {

  private final AccountTransActionsRepository accountTransActionsRepository;

	@GetMapping("/myBalance")
	public List<AccountTransactions> getBalanceDetails(@RequestParam long id) {
		List<AccountTransactions> accountTransactions = accountTransActionsRepository.
      findByCustomerIdOrderByTransactionDtDesc(id);

    if (accountTransactions != null) {
      return accountTransactions;
    } else {
      return null;
    }
	}
}
