package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Filter;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.transaction.Transaction;
import seedu.address.testutil.PersonUtil;
import seedu.address.testutil.TransactionUtil;

public class AddTransactionCommandTest {

    @Test
    public void constructor_nullTransaction_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddTransactionCommand(null, x -> null));
        assertThrows(NullPointerException.class, () -> new AddTransactionCommand(Index.fromZeroBased(0), null));
    }

    @Test
    public void execute_transactionAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingTransactionAdded modelStub = new ModelStubAcceptingTransactionAdded();
        Transaction validTransaction = TransactionUtil.TRANSACTION_ONE_COMPLETE;
        Index validIndex = TransactionUtil.VALID_INDEX;
        Person validPerson = PersonUtil.AMY;

        CommandResult commandResult = new AddTransactionCommand(validIndex, x -> validTransaction).execute(modelStub);

        assertEquals(String.format(AddTransactionCommand.MESSAGE_SUCCESS, validTransaction), commandResult.getFeedbackToUser());
        assertEquals(List.of(validTransaction), modelStub.transactionAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = PersonUtil.AMY;
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        AddCommand command = new AddCommand(PersonUtil.AMY);

        // same object -> returns true
        assertTrue(command.equals(command));

        // same values -> returns true
        assertTrue(command.equals(new AddCommand(PersonUtil.AMY)));

        // different types -> returns false
        assertFalse(command.equals(1));

        // null -> returns false
        assertFalse(command.equals(null));

        // different person -> returns false
        assertFalse(command.equals(new AddCommand(PersonUtil.BOB)));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortPersonList(Comparator<Person> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTransaction(Transaction target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteTransactionWithId(long personId) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTransaction(Transaction transaction) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTransaction(Transaction target, Transaction editedPerson) {
            throw new AssertionError("This method should not be called.");
        }


        @Override
        public ObservableList<Transaction> getFilteredTransactionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTransactionList(Predicate<Transaction> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingTransactionAdded extends ModelStub {
        final ArrayList<Transaction> transactionAdded = new ArrayList<>();

        @Override
        public void addTransaction(Transaction transaction) {
            requireNonNull(transaction);
            transactionAdded.add(transaction);
        }

        @Override
        public FilteredList<Person> getFilteredPersonList() {
            ObservableList<Person> obs = FXCollections.observableArrayList(PersonUtil.AMY);
            return new FilteredList<Person>(obs);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
