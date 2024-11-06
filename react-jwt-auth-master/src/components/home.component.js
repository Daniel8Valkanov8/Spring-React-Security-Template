import React, { Component } from "react";
import HomeHolidayComponent from "./HomeHolidayComponent"; // Импортиране на компонента



export default class Home extends Component {
  constructor(props) {
    super(props);

    this.state = {
      content: ""
    };
  }

  

  render() {
    return (
      <div className="container">
        <h1>Welcome to Travel Agency</h1>
        <p>
          We are a travel agency offering exciting vacations to a variety of destinations around the world.
          Explore some of our vacation options, and if you wish to make a reservation,
          you can create an account or log in if you already have one to complete your booking.
        </p>
        <header className="jumbotron">
          <HomeHolidayComponent /> {/* Включване на компонента */}
        </header>
      </div>
    );
  }
}
