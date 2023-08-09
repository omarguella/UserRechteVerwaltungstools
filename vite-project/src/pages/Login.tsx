import { Col, Form, Row, Space, Typography } from "antd";
import { useForm } from "antd/es/form/Form";
import { LogInIcon } from "lucide-react";
import { Link } from "react-router-dom";
import Input from "../components/forms/Input";
import { Button, Password } from "../components/forms/style.d";
import Back from "../components/layouts/Back";
import { Container } from "../components/layouts/style.d";
import UseNotification from "../hooks/notification";
import { useAppDispatch, useAppSelector } from "../hooks/redux";
import { LoginAction } from "../redux/actions/auth";
import { TLogin } from "../types/auth";
const { Title } = Typography;

function Login() {
  const [form] = useForm();
  const dispatch = useAppDispatch();
  const { loading } = useAppSelector(state => state.auth);
  const { openErrorNotification, contextHolder } = UseNotification();

  return (
    <Container>
      {contextHolder}
      <Back>
        <Row gutter={[16, 16]}>
          <Title level={2}>Login To Your Account</Title>
          <Col xs={24} sm={24} md={24} lg={24} xl={24}>
            <Form
              layout="vertical"
              name="login"
              form={form}
              onFinish={(values: TLogin) => {
                dispatch(LoginAction(values))
                  .unwrap()
                  .then(res => {
                    console.log(res);
                    window.location.href = "/";
                  })
                  .catch(err =>
                    openErrorNotification(`Something wrong try again`)
                  );
              }}
            >
              <Form.Item
                label="Email or Username"
                name="emailOrUsername"
                rules={[{ required: true, message: "Required field" }]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                label="Password"
                name="password"
                rules={[{ required: true, message: "Required field" }]}
              >
                <Password />
              </Form.Item>
              <Space
                align="center"
                direction="horizontal"
                style={{ display: "flex", justifyContent: "space-between" }}
              >
                <Link to={"/register"}>You don't have account?</Link>
                <Button
                  size={"large"}
                  type="primary"
                  style={{ marginTop: "10px" }}
                  loading={loading}
                  icon={<LogInIcon />}
                  htmlType={"submit"}
                >
                  {" "}
                  Login
                </Button>
              </Space>
            </Form>
          </Col>
        </Row>
      </Back>
    </Container>
  );
}

export default Login;
