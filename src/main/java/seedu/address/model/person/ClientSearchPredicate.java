package seedu.address.model.person;

import java.util.function.Predicate;

/**
 * Tests that a {@code Client} matches the given search criteria.
 */
public class ClientSearchPredicate implements Predicate<Person> {
    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String policyType;

    public ClientSearchPredicate(String name, String phone, String email, String address, String policyType) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.policyType = policyType;
    }

    @Override
    public boolean test(Person client) {
        boolean matches = true;

        if (name != null && !name.isEmpty()) {
            matches &= client.getName().fullName.toLowerCase().contains(name.toLowerCase());
        }

        if (phone != null && !phone.isEmpty()) {
            matches &= client.getPhone().value.equals(phone.trim());
        }

        if (email != null && !email.isEmpty()) {
            matches &= client.getEmail().value.equalsIgnoreCase(email.trim());
        }

        if (address != null && !address.isEmpty()) {
            matches &= client.getAddress().value.toLowerCase().contains(address.toLowerCase());
        }

        // if (policyType != null && !policyType.isEmpty()) {
        //     matches &= client.getPolicyType().toString().equalsIgnoreCase(policyType.trim());
        // }

        return matches;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ClientSearchPredicate)) {
            return false;
        }
        ClientSearchPredicate otherPredicate = (ClientSearchPredicate) other;
        return name.equals(otherPredicate.name)
                && phone.equals(otherPredicate.phone)
                && email.equals(otherPredicate.email)
                && address.equals(otherPredicate.address)
                && policyType.equals(otherPredicate.policyType);
    }
}
