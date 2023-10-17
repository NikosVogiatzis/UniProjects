package accommodations.reservervations;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;

/**
 * Η κλάση Date υλοποιεί μια ημερολογιακή ημερομηνία
 * που επιτρέπει τον προσδιορισμό μιας συγκεκριμένης ημέρας
 * σε Γρηγοριανή μορφή: ημέρα/μήνας/έτος (DMY).
 * <p>
 * H ακολουθία little-endian χρησιμοποιείται από την πλειονότητα
 * του κόσμου και είναι η προτιμώμενη μορφή από τα Ηνωμένα Έθνη όταν
 * γράφουν την πλήρη μορφή ημερομηνίας σε επίσημα έγγραφα.
 *
 * @version 20/1/2022
 */

public class Date implements Serializable
{
    private int day;
    private int month;
    private int year;

    //static final long serialVersionUID = 6058271249990515645L;

    /**
     * Καθορίζει αρχικές τιμές ανάλογα με τη/το
     * μέρα/μήνα/έτος που δημιουργείται ένα
     * αντικείμενο τύπου {@link Date}.
     */
    public Date()
    {
        this.day = LocalDate.now().getDayOfMonth();
        this.month = LocalDate.now().getMonthValue();
        this.year = LocalDate.now().getYear();
    }

    /**
     * Καθορίζει αρχικές τιμές ανάλογα με τη/το μέρα/μήνα/έτος
     * του αντικειμένου που βρίσκεται σαν παράμετρος.
     *
     * @param date Αντικείμενο τύπου {@link Date} που περιέχει τη/το μέρα/μήνα/έτος.
     */
    public Date(Date date)
    {
        this.day = LocalDate.now().getDayOfMonth();
        this.month = LocalDate.now().getMonthValue();
        this.year = LocalDate.now().getYear();

        setDate(date);
    }

    /**
     * Καθορίζει αρχικές τιμές ανάλογα με τις παραμέτρους που
     * δέχεται.
     * <p>
     * Οι παράμετροι που δέχεται (ημέρα/μήνα/έτος) θα πρέπει
     * διαδοχικά να αναγράφουν κάποια μελλοντική ημερομηνία.
     *
     * @param day   Καθορίζει την ημέρα (Τιμές απο 1-31).
     * @param month Καθορίζει τον μήνα (Τιμές απο 1-12).
     * @param year  Καθορίζει την ημέρα (Τιμές απο τωρινό_έτος-MAX).
     */
    public Date(int day, int month, int year)
    {
        this.day = LocalDate.now().getDayOfMonth() - 1;
        this.month = LocalDate.now().getMonthValue();
        this.year = LocalDate.now().getYear();

        setDate(day, month, year);
    }

    /**
     * Μετατρέπει μια συμβολοσειρά με μια αυστηρή σύνταξη
     * (μέρα[1-31]/μήνα[1-12]/έτος[τωρινό_έτος-MAX]) σε
     * ένα αντικείμενο τύπου {@link Date}.
     *
     * @param date Συμβολοσειρά που περιέχει τη/το μέρα/μήνα/έτος (Με αυστηρή
     *             σύνταξη μέρα[1-31]/μήνα[1-12]/έτος[τωρινό_έτος-MAX]).
     * @return Αντικείμενο τύπου {@link Date} που περιέχει τη/το μέρα/μήνα/έτος.
     */
    public Date dateGenerator(String date)
    {
        Date generatedData;
        try
        {
            String[] splitDate = date.split("/");
            generatedData = new Date(
                    Integer.parseInt(splitDate[0]),
                    Integer.parseInt(splitDate[1]),
                    Integer.parseInt(splitDate[2]));
        } catch (Exception ignored)
        {
            return null;
        }

        return generatedData;
    }

