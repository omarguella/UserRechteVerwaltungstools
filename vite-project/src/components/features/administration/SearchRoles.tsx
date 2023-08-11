import { Col, Form, Row } from "antd";
import { Select } from "../../forms/style.d";
import { useQuery } from "@tanstack/react-query";
import { _GET } from "../../../api/config";
import { Role } from "../../../types/roles";
import { ReformatRole } from "../../../utils/roles";

interface Props {
  setRole: (value: string) => void;
  role: string;
}

const SearchRoles = ({ setRole, role }: Props) => {
  const onChangeHandler = async (e: any) => {
    setRole(e);
  };

  const { data, isLoading, isError } = useQuery(
    ["available-roles"],
    async () => {
      const response = await _GET("/roles/edit");
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
          onChange={onChangeHandler}
          allowClear
        />
      </Form.Item>
    </Col>
  );
};

export default SearchRoles;
