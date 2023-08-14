import { Form, FormInstance, Space } from "antd";
import { SaveIcon } from "lucide-react";
import { FC } from "react";
import ListPrivateRoles from "../../features/administration/ListPrivateRoles";
import Input from "../Input";
import { Button } from "../style.d";
import ListRoles from "../../features/administration/ListRoles";
interface UserFormProps {
  action: any;
  InfoForm: FormInstance;
  mode?: "multiple" | "tags";
  role: "mixte" | "private";
  id?: number;
  form: "create" | "update";
}

const passwordPattern =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[.@$!%*?&])[A-Za-z\d.@$!%*?&]{8,}$/;
const phonePattern = /^(\\+[0-9]{1,3})?[0-9]{9,15}$/;

const UserForm: FC<UserFormProps> = ({
  action,
  InfoForm,
  mode,
  role,
  id,
  form,
}) => {
  return (
    <Form
      name="info-form"
      layout="vertical"
      form={InfoForm}
      onFinish={values => {
        console.log(InfoForm.getFieldsValue());
        if (form === "update") {
          action(values, values.id);
        } else {
          action(values);
        }
      }}
    >
      <Space direction="vertical" size="middle" style={{ display: "flex" }}>
        {form === "update" && (
          <Form.Item
            label="Id"
            name={"id"}
            rules={[{ required: true, message: "Required field" }]}
          >
            <Input disabled={true} />
          </Form.Item>
        )}
        <Form.Item
          label="Username"
          name={"username"}
          rules={[{ required: true, message: "Required field" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Email"
          name={"email"}
          rules={[
            { required: true, message: "Required field" },
            { type: "email", message: "Format email not valid" },
          ]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Password"
          name={"password"}
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
          <Input />
        </Form.Item>
        <Form.Item
          label="LastName"
          name={"lastname"}
          rules={[{ required: true, message: "Required field" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Name"
          name={"name"}
          rules={[{ required: true, message: "Required field" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Phone Number"
          name={"phoneNumber"}
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

        {role === "mixte" ? (
          <ListRoles mode={mode} />
        ) : (
          <ListPrivateRoles mode={mode} />
        )}

        <Button htmlType="submit" type="primary" icon={<SaveIcon />}>
          Save Information
        </Button>
      </Space>
    </Form>
  );
};

export default UserForm;
