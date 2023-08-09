import { useQuery } from "@tanstack/react-query";
import { Alert, Col, Form } from "antd";
import { _GET } from "../../../api/config";
import { Select } from "../../forms/style.d";
import { ReformatRole } from "../../../utils/roles";

const ListAvailableRole = () => {
  const {
    data: roles,
    isLoading,
    isError,
  } = useQuery({
    queryKey: ["private-roles"],
    queryFn: async () => {
      const response = await _GET("/roles/privatRoles/");
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
    <Col xs={24} sm={24} md={12} lg={8} xl={8}>
      <Form.Item name="role">
        <Select
          options={formattedRole}
          loading={isLoading}
          placeholder="Role choice"
        />
      </Form.Item>
    </Col>
  );
};

export default ListAvailableRole;
