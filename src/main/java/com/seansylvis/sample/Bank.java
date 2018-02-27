package com.seansylvis.sample;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import java.util.ArrayList;
import java.util.List;

/**
 * Banking partner from whom we borrow money through debt {@link Facility}s in order to extend loans to consumers. A
 * bank may require various {@link Covenant}s in place on all facilities or specific facilities.
 *
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class Bank implements Predicate<Loan> {

  private final int bankId;
  private final List<Covenant> covenants;
  private final String name;

  public Bank(int bankId, String name) {
    this.bankId = bankId;
    this.covenants = new ArrayList<>();
    this.name = name;
  }

  public void addCovenant(Covenant covenant) {
    covenants.add(covenant);
  }

  /**
   * Determines whether this bank allows us to fund the given loan based on any {@link Covenant}s in place at the bank.
   *
   * @param loan the loan to fund
   * @return true if this bank allows funding the loan
   */
  @Override
  public boolean apply(Loan loan) {
    return Predicates.and(covenants).apply(loan);
  }

  public int getBankId() {
    return bankId;
  }

  public String getName() {
    return name;
  }

}
