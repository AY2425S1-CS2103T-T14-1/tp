package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.policy.PolicyMap;

/**
 * Updates an existing policy for a client in Prudy.
 */
public class UpdatePolicyCommand extends Command {
    public static final String COMMAND_WORD = "update-policy";
    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Policy updated to:\n%2$s";
    public static final String MESSAGE_POLICY_NOT_FOUND = "Policy not found.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates the specified policy for the person identified "
            + "by the index number used in the last person listing. \n"
            + "Parameters: INDEX (must be a positive integer) "
            + "pt/[POLICY_TYPE]\n"
            + "Example: " + COMMAND_WORD + " 1 pt/health";

    private final Index index;
    private final PolicyMap policies;

    /**
     * Creates an UpdatePolicyCommand to update the specified {@code PolicyMap} of a client.
     *
     * @param index   Index of the client in the filtered client list to update the policy.
     * @param policies The new policy to be set.
     */
    public UpdatePolicyCommand(Index index, PolicyMap policies) {
        requireAllNonNull(index, policies);

        this.index = index;
        this.policies = policies;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        //update policymap
        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), policies);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_ARGUMENTS, index.getOneBased(), policies));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UpdatePolicyCommand)) {
            return false;
        }

        UpdatePolicyCommand upc = (UpdatePolicyCommand) other;
        return index.equals(upc.index) && policies.equals(upc.policies);
    }
}
