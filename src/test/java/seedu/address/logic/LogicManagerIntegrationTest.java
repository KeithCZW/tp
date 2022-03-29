package seedu.address.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.SerializableAddressBookStorage;
import seedu.address.storage.SerializableTempAddressBookStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonUtil;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.testutil.PersonUtil.*;
import static seedu.address.testutil.TypicalPersons.getEmptyTransactions;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

class LogicManagerIntegrationTest {

    private Model model;
    private Storage storage;
    private LogicManager logicManager;

    @TempDir
    public Path testFolder;

    @BeforeEach
    public void setUp() {
        SerializableAddressBookStorage addressBookStorage = new SerializableAddressBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        SerializableTempAddressBookStorage addressBookTempStorage = new SerializableTempAddressBookStorage(
                getTempFilePath("temp ab"));

        storage = new StorageManager(addressBookStorage, userPrefsStorage, addressBookTempStorage);
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        logicManager = new LogicManager(model, storage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void undoPrevModification_success()
            throws Exception {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // Execute add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + REMARK_DESC_AMY + TAG_DESC_FRIEND;

        logicManager.execute(addCommand);
        logicManager.undoPrevModification();

        assertEquals(expectedModel.getAddressBook(), model.getAddressBook());
    }

    @Test
    public void noModificationDoneDoNotCreateTemporaryFile_success()
            throws Exception {
        assertFalse(logicManager.savePrevAddressBookDataInTemp(model.getAddressBook()));
    }

    @Test
    public void modificationDoneCreateTemporaryFile_success()
            throws Exception {
        Person validPerson = PersonUtil.AMY;
        AddressBook newAddressBook = new AddressBook(model.getAddressBook());
        newAddressBook.addPerson(validPerson);

        assertTrue(logicManager.savePrevAddressBookDataInTemp(newAddressBook));
    }

    /**
     * Checks if after executing a command, logicManager is able to successfully save the updated
     * address book.
     * @throws Exception checks for execution of LogicManager is done in {@code LogicManagerTest}
     */
    @Test
    public void execute_storageSuccessfullySaveAfterCommandExecuted()
            throws Exception {
        Person validPerson = PersonUtil.AMY;
        AddressBook newAddressBook = new AddressBook(model.getAddressBook());
        newAddressBook.addPerson(validPerson);

        // Execute add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + REMARK_DESC_AMY + TAG_DESC_FRIEND;

        logicManager.execute(addCommand);
        assertTrue(storage.readAddressBook().isPresent());
        assertEquals(newAddressBook, storage.readAddressBook().get());
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logicManager.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }
}