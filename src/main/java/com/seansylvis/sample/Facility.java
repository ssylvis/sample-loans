package com.seansylvis.sample;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Debt instrument borrowed from a partner {@link Bank} which we use to extend consumer loans. A facility may have
 * various {@link Covenant}s in place imposed by the banking partner.
 *
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class Facility implements Predicate<Loan> {

  private final Bank bank;
  private final List<Covenant> covenants;
  private final int facilityId;
  private final float interestRate;
  private final int totalAmountCents;

  public Facility(int facilityId, Bank bank, float interestRate, int totalAmountCents) {
    this.bank = bank;
    this.covenants = new ArrayList<>();
    this.facilityId = facilityId;
    this.interestRate = interestRate;
    this.totalAmountCents = totalAmountCents;
  }

  public void addCovenant(Covenant covenant) {
    covenants.add(covenant);
  }

  /**
   * Determines whether this facility allows us to fund the given loan based on any {@link Covenant}s in place on the
   * facility or the banking partner.
   *
   * @param loan the loan to fund
   * @return true if this facility allows funding the loan
   */
  @Override
  public boolean apply(Loan loan) {
    return bank.apply(loan) && Predicates.and(covenants).apply(loan);
  }

  /**
   * Computes the expected yield of a list of loans as funded by this {@link Facility}.
   */
  public int computeExpectedYield(List<Loan> loans) {
    int sum = 0;
    for (Loan loan : loans) {
      sum += loan.computeExpectedYield(this);
    }
    return sum;
  }

  public Bank getBank() {
    return bank;
  }

  public int getFacilityId() {
    return facilityId;
  }

  public float getInterestRate() {
    return interestRate;
  }

  public int getTotalAmountCents() {
    return totalAmountCents;
  }

  @Override
  public String toString() {
    return Arrays.asList(
        String.valueOf(facilityId),
        String.valueOf(interestRate),
        String.valueOf(totalAmountCents)).toString();
  }
}
