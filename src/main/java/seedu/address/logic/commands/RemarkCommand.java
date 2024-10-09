package seedu.address.logic.commands;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import seedu.address.model.Model;
import seedu.address.commons.core.index.Index;


/**
 * Changes the remark of an existing person in the address book.
 */
public class RemarkCommand extends Command {

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Remark: %2$s";

    private final Index index;
    private final String remark;

    public static final String COMMAND_WORD = "remark";

    

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Edits the remark of the person identified "
        + "by the index number used in the last person listing. "
        + "Existing remark will be overwritten by the input.\n"
        + "Parameters: INDEX (must be a positive integer) "
        + "r/ [REMARK]\n"
        + "Example: " + COMMAND_WORD + " 1 "
        + "r/ Likes to swim.";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET =
            "Remark command not implemented yet";

    public RemarkCommand(Index index, String remark) {
        requireAllNonNull(index, remark);

        this.index = index;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Hello from remark");
    }

    @Override
    public boolean equals(Object other) {
        return other == this 
                || (other instanceof RemarkCommand 
                && index.equals(((RemarkCommand) other).index)
                && remark.equals(((RemarkCommand) other).remark));
    }

}
