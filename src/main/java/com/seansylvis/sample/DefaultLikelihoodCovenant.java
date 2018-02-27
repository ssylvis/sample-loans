package com.seansylvis.sample;

/**
 * {@link Covenant} based on the default likelihood of the loan.
 *
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class DefaultLikelihoodCovenant implements Covenant {

  private final float maxDefaultLikelihood;

  public DefaultLikelihoodCovenant(float maxDefaultLikelihood) {
    this.maxDefaultLikelihood = maxDefaultLikelihood;
  }

  @Override
  public boolean apply(Loan loan) {
    return loan.getDefaultLikelihood() <= maxDefaultLikelihood;
  }
}
