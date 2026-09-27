package com.iwfc.service;

import com.iwfc.exception.InvalidBookingException;
import com.iwfc.model.Booking;
import com.iwfc.model.FitnessSession;
import com.iwfc.model.Member;
import com.iwfc.repository.GenericRepository;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    @Test
    void shouldCreateValidBooking() throws Exception {

        GenericRepository<FitnessSession> sessions = new GenericRepository<>();
        GenericRepository<Booking> bookings = new GenericRepository<>();

        BookingService service = new BookingService(
                sessions,
                bookings,
                new NotificationService()
        );

        sessions.add(
                "S1",
                new FitnessSession(
                        "S1",
                        "Yoga",
                        DayOfWeek.MONDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0),
                        "Studio A",
                        null,
                        10,
                        false
                )
        );

        Booking booking = service.bookSession(
                new Member("U1", "Alice"),
                "S1",
                "B1"
        );

        assertEquals("B1", booking.getBookingId());
    }


    @Test
    void shouldRejectDuplicateMemberBooking() throws Exception {

        GenericRepository<FitnessSession> sessions = new GenericRepository<>();
        GenericRepository<Booking> bookings = new GenericRepository<>();

        BookingService service = new BookingService(
                sessions,
                bookings,
                new NotificationService()
        );

        Member member = new Member("U1", "Alice");

        sessions.add(
                "S1",
                new FitnessSession(
                        "S1",
                        "Yoga",
                        DayOfWeek.MONDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0),
                        "Studio A",
                        null,
                        10,
                        false
                )
        );

        service.bookSession(member, "S1", "B1");

        assertThrows(
                InvalidBookingException.class,
                () -> service.bookSession(member, "S1", "B2")
        );
    }


    @Test
    void shouldRejectBookingOutsideOperatingHours() throws Exception {

        GenericRepository<FitnessSession> sessions = new GenericRepository<>();
        GenericRepository<Booking> bookings = new GenericRepository<>();

        BookingService service = new BookingService(
                sessions,
                bookings,
                new NotificationService()
        );

        sessions.add(
                "S1",
                new FitnessSession(
                        "S1",
                        "Early Yoga",
                        DayOfWeek.MONDAY,
                        LocalTime.of(5, 0),
                        LocalTime.of(6, 0),
                        "Studio A",
                        null,
                        10,
                        false
                )
        );

        Member member = new Member("U1", "Alice");

        assertThrows(
                InvalidBookingException.class,
                () -> service.bookSession(member, "S1", "B1")
        );
    }


    @Test
    void shouldRejectBookingWhenSessionIsFull() throws Exception {

        GenericRepository<FitnessSession> sessions = new GenericRepository<>();
        GenericRepository<Booking> bookings = new GenericRepository<>();

        BookingService service = new BookingService(
                sessions,
                bookings,
                new NotificationService()
        );

        sessions.add(
                "S1",
                new FitnessSession(
                        "S1",
                        "Small Yoga Class",
                        DayOfWeek.MONDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0),
                        "Studio A",
                        null,
                        1,
                        false
                )
        );

        Member firstMember = new Member("U1", "Alice");
        Member secondMember = new Member("U2", "Bob");

        service.bookSession(firstMember, "S1", "B1");

        assertThrows(
                InvalidBookingException.class,
                () -> service.bookSession(secondMember, "S1", "B2")
        );
    }


    @Test
    void shouldRejectBookingForUnknownSession() throws Exception {

        GenericRepository<FitnessSession> sessions = new GenericRepository<>();
        GenericRepository<Booking> bookings = new GenericRepository<>();

        BookingService service = new BookingService(
                sessions,
                bookings,
                new NotificationService()
        );

        Member member = new Member("U1", "Alice");

        assertThrows(
                InvalidBookingException.class,
                () -> service.bookSession(member, "UNKNOWN", "B1")
        );
    }


    @Test
    void shouldRejectDuplicateBookingId() throws Exception {

        GenericRepository<FitnessSession> sessions = new GenericRepository<>();
        GenericRepository<Booking> bookings = new GenericRepository<>();

        BookingService service = new BookingService(
                sessions,
                bookings,
                new NotificationService()
        );

        sessions.add(
                "S1",
                new FitnessSession(
                        "S1",
                        "Yoga",
                        DayOfWeek.MONDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0),
                        "Studio A",
                        null,
                        10,
                        false
                )
        );

        Member firstMember = new Member("U1", "Alice");
        Member secondMember = new Member("U2", "Bob");

        service.bookSession(firstMember, "S1", "B1");

        assertThrows(
                InvalidBookingException.class,
                () -> service.bookSession(secondMember, "S1", "B1")
        );
    }
}