import React, { Component } from "react";
import UserService from "../services/user.service";
import EventBus from "../common/EventBus";


export default class BoardAdminHoliday extends Component {
  constructor(props) {
    super(props);

    this.state = {
      content: "",
      isAuthorized: false, // Add a new state to track authorization
    };
  }

  componentDidMount() {
    UserService.getAdminBoard().then(
      (response) => {
        this.setState({
          content: response.data,
          isAuthorized: true, // Set isAuthorized to true if the request is successful
        });
      },
      (error) => {
        this.setState({
          content:
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString(),
          isAuthorized: false, // Set isAuthorized to false if there's an error
        });

        if (error.response && error.response.status === 401) {
          EventBus.dispatch("logout");
        }
      }
    );
  }

  render() {
    return (
      <div className="container">
        <header className="jumbotron">
          <h3>{this.state.content}</h3>
          {}
          {this.state.isAuthorized && <BoardAdminHoliday/>}
        </header>
      </div>
    );
  }
}
