import { Alert, Card, Popconfirm, Space, Table } from "antd";

import { useQuery } from "@tanstack/react-query";
import { Helmet } from "react-helmet";
import { _GET } from "../api/config";
import Loading from "../components/common/Loading";
import { Button } from "../components/forms/style.d";
import { Trash } from "lucide-react";
import Search from "antd/es/input/Search";
import { useMemo, useState } from "react";
import { Permissions } from "../types/permissions";

const PermissionsAdministration = () => {
  const [text, setText] = useState("");
  const { data, isError, isLoading } = useQuery(["permissions"], async () => {
    const response = await _GET(`/auto/allPermissions`);
    return response.data as Permissions[];
  });
  const filtredData = useMemo(() => {
    if (!text) {
      return data;
    }
    console.log(text);
    return data?.filter(role => {
      const isMatchText =
        !text || role.roleName.toLowerCase().includes(text.toLowerCase());
      return isMatchText;
    });
  }, [data, text]);

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
      title: "Permission Key",
      dataIndex: "permissionKey",
      key: "permissionKey",
    },
    {
      title: "Delete",
      key: "delete",
      render: (_: any, record: Permissions) => {
        return (
          <Popconfirm
            title="Delete the Permission"
            description="Do you want to delete this permission? this action is irreversible"
            onConfirm={() => console.log("ok")}
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
      <Card style={{ marginTop: "20px" }}>
        <Table dataSource={datasource} columns={columns} />
      </Card>
    </div>
  );
};

export default PermissionsAdministration;
