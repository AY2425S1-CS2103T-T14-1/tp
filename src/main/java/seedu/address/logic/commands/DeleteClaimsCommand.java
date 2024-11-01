package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLAIM_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY_TYPE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CLIENTS;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.claim.Claim;
import seedu.address.model.claim.ClaimStatus;
import seedu.address.model.client.Client;
import seedu.address.model.policy.Policy;
import seedu.address.model.policy.PolicySet;
import seedu.address.model.policy.PolicyType;

/**
 * Deletes a claim from an existing policy in the address book.
 * This command allows the user to remove a specified claim from a policy type of a client identified by their index.
 * If no matching client, policy type, or claim is found, an appropriate error message is returned.
 */
public class DeleteClaimsCommand extends Command {

    public static final String COMMAND_WORD = "delete-claim";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a claim from the client identified by the index number used in the displayed client list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_POLICY_TYPE + "POLICY_TYPE "
            + PREFIX_CLAIM_INDEX + "CLAIM_INDEX(must be a positive integer)\n"
            + "Note: Use the 'list-claims' command to find the appropriate claim index "
            + "for the specified policy type.\n\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_POLICY_TYPE + "health "
            + PREFIX_CLAIM_INDEX + "1\n";

    public static final String MESSAGE_DELETE_CLAIM_SUCCESS = "Claim deleted for policy type"
            + "'%1$s' of client: %2$s.\n\n"
            + "Deleted Claim Details:\nStatus: %3$s | Description: %4$s.\n"
            + "Note: The indexing of remaining claims may have changed due to this deletion.";
    public static final String MESSAGE_NO_CLAIM_FOUND = "No claim found at the specified index.";
    public static final String MESSAGE_INVALID_CLIENT_INDEX = "The client index provided is invalid";
    public static final String MESSAGE_NO_POLICY_OF_TYPE = "No policy of type '%1$s' found for client: %2$s";

    private final Index clientIndex;
    private final PolicyType policyType;
    private final Index claimIndex;


    /**
     * Creates a DeleteClaimsCommand to delete the specified claim.
     *
     * @param clientIndex The index of the client in the filtered client list.
     * @param policyType  The type of the policy whose claim is to be deleted.
     * @param claimIndex  The index of the claim to delete.
     */
    public DeleteClaimsCommand(Index clientIndex, PolicyType policyType, Index claimIndex) {
        requireAllNonNull(clientIndex, policyType, claimIndex);
        this.clientIndex = clientIndex;
        this.policyType = policyType;
        this.claimIndex = claimIndex;
    }

    /**
     * Executes the delete claim command by removing the specified claim from a client's policy.
     * Updates the model if successful, or throws a CommandException if the client, policy, or claim is invalid.
     *
     * @param model {@code Model} which the command should operate on.
     * @return A CommandResult indicating the outcome of the command execution.
     * @throws CommandException If the index is invalid, the policy type is not found, or no matching claim found.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Client client = getClientFromModel(model);
        Policy policy = findPolicyByType(client, policyType);

        Claim claimToDelete = deleteClaimFromPolicy(policy);

        Client updatedClient = createUpdatedClient(client);
        updateModel(model, client, updatedClient);

        String successMessage = formatSuccessMessage(client, claimToDelete.getStatus(),
                claimToDelete.getClaimDescription());
        return new CommandResult(successMessage);
    }

    /**
     * Retrieves the client from the model based on the provided index.
     *
     * @param model The model containing the list of clients.
     * @return The client at the specified index.
     * @throws CommandException If the client index is invalid.
     */
    private Client getClientFromModel(Model model) throws CommandException {
        List<Client> lastShownList = model.getFilteredClientList();
        if (clientIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_CLIENT_INDEX);
        }
        return lastShownList.get(clientIndex.getZeroBased());
    }

    /**
     * Deletes the specified claim from the given policy.
     *
     * @param policy The policy from which the claim is to be removed.
     * @throws CommandException If no matching claim is found.
     */
    private Claim deleteClaimFromPolicy(Policy policy) throws CommandException {
        try {
            Claim claimToDelete = policy.getClaimList().getList().get(claimIndex.getZeroBased());
            boolean claimRemoved = policy.removeClaim(claimToDelete);
            if (!claimRemoved) {
                throw new CommandException(MESSAGE_NO_CLAIM_FOUND);
            }
            return claimToDelete;
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_NO_CLAIM_FOUND);
        }
    }

    private Client createUpdatedClient(Client client) {
        PolicySet updatedPolicySet = new PolicySet();
        updatedPolicySet.addAll(client.getPolicies());
        return new Client(client.getName(), client.getPhone(), client.getEmail(),
                client.getAddress(), client.getTags(), updatedPolicySet);
    }

    private void updateModel(Model model, Client originalClient, Client updatedClient) {
        model.setClient(originalClient, updatedClient);
        model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
    }

    /**
     * Formats the success message after deleting a claim.
     *
     * @param client The client whose claim was deleted.
     * @return The formatted success message.
     */
    private String formatSuccessMessage(Client client, ClaimStatus claimStatus, String claimDescription) {
        return String.format(MESSAGE_DELETE_CLAIM_SUCCESS, policyType, client.getName(), claimStatus, claimDescription);
    }


    /**
     * Finds the policy of the specified type for the given client.
     *
     * @param client     The client whose policies are to be searched.
     * @param policyType The type of policy to find.
     * @return The policy of the specified type.
     * @throws CommandException If no policy of the specified type is found.
     */
    private Policy findPolicyByType(Client client, PolicyType policyType) throws CommandException {
        Optional<Policy> policyOptional = client.getPolicies().stream()
                .filter(policy -> policy.getType().equals(policyType))
                .findFirst();

        if (policyOptional.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_POLICY_OF_TYPE, policyType, client.getName()));
        }
        return policyOptional.get();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DeleteClaimsCommand)) {
            return false;
        }
        DeleteClaimsCommand otherCommand = (DeleteClaimsCommand) other;
        return clientIndex.equals(otherCommand.clientIndex)
                && policyType.equals(otherCommand.policyType)
                && claimIndex.equals(otherCommand.claimIndex);
    }
}
