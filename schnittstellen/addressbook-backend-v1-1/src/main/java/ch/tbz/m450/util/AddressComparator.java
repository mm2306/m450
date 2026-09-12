package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;

import java.util.Comparator;

public class AddressComparator implements Comparator<Address> {

    private static final Comparator<String> NULL_SAFE_STRING =
            Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER);

    private static final Comparator<Address> COMPARATOR = Comparator
            .nullsFirst(Comparator.comparing(Address::getLastname, NULL_SAFE_STRING)
                    .thenComparing(Address::getFirstname, NULL_SAFE_STRING)
                    .thenComparingInt(Address::getId));

    @Override
    public int compare(Address a1, Address a2) {
        return COMPARATOR.compare(a1, a2);
    }
}
