import { Card, Popconfirm, Table } from "antd";
import { Trash } from "lucide-react";
import { FC } from "react";
import UseNotification from "../../../hooks/notification";
import {
  DeletePermissionAction,
  UpdatePermissionAction,
} from "../../../redux/actions/permissions";
import { useAppDispatch } from "../../../redux/store";
import { Permissions } from "../../../types/permissions";
import { Button } from "../../forms/style.d";

interface ListPermissionsProps {
  permissions: Permissions[];
  refetch: () => void;
}

export type PayloadPermission = Partial<
  Pick<Permissions, "permissionKey" | "roleName" | "type">
>;

const ListPermissions: FC<ListPermissionsProps> = ({
  permissions,
  refetch,
}) => {
  const dispatch = useAppDispatch();

  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();

  const UpdateType = (data: PayloadPermission) => {
    dispatch(UpdatePermissionAction({ data }))
      .unwrap()
      .then(() => {
        refetch();
        openSuccessNotification("Permission Successfully Updated");
      })
      .catch(err => openErrorNotification(err));
  };

  const DeletePermission = (data: Omit<PayloadPermission, "type">) => {
    dispatch(DeletePermissionAction({ data }))
      .unwrap()
      .then(() => {
        refetch();
        openSuccessNotification("Permission Successfully Delete");
      })
      .catch(err => openErrorNotification(err));
  };

  const columns = [
    {
      title: "Permission Key",
      dataIndex: "permissionKey",
      key: "permissionKey",
    },
    {
      title: "Role Name",
      dataIndex: "roleName",
      key: "roleName",
    },
    {
      title: "Type",
      dataIndex: "type",
      key: "type",
      render: (_: any, record: Permissions) => {
        const data = {
          permissionKey: record.permissionKey,
          roleName: record.roleName,
          type: record.type === "ALL" ? "ME" : "ALL",
        };
        return <Button onClick={() => UpdateType(data)}>{record.type}</Button>;
      },
    },
    {
      title: "Delete",
      key: "delete",
      render: (_: any, record: Permissions) => {
        const data = {
          permissionKey: record.permissionKey,
          roleName: record.roleName,
        };
        return (
          <Popconfirm
            title="Delete the permission"
            description="Do you want to delete this permission? this action is irreversible"
            onConfirm={() => DeletePermission(data)}
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
            />
          </Popconfirm>
        );
      },
    },
  ];

  const datasource: Permissions[] = permissions ?? [];
  return (
    <div style={{ marginTop: "20px" }}>
      {contextHolder}
      <Card title="Lists of permissions">
        <Table dataSource={datasource} columns={columns} />
      </Card>
    </div>
  );
};

export default ListPermissions;
