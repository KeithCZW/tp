package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.testutil.TransactionUtil.INDEX_FIRST_TRANSACTION;
import static seedu.address.testutil.TransactionUtil.INDEX_SECOND_TRANSACTION;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalTransactions.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Field;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.PersonUtil;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class UnpayCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Transaction transactionToEdit = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        Transaction editedTransaction = transactionToEdit.setStatusTo(UnpayCommand.class);
        UnpayCommand command = new UnpayCommand(INDEX_FIRST_TRANSACTION);

        String expectedMessage = UnpayCommand.MESSAGE_SUCCESS;

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setTransaction(transactionToEdit, editedTransaction);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTransactionList().size() + 1);
        UnpayCommand UnpayCommand = new UnpayCommand(outOfBoundIndex);

        assertCommandFailure(UnpayCommand, model, Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showTransactionAtIndex(model, INDEX_FIRST_TRANSACTION);

        Transaction transactionToEdit = model.getFilteredTransactionList().get(INDEX_FIRST_TRANSACTION.getZeroBased());
        Transaction editedTransaction = transactionToEdit.setStatusTo(UnpayCommand.class);
        UnpayCommand command = new UnpayCommand(INDEX_FIRST_TRANSACTION);

        String expectedMessage = UnpayCommand.MESSAGE_SUCCESS;

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showTransactionAtIndex(expectedModel, INDEX_FIRST_TRANSACTION);
        expectedModel.setTransaction(transactionToEdit, editedTransaction);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showTransactionAtIndex(model, INDEX_FIRST_TRANSACTION);

        Index outOfBoundIndex = INDEX_SECOND_TRANSACTION;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getTransactionList().size());

        UnpayCommand UnpayCommand = new UnpayCommand(outOfBoundIndex);

        assertCommandFailure(UnpayCommand, model, Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);
    }


    @Test
    public void equals() {
        final UnpayCommand firstUnpayCommand =
                new UnpayCommand(INDEX_FIRST_TRANSACTION);

        // same values -> returns true
        UnpayCommand secondUnpayCommand =
                new UnpayCommand(INDEX_SECOND_TRANSACTION);

        UnpayCommand firstUnpayCommandCopy = new
                UnpayCommand(INDEX_FIRST_TRANSACTION);

        // same object -> returns true
        assertTrue(firstUnpayCommand.equals(firstUnpayCommand));

        // same values -> return true
        assertTrue(firstUnpayCommand.equals(firstUnpayCommandCopy));

        // null -> returns falseß
        assertFalse(firstUnpayCommand.equals(null));

        // different types -> returns false
        assertFalse(firstUnpayCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(firstUnpayCommand.equals(secondUnpayCommand));
    }
}
