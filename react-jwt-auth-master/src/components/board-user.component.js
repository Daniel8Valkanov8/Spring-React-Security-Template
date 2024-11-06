import React, { useState, useEffect } from "react";
import UserService from "../services/user.service";
import EventBus from "../common/EventBus";
import CreateReservationComponent from "../components/CreateReservation";

const BoardUser = ({ currentUser }) => {
  const [content, setContent] = useState("");
  const [isAuthorized, setIsAuthorized] = useState(false);
  const [filteredVacations, setFilteredVacations] = useState([]);

  useEffect(() => {
    UserService.getUserBoard().then(
      (response) => {
        setContent(response.data);
        setIsAuthorized(true);
      },
      (error) => {
        setContent(
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
            error.message ||
            error.toString()
        );
        setIsAuthorized(false);

        if (error.response && error.response.status === 401) {
          EventBus.dispatch("logout");
        }
      }
    );
  }, []);

  useEffect(() => {
    const fetchVacations = async () => {
      try {
        const response = await UserService.getVacations();
        const validVacations = response.data.filter(
          (holiday) => holiday.duration > 0 && holiday.freeSlots > 0
        );
        setFilteredVacations(validVacations);
      } catch (error) {
        console.error("Error fetching vacations:", error);
      }
    };

    fetchVacations();
  }, []);

  return (
    <div className="container">
      <header className="jumbotron">
        <h3>{content}</h3>
        {isAuthorized && <CreateReservationComponent currentUser={currentUser} vacations={filteredVacations} />}
      </header>
    </div>
  );
};

export default BoardUser;
