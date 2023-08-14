import { Col, Form, Row, Typography } from "antd";
import { useForm } from "antd/es/form/Form";
import { LogInIcon, Regex } from "lucide-react";
import { FetchRole } from "../api/fetchRoles";
import Input from "../components/forms/Input";
import { Button, Password, Select } from "../components/forms/style.d";
import Back from "../components/layouts/Back";
import { Container } from "../components/layouts/style.d";
import { ReformatRole } from "../utils/roles";
import { useAppDispatch, useAppSelector } from "../hooks/redux";
import UseNotification from "../hooks/notification";
import { RegisterAction } from "../redux/actions/auth";
import { TRegistration } from "../types/auth";
import { Helmet } from "react-helmet";

function Register() {
  const { Title } = Typography;
  const [form] = useForm();
  const { data, isLoading, error } = FetchRole();
  const formattedRole = ReformatRole(data) ?? [];

  const dispatch = useAppDispatch();
  const { loading } = useAppSelector(state => state.auth);
  const { openErrorNotification, contextHolder } = UseNotification();
  const passwordPattern =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[.@$!%*?&])[A-Za-z\d.@$!%*?&]{8,}$/;
  const phonePattern = /^(\\+[0-9]{1,3})?[0-9]{9,15}$/;
  return (
    <Container style={{ flexDirection: "column" }}>
      <Helmet>
        <title>Register</title>
      </Helmet>
      {contextHolder}
      <Back>
        <Row gutter={[16, 16]}>
          <Title level={2}>Register Your Account</Title>
          <Col xs={24} sm={24} md={24} lg={24} xl={24}>
            <Form
              layout="vertical"
              name="register"
              form={form}
              onFinish={(values: TRegistration) => {
                dispatch(RegisterAction(values))
                  .unwrap()
                  .then(() => (window.location.href = "/login"))
                  .catch(err => openErrorNotification(err));
              }}
            >
              <Row gutter={[16, 16]}>
                <Col xs={24} sm={24} md={12} lg={12} xl={12}>
                  <Form.Item
                    label="Username"
                    name="username"
                    required
                    rules={[{ required: true, message: "Required field" }]}
                  >
                    <Input />
                  </Form.Item>
                  <Form.Item
                    label="Email"
                    name="email"
                    rules={[
                      { required: true, message: "Required field" },
                      {
                        type: "email",
                        message: "The input is not valid E-mail!",
                      },
                    ]}
                  >
                    <Input />
                  </Form.Item>
                  <Form.Item
                    label="Password"
                    name="password"
                    rules={[
                      {
                        required: true,
                        message: "Required Password",
                      },
                      {
                        pattern: passwordPattern,
                        message:
                          "Required Password does not meet the requirements",
                      },
                    ]}
                  >
                    <Password />
                  </Form.Item>
                </Col>
                <Col xs={24} sm={24} md={12} lg={12} xl={12}>
                  <Form.Item
                    label="Name"
                    name="name"
                    rules={[{ required: true, message: "Required field" }]}
                  >
                    <Input />
                  </Form.Item>
                  <Form.Item
                    label="LastName"
                    name="lastname"
                    rules={[{ required: true, message: "Required field" }]}
                  >
                    <Input />
                  </Form.Item>
                  <Form.Item
                    label="Phone"
                    name="phoneNumber"
                    rules={[
                      { required: true, message: "Required field" },
                      {
                        pattern: phonePattern,
                        message: "Invalid phone format",
                      },
                    ]}
                  >
                    <Input />
                  </Form.Item>
                </Col>
                <Col xs={24} sm={24} md={12} lg={24} xl={24}>
                  <Form.Item
                    label="Role"
                    name="role"
                    rules={[{ required: true, message: "Required field" }]}
                  >
                    <Select options={formattedRole} loading={isLoading} />
                  </Form.Item>
                </Col>
              </Row>
              <Row justify={"end"}>
                <Button
                  size={"large"}
                  type="primary"
                  htmlType="submit"
                  icon={<LogInIcon />}
                  loading={loading}
                >
                  Register
                </Button>
              </Row>
            </Form>
          </Col>
        </Row>
      </Back>
    </Container>
  );
}

export default Register;
