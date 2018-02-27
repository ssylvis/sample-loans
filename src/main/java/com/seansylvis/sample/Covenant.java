package com.seansylvis.sample;

import com.google.common.base.Predicate;

/**
 * A covenant or agreement with a bank that defines what kinds of loans we can fund.
 *
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public interface Covenant extends Predicate<Loan> {

  /**
   * Predicate that returns true if this covenant allows us to fund the given loan.
   */
  @Override
  boolean apply(Loan loan);
}
