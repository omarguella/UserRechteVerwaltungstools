import { Col, Form, Row } from "antd";
import { Select } from "../../forms/style.d";
import { useQuery } from "@tanstack/react-query";
import { _GET } from "../../../api/config";
import { Role } from "../../../types/roles";
import { ReformatRole } from "../../../utils/roles";

const SearchRoles = () => {
  const { data, isLoading, isError } = useQuery(
    ["available-roles"],
    async () => {
      const response = await _GET("/roles");
      return response.data as Role[];
    }
  );

  if (isError) {
    return <h1>Failed to load roles</h1>;
  }

  const formattedRole = ReformatRole(data ?? []);

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

export default SearchRoles;
