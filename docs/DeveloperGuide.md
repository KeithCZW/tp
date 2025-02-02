---
layout: page
title: CinnamonBun Developer Guide
---
* Table of Contents
1. Acknowledgements
2. Setting up
3. Design
   1. Architecture
   2. UI Components
4. Implementation

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Membership functionality

#### Proposed Implementation
The proposed membership functionality will be to store all available memberships into a list and allow clients to be assigned a membership.

Membership details will be created by users, user can then assign an existing membership to a client.

#### Design considerations:

**Aspect: How it executes:**

* **Alternative 1 (current choice):** Create a list of Memberships, assign membership index to client.
  * Pros: Allows for easy management of memberships
  * Cons: Have to handle edge cases, what if user deletes a membership? etc.

* **Alternative 2:** Create a new membership whenever assigning a membership to client.
  * Pros: Easy to implement
  * Cons: Harder to manage individual memberships, functions similar to a tag, but with extra variables.

### \[Proposed\] Transaction functionality

#### Proposed Implementation
The proposed Transaction Functionality will allow users to store a transaction and assign it to a client. 
User will have to specify the client the transaction will be assigned to, and input all the transaction's attributes.

The current implementation of `Transaction` class is similar to Person class. Every field/attribute of transaction needs to 
extend from the `TransactionField` class. The `Transaction` class will have a list of `TransactionField`s in which all of it's 
fields must be registered in the `TransactionFieldRegistry`. Each field is either a required field or an optional field. 

Transaction class consists of fields `Amount`, `TransactionDate`, `DueDate`, and `Note`.

#### Design considerations:

**Aspect: How it executes:**

* **Alternative 1:** Create a list (`FilteredList`) of Transactions, controlled by `ModelManager`. 
    Everytime a user create a transaction, a new instance of transaction will be added to the list and a person/client
  specified by it's unique identifier (`Email`) will be referenced by this transaction. To list all of the transactions 
    of a particular person, the `FilteredList` should be updated to only contain `Transaction`
    with a reference to the person's id. 
    * Pros: Consistent design with the Person class.
    * Cons: Have to handle cases when a user is updated/removed. The input specified by the users 
    corresponds to the index of the displayed clients/users. Hence we need to retrieve the client's attributes 
    before initializing the Transaction object.


* **Alternative 2 (current implementation):** Every person object has a list of transactions which will be
    initialized with an empty list. Each time a user add a transaction, the object will be 
    added into the specified Person's Transaction List.
    * Pros: Easy to implement
    * Cons: Lower abstraction especially when displaying the transaction to the UI. Inconsistent design
    in comparison to the `Person` class.

### \[Proposed\] Sort functionality
**Proposed implementation**

The proposed sort mechanism is facilitated by `SortCommand`. It extends `Command` and the main logic of sort is in it's
`execute` function which returns a `CommandResult` object.

The `SortCommand#execute()` function would first parse the user's inputs. For every field parsed, the function would create a 
`comparator` for that field using either of the functions:

* `SortCommand#getComparatorDefault()` --- Creates a comparator with the field specified in ascending order
* `SortCommand#getComparatorDescending()` --- Creates a comparator with the field specified in descending order

One sort command allows for sorting multiple fields at a time in the order specified. Stating `sort n/ a/` means 
sort the list by name in ascending order followed by the client's addresses. Clients with same name would be then
sorted based on their addresses. 

Thus, after creating the `comparator` for a particular field, it'll be added upon a previously created comparator using.

To be able to sort the client's list, we exposed an operation in the `Model` interface as `Model#sortPersonList()`.
We then passed the `comparator` created and passed it to `Model#sortPersonList()` in `SortCommand#execute()`.

Java's `list` library will then handle the sorting based on the `comparator`.

#### Design considerations:

**Aspect: How it executes:**

* **Alternative 1 (current choice):** Each field class will handle how to sort its own data, `SortCommand` will then
wrap it into a comparator and pass to `Model#sortPersonList()`.
  * Pros: Easy to implement, each field class can handle their own sorting of its data. Will not clutter `SortCommand`.
  * Cons: Does not allow for more complicated chaining of fields since the way each field is being sorted is independent of the other.
  

* **Alternative 2:** `SortCommand` will determine how the fields are to be sorted.
    * Pros: Allows `SortCommand` to have full flexibility in deciding how the fields are to be sorted and may allow for
  more complicated chaining of fields.
    * Cons: Will clutter `SortCommand` and may not be manageable once there are a lot of fields.


