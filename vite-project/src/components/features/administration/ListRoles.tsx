import { useQuery } from "@tanstack/react-query";
import { Alert, Col, Form } from "antd";
import { _GET } from "../../../api/config";
import { Select } from "../../forms/style.d";
import { ReformatRole } from "../../../utils/roles";

interface Props {
  mode?: "multiple" | "tags";
}

const ListRoles = ({ mode }: Props) => {
  const {
    data: roles,
    isLoading,
    isError,
  } = useQuery({
    queryKey: ["roles"],
    queryFn: async () => {
      const response = await _GET("/roles/edit/");
      const roles = response.data;
      return roles;
    },
  });

  if (isLoading) {
    return <div>loading ......</div>;
  }

  if (isError) {
    return (
      <Alert
        message="Error Ocurred"
        description="Something wrong try again"
        type="error"
      />
    );
  }
  const formattedRole = ReformatRole(roles ?? []);
  return (
    <Form.Item
      name="roles"
      label={"Roles"}
      rules={[{ required: true, message: "Required field" }]}
    >
      <Select
        options={formattedRole}
        loading={isLoading}
        placeholder="Role choice"
        mode={mode}
        allowClear
      />
    </Form.Item>
  );
};

export default ListRoles;
