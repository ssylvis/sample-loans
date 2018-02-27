package com.seansylvis.sample;

import java.util.Arrays;

/**
 * Money to be borrowed by a consumer at a given interest rate. We may or may not choose to fund a particular loan.
 *
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class Loan {

  private final int amountCents;
  private final float defaultLikelihood;
  private final int loanId;
  private final float interestRate;
  private final String state;

  public Loan(int loanId, int amountCents, float defaultLikelihood, float interestRate, String state) {
    this.amountCents = amountCents;
    this.defaultLikelihood = defaultLikelihood;
    this.loanId = loanId;
    this.interestRate = interestRate;
    this.state = state;
  }

  /**
   * Computes the expected yield of this loan as funded by the given {@link Facility}.
   */
  public int computeExpectedYield(Facility facility) {
    return Math.round(
        ((1.0f - defaultLikelihood) * interestRate * amountCents)
            - (defaultLikelihood * amountCents)
            - (facility.getInterestRate() * amountCents)
    );
  }

  public int getAmountCents() {
    return amountCents;
  }

  public float getDefaultLikelihood() {
    return defaultLikelihood;
  }

  public int getLoanId() {
    return loanId;
  }

  public float getInterestRate() {
    return interestRate;
  }

  public String getState() {
    return state;
  }

  @Override
  public String toString() {
    return Arrays.asList(
        String.valueOf(loanId),
        String.valueOf(defaultLikelihood),
        String.valueOf(interestRate),
        String.valueOf(state)).toString();
  }
}