    /**
     * Μέθοδος που υπολογίζει την αμέσως επόμενη ημερομηνία με
     * βάση την ήδη καθορισμένη ημερομηνία του αντικειμένου.
     *
     * @return Αντικείμενο τύπου {@link Date} που περιέχει την επόμενη
     * ημερομηνία με βάση την τωρινή του αντικειμένου.
     */
    private Date nextDate()
    {
        if (day == 28)
        {
            if (month == 2)
            {
                if (Year.isLeap(year))
                    day = 29;
                else
                {
                    day = 1;
                    month = 3;
                }
            } else
                day += 1;
        } else if (day == 29)
        {
            if (month == 2)
            {
                day = 1;
                month = 3;
            } else
                day += 1;
        } else if (day == 30)
        {
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
                day += 1;
            else
            {
                day = 1;
                month += 1;
            }
        } else if (day == 31)
        {
            day = 1;
            if (month == 12)
            {
                year += 1;
                month = 1;
            } else
                month += 1;
        } else if (day > 0 && day < 28)
            day += 1;

        return this;
    }

    /**
     * Μέθοδος υπολογισμού για όλες τις ενδιάμεσες τιμές που
     * υπάρχουν μεταξύ δυο ημερομηνιών.
     *
     * @param startDate Αντικείμενο τύπου {@link Date}
     *                  που καθορίζει την αρχική ημερομηνία.
     * @param endDate   Αντικείμενο τύπου {@link Date}
     *                  που καθορίζει την τελική ημερομηνία.
     * @return Μια λίστα από όλες τις ενδιάμεσες ημερομηνίες
     * μεταξύ του {@param startDate} και {@param endDate}.
     */
    public ArrayList<Date> intermediateDates(Date startDate, Date endDate)
    {
        ArrayList<Date> allDates = new ArrayList<>();

        if (startDate == null || endDate == null)
            return null;

        if (isPreviousDate(startDate))
            return null;

        allDates.add(new Date(startDate));

        if (startDate.getYear() == endDate.getYear())
        {
            if (startDate.getMonth() == endDate.getMonth())
                if (startDate.getDay() > endDate.getDay())
                    allDates = null;
                else if (startDate.getMonth() > startDate.getMonth())
                    allDates = null;

            if (endDate.getMonth() < startDate.getMonth())
                allDates = null;
        } else if (startDate.getYear() > endDate.getYear())
            allDates = null;

        int loop = 0;
        while (!(
                startDate.getDay() == endDate.getDay()
                        && startDate.getMonth() == endDate.getMonth()
                        && startDate.getYear() == endDate.getYear()
        ))
        {
            if (allDates == null)
                return null;

            Date temp = new Date(allDates.get(loop));
            allDates.add(temp.nextDate());
            startDate.nextDate();
            loop++;
        }

        return allDates;
    }

    /**
     * Ελέγχει αν η δοθείσα ημερομηνία είναι ορθή με βάση την
     * τωρινή ημερομηνία. Μια ημερομηνία θεωρείται ορθή όταν
     * πρόκειται για μια μελλοντική ημερομηνία. Σε περίπτωση
     * που πρόκειται για ορθή ημερομηνία τότε επιστρέφει true
     * σε κάθε άλλη διαφορετική περίπτωση false.
     *
     * @param day   Καθορίζει την ημέρα (Τιμές απο 1-31).
     * @param month Καθορίζει τον μήνα (Τιμές απο 1-12).
     * @param year  Καθορίζει την ημέρα (Τιμές απο τωρινό_έτος-MAX).
     * @return Επιστρέφει λογικές τιμές, ανάλογα τη δοθείσα
     * ημερομηνία (aν η δοθείσα ημερομηνία είναι ορθή με βάση
     * την τωρινή ημερομηνία ή οχι)
     */
    private boolean checkDate(int day, int month, int year)
    {
        boolean isCorrect = month >= 1 && month <= 12;

        if (year < LocalDate.now().getYear())
            isCorrect = false;

        if (year == LocalDate.now().getYear())
        {
            if (month < LocalDate.now().getMonthValue())
                isCorrect = false;

            if (month == LocalDate.now().getMonthValue())
                if (day < LocalDate.now().getDayOfMonth())
                    isCorrect = false;
        }

        if (day < 1)
            isCorrect = false;

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
        {
            if (day > 31)
                isCorrect = false;
        } else if (day > 30)
            isCorrect = false;

        if (month == 2 && Year.isLeap(year))
            if (day > 29)
                isCorrect = false;
        if (month == 2 && !Year.isLeap(year))
            if (day > 28)
                isCorrect = false;

        return isCorrect;
    }

