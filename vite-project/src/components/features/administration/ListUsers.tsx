import { Button, Space, Table } from "antd";
import { Edit, Trash } from "lucide-react";
import { FC } from "react";
import { User } from "../../../types/user";

interface ListUsersProps {
  users: User[];
  setUsers: (value: User[]) => void;
}

const ListUsers: FC<ListUsersProps> = ({ users, setUsers }) => {
  const dataSource: User[] = users;

  const columns = [
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
    },
    {
      title: "Username",
      dataIndex: "username",
      key: "username",
    },
    {
      title: "Actions",
      key: "action",
      render: (_: any, record: User) => {
        return (
          <Space size={"middle"}>
            <Button
              type="primary"
              icon={<Edit />}
              style={{ display: "flex", alignItems: "center", gap: "2" }}
              ghost
            >
              Update User
            </Button>
            <Button
              type="primary"
              icon={<Trash />}
              style={{ display: "flex", alignItems: "center", gap: "2" }}
              danger
            >
              Delete User
            </Button>
          </Space>
        );
      },
    },
  ];
  return (
    <Table
      dataSource={dataSource}
      columns={columns}
      style={{ width: "100%" }}
    />
  );
};

export default ListUsers;
