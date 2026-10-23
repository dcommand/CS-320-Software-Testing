package contact;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ContactTest {
    private Contact contact;

    @BeforeEach
    void setUp() {
        contact = new Contact("C001", "Daniel", "Commander", "0123456789", "123 Main St");
    }

    @Test
    void createsContactWithValidFields() {
        assertAll(
                () -> assertEquals("C001", contact.getContactId()),
                () -> assertEquals("Daniel", contact.getFirstName()),
                () -> assertEquals("Commander", contact.getLastName()),
                () -> assertEquals("0123456789", contact.getPhone()),
                () -> assertEquals("123 Main St", contact.getAddress()));
    }

    @Test
    void acceptsFieldsAtTheirMaximumLengths() {
        Contact boundary = new Contact("1234567890", "ABCDEFGHIJ", "KLMNOPQRST",
                "9876543210", "123456789012345678901234567890");

        assertAll(
                () -> assertEquals("1234567890", boundary.getContactId()),
                () -> assertEquals("ABCDEFGHIJ", boundary.getFirstName()),
                () -> assertEquals("KLMNOPQRST", boundary.getLastName()),
                () -> assertEquals("9876543210", boundary.getPhone()),
                () -> assertEquals("123456789012345678901234567890", boundary.getAddress()));
    }

    @Test
    void allowsEmptyLengthLimitedFieldsBecauseNoMinimumIsSpecified() {
        Contact empty = new Contact("", "", "", "0123456789", "");
        assertAll(
                () -> assertEquals("", empty.getContactId()),
                () -> assertEquals("", empty.getFirstName()),
                () -> assertEquals("", empty.getLastName()),
                () -> assertEquals("", empty.getAddress()));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidContactId(String invalid) {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact(invalid, "Daniel", "Commander", "0123456789", "123 Main St"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"ABCDEFGHIJK"})
    void rejectsInvalidFirstName(String invalid) {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C001", invalid, "Commander", "0123456789", "123 Main St"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"ABCDEFGHIJK"})
    void rejectsInvalidLastName(String invalid) {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C001", "Daniel", invalid, "0123456789", "123 Main St"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123456789", "12345678901", "12345a7890", "12345 7890",
            "12345-7890", "abcdefghij", "\uFF11\uFF12\uFF13\uFF14\uFF15\uFF16\uFF17\uFF18\uFF19\uFF10"})
    void rejectsInvalidPhone(String invalid) {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C001", "Daniel", "Commander", invalid, "123 Main St"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"1234567890123456789012345678901"})
    void rejectsInvalidAddress(String invalid) {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C001", "Daniel", "Commander", "0123456789", invalid));
    }

    @Test
    void contactIdIsFinalAndHasNoSetter() throws NoSuchFieldException {
        int modifiers = Contact.class.getDeclaredField("contactId").getModifiers();
        assertTrue(Modifier.isPrivate(modifiers));
        assertTrue(Modifier.isFinal(modifiers));
        assertThrows(NoSuchMethodException.class,
                () -> Contact.class.getMethod("setContactId", String.class));
    }

    @Test
    void updatesPermittedFieldsAtTheirLimitsWithoutChangingId() {
        contact.setFirstName("ABCDEFGHIJ");
        contact.setLastName("KLMNOPQRST");
        contact.setPhone("9998887777");
        contact.setAddress("123456789012345678901234567890");

        assertAll(
                () -> assertEquals("C001", contact.getContactId()),
                () -> assertEquals("ABCDEFGHIJ", contact.getFirstName()),
                () -> assertEquals("KLMNOPQRST", contact.getLastName()),
                () -> assertEquals("9998887777", contact.getPhone()),
                () -> assertEquals("123456789012345678901234567890", contact.getAddress()));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"ABCDEFGHIJK"})
    void rejectsInvalidFirstNameUpdateAndPreservesValue(String invalid) {
        assertThrows(IllegalArgumentException.class, () -> contact.setFirstName(invalid));
        assertEquals("Daniel", contact.getFirstName());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"ABCDEFGHIJK"})
    void rejectsInvalidLastNameUpdateAndPreservesValue(String invalid) {
        assertThrows(IllegalArgumentException.class, () -> contact.setLastName(invalid));
        assertEquals("Commander", contact.getLastName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123456789", "12345678901", "12345a7890", "12345 7890",
            "12345-7890", "abcdefghij", "\uFF11\uFF12\uFF13\uFF14\uFF15\uFF16\uFF17\uFF18\uFF19\uFF10"})
    void rejectsInvalidPhoneUpdateAndPreservesValue(String invalid) {
        assertThrows(IllegalArgumentException.class, () -> contact.setPhone(invalid));
        assertEquals("0123456789", contact.getPhone());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"1234567890123456789012345678901"})
    void rejectsInvalidAddressUpdateAndPreservesValue(String invalid) {
        assertThrows(IllegalArgumentException.class, () -> contact.setAddress(invalid));
        assertEquals("123 Main St", contact.getAddress());
    }
}
