import { FC } from "react";
import { User } from "../../../types/user";
import { Table } from "antd";
import { Role } from "../../../types/roles";

type RoleListProps = Pick<User, "roles">;

const RoleList: FC<RoleListProps> = ({ roles }) => {
  const dataSource: Role[] = roles;

  const columns = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
    },
  ];
  return <Table dataSource={dataSource} columns={columns} />;
};

export default RoleList;
