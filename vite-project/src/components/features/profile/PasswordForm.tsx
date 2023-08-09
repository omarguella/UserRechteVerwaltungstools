import { Form, Space } from "antd";
import Title from "antd/es/typography/Title";
import { SaveIcon } from "lucide-react";
import { Button, Password } from "../../forms/style.d";

const PasswordForm = () => {
  const [PasswordInstance] = Form.useForm();
  const passwordPattern =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[.@$!%*?&])[A-Za-z\d.@$!%*?&]{8,}$/;
  return (
    <Form
      name="password-form"
      layout="vertical"
      form={PasswordInstance}
      onFinish={values => {
        console.log(values);
      }}
    >
      <Space direction="vertical" size="middle" style={{ display: "flex" }}>
        <Form.Item
          label="Old Password"
          name={"old"}
          rules={[
            {
              required: true,
              message: "Required Password",
            },
          ]}
        >
          <Password />
        </Form.Item>
        <Form.Item
          label="New Password"
          name={"new"}
          rules={[
            {
              required: true,
              message: "Required Password",
            },
            {
              pattern: passwordPattern,
              message: "Required Password does not meet the requirements",
            },
          ]}
        >
          <Password />
        </Form.Item>
        <Button htmlType="submit" type="primary" icon={<SaveIcon />}>
          Change Password
        </Button>
      </Space>
    </Form>
  );
};

export default PasswordForm;