    /**
     * Ορίζει τιμές ανάλογα με τις παραμέτρους που
     * δέχεται.
     * <p>
     * Οι παράμετροι που δέχεται (ημέρα/μήνα/έτος) θα πρέπει
     * διαδοχικά να αναγράφουν κάποια μελλοντική ημερομηνία.
     *
     * @param day   Καθορίζει την ημέρα (Τιμές απο 1-31).
     * @param month Καθορίζει τον μήνα (Τιμές απο 1-12).
     * @param year  Καθορίζει την ημέρα (Τιμές απο τωρινό_έτος-MAX).
     * @return Επιστρέφει λογικές τιμές, ανάλογα τη δοθείσα
     * ημερομηνία (αν πρόκειται για ορθή ημερομηνία ή οχι).
     */
    public boolean setDate(int day, int month, int year)
    {
        boolean isCorrect = checkDate(day, month, year);

        if (isCorrect)
        {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        return isCorrect;
    }

    /**
     * Ορίζει αρχικές τιμές ανάλογα με την ημερομηνία που
     * περιέχει το αντικείμενο {@link Date} που έχει ως παράμετρο.
     * <p>
     * Οι παράμετροι που δέχεται (ημέρα/μήνα/έτος) θα πρέπει
     * διαδοχικά να αναγράφουν κάποια μελλοντική ημερομηνία.
     *
     * @param date Συμβολοσειρά που περιέχει τη/το μέρα/μήνα/έτος (Με αυστηρή
     *             σύνταξη μέρα[1-31]/μήνα[1-12]/έτος[τωρινό_έτος-MAX]).
     * @return Επιστρέφει λογικές τιμές, ανάλογα τη δοθείσα
     * ημερομηνία (αν πρόκειται για ορθή ημερομηνία ή οχι).
     */
    public boolean setDate(Date date)
    {
        return setDate(date.getDay(), date.getMonth(), date.getYear());
    }

    /**
     * Μέθοδος που επιστρέφει το ίδιο το αντικείμενο που είναι ορισμένο.
     *
     * @return Επιστρέφει το αντικείμενο.
     */
    public Date getDate()
    {
        return this;
    }

    /**
     * Μέθοδος που επιστρέφει μια συμβολοσειρά όπου αναγράφεται η
     * ημερομηνία του αντικειμένου.
     *
     * @return Συμβολοσειρά με τα στοιχεία του ίδιου του αντικειμένου (ημέρα/μήνας/έτος).
     */
    @Override
    public String toString()
    {
        return day + "/" + month + "/" + year;
    }

    /**
     * Επιστρέφει την ημέρα της ημερομηνίας.
     *
     * @return Ημέρα της ημερομηνίας σε ακέραιο αριθμό.
     */
    public int getDay()
    {
        return day;
    }

    /**
     * Επιστρέφει τον μήνα της ημερομηνίας.
     *
     * @return Μήνας της ημερομηνίας σε ακέραιο αριθμό.
     */
    public int getMonth()
    {
        return month;
    }

    /**
     * Επιστρέφει το έτος της ημερομηνίας.
     *
     * @return Έτος της ημερομηνίας σε ακέραιο αριθμό.
     */
    public int getYear()
    {
        return year;
    }

    /**
     * Υπολογίζει, με βάση την παρούσα ημερομηνία, αν
     * πρόκειται για παρελθοντική ή όχι.
     *
     * @param dateToCheck Η προς έλεγχο ημερομηνία.
     * @return Επιστρέφει λογικές τιμές, ανάλογα τη δοθείσα
     * ημερομηνία (αν πρόκειται για παρελθοντική ημερομηνία ή οχι).
     */
    private boolean isPreviousDate(Date dateToCheck)
    {
        return dateToCheck.getDate().getDay() < LocalDate.now().getDayOfMonth()
                && dateToCheck.getDate().getMonth() == LocalDate.now().getMonthValue()
                && dateToCheck.getDate().getYear() == LocalDate.now().getYear();
    }
}