package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.*;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FindClientCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ClientSearchPredicate;

/**
 * Parses input arguments and creates a new FindClientCommand object
 */
public class FindClientCommandParser implements Parser<FindClientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindClientCommand
     * and returns a FindClientCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FindClientCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_POLICY_TYPE);

        String preamble = argMultimap.getPreamble().trim();
        String name = argMultimap.getValue(PREFIX_NAME).orElse(preamble.isEmpty() ? null : preamble);
        String phone = argMultimap.getValue(PREFIX_PHONE).orElse(null);
        String email = argMultimap.getValue(PREFIX_EMAIL).orElse(null);
        String address = argMultimap.getValue(PREFIX_ADDRESS).orElse(null);
        String policyType = argMultimap.getValue(PREFIX_POLICY_TYPE).orElse(null);

        // Check if at least one parameter is provided
        if (name == null && phone == null && email == null && address == null && policyType == null) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FindClientCommand.MESSAGE_USAGE));
        }

        // Perform input validation
        if (name != null) {
            name = name.trim();
            validateName(name);
        }
        if (phone != null) {
            phone = phone.trim();
            validatePhone(phone);
        }
        if (email != null) {
            email = email.trim();
            validateEmail(email);
        }
        if (address != null) {
            address = address.trim();
            validateAddress(address);
        }
        if (policyType != null) {
            policyType = policyType.trim();
            validatePolicyType(policyType);
        }

        return new FindClientCommand(new ClientSearchPredicate(name, phone, email, address, policyType));
    }

    private void validateName(String name) throws ParseException {
        if (name.length() < 1 || name.length() > 100) {
            throw new ParseException("Error: Name must be between 1 and 100 characters.");
        }
    }

    private void validatePhone(String phone) throws ParseException {
        if (!phone.matches("\\d{8}")) {
            throw new ParseException("Error: Phone number must be exactly 8 digits.");
        }
    }

    private void validateEmail(String email) throws ParseException {
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new ParseException("Error: Invalid email format. Please provide a valid email address.");
        }
    }

    private void validateAddress(String address) throws ParseException {
        if (address.length() < 1 || address.length() > 255) {
            throw new ParseException("Error: Address must be between 1 and 255 characters.");
        }
    }

    private void validatePolicyType(String policyType) throws ParseException {
        String[] validTypes = {"life", "health", "education"};
        boolean isValid = false;
        for (String type : validTypes) {
            if (type.equalsIgnoreCase(policyType)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new ParseException("Error: Invalid policy type. Please provide a valid policy type (e.g., Life, Health, Education).");
        }
    }
}
