package com.seansylvis.sample;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class LoanProcessorTest {

  @Test
  public void testProcessNoFacilities() {
    LoanProcessor loanProcessor = new LoanProcessor();
    Loan loan = createLoan(1, 100, 0.1f, "CA");
    Assert.assertNull(loanProcessor.process(loan));
  }

  @Test
  public void testProcessOneFacility() {
    LoanProcessor loanProcessor = new LoanProcessor();
    Facility facility = createFacility(1, 200);
    loanProcessor.addFacility(facility);

    Loan loan0 = createLoan(1, 100, 0.1f, "CA");
    Assert.assertEquals(facility, loanProcessor.process(loan0));
    Loan loan1 = createLoan(2, 100, 0.1f, "CA");
    Assert.assertEquals(facility, loanProcessor.process(loan1));
    Loan loan2 = createLoan(3, 1, 0.1f, "CA");
    Assert.assertNull(loanProcessor.process(loan2));
  }

  @Test
  public void testProcessOneFacilityManyCovenants() {
    LoanProcessor loanProcessor = new LoanProcessor();
    Facility facility = createFacility(1, 200);
    loanProcessor.addFacility(facility);

    facility.addCovenant(createCovenant("ID"));
    facility.addCovenant(createCovenant("OR"));
    facility.addCovenant(createCovenant("WA"));

    Loan loan0 = createLoan(1, 100, 0.1f, "CA");
    Assert.assertEquals(facility, loanProcessor.process(loan0));
    Loan loan1 = createLoan(2, 100, 0.1f, "OR");
    Assert.assertNull(loanProcessor.process(loan1));
    Loan loan2 = createLoan(3, 100, 0.1f, "CA");
    Assert.assertEquals(facility, loanProcessor.process(loan2));
  }

  @Test
  public void testProcessManyFacilities() {
    LoanProcessor loanProcessor = new LoanProcessor();
    Facility facility0 = createFacility(1, 0.1f, 50);
    loanProcessor.addFacility(facility0);
    Facility facility1 = createFacility(2, 0.1f, 100);
    loanProcessor.addFacility(facility1);
    Facility facility2 = createFacility(3, 0.05f, 50);
    loanProcessor.addFacility(facility2);

    Loan loan0 = createLoan(1, 50, 0.1f, "CA");
    Assert.assertEquals(facility2, loanProcessor.process(loan0));
    Loan loan1 = createLoan(2, 75, 0.1f, "CA");
    Assert.assertEquals(facility1, loanProcessor.process(loan1));
    Loan loan2 = createLoan(3, 50, 0.1f, "CA");
    Assert.assertEquals(facility0, loanProcessor.process(loan2));
    Loan loan3 = createLoan(4, 30, 0.1f, "CA");
    Assert.assertNull(loanProcessor.process(loan3));
  }

  @Test
  public void testProcessManyFacilitiesManyCovenants() {
    LoanProcessor loanProcessor = new LoanProcessor();
    Facility facility0 = createFacility(1, 0.1f, 50);
    loanProcessor.addFacility(facility0);
    Facility facility1 = createFacility(2, 0.05f, 50);
    loanProcessor.addFacility(facility1);
    Facility facility2 = createFacility(3, 0.025f, 50);
    loanProcessor.addFacility(facility2);

    facility0.addCovenant(createCovenant("ID"));
    facility1.addCovenant(createCovenant("OR"));
    facility2.addCovenant(createCovenant("WA"));

    Loan loan0 = createLoan(1, 50, 0.1f, "CA");
    Assert.assertEquals(facility2, loanProcessor.process(loan0));
    Loan loan1 = createLoan(2, 75, 0.1f, "CA");
    Assert.assertNull(loanProcessor.process(loan1));
    Loan loan2 = createLoan(3, 50, 0.1f, "OR");
    Assert.assertEquals(facility0, loanProcessor.process(loan2));
    Loan loan3 = createLoan(4, 25, 0.1f, "ID");
    Assert.assertEquals(facility1, loanProcessor.process(loan3));
    Loan loan4 = createLoan(4, 25, 0.1f, "OR");
    Assert.assertNull(loanProcessor.process(loan4));
    Loan loan5 = createLoan(4, 25, 0.1f, "CA");
    Assert.assertEquals(facility1, loanProcessor.process(loan5));
  }

  private Covenant createCovenant(String bannedState) {
    return new BannedStateCovenant(bannedState);
  }

  private Facility createFacility(int facilityId, int totalAmountCents) {
    return createFacility(facilityId, 0.1f, totalAmountCents);
  }

  private Facility createFacility(int facilityId, float interestRate, int totalAmountCents) {
    return new Facility(facilityId, new Bank(1, "bank"), interestRate, totalAmountCents);
  }

  private Loan createLoan(int loanId, int amountCents, float interestRate, String state) {
    return new Loan(loanId, amountCents, 0.5f, interestRate, state);
  }

}
