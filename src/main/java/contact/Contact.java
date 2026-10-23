package contact;

/** A contact whose fields always satisfy the assignment's validation rules. */
public class Contact {
    private final String contactId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public Contact(String contactId, String firstName, String lastName,
                   String phone, String address) {
        validateLength(contactId, 10, "Contact ID");
        this.contactId = contactId;
        setFirstName(firstName);
        setLastName(lastName);
        setPhone(phone);
        setAddress(address);
    }

    public String getContactId() {
        return contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setFirstName(String firstName) {
        validateLength(firstName, 10, "First name");
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        validateLength(lastName, 10, "Last name");
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        // Keep phone numbers as text to preserve leading zeros.
        if (phone == null || !phone.matches("[0-9]{10}")) {
            throw new IllegalArgumentException("Phone must contain exactly 10 digits.");
        }
        this.phone = phone;
    }

    public void setAddress(String address) {
        validateLength(address, 30, "Address");
        this.address = address;
    }

    private static void validateLength(String value, int maxLength, String field) {
        if (value == null || value.length() > maxLength) {
            throw new IllegalArgumentException(
                    field + " must not be null or longer than " + maxLength + " characters.");
        }
    }
}
