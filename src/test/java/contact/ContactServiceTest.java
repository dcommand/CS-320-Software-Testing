package contact;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ContactServiceTest {
    private ContactService service;
    private Contact first;
    private Contact second;

    @BeforeEach
    void setUp() {
        service = new ContactService();
        first = new Contact("C001", "Daniel", "Commander", "0123456789", "123 Main St");
        second = new Contact("C002", "Alex", "Smith", "9876543210", "456 Oak St");
    }

    @Test
    void addsContactsWithDifferentIds() {
        service.addContact(first);
        service.addContact(second);
        assertSame(first, service.getContact("C001"));
        assertSame(second, service.getContact("C002"));
    }

    @Test
    void rejectsDuplicateIdWithoutReplacingOriginal() {
        service.addContact(first);
        Contact duplicate = new Contact("C001", "Other", "Person", "1112223333", "789 Elm St");

        assertThrows(IllegalArgumentException.class, () -> service.addContact(duplicate));
        assertSame(first, service.getContact("C001"));
        assertEquals("Daniel", service.getContact("C001").getFirstName());
    }

    @Test
    void rejectsAddingTheSameContactTwice() {
        service.addContact(first);
        assertThrows(IllegalArgumentException.class, () -> service.addContact(first));
        assertSame(first, service.getContact("C001"));
    }

    @Test
    void rejectsNullContact() {
        assertThrows(IllegalArgumentException.class, () -> service.addContact(null));
    }

    @Test
    void deletesOnlyTheContactWithTheRequestedId() {
        service.addContact(first);
        service.addContact(second);

        service.deleteContact("C001");

        assertThrows(IllegalArgumentException.class, () -> service.getContact("C001"));
        assertSame(second, service.getContact("C002"));
    }

    @Test
    void permitsReusingAnIdAfterItsContactIsDeleted() {
        service.addContact(first);
        service.deleteContact("C001");
        Contact replacement = new Contact("C001", "New", "Person", "1112223333", "789 Elm St");
        service.addContact(replacement);
        assertSame(replacement, service.getContact("C001"));
    }

    @Test
    void separateServicesHaveIndependentStorage() {
        service.addContact(first);
        ContactService otherService = new ContactService();
        assertThrows(IllegalArgumentException.class, () -> otherService.getContact("C001"));
        otherService.addContact(second);
        assertThrows(IllegalArgumentException.class, () -> service.getContact("C002"));
    }

    @Test
    void updatesOnlyFirstNameForTheRequestedContact() {
        service.addContact(first);
        service.addContact(second);
        service.updateFirstName("C001", "ABCDEFGHIJ");

        assertAll(
                () -> assertEquals("ABCDEFGHIJ", service.getContact("C001").getFirstName()),
                () -> assertEquals("Commander", first.getLastName()),
                () -> assertEquals("0123456789", first.getPhone()),
                () -> assertEquals("123 Main St", first.getAddress()),
                () -> assertEquals("C001", first.getContactId()),
                () -> assertEquals("Alex", second.getFirstName()));
    }

    @Test
    void updatesOnlyLastNameForTheRequestedContact() {
        service.addContact(first);
        service.addContact(second);
        service.updateLastName("C001", "ABCDEFGHIJ");

        assertAll(
                () -> assertEquals("ABCDEFGHIJ", service.getContact("C001").getLastName()),
                () -> assertEquals("Daniel", first.getFirstName()),
                () -> assertEquals("0123456789", first.getPhone()),
                () -> assertEquals("123 Main St", first.getAddress()),
                () -> assertEquals("C001", first.getContactId()),
                () -> assertEquals("Smith", second.getLastName()));
    }

    @Test
    void updatesOnlyPhoneForTheRequestedContact() {
        service.addContact(first);
        service.addContact(second);
        service.updatePhone("C001", "0001112222");

        assertAll(
                () -> assertEquals("0001112222", service.getContact("C001").getPhone()),
                () -> assertEquals("Daniel", first.getFirstName()),
                () -> assertEquals("Commander", first.getLastName()),
                () -> assertEquals("123 Main St", first.getAddress()),
                () -> assertEquals("C001", first.getContactId()),
                () -> assertEquals("9876543210", second.getPhone()));
    }

    @Test
    void updatesOnlyAddressForTheRequestedContact() {
        service.addContact(first);
        service.addContact(second);
        service.updateAddress("C001", "123456789012345678901234567890");

        assertAll(
                () -> assertEquals("123456789012345678901234567890", service.getContact("C001").getAddress()),
                () -> assertEquals("Daniel", first.getFirstName()),
                () -> assertEquals("Commander", first.getLastName()),
                () -> assertEquals("0123456789", first.getPhone()),
                () -> assertEquals("C001", first.getContactId()),
                () -> assertEquals("456 Oak St", second.getAddress()));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"ABCDEFGHIJK"})
    void rejectsInvalidFirstNameUpdate(String invalid) {
        service.addContact(first);
        assertThrows(IllegalArgumentException.class, () -> service.updateFirstName("C001", invalid));
        assertEquals("Daniel", service.getContact("C001").getFirstName());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"ABCDEFGHIJK"})
    void rejectsInvalidLastNameUpdate(String invalid) {
        service.addContact(first);
        assertThrows(IllegalArgumentException.class, () -> service.updateLastName("C001", invalid));
        assertEquals("Commander", service.getContact("C001").getLastName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123456789", "12345678901", "12345a7890", "12345 7890",
            "12345-7890", "abcdefghij", "\uFF11\uFF12\uFF13\uFF14\uFF15\uFF16\uFF17\uFF18\uFF19\uFF10"})
    void rejectsInvalidPhoneUpdate(String invalid) {
        service.addContact(first);
        assertThrows(IllegalArgumentException.class, () -> service.updatePhone("C001", invalid));
        assertEquals("0123456789", service.getContact("C001").getPhone());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"1234567890123456789012345678901"})
    void rejectsInvalidAddressUpdate(String invalid) {
        service.addContact(first);
        assertThrows(IllegalArgumentException.class, () -> service.updateAddress("C001", invalid));
        assertEquals("123 Main St", service.getContact("C001").getAddress());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"missing"})
    void rejectsLookupForMissingOrNullId(String id) {
        service.addContact(first);
        assertThrows(IllegalArgumentException.class, () -> service.getContact(id));
        assertSame(first, service.getContact("C001"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"missing"})
    void rejectsDeletionForMissingOrNullId(String id) {
        service.addContact(first);
        assertThrows(IllegalArgumentException.class, () -> service.deleteContact(id));
        assertSame(first, service.getContact("C001"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"missing"})
    void rejectsEveryUpdateForMissingOrNullId(String id) {
        service.addContact(first);
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> service.updateFirstName(id, "Alex")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> service.updateLastName(id, "Smith")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> service.updatePhone(id, "1112223333")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> service.updateAddress(id, "456 Oak St")));
        assertAll(
                () -> assertEquals("Daniel", first.getFirstName()),
                () -> assertEquals("Commander", first.getLastName()),
                () -> assertEquals("0123456789", first.getPhone()),
                () -> assertEquals("123 Main St", first.getAddress()));
    }
}
