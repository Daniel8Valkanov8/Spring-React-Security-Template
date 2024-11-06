import React, { useState, useEffect } from 'react';
import './HomeHolidayComponent.css'; // Не забравяй да създадеш този CSS файл

const HomeHolidayComponent = () => {
  const [holidays, setHolidays] = useState([]);

  useEffect(() => {
    // Актуализиран URL за получаване на данните
    fetch('http://localhost:8080/holidays')
      .then(response => response.json())
      .then(data => setHolidays(data.filter(holiday => holiday.freeSlots > 0))) // Филтрира празниците
      .catch(error => console.error('Error fetching holidays:', error));
  }, []);

  return (
    <div className="holiday-container">
      {holidays.map(holiday => (
        <div key={holiday.id} className="holiday-card">
          <h3 className="holiday-title">{holiday.title}</h3>
          <p className="holiday-location">
            {holiday.location.city}, {holiday.location.country}
          </p>
          <p className="holiday-dates">
            {new Date(holiday.startDate).toLocaleDateString()} - {holiday.duration} days
          </p>
          <p className="holiday-price">{holiday.price} $</p>
          <p className="holiday-slots">{holiday.freeSlots} slots available</p>
        </div>
      ))}
    </div>
  );
};

export default HomeHolidayComponent;
