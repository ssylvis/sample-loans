package com.seansylvis.sample;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class DefaultLikelihoodCovenantTest {

  @Test
  public void testAllowLessThan() {
    Covenant covenant = createCovenant(0.5f);
    Loan loan = createLoan(0.1f);
    Assert.assertTrue(covenant.apply(loan));
  }

  @Test
  public void testAllowEqual() {
    Covenant covenant = createCovenant(0.5f);
    Loan loan = createLoan(0.5f);
    Assert.assertTrue(covenant.apply(loan));
  }

  @Test
  public void testDisallowGreaterThan() {
    Covenant covenant = createCovenant(0.5f);
    Loan loan = createLoan(0.51f);
    Assert.assertFalse(covenant.apply(loan));
  }

  private Covenant createCovenant(float maxDefaultLikelihood) {
    return new DefaultLikelihoodCovenant(maxDefaultLikelihood);
  }

  private Loan createLoan(float defaultLikelihood) {
    return new Loan(1, 100, defaultLikelihood, 0.1f, "CA");
  }

}
