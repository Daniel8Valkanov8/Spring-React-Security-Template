package com.courseproject.travelagencyrestapiawtentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location location;
    private CreateLocationDTO createLocationDTO;
    private UpdateLocationDTO updateLocationDTO;

    @BeforeEach
    public void setUp() {
        location = new Location();
        location.setId(1L);
        location.setNumber("123");
        location.setCountry("Bulgaria");
        location.setCity("Sofia");
        location.setStreet("Vitosha Blvd");

        createLocationDTO = new CreateLocationDTO();
        createLocationDTO.setNumber("123");
        createLocationDTO.setCountry("Bulgaria");
        createLocationDTO.setCity("Sofia");
        createLocationDTO.setStreet("Vitosha Blvd");

        updateLocationDTO = new UpdateLocationDTO();
        updateLocationDTO.setId(1L);
        updateLocationDTO.setNumber("124");
        updateLocationDTO.setCountry("Bulgaria");
        updateLocationDTO.setCity("Sofia");
        updateLocationDTO.setStreet("Patriarch Evtimiy Blvd");
    }

    @Test
    public void testCreateLocation() {
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        ResponseLocationDTO responseLocationDTO = locationService.createLocation(createLocationDTO);
        assertNotNull(responseLocationDTO);
        assertEquals("123", responseLocationDTO.getNumber());
        assertEquals("Bulgaria", responseLocationDTO.getCountry());
        assertEquals("Sofia", responseLocationDTO.getCity());
        assertEquals("Vitosha Blvd", responseLocationDTO.getStreet());
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    public void testDeleteLocationSuccess() {
        when(locationRepository.existsById(location.getId())).thenReturn(true);
        boolean isDeleted = locationService.deleteLocation(location.getId());
        assertTrue(isDeleted);
        verify(locationRepository, times(1)).deleteById(location.getId());
    }

    @Test
    public void testDeleteLocationFailure() {
        when(locationRepository.existsById(location.getId())).thenReturn(false);
        boolean isDeleted = locationService.deleteLocation(location.getId());
        assertFalse(isDeleted);
        verify(locationRepository, times(0)).deleteById(location.getId());
    }

    @Test
    public void testGetAllLocations() {
        List<Location> locationList = new ArrayList<>();
        locationList.add(location);
        when(locationRepository.findAll()).thenReturn(locationList);
        List<ResponseLocationDTO> responseLocationDTOList = locationService.getAllLocations();
        assertNotNull(responseLocationDTOList);
        assertEquals(1, responseLocationDTOList.size());
        assertEquals("123", responseLocationDTOList.get(0).getNumber());
        assertEquals("Bulgaria", responseLocationDTOList.get(0).getCountry());
        assertEquals("Sofia", responseLocationDTOList.get(0).getCity());
        assertEquals("Vitosha Blvd", responseLocationDTOList.get(0).getStreet());
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    public void testGetLocationByIdSuccess() {
        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));
        ResponseLocationDTO responseLocationDTO = locationService.getLocationById(location.getId());
        assertNotNull(responseLocationDTO);
        assertEquals("123", responseLocationDTO.getNumber());
        assertEquals("Bulgaria", responseLocationDTO.getCountry());
        assertEquals("Sofia", responseLocationDTO.getCity());
        assertEquals("Vitosha Blvd", responseLocationDTO.getStreet());
        verify(locationRepository, times(1)).findById(location.getId());
    }

    @Test
    public void testGetLocationByIdNotFound() {
        when(locationRepository.findById(location.getId())).thenReturn(Optional.empty());
        ResponseLocationDTO responseLocationDTO = locationService.getLocationById(location.getId());
        assertNull(responseLocationDTO);
        verify(locationRepository, times(1)).findById(location.getId());
    }

    @Test
    public void testUpdateLocationSuccess() {
        when(locationRepository.findById(updateLocationDTO.getId())).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        ResponseLocationDTO responseLocationDTO = locationService.updateLocation(updateLocationDTO);
        assertNotNull(responseLocationDTO);
        assertEquals("124", responseLocationDTO.getNumber());
        assertEquals("Bulgaria", responseLocationDTO.getCountry());
        assertEquals("Sofia", responseLocationDTO.getCity());
        assertEquals("Patriarch Evtimiy Blvd", responseLocationDTO.getStreet());
        verify(locationRepository, times(1)).findById(updateLocationDTO.getId());
        verify(locationRepository, times(1)).save(any(Location.class));
    }


    @Test
    public void testUpdateLocationNotFound() {
        when(locationRepository.findById(updateLocationDTO.getId())).thenReturn(Optional.empty());
        ResponseLocationDTO responseLocationDTO = locationService.updateLocation(updateLocationDTO);
        assertNull(responseLocationDTO);
        verify(locationRepository, times(1)).findById(updateLocationDTO.getId());
        verify(locationRepository, times(0)).save(any(Location.class));
    }
}
