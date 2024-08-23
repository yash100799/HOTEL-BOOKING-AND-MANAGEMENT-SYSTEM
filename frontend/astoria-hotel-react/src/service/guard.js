import React, { Component } from "react";
import { Navigate, useLocation } from "react-router-dom";
import ApiService from "./ApiService";

//Will throw back to login page if trying to access paths which are only available for autheticated USERS
export const ProtectedRoute = ({ element: Component }) => {

    const location = useLocation();

    return ApiService.isAuthenticated() ? (

        Component

    ) : (

        <Navigate to="/login" replace state={{ from: location }} />

    );

};

//Will protect Admin Routes
export const AdminRoute = ({ element: Component }) => {

    const location = useLocation();

    return ApiService.isAdmin() ? (

        Component

    ) : (

        <Navigate to="/login" replace state={{ from: location }} />

    );

};