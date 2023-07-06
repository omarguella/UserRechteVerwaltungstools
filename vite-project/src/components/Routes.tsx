import React from "react";
import { createBrowserRouter } from "react-router-dom";
const HomePage = React.lazy(() => import("../pages/Home"));
const RegisterPage = React.lazy(() => import("../pages/Register"));
const LoginPage = React.lazy(() => import("../pages/Login"));
import PrivateRoute from "./PrivateRoute";
import { withLoading } from "../hoc/withLoading.hoc";

/* with loading */
const Register = withLoading(RegisterPage);
const Login = withLoading(LoginPage);
const Home = withLoading(HomePage);
const routes = createBrowserRouter([
  {
    path: "/",
    element: (
      <PrivateRoute>
        <Home />
      </PrivateRoute>
    ),
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
