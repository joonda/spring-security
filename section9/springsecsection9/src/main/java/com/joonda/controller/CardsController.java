package com.joonda.controller;

import com.joonda.model.Cards;
import com.joonda.repository.CardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardsController {

  private final CardsRepository cardsRepository;

	@GetMapping("/myCards")
	public List<Cards> getBalanceDetails(@RequestParam long id) {
    List<Cards> cards = cardsRepository.findByCustomerId(id);
    if (cards != null) {
      return cards;
    } else {
      return null;
    }
  }
}
