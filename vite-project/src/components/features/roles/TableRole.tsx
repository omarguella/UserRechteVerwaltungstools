import { Popconfirm, Space, Table, Tag } from "antd";
import { FC, useMemo, useState } from "react";
import { Role } from "../../../types/roles";
import { Button } from "../../forms/style.d";
import { Edit, Trash } from "lucide-react";
import { FindCookies } from "../../../lib/cookies";
import Drawer from "../../common/Drawer";

interface TableRoleProps {
  roles: Role[];
  selectedRole: string;
}

const TableRole: FC<TableRoleProps> = ({ roles, selectedRole }) => {
  const cookiesPermission: [] = FindCookies("current-role");
  const columns = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
    },
    {
      title: "Status",
      dataIndex: "isPrivate",
      key: "isPrivate",
      render: (_: any, record: Role) => {
        return (
          <p>
            {record.isPrivate ? (
              <Tag color="red">Private</Tag>
            ) : (
              <Tag color="green">Public</Tag>
            )}
          </p>
        );
      },
    },
    {
      title: "Actions",
      key: "actions",
      render: (_: any, record: Role) => {
        return (
          <Space size={"middle"}>
            <Button
              type="primary"
              icon={<Edit />}
              style={{ display: "flex", alignItems: "center", gap: "2" }}
              ghost
              loading={false}
              onClick={() => {
                ("");
              }}
              disabled={
                !cookiesPermission.some(p => p === "ROLE_MANAGER_PUT_ALL")
              }
            >
              Update Permission
            </Button>
            <Drawer
              title="Update Role"
              open={false}
              onClose={() => {
                ("");
              }}
              placement="right"
            >
              {/* {loadingUpdate ? (
                <Skeleton />
              ) : (
                <UserForm
                  InfoForm={InfoForm}
                  action={UpdateUser}
                  mode={"multiple"}
                  role="mixte"
                  id={record?.id}
                  form="update"
                />
              )} */}
            </Drawer>

            <Button
              type="primary"
              icon={<Edit />}
              style={{ display: "flex", alignItems: "center", gap: "2" }}
              default
              loading={false}
              onClick={() => {
                ("");
              }}
              disabled={
                !cookiesPermission.some(p => p === "ROLE_MANAGER_PUT_ALL")
              }
            >
              Update Role
            </Button>
            <Drawer
              title="Update Role"
              open={false}
              onClose={() => {
                ("");
              }}
              placement="right"
            >
              {/* {loadingUpdate ? (
                <Skeleton />
              ) : (
                <UserForm
                  InfoForm={InfoForm}
                  action={UpdateUser}
                  mode={"multiple"}
                  role="mixte"
                  id={record?.id}
                  form="update"
                />
              )} */}
            </Drawer>

            <Popconfirm
              title="Delete the User"
              description="Do you want to delete this role? this action is irreversible"
              onConfirm={() => {
                ("");
              }}
              okText="Yes"
              cancelText="No"
            >
              <Button
                type="primary"
                icon={<Trash />}
                style={{
                  display: "flex",
                  alignItems: "center",
                  gap: "2",
                  justifyContent: "center",
                }}
                danger
                disabled={
                  !cookiesPermission.some(p => p === "ROLE_MANAGER_DELETE_ALL")
                }
              />
            </Popconfirm>
          </Space>
        );
      },
    },
  ];

  const filteredRole = useMemo(
    () => () => {
      if (!selectedRole) {
        return roles;
      }
      return roles.filter((role: Role) =>
        selectedRole === "private"
          ? role.isPrivate === true
          : role.isPrivate === false
      );
    },
    [roles, selectedRole]
  );

  return (
    <>
      <Table
        dataSource={filteredRole()}
        columns={columns}
        style={{ width: "100%" }}
      />
    </>
  );
};

export default TableRole;
