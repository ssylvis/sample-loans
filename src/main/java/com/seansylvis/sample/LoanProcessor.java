package com.seansylvis.sample;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Simple streaming loan processor containing a set of {@link Facility}s. Loans are processed via a call to
 * {@link #process(Loan)}, which might result in a facility assigned to the loan.
 *
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class LoanProcessor {

  private final SortedSet<Facility> facilities;
  private final Multimap<Facility, Loan> fundedLoans;

  public LoanProcessor() {
    this.facilities = new TreeSet<>(new FacilityInterestRate());
    this.fundedLoans = HashMultimap.create();
  }

  /**
   * Adds a facility to this loan processor.
   */
  public void addFacility(Facility facility) {
    facilities.add(facility);
  }

  /**
   * @return the list of loans being funded by the given facility
   */
  public List<Loan> getFundedLoans(Facility facility) {
    return new ArrayList<>(fundedLoans.get(facility));
  }

  /**
   * Determines whether we should fund the given loan. In order to process a loan, there must exist a facility:
   * 1. whose {@link Covenant}s, or banking partner covenants, do not disallow the loan
   * 2. has sufficient funds available
   *
   * @param loan the loan to process
   * @return the {@link Facility} assigned to the loan, or null if none is available
   */
  public Facility process(Loan loan) {
    // iterate over available facilities, starting with one with the lowest interest rate
    for (Facility facility : facilities) {
      if (canAssignLoan(facility, loan)) {
        fundedLoans.put(facility, loan);
        return facility;
      }
    }

    // No acceptable facility found, so return null
    return null;
  }

  @Override
  public String toString() {
    return facilities.toString();
  }

  private boolean canAssignLoan(Facility facility, Loan loan) {
    // if the facility allows the loan AND has available funds, then assign the loan
    if (!facility.apply(loan)) {
      return false;
    }

    int projectedSum = sumOfLoans(fundedLoans.get(facility)) + loan.getAmountCents();
    return projectedSum <= facility.getTotalAmountCents();
  }

  private int sumOfLoans(Collection<Loan> loans) {
    int sum = 0;
    for (Loan loan : loans) {
      sum += loan.getAmountCents();
    }
    return sum;
  }

  /**
   * Sorts facilities by their interest rate, using the facilityId as a trivial tie-breaker.
   */
  private static class FacilityInterestRate implements Comparator<Facility> {

    @Override
    public int compare(Facility facility1, Facility facility2) {
      int result = (int) Math.signum(facility1.getInterestRate() - facility2.getInterestRate());
      if (result == 0) {
        result = facility1.getFacilityId() - facility2.getFacilityId();
      }
      return result;
    }
  }
}
