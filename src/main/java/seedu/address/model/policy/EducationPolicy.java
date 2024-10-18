package seedu.address.model.policy;

import java.time.LocalDate;
import java.time.Period;

/**
 * A Policy of type Education.
 */
public class EducationPolicy extends Policy {
    private static final double DEFAULT_PREMIUM_AMOUNT = 50;
    private static final double DEFAULT_COVERAGE_AMOUNT = 5000;
    private static final Period DEFAULT_EXPIRY_DATE_PERIOD = Period.ofYears(5);

    /**
     * Constructor for a new EducationPolicy initialized with defaults.
     */
    public EducationPolicy() {
        super(DEFAULT_PREMIUM_AMOUNT, DEFAULT_COVERAGE_AMOUNT, LocalDate.now().plus(DEFAULT_EXPIRY_DATE_PERIOD));
    }

    /**
     * Constructor for a new EducationPolicy with selected fields initialized.
     * Use a negative number (i.e., -1) for premiumAmount and coverageAmount to initialize
     * the default values for these fields.
     * Use null for expiryDate to initialize the default expiryDate.
     *
     * @param premiumAmount the price of the policy, paid per month.
     *                      Use a negative number to initialize this policy with the default premiumAmount.
     * @param coverageAmount the maximum amount that can be claimed under this policy.
     *                       Use a negative number to initialize this policy with the default coverageAmount.
     * @param expiryDate the date of Policy's expiry.
     *                   Use null to initialize this policy with the default expiryDate.
     */
    public EducationPolicy(double premiumAmount, double coverageAmount, LocalDate expiryDate) {
        super(
                premiumAmount < 0 ? DEFAULT_PREMIUM_AMOUNT : premiumAmount,
                coverageAmount < 0 ? DEFAULT_COVERAGE_AMOUNT : coverageAmount,
                expiryDate == null ? LocalDate.now().plus(DEFAULT_EXPIRY_DATE_PERIOD) : expiryDate);
    }

    @Override
    public PolicyType getType() {
        return PolicyType.EDUCATION;
    }

    @Override
    public String toString() {
        return String.format("Policy type: %s | ", getType()) + super.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EducationPolicy)) {
            return false;
        }

        EducationPolicy p = (EducationPolicy) other;
        return super.equals(p);
    }
}