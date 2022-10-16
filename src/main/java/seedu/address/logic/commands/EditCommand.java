package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GITHUBUSERNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.Gender;
import seedu.address.model.person.GithubUsername;
import seedu.address.model.person.Location;
import seedu.address.model.person.ModuleCode;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Professor;
import seedu.address.model.person.Student;
import seedu.address.model.person.TeachingAssistant;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
        + "by the index number used in the displayed person list. "
        + "Existing values will be overwritten by the input values.\n"
        + "Parameters: INDEX (must be a positive integer) "
        + "[" + PREFIX_NAME + "NAME] "
        + "[" + PREFIX_MODULE_CODE + "MODULE_CODE] "
        + "[" + PREFIX_PHONE + "PHONE] "
        + "[" + PREFIX_GENDER + "GENDER] "
        + "[" + PREFIX_EMAIL + "EMAIL] "
        + "[" + PREFIX_TAG + "TAG]... "
        + "[" + PREFIX_LOCATION + "LOCATION] "
        + "[" + PREFIX_GITHUBUSERNAME + "GITHUB USERNAME]\n"
        + "Example: " + COMMAND_WORD + " 1 "
        + PREFIX_PHONE + "91234567 "
        + PREFIX_EMAIL + "johndoe@example.com";


    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the Student with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = null;
        if (personToEdit instanceof Student) {
            Student studentToEdit = (Student) personToEdit;
            editedPerson = createEditedPerson(studentToEdit, editPersonDescriptor);
        } else if (personToEdit instanceof Professor) {
            Professor professorToEdit = (Professor) personToEdit;
            editedPerson = createEditedPerson(professorToEdit, editPersonDescriptor);
        } else if (personToEdit instanceof TeachingAssistant) {
            TeachingAssistant taToEdit = (TeachingAssistant) personToEdit;
            editedPerson = createEditedPerson(taToEdit, editPersonDescriptor);
        }

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Student personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());

        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Set<ModuleCode> updatedModuleCodes = editPersonDescriptor
                .getModuleCodes().orElse(personToEdit.getModuleCodes());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Gender updatedGender = editPersonDescriptor.getGender().orElse(personToEdit.getGender());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Location updatedLocation = editPersonDescriptor.getLocation().orElse(personToEdit.getLocation());
        GithubUsername updatedUsername = editPersonDescriptor.getGithubUsername().orElse(personToEdit.getUsername());
        return new Student(updatedName, updatedPhone, updatedEmail, updatedGender, updatedTags, updatedLocation,
                updatedUsername, updatedModuleCodes);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Professor personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        ModuleCode updatedModuleCode = editPersonDescriptor.getModuleCode().orElse(personToEdit.getModuleCode());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Gender updatedGender = editPersonDescriptor.getGender().orElse(personToEdit.getGender());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Location updatedLocation = editPersonDescriptor.getLocation().orElse(personToEdit.getLocation());
        GithubUsername updatedUsername = editPersonDescriptor.getGithubUsername().orElse(personToEdit.getUsername());

        return new Professor(updatedName, updatedModuleCode, updatedPhone, updatedEmail, updatedGender, updatedTags,
            updatedLocation, updatedUsername);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(TeachingAssistant personToEdit,
                                             EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        ModuleCode updatedModuleCode = editPersonDescriptor.getModuleCode().orElse(personToEdit.getModuleCode());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Gender updatedGender = editPersonDescriptor.getGender().orElse(personToEdit.getGender());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Location updatedLocation = editPersonDescriptor.getLocation().orElse(personToEdit.getLocation());
        GithubUsername updatedUsername = editPersonDescriptor.getGithubUsername().orElse(personToEdit.getUsername());
        return new TeachingAssistant(updatedName, updatedModuleCode, updatedPhone,
            updatedEmail, updatedGender, updatedTags, updatedLocation, updatedUsername);
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
            && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private ModuleCode moduleCode;

        private Set<ModuleCode> moduleCodes;
        private Phone phone;
        private Email email;
        private Gender gender;
        private Set<Tag> tags;
        private Location location;
        private GithubUsername githubUsername;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setModuleCodes(toCopy.moduleCodes);
            setModuleCode(toCopy.moduleCode);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setGender(toCopy.gender);
            setTags(toCopy.tags);
            setLocation(toCopy.location);
            setGithubUsername(toCopy.githubUsername);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, gender, tags, location, githubUsername);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public Optional<Gender> getGender() {
            return Optional.ofNullable(gender);
        }

        /**
         * Sets {@code moduleCodes} to this object's { @code moduleCodes }.
         * A defensive copy of { @code moduleCodes } is used internally.
         */
        public void setModuleCodes(Set<ModuleCode> moduleCodes) {
            this.moduleCodes = (moduleCodes != null) ? new HashSet<>(moduleCodes) : null;
        }

        /**
         * Returns an unmodifiable moduleCode set, which throws { @code UnsupportedOperationException }
         * if modification is attempted.
         * Returns { @code Optional#empty() } if { @code moduleCode } is null.
         */
        public Optional<Set<ModuleCode>> getModuleCodes() {
            return (moduleCodes != null) ? Optional.of(Collections.unmodifiableSet(moduleCodes)) : Optional.empty();
        }

        public void setModuleCode(ModuleCode moduleCode) {
            this.moduleCode = moduleCode;
        }

        public Optional<ModuleCode> getModuleCode() {
            return Optional.ofNullable(moduleCode);
        }

        public Optional<Location> getLocation() {
            return Optional.ofNullable(location);
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Optional<GithubUsername> getGithubUsername() {
            return Optional.ofNullable(githubUsername);
        }

        public void setGithubUsername(GithubUsername username) {
            this.githubUsername = username;
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                && getPhone().equals(e.getPhone())
                && getModuleCode().equals(e.getModuleCode())
                && getModuleCodes().equals(e.getModuleCodes())
                && getEmail().equals(e.getEmail())
                && getGender().equals(e.getGender())
                && getTags().equals(e.getTags())
                && getLocation().equals(e.getLocation())
                && getGithubUsername().equals(e.getGithubUsername());
        }

    }

}