### \[Proposed\] Command chaining
**Proposed implementation**

The proposed command chaining mechanism is facilitated in the `execute()` function in the `LogicManager` class which is where the user's input is parsed, executed and then returned as a `CommandResult`.

To facilitate multiple commands, the program will split the given user input by a specified delimiter - in this case `|` will be used to seperate multiple commands. Once the input has been split, the program can then evaluate each command sequentially by iterating through the individual commands collected.

#### Design considerations:

**Aspect: Handling of special commands and errors:**

* **Alternative 1 (current choice):** The special commands `help` and `exit` and command errors will break the chain of execution.
    * Pros: Intuitive as `help` and `exit` often require immediate action thus subsequent commands are unlikely to be executed anyway even in normal circumstances. Errors in preceding commands may also affect subsequent commands and thus should stop execution to be rectified.
    * Cons: Will not allow the execution of the following commands which may cause some confusion.


* **Alternative 2:** Ignore special commands and errors in the middle of the command chain and execute the following commands regardless.
    * Pros: Allows all commands to be executed which may be expected by some. May even make more sense as calling `help` or `exit` in the command chain does not make much sense and may more often or not be an error.
    * Cons: Calling `help` or `exit` in the middle of a chain will be useless and some commands may be incorrectly run if preceding commands are invalid.


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of clients
* wants to be able to mail large groups of people
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manage clients faster than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                               | So that I can…​                                                       |
| ------- |--------------------------------------------|--------------------------------------------| --------------------------------------------------------------------- |
| `* * *` | business owner                                   | list all my clients information            | see my clients information.                 |
| `* * *` | business owner                                       | add a new client to CinnamonBun            |                                                                       |
| `* * *` | business owner                                       | edit a client’s information                | keep my client’s information updated.                                  |
| `* * *` | business owner                                       | delete a client information                | remove those who are no longer customers. |
| `* *`   | business owner                                       | find a client based on keywords            | easily find a specific client or group of clients.               |
 | `* * *` | business owner                                 | store a transaction of a particular client | easily keep track of unpaid transactions |
| `* * ` | business owner                                 | sort my clients based on certain field                        | easily sort and see the customers based on the field I want |


*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `CinnamonBun` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Delete a person**

**MSS**

1.  User requests to list persons
2.  AddressBook shows a list of persons
3.  User requests to delete a specific person in the list
4.  AddressBook deletes the person

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case: Find a person**

**MSS**

1.  User requests to find clients with the specified keyword(s)
2.  CinnamonBun shows a list of clients with an attribute containing at least one keyword

**Extensions**

* 1a. No keyword is specified.

    * 1a1. CinnamonBun shows an error message.

    Use case resumes at step 2.

* 2a. The list is empty.

  Use case ends.

**Use case: Edit a person information**

**MSS**
1. User specify which person to be edited
2. User inputs the values to be edited
3. AddressBook edits the value

   Use case ends.

**Extensions**

* 1a. No person index specified
    * 1a1. AddressBook shows an error message.

      Use case resumes at step 1.

* 2a. No fields are provided
    * 2a1. AddressBook shows an error message.

      Use case resumes at step 2.
* 2b. Some fields are inputed wrongly
    * 2b1. AddressBook shows the appropriate error message.

      Use case resumes at step 2.

* 2c. Value edited is email and there is already an existing email by another person in the addressBook
    * 2c1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case: Sort customer list**

**MSS**
1. User inputs the fields the list is to be sorted on.
2. AddressBook sorts the person list accordingly in order of the fields specified.
3. The sorted list is displayed.

**Extensions**
* 1a. User inputs no fields
  * 1a1. An error message is shown.

    Use case resumes at step 1.
* 1b. User inputs non-existent/not supported fields
  * 1b1. An error message is shown
  
     Use case resumes at step 1


### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  A user should be able to easily find a client.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Sorting client's list
1. Sorting the client's list based on certain fields
   1. Prerequisites: There needs to be existing client data in the client's list.
   2. Test case: `sort n/`<br>
       Expected: The client's list will display the clients in ascending alphabetical order.
   3. Test case: `sort n/ a/ p/ desc`<br>
       Expected: The client's list will display the clients in ascending alphabetical order. Clients with the same name will
       then be displayed according to their addresses in ascending order. And if they also have the same address, they'll be 
        displayed based on their phone number in descending order.
   4. Test case: `sort l:)/ djewijw p/`<br>
      Expected: An error would be thrown as the fields specified do not exist.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
