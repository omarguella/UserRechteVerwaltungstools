import { Form, FormInstance } from "antd";
import { FC } from "react";
import { Button, Input, Select } from "../style.d";
import { Save } from "lucide-react";
import { PrivateRole } from "../../../constants/roles";
import { Role } from "../../../types/roles";

interface RoleFormProps {
  form: FormInstance;
  action: (data: Role) => void;
  type: "create" | "update";
}

const RoleForm: FC<RoleFormProps> = ({ form, action, type }) => {
  return (
    <Form
      name="role"
      layout="vertical"
      form={form}
      onFinish={values => {
        action(values);
      }}
    >
      {type === "update" && (
        <Form.Item
          label="Id"
          name={"id"}
          rules={[{ required: true, message: "Required field" }]}
        >
          <Input placeholder="Role name" disabled={true} />
        </Form.Item>
      )}
      <Form.Item
        label="Name"
        name={"name"}
        rules={[{ required: true, message: "Required field" }]}
      >
        <Input placeholder="Role name" />
      </Form.Item>
      <Form.Item
        label="Session Timer"
        name={"sessionTimer"}
        rules={[{ required: true, message: "Required field" }]}
      >
        <Input placeholder="Session timer" />
      </Form.Item>
      <Form.Item
        label="Level"
        name={"level"}
        rules={[{ required: true, message: "Required field" }]}
      >
        <Input placeholder="Level" />
      </Form.Item>
      <Form.Item
        label="Is private"
        name={"isPrivate"}
        rules={[{ required: true, message: "Required field" }]}
      >
        <Select placeholder="Is private" options={PrivateRole} />
      </Form.Item>
      <Button icon={<Save />} type="primary" htmlType="submit">
        Save
      </Button>
    </Form>
  );
};

export default RoleForm;
