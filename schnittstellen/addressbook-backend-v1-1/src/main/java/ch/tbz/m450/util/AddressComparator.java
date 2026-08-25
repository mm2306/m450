package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AddressComparator implements Comparator<Address> {

    public enum SortField {
        LASTNAME,
        FIRSTNAME,
        PHONENUMBER,
        ID,
        REGISTRATION_DATE
    }

    private final List<SortField> sortFields;

    public AddressComparator() {
        this(List.of(SortField.LASTNAME, SortField.FIRSTNAME, SortField.ID));
    }

    public AddressComparator(SortField primaryField) {
        this(List.of(primaryField, SortField.LASTNAME, SortField.FIRSTNAME, SortField.ID));
    }

    public AddressComparator(List<SortField> sortFields) {
        this.sortFields = (sortFields != null && !sortFields.isEmpty())
                ? sortFields
                : List.of(SortField.LASTNAME, SortField.FIRSTNAME, SortField.ID);
    }

    @Override
    public int compare(Address a1, Address a2) {
        if (a1 == a2) return 0;
        if (a1 == null) return -1;
        if (a2 == null) return 1;

        for (SortField field : sortFields) {
            int result = compareByField(a1, a2, field);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    private int compareByField(Address a1, Address a2, SortField field) {
        return switch (field) {
            case LASTNAME -> compareStrings(a1.getLastname(), a2.getLastname());
            case FIRSTNAME -> compareStrings(a1.getFirstname(), a2.getFirstname());
            case PHONENUMBER -> compareStrings(a1.getPhonenumber(), a2.getPhonenumber());
            case ID -> Integer.compare(a1.getId(), a2.getId());
            case REGISTRATION_DATE -> compareDates(a1.getRegistrationDate(), a2.getRegistrationDate());
        };
    }

    private int compareStrings(String s1, String s2) {
        if (Objects.equals(s1, s2)) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;
        return s1.compareToIgnoreCase(s2);
    }

    private int compareDates(java.util.Date d1, java.util.Date d2) {
        if (Objects.equals(d1, d2)) return 0;
        if (d1 == null) return -1;
        if (d2 == null) return 1;
        return d1.compareTo(d2);
    }
}

