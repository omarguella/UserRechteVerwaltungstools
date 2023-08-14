import { Alert, Card, Popconfirm, Space, Table } from "antd";

import { useQuery } from "@tanstack/react-query";
import { Helmet } from "react-helmet";
import { _GET } from "../api/config";
import Loading from "../components/common/Loading";
import { Button } from "../components/forms/style.d";
import { Plus, Trash } from "lucide-react";
import Search from "antd/es/input/Search";
import { useMemo, useState } from "react";
import { Permissions } from "../types/permissions";
import { useAppDispatch } from "../redux/store";
import { DeletePermissionByKeyAction } from "../redux/actions/permissions";
import { PermissionsSlice } from "../redux/reducers/permissions";
import Drawer from "../components/common/Drawer";
import { useAppSelector } from "../hooks/redux";
import PermissionsForm from "../components/forms/permissions/PermissionsForm";
import { FindCookies } from "../lib/cookies";
import UseNotification from "../hooks/notification";

const PermissionsAdministration = () => {
  const [text, setText] = useState("");
  const dispatch = useAppDispatch();
  const { modal, loading } = useAppSelector(state => state.permissions.create);
  const cookiesPermission: [] = FindCookies("current-role");
    const {  openErrorNotification, openSuccessNotification } =
        UseNotification();

  const { data, isError, isLoading, refetch } = useQuery(
    ["permissions"],
    async () => {
      const response = await _GET(`/permissions`);
      return response.data as Permissions[];
    }
  );
  const filtredData = useMemo(() => {
    if (!text) {
      return data;
    }
    return data?.filter(role => {
      const isMatchText =
        !text || role.name.toLowerCase().includes(text.toLowerCase());
      return isMatchText;
    });
  }, [data, text]);

  const DeletePermissionByKey = (key: string) => {
    dispatch(DeletePermissionByKeyAction({ key }))
        .unwrap()
        .then(() => {
            refetch();
            openSuccessNotification("Permission Deleted Successfully");
        })
        .catch(err => openErrorNotification(err));

  };
  if (isLoading) {
    return <Loading />;
  }

  if (isError) {
    return (
      <div style={{ width: "100%", padding: "5px" }}>
        <Space direction="vertical" size="middle" style={{ display: "flex" }}>
          <Alert
            message="No Permissions Found "
            type="error"
            style={{ fontWeight: "bold" }}
          />
        </Space>
      </div>
    );
  }
  const columns = [
    {
      title: "Permission Keys",
      dataIndex: "key",
      key: "key",
    },
      {
          title: "Permission name",
          dataIndex: "name",
          key: "name",
      },
    {
      title: "Delete",
      key: "delete",
      render: (_: any, record: Permissions) => {
        return (
          <Popconfirm
            title="Delete the Permission"
            description="Do you want to delete this permission? this action is irreversible"
            onConfirm={() => DeletePermissionByKey(record.key)}
            okText="Yes"
            cancelText="No"
            disabled={
              !cookiesPermission.some(p => p === "PERMISSION_MANAGER_DELETE_ALL")
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
                !cookiesPermission.some(p => p === "PERMISSION_MANAGER_DELETE_ALL")
              }
            />
          </Popconfirm>
        );
      },
    },
  ];
  const datasource: Permissions[] = filtredData ?? [];
  return (
    <div style={{ width: "100%", padding: "5px" }}>
      <Helmet>
        <title>Permissions administration</title>
      </Helmet>

      <Space direction="vertical" size="middle" style={{ display: "flex" }}>
        <Alert
          message={`List of permissions`}
          type="success"
          style={{ fontWeight: "bold" }}
        />
      </Space>
      <div style={{ marginTop: "20px" }}>
        <Search
          placeholder="Search permission by name"
          onChange={e => setText(e.target.value)}
        />
      </div>

      <Button
        type="default"
        icon={<Plus />}
        style={{ marginTop: "30px" }}
        onClick={() => dispatch(PermissionsSlice.actions.OpenCreateModal())}
        disabled={!cookiesPermission.some(p => p === "PERMISSION_MANAGER_POST_ALL")}
      >
        Add Permission
      </Button>

      <Card style={{ marginTop: "20px" }}>
        <Table dataSource={datasource} columns={columns} />
      </Card>

      <Drawer
        title="Create Permission"
        open={modal}
        onClose={() => dispatch(PermissionsSlice.actions.CloseCreateModal())}
        placement="right"
      >
        <PermissionsForm refetch={refetch} />
      </Drawer>
    </div>
  );
};

export default PermissionsAdministration;
