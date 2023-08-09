import React from "react";
import { createBrowserRouter } from "react-router-dom";
import { withLoading } from "../hoc/withLoading.hoc";
import PrivateRoute from "./PrivateRoute";
import Layout from "./layouts/Layout";

const RegisterPage = React.lazy(() => import("../pages/Register"));
const LoginPage = React.lazy(() => import("../pages/Login"));
const ProfilePage = React.lazy(() => import("../pages/Profile"));
const AdministrationPage = React.lazy(() => import("../pages/Administration"));
/* with loading */
const Register = withLoading(RegisterPage);
const Login = withLoading(LoginPage);
const Profile = withLoading(ProfilePage);
const Administration = withLoading(AdministrationPage);
const routes = createBrowserRouter([
  {
    path: "/",
    element: (
      <PrivateRoute>
        <Layout />
      </PrivateRoute>
    ),
    children: [
      {
        path: "/",
        element: (
          <PrivateRoute>
            <Profile />
          </PrivateRoute>
        ),
      },
      {
        path: "/administration",
        element: (
          <PrivateRoute>
            <Administration />
          </PrivateRoute>
        ),
      },
    ],
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/register",
    element: <Register />,
  },
]);

export default routes;
