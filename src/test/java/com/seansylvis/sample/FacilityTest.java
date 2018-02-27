package com.seansylvis.sample;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class FacilityTest {

  @Test
  public void testAllowNoCovenants() {
    Facility facility = createFacility();
    Loan loan = createLoan("CA");
    Assert.assertTrue(facility.apply(loan));
  }

  @Test
  public void testAllowBankCovenants() {
    Facility facility = createFacility();
    Bank bank = facility.getBank();
    bank.addCovenant(createCovenant("ID"));
    bank.addCovenant(createCovenant("OR"));
    bank.addCovenant(createCovenant("WA"));
    Loan loan = createLoan("CA");
    Assert.assertTrue(facility.apply(loan));
  }

  @Test
  public void testAllowCombinedCovenants() {
    Facility facility = createFacility();
    facility.addCovenant(createCovenant("ID"));
    facility.getBank().addCovenant(createCovenant("OR"));
    facility.addCovenant(createCovenant("WA"));
    Loan loan = createLoan("CA");
    Assert.assertTrue(facility.apply(loan));
  }

  @Test
  public void testAllowManyCovenants() {
    Facility facility = createFacility();
    facility.addCovenant(createCovenant("ID"));
    facility.addCovenant(createCovenant("OR"));
    facility.addCovenant(createCovenant("WA"));
    Loan loan = createLoan("CA");
    Assert.assertTrue(facility.apply(loan));
  }

  @Test
  public void testDisallow() {
    Facility facility = createFacility();
    facility.addCovenant(createCovenant("CA"));
    Loan loan = createLoan("CA");
    Assert.assertFalse(facility.apply(loan));
  }

  @Test
  public void testDisallowBankCovenant() {
    Facility facility = createFacility();
    Bank bank = facility.getBank();
    bank.addCovenant(createCovenant("CA")); // banned state
    bank.addCovenant(createCovenant("OR"));
    bank.addCovenant(createCovenant("WA"));
    Loan loan = createLoan("CA");
    Assert.assertFalse(facility.apply(loan));
  }

  @Test
  public void testDisallowCombinedCovenants() {
    Facility facility = createFacility();
    facility.getBank().addCovenant(createCovenant("CA")); // banned state
    facility.addCovenant(createCovenant("OR"));
    facility.addCovenant(createCovenant("WA"));
    Loan loan = createLoan("CA");
    Assert.assertFalse(facility.apply(loan));
  }

  @Test
  public void testDisallowManyCovenants() {
    Facility facility = createFacility();
    facility.addCovenant(createCovenant("CA")); // banned state
    facility.addCovenant(createCovenant("OR"));
    facility.addCovenant(createCovenant("WA"));
    Loan loan = createLoan("CA");
    Assert.assertFalse(facility.apply(loan));
  }

  private Covenant createCovenant(String bannedState) {
    return new BannedStateCovenant(bannedState);
  }

  private Facility createFacility() {
    return new Facility(1, new Bank(1, "bank"), 0.1f, 100);
  }

  private Loan createLoan(String state) {
    return new Loan(1, 100, 0.5f, 0.1f, state);
  }

}
