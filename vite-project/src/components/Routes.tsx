import React from "react";
import { createBrowserRouter } from "react-router-dom";
import { withLoading } from "../hoc/withLoading.hoc";
import PrivateRoute from "./PrivateRoute";
import Layout from "./layouts/Layout";
import RolePermissions from "../pages/RolePermissions";

const RegisterPage = React.lazy(() => import("../pages/Register"));
const LoginPage = React.lazy(() => import("../pages/Login"));
const ProfilePage = React.lazy(() => import("../pages/Profile"));
const AdministrationPage = React.lazy(() => import("../pages/Administration"));
const LogsPage = React.lazy(() => import("../pages/Logs"));
const RolesAdministrationPage = React.lazy(
  () => import("../pages/RolesAdministration")
);
const PermissionsAdministrationPage = React.lazy(
  () => import("../pages/PermissionsAdministration")
);
/* with loading */
const Register = withLoading(RegisterPage);
const Login = withLoading(LoginPage);
const Profile = withLoading(ProfilePage);
const Administration = withLoading(AdministrationPage);
const RolesAdministration = withLoading(RolesAdministrationPage);
const PermissionsAdministration = withLoading(PermissionsAdministrationPage);
const Logs = withLoading(LogsPage);
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
      {
        path: "/roles",
        element: (
          <PrivateRoute>
            <RolesAdministration />
          </PrivateRoute>
        ),
      },
      {
        path: "/roles/:name",
        element: (
          <PrivateRoute>
            <RolePermissions />
          </PrivateRoute>
        ),
      },
      {
        path: "/permissions",
        element: (
          <PrivateRoute>
            <PermissionsAdministration />
          </PrivateRoute>
        ),
      },
      {
        path: "/logs",
        element: (
          <PrivateRoute>
            <Logs />
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
