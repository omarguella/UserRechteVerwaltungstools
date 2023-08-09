import { useEffect } from "react";
import { useAppSelector } from "../hooks/redux";
import { useNavigate } from "react-router-dom";

const PrivateRoute = ({ children }: { children: React.ReactNode }) => {
  const { tokens, isConnected } = useAppSelector(state => state.auth);
  /* const { isConnected, user } = useAppSelector(state => state.auth); */
  const navigate = useNavigate();
  useEffect(() => {
    if (!isConnected || !tokens) {
      return navigate("/login");
    }
  }, [isConnected, tokens]);

  return <>{children}</>;
};

export default PrivateRoute;
