package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.policy.EditPolicyDescriptor;
import seedu.address.model.policy.Policy;
import seedu.address.model.policy.PolicySet;
import seedu.address.model.policy.PolicyType;

/**
 * Updates an existing policy for a client in Prudy.
 */
public class EditPolicyCommand extends Command {
    public static final String COMMAND_WORD = "update-policy";
    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Policy updated to:\n%2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field needs to be updated";
    public static final String MESSAGE_POLICY_NOT_FOUND = "Policy of specified type not found.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates the specified policy for the person identified "
            + "by the index number used in the last person listing. \n"
            + "Parameters: INDEX (must be a positive integer) "
            + "pt/[POLICY_TYPE]\n"
            + "Example: " + COMMAND_WORD + " 1 pt/health";

    private final Index index;
    private final EditPolicyDescriptor editPolicyDescriptor;

    /**
     * Creates an UpdatePolicyCommand to update the specified {@code PolicyMap} of a client.
     *
     * @param index   Index of the client in the filtered client list to update the policy.
     * @param editPolicyDescriptor The new optional fields in policy to be set.
     */
    public EditPolicyCommand(Index index, EditPolicyDescriptor editPolicyDescriptor) {
        requireAllNonNull(index, editPolicyDescriptor);

        this.index = index;
        this.editPolicyDescriptor = editPolicyDescriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= model.getFilteredPersonList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        PolicySet personPolicies = personToEdit.getPolicySet();

        PolicyType policyTypeToEdit = editPolicyDescriptor.getPolicyType();
        Policy policyToRemove = findPolicyByType(personPolicies, policyTypeToEdit);

        if (policyToRemove == null) {
            throw new CommandException(MESSAGE_POLICY_NOT_FOUND);
        }

        personPolicies.remove(policyToRemove);
        Policy editedPolicy = createEditedPolicy(policyToRemove, editPolicyDescriptor);
        personPolicies.add(editedPolicy);

        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), personPolicies);

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format("Edited policy of type %s for %s", policyTypeToEdit, personToEdit.getName()));

    }

    /**
     * Finds a policy by the given type in the provided set of policies.
     */
    private Policy findPolicyByType(PolicySet policySet, PolicyType policyType) {
        for (Policy policy : policySet) {
            if (policy.getType().equals(policyType)) {
                return policy;
            }
        }
        return null;
    }

    /**
     * Creates a new Policy by copying the old policy and applying the edited fields.
     */
    private Policy createEditedPolicy(Policy policyToEdit, EditPolicyDescriptor descriptor) {
        PremiumAmount updatedPremiumAmount = descriptor.getPremiumAmount().orElse(policyToEdit.getPremiumAmount());
        CoverageAmount updatedCoverageAmount = descriptor.getCoverageAmount().orElse(policyToEdit.getCoverageAmount());
        ExpiryDate updatedExpiryDate = descriptor.getExpiryDate().orElse(policyToEdit.getExpiryDate());
        Policy policy = Policy.makePolicy(descriptor.getPolicyType(),updatedPremiumAmount,
                updatedCoverageAmount, updatedExpiryDate);
        return policy;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditPolicyCommand)) {
            return false;
        }

        EditPolicyCommand otherCommand = (EditPolicyCommand) other;
        return index.equals(otherCommand.index)
                && editPolicyDescriptor.equals(otherCommand.editPolicyDescriptor);
    }
}
