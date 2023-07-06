import { Space } from "antd";
import { Button } from "../components/forms/style.d";
import { Container } from "../components/layouts/style.d";
import { Link } from "react-router-dom";

function Home() {
  return (
    <Container>
      <Space direction="horizontal">
        <Button type="primary" size="large">
          <Link to={"/login"}>Login</Link>
        </Button>
        <Button type="primary" size="large">
          <Link to={"/register"}>Register</Link>
        </Button>
      </Space>
    </Container>
  );
}

export default Home;
