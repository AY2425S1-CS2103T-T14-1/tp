package seedu.address.logic.parser;

import static seedu.address.commons.core.dateformatter.DateFormatter.MM_DD_YYYY_FORMATTER;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_POLICY_TYPE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.POLICY_TYPE_DESC_LIFE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY_COVERAGE_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY_EXPIRY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY_PREMIUM_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY_TYPE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.dateformatter.DateFormatter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddPolicyCommand;
import seedu.address.model.policy.LifePolicy;
import seedu.address.model.policy.Policy;

public class AddPolicyCommandParserTest {
    private final AddPolicyCommandParser parser = new AddPolicyCommandParser();
    private final String premiumAmount = "500.0";
    private final String coverageAmount = "6000.0";
    private final String expiryDate = "12/09/2023";

    @Test
    public void parse_compulsoryFieldPresent_success() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + POLICY_TYPE_DESC_LIFE;
        Policy expectedPolicy = new LifePolicy();
        AddPolicyCommand expectedCommand = new AddPolicyCommand(INDEX_FIRST_PERSON, expectedPolicy);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_optionalFieldsPresent_success() {
        // Absent premiumAmount
        String userInput = INDEX_FIRST_PERSON.getOneBased() + POLICY_TYPE_DESC_LIFE + " "
                + PREFIX_POLICY_COVERAGE_AMOUNT + coverageAmount + " "
                + PREFIX_POLICY_EXPIRY_DATE + expiryDate;
        Policy expectedPolicy = new LifePolicy(-1.0, Double.parseDouble(coverageAmount),
                LocalDate.parse(expiryDate, MM_DD_YYYY_FORMATTER));
        AddPolicyCommand expectedCommand = new AddPolicyCommand(INDEX_FIRST_PERSON, expectedPolicy);
        assertParseSuccess(parser, userInput, expectedCommand);

        // Absent premiumAmount
        userInput = INDEX_FIRST_PERSON.getOneBased() + POLICY_TYPE_DESC_LIFE + " "
                + PREFIX_POLICY_PREMIUM_AMOUNT + premiumAmount + " "
                + PREFIX_POLICY_EXPIRY_DATE + expiryDate;
        expectedPolicy = new LifePolicy(Double.parseDouble(premiumAmount), -1.0,
                LocalDate.parse(expiryDate, MM_DD_YYYY_FORMATTER));
        expectedCommand = new AddPolicyCommand(INDEX_FIRST_PERSON, expectedPolicy);
        assertParseSuccess(parser, userInput, expectedCommand);

        // Absent expiryDate
        userInput = INDEX_FIRST_PERSON.getOneBased() + POLICY_TYPE_DESC_LIFE + " "
                + PREFIX_POLICY_PREMIUM_AMOUNT + premiumAmount + " "
                + PREFIX_POLICY_COVERAGE_AMOUNT + coverageAmount;
        expectedPolicy = new LifePolicy(Double.parseDouble(premiumAmount), Double.parseDouble(coverageAmount),
                null);
        expectedCommand = new AddPolicyCommand(INDEX_FIRST_PERSON, expectedPolicy);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPolicyCommand.MESSAGE_USAGE);

        // No input at all
        assertParseFailure(parser, "", expectedMessage);

        // Missing index
        assertParseFailure(parser, POLICY_TYPE_DESC_LIFE, expectedMessage);

        // Missing policy type
        assertParseFailure(parser, String.valueOf(INDEX_FIRST_PERSON.getOneBased()), expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPolicyCommand.MESSAGE_USAGE);

        // Invalid index (non-numeric input)
        assertParseFailure(parser, "foo" + POLICY_TYPE_DESC_LIFE, expectedMessage);

        // Invalid policy type
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + INVALID_POLICY_TYPE_DESC,
                Policy.POLICY_TYPE_MESSAGE_CONSTRAINTS);

        // Negative premium amount
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + POLICY_TYPE_DESC_LIFE + " "
                + PREFIX_POLICY_PREMIUM_AMOUNT + "-1", Policy.AMOUNT_MESSAGE_CONSTRAINTS);

        // Negative coverage amount
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + POLICY_TYPE_DESC_LIFE + " "
                + PREFIX_POLICY_COVERAGE_AMOUNT + "-1", Policy.AMOUNT_MESSAGE_CONSTRAINTS);

        // Invalid expiry date
        assertParseFailure(parser, INDEX_FIRST_PERSON.getOneBased() + POLICY_TYPE_DESC_LIFE + " "
                + PREFIX_POLICY_EXPIRY_DATE + "99/99/9999", DateFormatter.MM_DD_YYYY_MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_multipleInvalidValues_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPolicyCommand.MESSAGE_USAGE);

        // Invalid index and invalid policy type
        assertParseFailure(parser, "foo" + INVALID_POLICY_TYPE_DESC, expectedMessage);
    }

    @Test
    public void parse_validIndexAndInvalidPolicyType_failure() {
        // Valid index but invalid policy type
        assertParseFailure(parser, INDEX_SECOND_PERSON.getOneBased() + INVALID_POLICY_TYPE_DESC,
                Policy.POLICY_TYPE_MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validPolicyAndInvalidIndex_failure() {
        // Valid policy but invalid index
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPolicyCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "foo" + POLICY_TYPE_DESC_LIFE, expectedMessage);
    }

    @Test
    public void parse_multiplePolicies_failure() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_POLICY_TYPE);

        // Index present but no policies
        assertParseFailure(parser, String.valueOf(INDEX_FIRST_PERSON.getOneBased()) + POLICY_TYPE_DESC_LIFE
                + POLICY_TYPE_DESC_LIFE, expectedMessage);
    }
}
