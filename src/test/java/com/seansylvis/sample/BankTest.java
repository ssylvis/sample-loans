package com.seansylvis.sample;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class BankTest {

  @Test
  public void testAllowNoCovenants() {
    Bank bank = createBank();
    Loan loan = createLoan("CA");
    Assert.assertTrue(bank.apply(loan));
  }

  @Test
  public void testAllowManyCovenants() {
    Bank bank = createBank();
    bank.addCovenant(createCovenant("ID"));
    bank.addCovenant(createCovenant("OR"));
    bank.addCovenant(createCovenant("WA"));
    Loan loan = createLoan("CA");
    Assert.assertTrue(bank.apply(loan));
  }

  @Test
  public void testDisallow() {
    Bank bank = createBank();
    bank.addCovenant(createCovenant("CA"));
    Loan loan = createLoan("CA");
    Assert.assertFalse(bank.apply(loan));
  }

  @Test
  public void testDisallowManyCovenants() {
    Bank bank = createBank();
    bank.addCovenant(createCovenant("CA")); // banned state
    bank.addCovenant(createCovenant("OR"));
    bank.addCovenant(createCovenant("WA"));
    Loan loan = createLoan("CA");
    Assert.assertFalse(bank.apply(loan));
  }

  private Bank createBank() {
    return new Bank(1, "bank");
  }

  private Covenant createCovenant(String bannedState) {
    return new BannedStateCovenant(bannedState);
  }

  private Loan createLoan(String state) {
    return new Loan(1, 100, 0.5f, 0.1f, state);
  }

}
