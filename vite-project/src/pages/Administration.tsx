import { Alert, Card, Divider, Row, Space } from "antd";
import SearchRoles from "../components/features/administration/SearchRoles";
import SearchByUsername from "../components/features/administration/SearchByUsername";
import SearchByEmail from "../components/features/administration/SearchByEmail";
import ListUsers from "../components/features/administration/ListUsers";
import { useState } from "react";
import { User } from "../types/user";
import ListAvailableRole from "../components/features/administration/ListAvailableRole";

const Administration = () => {
  const [users, setUsers] = useState<User[]>([]);
  return (
    <div style={{ width: "100%" }}>
      <Space direction="vertical" size="middle" style={{ display: "flex" }}>
        <Alert
          message="Users Administration"
          type="success"
          style={{ fontWeight: "bold" }}
        />
      </Space>
      <Divider />
      <Row gutter={[16, 16]}>
        <SearchRoles />
        <SearchByUsername setUsers={setUsers} />
        <SearchByEmail setUsers={setUsers} />
      </Row>
      <Row gutter={[16, 16]}>
        <Card title="List of users" style={{ width: "100%" }}>
          <ListUsers users={users} setUsers={setUsers} />
        </Card>
      </Row>

      <Divider />
      <Row gutter={[16, 16]}>
        <Card title="List of private roles" style={{ width: "100%" }}>
          <ListAvailableRole />
        </Card>
      </Row>
    </div>
  );
};

export default Administration;
