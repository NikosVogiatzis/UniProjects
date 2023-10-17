package accommodations;

import accommodations.reservervations.Date;
import accommodations.reservervations.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CalendarUnitTests
{
    Date date;
    Reservation reservation;

    @BeforeEach
    void setUp()
    {
        date = new Date();
        reservation = new Reservation();
    }

    @Test
    void dateGenerator()
    {
        assertEquals(
                new Date(10, 1, 2022).toString(),
                date.dateGenerator("10/1/2022").toString()
        );
        assertNull(date.dateGenerator("10-1-2022"));
    }

    @Test
    void intermediateDates()
    {
        AtomicInteger datePosition = new AtomicInteger();
        ArrayList<Date> intermediateDates = new ArrayList<>();

        intermediateDates.add(new Date(30, 12, 2021));
        intermediateDates.add(new Date(31, 12, 2021));
        intermediateDates.add(new Date(1, 1, 2022));
        intermediateDates.add(new Date(2, 1, 2022));

        try
        {
            datePosition.set(0);
            date.intermediateDates(
                    new Date(30, 12, 2021),
                    new Date(2, 1, 2022)).forEach(e ->
                    assertEquals(intermediateDates.get(datePosition.getAndIncrement()).toString(), e.toString())
            );

            fail();
        } catch (final NullPointerException ignored)
        {
            assertTrue(true);
        }

        intermediateDates.clear();
        intermediateDates.add(new Date(27, 2, 2033));
        intermediateDates.add(new Date(28, 2, 2033));
        intermediateDates.add(new Date(1, 3, 2033));
        intermediateDates.add(new Date(2, 3, 2033));

        try
        {
            datePosition.set(0);
            date.intermediateDates(
                    new Date(27, 2, 2033),
                    new Date(2, 3, 2033)).forEach(e ->
                    assertEquals(intermediateDates.get(datePosition.getAndIncrement()).toString(), e.toString())
            );

            assertTrue(true);
        } catch (final NullPointerException ignored)
        {
            fail();
        }

        intermediateDates.clear();
        intermediateDates.add(new Date(27, 2, 2032));
        intermediateDates.add(new Date(28, 2, 2032));
        intermediateDates.add(new Date(29, 2, 2032));
        intermediateDates.add(new Date(1, 3, 2032));
        intermediateDates.add(new Date(2, 3, 2032));

        try
        {
            datePosition.set(0);
            date.intermediateDates(
                    new Date(27, 2, 2032),
                    new Date(2, 3, 2032)).forEach(e ->
                    assertEquals(intermediateDates.get(datePosition.getAndIncrement()).toString(), e.toString())
            );

            assertTrue(true);
        } catch (final NullPointerException ignored)
        {
            fail();
        }

        intermediateDates.clear();
        intermediateDates.add(new Date(27, 12, 2032));
        intermediateDates.add(new Date(28, 12, 2032));
        intermediateDates.add(new Date(29, 12, 2032));
        intermediateDates.add(new Date(30, 12, 2032));
        intermediateDates.add(new Date(31, 12, 2032));
        intermediateDates.add(new Date(1, 1, 2033));
        intermediateDates.add(new Date(2, 1, 2033));

        try
        {
            datePosition.set(0);
            date.intermediateDates(
                    new Date(27, 12, 2032),
                    new Date(2, 1, 2033)).forEach(e ->
                    assertEquals(intermediateDates.get(datePosition.getAndIncrement()).toString(), e.toString())
            );

            assertTrue(true);
        } catch (final NullPointerException ignored)
        {
            fail();
        }
    }

    @Test
    void setDate()
    {
        assertFalse(date.setDate(1, 1, 2022));
        assertFalse(date.setDate(30, 2, 2022));
        assertFalse(date.setDate(-1, 9, 2020));
        assertFalse(date.setDate(31, 4, 2020));
        assertTrue(date.setDate(1, 2, 2023));
    }

    @Test
    void testToString()
    {
        date.setDate(19, 7, 2023);
        assertEquals("19/7/2023", date.toString());
    }

    @Test
    void defaultReservationCheckNullity()
    {
        assertNull(reservation.getCustomer());
        assertNull(reservation.getReservationPeriod());
        assertEquals(0, reservation.getId());
    }
}