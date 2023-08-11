import { Alert, Card, Divider, Row, Space } from "antd";
import { useMemo, useState } from "react";
import ListUsers from "../components/features/administration/ListUsers";
import SearchByEmail from "../components/features/administration/SearchByEmail";
import SearchByUsername from "../components/features/administration/SearchByUsername";
import SearchRoles from "../components/features/administration/SearchRoles";
import { User } from "../types/user";
import { useQuery } from "@tanstack/react-query";
import { useAppDispatch } from "../redux/store";
import { GetUsersAction } from "../redux/actions/user";

const Administration = () => {
  const [email, setEmail] = useState<string>("");
  const [username, setUsername] = useState<string>("");
  const [role, setRole] = useState<string>("");
  const dispatch = useAppDispatch();

  const { data, isLoading, isError, refetch } = useQuery(
    ["users"],
    async () => {
      const response = await dispatch(GetUsersAction());
      return response.payload as User[];
    }
  );

  const filteredData = useMemo(() => {
    if (!username && !email && !role) {
      return data;
    }

    return data?.filter(
      item =>
        item.username.toLowerCase().includes(username.toLowerCase()) &&
        item.email.toLowerCase().includes(email.toLowerCase()) &&
        item.roles.some(r => r.name.toLowerCase() === role.toLowerCase())
    );
  }, [data, email, username, role]);

  if (isLoading) {
    return <p>Loading .....</p>;
  }

  if (isError) {
    return <p>Error occured try again</p>;
  }
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
        <SearchRoles setRole={setRole} role={role} />
        <SearchByUsername setUsername={setUsername} username={username} />
        <SearchByEmail setEmail={setEmail} email={email} />
      </Row>
      <Row gutter={[16, 16]}>
        <Card title="List of users" style={{ width: "100%" }}>
          <ListUsers users={filteredData!} refetch={refetch} />
        </Card>
      </Row>

      <Divider />
    </div>
  );
};

export default Administration;
