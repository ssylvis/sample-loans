package com.seansylvis.sample;

/**
 * {@link Covenant} based on the state where the loan originated.
 *
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class BannedStateCovenant implements Covenant {

  private final String bannedState;

  public BannedStateCovenant(String bannedState) {
    this.bannedState = bannedState;
  }

  @Override
  public boolean apply(Loan loan) {
    return !loan.getState().equals(bannedState);
  }
}
