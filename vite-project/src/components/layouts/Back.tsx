import { FC } from "react";
import { Button } from "../forms/style.d";
import { ArrowBigLeft } from "lucide-react";
import { Link } from "react-router-dom";

interface BackProps {
  children: React.ReactNode;
}

const Back: FC<BackProps> = ({ children }) => {
  return (
    <div style={{ display: "flex", flexDirection: "column", gap: "50px" }}>
      <Button icon={<ArrowBigLeft />} size="medium">
        <Link to={"/"}>Back to home</Link>
      </Button>
      {children}
    </div>
  );
};

export default Back;
