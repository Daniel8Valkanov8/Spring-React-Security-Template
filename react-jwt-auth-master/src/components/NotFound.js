// src/components/NotFound.js

import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './NotFound.css'; // Създайте този CSS файл за стилизации

const NotFound = () => {
  return (
    <div className="container text-center mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="alert alert-danger">
            <h1 className="display-4">Travel Agency</h1>
            <h2 className="display-4">404 Not Found</h2>
            <p>The page you are looking for does not exist.</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NotFound;
