import { Table } from "antd";
import { FC } from "react";
import { Role } from "../../../types/roles";
import { User } from "../../../types/user";

type RoleListProps = Pick<User, "roles">;

const RoleList: FC<RoleListProps> = ({ roles }) => {
  const dataSource: any[] = roles;
  /* const _formatted = ReformatRole(roles);
  CreateCookies("current-role", _formatted); */
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
