package com.bank.account_service.util;

import java.time.LocalDate;
import java.time.Period;

public class IdNumberUtil {

    public static LocalDate extractDateOfBirth(String idNumber) {

        if (idNumber == null || idNumber.length() != 13) {
            throw new IllegalArgumentException("Invalid ID Number format");
        }

        String yy = idNumber.substring(0, 2);
        String mm = idNumber.substring(2, 4);
        String dd = idNumber.substring(4, 6);

        int year = Integer.parseInt(yy);
        int month = Integer.parseInt(mm);
        int day = Integer.parseInt(dd);

        // Century detection
        int currentYear = LocalDate.now().getYear() % 100;
        int fullYear = (year <= currentYear) ? 2000 + year : 1900 + year;

        return LocalDate.of(fullYear, month, day);
    }

    public static int calculateAge(String idNumber) {
        LocalDate dob = extractDateOfBirth(idNumber);
        return Period.between(dob, LocalDate.now()).getYears();
    }
}
