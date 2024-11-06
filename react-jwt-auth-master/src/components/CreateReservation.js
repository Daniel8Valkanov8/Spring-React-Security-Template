import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './HomeHolidayComponent.css';
import 'bootstrap/dist/css/bootstrap.min.css';

const CreateReservationComponent = ({ currentUser }) => {
  const [holidays, setHolidays] = useState([]);
  const [selectedHoliday, setSelectedHoliday] = useState(null);
  const [contactName, setContactName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [successMessage, setSuccessMessage] = useState(null);
  const [errors, setErrors] = useState({});

  const user = JSON.parse(localStorage.getItem('user'));
  const bearerToken = user?.accessToken;

  useEffect(() => {
    fetchHolidays();
  }, [bearerToken]);

  const fetchHolidays = () => {
    axios.get('http://localhost:8080/holidays', {
      headers: {
        Authorization: `Bearer ${bearerToken}`,
      },
    })
    .then(response => {
      const validHolidays = response.data.filter(holiday => 
        holiday.freeSlots > 0 && holiday.duration > 0
      );
      setHolidays(validHolidays);
    })
    .catch(error => console.error('Error fetching holidays:', error));
  };

  const handleCreateReservationClick = (holiday) => {
    setSelectedHoliday(selectedHoliday?.id === holiday.id ? null : holiday);
  };

  const handleFinishClick = () => {
    const newErrors = {};

    if (!/^\d{10,}$/.test(phoneNumber)) {
      newErrors.phoneNumber = 'Phone number must contain at least 10 characters';
    }

    if (!/^.{5,}$/.test(contactName) || !/\s/.test(contactName)) {
      newErrors.contactName = 'Contact name must contain at least 5 characters and include a space';
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    } else {
      setErrors({});
    }

    if (selectedHoliday) {
      axios.post('http://localhost:8080/user-reservations', {
        contactName,
        phoneNumber,
        holiday: selectedHoliday.id,
      }, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${bearerToken}`,
        },
      })
      .then(response => {
        console.log('Reservation created:', response.data);
        
        // Reload holidays after successful reservation
        fetchHolidays();

        // Set the success message with the current user's name
        setSuccessMessage(
          `Congratulations, ${currentUser?.username || 'User'}, you have made a successful booking.
           Contact name: ${contactName},
            Phone number: ${phoneNumber}`
        );

        // Clear the form and selected holiday
        setSelectedHoliday(null);
        setContactName('');
        setPhoneNumber('');

        // Hide the success message after 5 seconds
        setTimeout(() => {
          setSuccessMessage(null);
        }, 5000);
      })
      .catch(error => console.error('Error creating reservation:', error));
    }
  };

  return (
    <div className="container mt-4">
      <div className="row">
        <div className="col-md-8">
          <div className="holiday-container">
            {holidays.map(holiday => (
              <div 
                key={holiday.id} 
                className={`holiday-card ${selectedHoliday?.id === holiday.id ? 'expanded' : ''}`}
              >
                <h3 className="holiday-title">{holiday.title}</h3>
                <p className="holiday-location">
                  {holiday.location.city}, {holiday.location.country}
                </p>
                <p className="holiday-dates">
                  {new Date(holiday.startDate).toLocaleDateString()} - {holiday.duration} days
                </p>
                <p className="holiday-price">{holiday.price} USD</p>
                <p className="holiday-slots">{holiday.freeSlots} slots available</p>
                <button 
                  className="btn btn-primary"
                  onClick={() => handleCreateReservationClick(holiday)}
                >
                  {selectedHoliday?.id === holiday.id ? 'Close' : 'Create Reservation'}
                </button>
                {selectedHoliday?.id === holiday.id && (
                  <div className="reservation-form mt-3">
                    <h4>Reservation Form</h4>
                    <input 
                      type="text"
                      className={`form-control mb-2 ${errors.contactName ? 'is-invalid' : ''}`}
                      placeholder="Your Name"
                      value={contactName}
                      onChange={(e) => setContactName(e.target.value)}
                    />
                    {errors.contactName && <small className="text-danger">{errors.contactName}</small>}
                    <input 
                      type="text"
                      className={`form-control mb-2 ${errors.phoneNumber ? 'is-invalid' : ''}`}
                      placeholder="Phone Number"
                      value={phoneNumber}
                      onChange={(e) => setPhoneNumber(e.target.value)}
                    />
                    {errors.phoneNumber && <small className="text-danger">{errors.phoneNumber}</small>}
                    <button 
                      className="btn btn-success"
                      onClick={handleFinishClick}
                    >
                      Finish
                    </button>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
        
        <div className="col-md-4">
          {successMessage && (
            <div className="alert alert-success" role="alert">
              {successMessage}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default CreateReservationComponent;
