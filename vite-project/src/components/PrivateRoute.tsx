import { useEffect } from "react";
import { useAppSelector } from "../hooks/redux";
import { useNavigate } from "react-router-dom";

const PrivateRoute = ({ children }: { children: React.ReactNode }) => {
  const isConnected = false;
  const user = {};
  /* const { isConnected, user } = useAppSelector(state => state.auth); */
  const navigate = useNavigate();
  useEffect(() => {
    if (!isConnected || !user) {
      return navigate("/login");
    }
  }, [isConnected, user]);

  return <>{children}</>;
};

export default PrivateRoute;
