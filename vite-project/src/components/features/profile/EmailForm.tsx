import { Form, Space } from "antd";
import { FC } from "react";
import Input from "../../forms/Input";
import Title from "antd/es/typography/Title";
import { Button } from "../../forms/style.d";
import { SaveIcon } from "lucide-react";

interface EmailFormProps {
  email: string;
}

const EmailForm: FC<EmailFormProps> = ({ email }) => {
  const [EmailInstance] = Form.useForm();
  return (
    <Form
      name="email-form"
      layout="vertical"
      form={EmailInstance}
      onFinish={values => {
        console.log(values);
      }}
    >
      <Space direction="vertical" size="middle" style={{ display: "flex" }}>
        <Form.Item
          label="Your Email"
          name={"email"}
          rules={[
            { required: true, message: "Required field" },
            {
              type: "email",
              message: "The input is not valid E-mail!",
            },
          ]}
          initialValue={email}
        >
          <Input />
        </Form.Item>
        <Button htmlType="submit" type="primary" icon={<SaveIcon />}>
          Change Email
        </Button>
      </Space>
    </Form>
  );
};

export default EmailForm;
