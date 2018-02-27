package com.seansylvis.sample;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class BannedStateCovenantTest {

  @Test
  public void testAllow() {
    Covenant covenant = createCovenant("CA");
    Loan loan = createLoan("WA");
    Assert.assertTrue(covenant.apply(loan));
  }

  @Test
  public void testDisallow() {
    Covenant covenant = createCovenant("CA");
    Loan loan = createLoan("CA");
    Assert.assertFalse(covenant.apply(loan));
  }

  private Covenant createCovenant(String bannedState) {
    return new BannedStateCovenant(bannedState);
  }

  private Loan createLoan(String state) {
    return new Loan(1, 100, 0.5f, 0.1f, state);
  }

}
