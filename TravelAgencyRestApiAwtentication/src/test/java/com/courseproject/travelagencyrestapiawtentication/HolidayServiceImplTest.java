package com.courseproject.travelagencyrestapiawtentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HolidayServiceImplTest {

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private HolidayServiceImpl holidayService;

    private Location location;
    private Holiday holiday;

    @BeforeEach
    void setUp() {
        location = new Location(1L, "100", "Bulgaria", "Sofia", "Vitosha");
        holiday = new Holiday();
        holiday.setId(1L);
        holiday.setTitle("Summer Vacation");
        holiday.setDuration(7);
        holiday.setFreeSlots(10);
        holiday.setPrice("500");
        holiday.setLocation(location);
        holiday.setStartDate(new Date());
    }


    @Test
    void testCreateHoliday() {
        CreateHolidayDTO createHolidayDTO = new CreateHolidayDTO();
        createHolidayDTO.setTitle("Summer Vacation");
        createHolidayDTO.setDuration(7);
        createHolidayDTO.setFreeSlots(10);
        createHolidayDTO.setPrice("500");
        createHolidayDTO.setLocation(Integer.valueOf(1));
        createHolidayDTO.setStartDate(new Date());

        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));
        when(holidayRepository.save(any(Holiday.class))).thenReturn(holiday);

        ResponseHolidayDTO response = holidayService.createHoliday(createHolidayDTO);

        assertNotNull(response);
        assertEquals(holiday.getTitle(), response.getTitle());
        verify(holidayRepository, times(1)).save(any(Holiday.class));
    }

    @Test
    void testGetAllHolidays() {
        when(holidayRepository.findAll()).thenReturn(Arrays.asList(holiday));

        var responseList = holidayService.getAllHolidays();

        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
        assertEquals(holiday.getTitle(), responseList.get(0).getTitle());
        verify(holidayRepository, times(1)).findAll();
    }

    @Test
    void testGetHolidayById() {
        when(holidayRepository.findById(holiday.getId())).thenReturn(Optional.of(holiday));
        ResponseHolidayDTO response = holidayService.getHolidayById(holiday.getId());
        assertNotNull(response);
        assertEquals(holiday.getTitle(), response.getTitle());
        verify(holidayRepository, times(1)).findById(holiday.getId());
    }

    @Test
    void testGetHolidayByIdNotFound() {
        when(holidayRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseHolidayDTO response = holidayService.getHolidayById(1L);
        assertNull(response);
        verify(holidayRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateHoliday() {
        UpdateHolidayDTO updateHolidayDTO = new UpdateHolidayDTO();
        updateHolidayDTO.setId(holiday.getId());
        updateHolidayDTO.setTitle("Updated Vacation");
        updateHolidayDTO.setDuration(10);
        updateHolidayDTO.setFreeSlots(8);
        updateHolidayDTO.setPrice("600");
        updateHolidayDTO.setStartDate(new Date());
        when(holidayRepository.findById(holiday.getId())).thenReturn(Optional.of(holiday));
        when(holidayRepository.save(any(Holiday.class))).thenReturn(holiday);
        ResponseHolidayDTO response = holidayService.updateHoliday(updateHolidayDTO);
        assertNotNull(response);
        assertEquals("Updated Vacation", response.getTitle());
        verify(holidayRepository, times(1)).findById(holiday.getId());
        verify(holidayRepository, times(1)).save(any(Holiday.class));
    }


    @Test
    void testUpdateHolidayNotFound() {
        UpdateHolidayDTO updateHolidayDTO = new UpdateHolidayDTO();
        updateHolidayDTO.setId(1L);
        when(holidayRepository.findById(updateHolidayDTO.getId())).thenReturn(Optional.empty());
        ResponseHolidayDTO response = holidayService.updateHoliday(updateHolidayDTO);
        assertNull(response);
        verify(holidayRepository, times(1)).findById(updateHolidayDTO.getId());
        verify(holidayRepository, never()).save(any(Holiday.class));
    }


    @Test
    void testDeleteHoliday() {
        when(holidayRepository.existsById(holiday.getId())).thenReturn(true);
        Boolean result = holidayService.deleteHoliday(holiday.getId());
        assertTrue(result);
        verify(holidayRepository, times(1)).deleteById(holiday.getId());
    }


    @Test
    void testDeleteHolidayNotFound() {
        when(holidayRepository.existsById(1L)).thenReturn(false);
        Boolean result = holidayService.deleteHoliday(1L);
        assertFalse(result);
        verify(holidayRepository, never()).deleteById(anyLong());
    }
}
