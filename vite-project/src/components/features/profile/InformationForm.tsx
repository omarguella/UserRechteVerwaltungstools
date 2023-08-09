import { Form, Space } from "antd";
import Title from "antd/es/typography/Title";
import { FC } from "react";
import Input from "../../forms/Input";
import { Button } from "../../forms/style.d";
import { User } from "../../../types/user";
import { SaveIcon } from "lucide-react";

type InformationFormProps = Pick<
  User,
  "username" | "name" | "lastname" | "phoneNumber"
>;

const InformationForm: FC<InformationFormProps> = ({
  username,
  name,
  lastname,
  phoneNumber,
}) => {
  const [InfoForm] = Form.useForm();
  return (
    <Form
      name="info-form"
      layout="vertical"
      form={InfoForm}
      onFinish={values => {
        console.log(values);
      }}
    >
      <Space direction="vertical" size="middle" style={{ display: "flex" }}>
        <Form.Item
          label="Username"
          name={"username"}
          initialValue={username}
          rules={[{ required: true, message: "Required field" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Name"
          name={"name"}
          initialValue={name}
          rules={[{ required: true, message: "Required field" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="LastName"
          name={"lastname"}
          initialValue={lastname}
          rules={[{ required: true, message: "Required field" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Phone Number"
          name={"phoneNumber"}
          initialValue={phoneNumber}
          rules={[{ required: true, message: "Required field" }]}
        >
          <Input />
        </Form.Item>
        <Button htmlType="submit" type="primary" icon={<SaveIcon />}>
          Change Information
        </Button>
      </Space>
    </Form>
  );
};

export default InformationForm;
