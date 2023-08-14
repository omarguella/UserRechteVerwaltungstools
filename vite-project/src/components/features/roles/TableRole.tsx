import { Form, Popconfirm, Space, Table, Tag } from "antd";
import { FC, useMemo, useState } from "react";
import { Role } from "../../../types/roles";
import { Button, Select } from "../../forms/style.d";
import { Edit, Trash } from "lucide-react";
import { FindCookies } from "../../../lib/cookies";
import Drawer from "../../common/Drawer";
import { useAppDispatch, useAppSelector } from "../../../hooks/redux";
import { RolesSlice } from "../../../redux/reducers/roles";
import RoleForm from "../../forms/role/RoleForm";
import {
  DeleteRoleAction,
  FindRoleByNameAction,
  UpdateRoleByNameAction,
} from "../../../redux/actions/roles";
import UseNotification from "../../../hooks/notification";
import { useNavigate } from "react-router-dom";

interface TableRoleProps {
  roles: Role[];
  selectedRole: string;
  refetch: () => void;
}

const TableRole: FC<TableRoleProps> = ({ roles, selectedRole, refetch }) => {
  const cookiesPermission: [] = FindCookies("current-role");
  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();
  const dispatch = useAppDispatch();
  const { modal: modalUpdate, loading: loadingUpdate } = useAppSelector(
    state => state.roles.update
  );

  const navigate = useNavigate();
  const [InfoForm] = Form.useForm();
  const [RoleFormInstance] = Form.useForm();
  const FetchRole = (name: string) => {
    InfoForm.resetFields();
    dispatch(FindRoleByNameAction({ name }))
      .unwrap()
      .then(res => {
        InfoForm.setFieldsValue(res);
      })
      .catch(err => openErrorNotification(err));
  };

  const UpdateRole = (data: Role) => {
    dispatch(UpdateRoleByNameAction({ data }))
      .unwrap()
      .then(() => {
        refetch();
        openSuccessNotification(`Role Updated Successfully`);
      })
      .catch(err => openErrorNotification(err));
  };

  const DeleteRole = (name: string) => {
    if (RoleFormInstance.getFieldValue("role") === undefined) {
      openErrorNotification("Required New Role");
    } else {
      const role = RoleFormInstance.getFieldValue("role");

      dispatch(DeleteRoleAction({ name, role }))
        .unwrap()
        .then(() => {
          refetch();
          openSuccessNotification(`Role Deleted Successfully`);
        })
        .catch(err => openErrorNotification(err));
    }
  };

  const ClearFormCloseModal = () => {
    InfoForm.resetFields();
    dispatch(RolesSlice.actions.CloseUpdateModal());
  };
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
              onClick={() => navigate(`/roles/${record.name}`)}
              disabled={
                !cookiesPermission.some(p => p === "ROLE_MANAGER_PUT_ALL")
              }
            >
              Update Permission
            </Button>

            <Button
              type="primary"
              icon={<Edit />}
              style={{ display: "flex", alignItems: "center", gap: "2" }}
              default
              loading={loadingUpdate}
              onClick={() => FetchRole(record.name)}
              disabled={
                !cookiesPermission.some(p => p === "ROLE_MANAGER_PUT_ALL")
              }
            >
              Update Role
            </Button>

            <Popconfirm
              title="Delete the Role"
              description={
                <div>
                  <p>
                    Do you want to delete this role? this action is
                    irreversible?
                  </p>
                  <p>Please Check the new role if you want to continue</p>
                  <Form name="role" form={RoleFormInstance}>
                    <Form.Item
                      name={"role"}
                      rules={[{ required: true, message: "Required Field" }]}
                    >
                      <Select
                        placeholder="New Role"
                        options={roles
                          .filter(role => role.name != record.name)
                          .map(role => {
                            return {
                              label: role.name,
                              value: role.name,
                            };
                          })}
                      />
                    </Form.Item>
                  </Form>
                </div>
              }
              onConfirm={() => DeleteRole(record.name)}
              okText="Yes"
              cancelText="No"
              disabled={
                !cookiesPermission.some(p => p === "ROLE_MANAGER_DELETE_ALL")
              }
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
      {contextHolder}
      <Table
        dataSource={filteredRole()}
        columns={columns}
        style={{ width: "100%" }}
      />
      <Drawer
        title="Update Role"
        open={modalUpdate}
        onClose={ClearFormCloseModal}
        placement="right"
      >
        <RoleForm form={InfoForm} action={UpdateRole} type="update" />
      </Drawer>
    </>
  );
};

export default TableRole;
