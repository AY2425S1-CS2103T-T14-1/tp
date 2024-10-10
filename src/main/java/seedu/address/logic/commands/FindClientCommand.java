package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.ClientSearchPredicate;

/**
 * Finds and lists all clients in the address book who match the given criteria.
 * Matching is case insensitive for name, email, address, and policy type.
 * Phone number matching is exact.
 */
public class FindClientCommand extends Command {

    public static final String COMMAND_WORD = "find-client";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all clients matching the given criteria "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pt/POLICY_TYPE]\n"
            + "Example: " + COMMAND_WORD + " n/John Doe p/99998888";

    private final ClientSearchPredicate predicate;

    public FindClientCommand(ClientSearchPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredClientList(predicate);
        int matchedClients = model.getFilteredClientList().size();

        if (matchedClients == 0) {
            return new CommandResult("No clients found matching the given criteria.");
        }

        return new CommandResult(
                String.format(Messages.MESSAGE_CLIENTS_LISTED_OVERVIEW, matchedClients));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindClientCommand // instanceof handles nulls
                && predicate.equals(((FindClientCommand) other).predicate)); // state check
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }

    public ClientSearchPredicate getPredicate() {
        return predicate;
    }
}
