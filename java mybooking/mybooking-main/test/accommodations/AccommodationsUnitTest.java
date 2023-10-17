package accommodations;

import accommodations.reservervations.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import users.Customers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AccommodationsUnitTest
{
    Accommodations accommodations;

    @BeforeEach
    void setUp()
    {
        accommodations = new Accommodations();
        creationOfDefaultAccommodations();
    }

    @Test
    void searchPrivateAccommodations()
    {
        ArrayList<ArrayList<Integer>> ranges = new ArrayList<>();
        ArrayList<Integer> smMinMax = new ArrayList<>();
        ArrayList<Integer> priceMinMax = new ArrayList<>();

        smMinMax.add(0); // ελάχιστα τετραγωνικά
        smMinMax.add(100); // μέγιστα τετραγωνικά
        priceMinMax.add(0); // ελάχιστη τιμή
        priceMinMax.add(150); // μέγιστη τιμή

        // Οριοθέτηση τ.μ. και τιμής και τοποθέτηση σε μια
        // λίστα [[min τ.μ. , max τ.μ.], [min τιμή, max τιμή]]
        ranges.add(smMinMax);
        ranges.add(priceMinMax);

        ArrayList<String> characteristics = new ArrayList<>();
        characteristics.add("view");

        try
        {
            accommodations.SearchPrivateAccommodations("New York", 2, ranges, characteristics).forEach(e -> assertEquals(1017, e.getId()));
            accommodations.SearchPrivateAccommodations("Berlin", 4, ranges, characteristics).forEach(e -> assertEquals((Integer) null, e.getId()));
        } catch (final NullPointerException e)
        {
            fail("Test: searchPrivateAccommodations() failed! \n " + e);
        }
    }

    @Test
    void containsAtLeastOneCharacteristicRoom()
    {
        ArrayList<String> characteristicsExpected = new ArrayList<>();
        characteristicsExpected.add("view");
        characteristicsExpected.add("garage");
        accommodations.setCharacteristics(characteristicsExpected);

        ArrayList<String> characteristics = new ArrayList<>();
        assertFalse(accommodations.ContainsAtLeastOneCharacteristicPrivate(characteristics));
        assertFalse(accommodations.ContainsAtLeastOneCharacteristicRoom(characteristics));
        characteristics.add("view");
        assertTrue(accommodations.ContainsAtLeastOneCharacteristicPrivate(characteristics));
        assertTrue(accommodations.ContainsAtLeastOneCharacteristicRoom(characteristics));
        characteristics.add("parking lot");
        assertTrue(accommodations.ContainsAtLeastOneCharacteristicPrivate(characteristics));
        assertTrue(accommodations.ContainsAtLeastOneCharacteristicRoom(characteristics));
        characteristics.clear();
        assertFalse(accommodations.ContainsAtLeastOneCharacteristicPrivate(characteristics));
        assertFalse(accommodations.ContainsAtLeastOneCharacteristicRoom(characteristics));
        characteristics.add("parking lot");
        assertFalse(accommodations.ContainsAtLeastOneCharacteristicPrivate(characteristics));
        assertFalse(accommodations.ContainsAtLeastOneCharacteristicRoom(characteristics));
    }

    @Test
    void searchHotelRooms()
    {
        ArrayList<ArrayList<Integer>> ranges = new ArrayList<>();
        ArrayList<Integer> smMinMax = new ArrayList<>();
        ArrayList<Integer> priceMinMax = new ArrayList<>();

        smMinMax.add(0); // ελάχιστα τετραγωνικά
        smMinMax.add(100); // μέγιστα τετραγωνικά
        priceMinMax.add(0); // ελάχιστη τιμή
        priceMinMax.add(150); // μέγιστη τιμή

        // Οριοθέτηση τ.μ. και τιμής και τοποθέτηση σε μια
        // λίστα [[min τ.μ. , max τ.μ.], [min τιμή, max τιμή]]
        ranges.add(smMinMax);
        ranges.add(priceMinMax);

        ArrayList<String> characteristics = new ArrayList<>();
        characteristics.add("view");

        try
        {
            accommodations.SearchHotelRooms("Athens", 2, ranges, characteristics).forEach(e -> assertEquals(1022, e.getId()));
            accommodations.SearchHotelRooms("Italy", 2, ranges, characteristics).forEach(e -> assertEquals((Integer) null, e.getId()));
        } catch (final NullPointerException e)
        {
            fail("Test: searchHotelRooms() failed! \n " + e);
        }
    }

    @Test
    void findRoom()
    {
        assertEquals(-1, accommodations.FindRoom(1234));
        assertEquals(-1, accommodations.FindRoom(-1234));
        assertEquals(-1, accommodations.FindRoom(0));
        assertEquals(0, accommodations.FindRoom(1022));
        assertEquals(1, accommodations.FindRoom(1023));
    }

    @Test
    void findAccommodation()
    {
        assertEquals(-1, accommodations.FindAccommodation(1234));
        assertEquals(-1, accommodations.FindAccommodation(-1234));
        assertEquals(-1, accommodations.FindAccommodation(0));
        assertEquals(0, accommodations.FindAccommodation(1017));
        assertEquals(1, accommodations.FindAccommodation(1018));
    }

    @Test
    void cancelReservationPrivateAccommodation()
    {
        assertTrue(accommodations.CancelReservationPrivateAccommodation(
                126,
                new Customers("Nick", "password", "Customer", "male")
        ));

        assertTrue(accommodations.getAirbnb().get(0).Reserve(
                new Customers("Nick", "password", "Customer", "male"),
                new Date(6, 9, 2022)
        ));

        assertTrue(accommodations.getAirbnb().get(0).Reserve(
                new Customers("Maria", "password", "Customer", "female"),
                new Date(27, 9, 2023)
        ));

        assertTrue(accommodations.getAirbnb().get(0).Reserve(
                new Customers("Nick", "password", "Customer", "male"),
                new Date(27, 9, 2023)
        ));

        assertFalse(accommodations.CancelReservationPrivateAccommodation(
                -1,
                new Customers("Maria", "password", "Customer", "female")
        ));
    }

    @Test
    void cancelReservationHotelRoom()
    {
        assertFalse(accommodations.CancelReservationHotelRoom(
                127,
                new Customers("Maria", "password", "Customer", "female")
        ));

        assertTrue(accommodations.getRooms().get(0).Reserve(
                new Customers("Nick", "password", "Customer", "male"),
                new Date(12, 5, 2022)
        ));

        assertTrue(accommodations.getRooms().get(0).Reserve(
                new Customers("Maria", "password", "Customer", "female"),
                new Date(21, 9, 2023)
        ));

        assertTrue(accommodations.getRooms().get(0).Reserve(
                new Customers("Nick", "password", "Customer", "male"),
                new Date(21, 9, 2023)
        ));

        assertFalse(accommodations.CancelReservationHotelRoom(
                127,
                new Customers("Maria", "password", "Customer", "female")
        ));
    }

    @Test
    void userHotelReservations()
    {
        assertEquals(
                1,
                accommodations.UserHotelReservations(new Customers("Nikol", "password", "Customer", "female")).size()
        );

        assertEquals(
                2,
                accommodations.UserHotelReservations(new Customers("Rose", "password", "Customer", "female")).size()
        );
    }

    @Test
    void userPrivateReservations()
    {
        assertEquals(
                1,
                accommodations.UserPrivateReservations(new Customers("Nick", "password", "Customer", "male")).size()
        );

        assertEquals(
                2,
                accommodations.UserPrivateReservations(new Customers("Isabella", "password", "Customer", "female")).size()
        );
    }

    /**
     * Αρχικοποίηση μερικών καταλυμάτων και
     * κρατήσεων για δοκιμή περιπτώσεων.
     */
    void creationOfDefaultAccommodations()
    {
        // Καθαρισμός όλων των υπάρχοντων καταλυμάτων
        accommodations.getAirbnb().clear();
        accommodations.getRooms().clear();

        // Χαρακτηριστικά
        ArrayList<String> characteristics = new ArrayList<>();
        characteristics.add("view");
        characteristics.add("garage");

        // Ιδιωτικά καταλύματα
        PrivateAccommodation b = new PrivateAccommodation(45, 114, "Airbnb", "New York", "Luxury Apartments", accommodations.identifierManager(), 2, characteristics, "uploads/accommodation6.png");
        PrivateAccommodation c = new PrivateAccommodation(65, 230, "Airbnb", "London", "Suits El. Greco", accommodations.identifierManager(), 3, characteristics, "uploads/accommodation7.png");
        accommodations.getAirbnb().add(b);
        accommodations.getAirbnb().add(c);

        // Δωμάτια ξενοδοχείων
        HotelRooms d = new HotelRooms(243, 35, 67, "Acropolis Palace", "Athens", 2, 1022, 2, characteristics, "uploads/room0.png");
        HotelRooms e = new HotelRooms(567, 43, 113, "Galaxy Hotel", "Thessaloniki", 5, 1023, 3, characteristics, "uploads/room1.png");
        accommodations.getRooms().add(d);
        accommodations.getRooms().add(e);

        // Κρατήσεις: ιδιωτικά καταλύματα
        b.Reserve(new Customers("Nick", "password", "Customer", "male"), new Date(6, 9, 2022));
        b.Reserve(new Customers("Nick", "password", "Customer", "male"), new Date(7, 9, 2022));
        b.Reserve(new Customers("Nick", "password", "Customer", "male"), new Date(8, 9, 2022));
        c.Reserve(new Customers("Isabella", "password", "Customer", "female"), new Date(5, 6, 2022));
        c.Reserve(new Customers("Isabella", "password", "Customer", "female"), new Date(6, 6, 2022));
        c.Reserve(new Customers("Isabella", "password", "Customer", "female"), new Date(7, 6, 2022));
        b.Reserve(new Customers("Isabella", "password", "Customer", "female"), new Date(8, 6, 2022));

        // Κρατήσεις: δωμάτια ξενοδοχείων
        d.Reserve(new Customers("Nikol", "password", "Customer", "female"), new Date(12, 5, 2022));
        e.Reserve(new Customers("Rose", "password", "Customer", "female"), new Date(31, 12, 2022));
        d.Reserve(new Customers("Rose", "password", "Customer", "female"), new Date(1, 1, 2023));
    }
}