package com.courseproject.travelagencyrestapiawtentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private HolidayRepository holidayRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Holiday holiday;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        holiday = new Holiday();
        holiday.setId(1L);
        holiday.setTitle("Summer Vacation");
        holiday.setDuration(7);
        holiday.setFreeSlots(10);
        holiday.setPrice("500");

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setPhoneNumber("123456789");
        reservation.setContactName("John Doe");
        reservation.setHoliday(holiday);
    }




    @Test
    void testCreateReservationHolidayFull() {
        holiday.setFreeSlots(0);
        CreateReservationDTO createReservationDTO = new CreateReservationDTO();
        createReservationDTO.setPhoneNumber("123456789");
        createReservationDTO.setContactName("John Doe");
        createReservationDTO.setHoliday(holiday.getId());
        when(holidayRepository.findById(holiday.getId())).thenReturn(Optional.of(holiday));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(createReservationDTO);
        });
        assertEquals("Holiday is full", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testGetAllReservations() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));
        var responseList = reservationService.getAllReservations();
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        assertEquals(reservation.getContactName(), responseList.get(0).getContactName());
        verify(reservationRepository, times(1)).findAll();
    }


    @Test
    void testGetReservationById() {
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        ResponseReservationDTO response = reservationService.getReservationById(reservation.getId());
        assertNotNull(response);
        assertEquals(reservation.getContactName(), response.getContactName());
        verify(reservationRepository, times(1)).findById(reservation.getId());
    }


    @Test
    void testGetReservationByIdNotFound() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseReservationDTO response = reservationService.getReservationById(1L);
        assertNull(response);
        verify(reservationRepository, times(1)).findById(1L);
    }


    @Test
    void testUpdateReservation() {
        UpdateReservationDTO updateReservationDTO = new UpdateReservationDTO();
        updateReservationDTO.setId(reservation.getId());
        updateReservationDTO.setPhoneNumber("987654321");
        updateReservationDTO.setContactName("Ivan Ivanov");
        updateReservationDTO.setHoliday(holiday.getId());
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(holidayRepository.findById(holiday.getId())).thenReturn(Optional.of(holiday));
        ResponseReservationDTO response = reservationService.updateReservation(updateReservationDTO);
        assertNotNull(response);
        assertEquals("Ivan Ivanov", response.getContactName());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testUpdateReservationNotFound() {
        UpdateReservationDTO updateReservationDTO = new UpdateReservationDTO();
        updateReservationDTO.setId(1L);
        when(reservationRepository.findById(updateReservationDTO.getId())).thenReturn(Optional.empty());
        ResponseReservationDTO response = reservationService.updateReservation(updateReservationDTO);
        assertNull(response);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testDeleteReservation() {
        when(reservationRepository.existsById(reservation.getId())).thenReturn(true);
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        Boolean result = reservationService.deleteReservation(reservation.getId());
        assertTrue(result);
        verify(reservationRepository, times(1)).deleteById(reservation.getId());
    }


    @Test
    void testDeleteReservationNotFound() {
        when(reservationRepository.existsById(anyLong())).thenReturn(false);
        Boolean result = reservationService.deleteReservation(1L);
        assertFalse(result);
        verify(reservationRepository, never()).deleteById(anyLong());
    }
}
