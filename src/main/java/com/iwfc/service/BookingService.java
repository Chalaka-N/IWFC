package com.iwfc.service;

import com.iwfc.exception.InvalidBookingException;
import com.iwfc.model.Booking;
import com.iwfc.model.FitnessSession;
import com.iwfc.model.Member;
import com.iwfc.repository.GenericRepository;

import java.time.LocalTime;

public class BookingService {
    private final GenericRepository<FitnessSession> sessionRepository;
    private final GenericRepository<Booking> bookingRepository;
    private final NotificationService notificationService;

    public BookingService(GenericRepository<FitnessSession> sessionRepository,
                          GenericRepository<Booking> bookingRepository,
                          NotificationService notificationService) {
        this.sessionRepository = sessionRepository;
        this.bookingRepository = bookingRepository;
        this.notificationService = notificationService;
    }

    public Booking bookSession(Member member, String sessionId, String bookingId) throws InvalidBookingException {
        FitnessSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new InvalidBookingException("Session not found."));

        if (session.getStartTime().isBefore(LocalTime.of(6, 0)) ||
            session.getEndTime().isAfter(LocalTime.of(22, 0))) {
            throw new InvalidBookingException("Booking is outside operating hours (06:00-22:00).");
        }

        boolean memberAlreadyBooked = bookingRepository.findAll().stream()
                .anyMatch(b -> b.getMemberId().equals(member.getUserId()) && b.getSessionId().equals(sessionId));
        if (memberAlreadyBooked) {
            throw new InvalidBookingException("Member already has a booking for this session.");
        }

        long bookingsForSession = bookingRepository.findAll().stream()
                .filter(b -> b.getSessionId().equals(sessionId)).count();
        if (bookingsForSession >= session.getCapacity()) {
            throw new InvalidBookingException("Session capacity has been reached.");
        }

        Booking booking = new Booking(bookingId, member.getUserId(), sessionId);
        try {
            bookingRepository.add(bookingId, booking);
        } catch (Exception ex) {
            throw new InvalidBookingException("Booking could not be created: " + ex.getMessage());
        }
        notificationService.send("Booking " + bookingId + " confirmed for session " + session.getName());
        return booking;
    }
}
