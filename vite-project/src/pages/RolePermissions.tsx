import { useQuery } from "@tanstack/react-query";
import { Alert, Space } from "antd";
import { Helmet } from "react-helmet";
import { useParams } from "react-router-dom";
import { _GET } from "../api/config";
import Loading from "../components/common/Loading";
import AddPermission from "../components/features/role-permissions/AddPermission";
import ListPermissions from "../components/features/role-permissions/ListPermissions";

const RolePermissions = () => {
  const { name } = useParams();

  const { data: roleName, isError, isLoading, refetch } = useQuery(
    ["getPermissions"],
    async () => {
      const response = await _GET(`/permissionRole/all/${name}`);
      return response.data;
    }
  );

  if (isLoading) {
    return <Loading />;
  }

  if (isError) {
    return (
      <div style={{ width: "100%", padding: "5px" }}>
        <Space direction="vertical" size="middle" style={{ display: "flex" }}>
          <Alert
            message="No Permissions Found for this role"
            type="error"
            style={{ fontWeight: "bold" }}
          />
        </Space>
      </div>
    );
  }

  return (
    <div style={{ width: "100%", padding: "5px" }}>
      <Helmet>
        <title>Roles permissions</title>
      </Helmet>

      <Space direction="vertical" size="middle" style={{ display: "flex" }}>
        <Alert
          message={`Edit permissions of the role${name}`}
          type="success"
          style={{ fontWeight: "bold" }}
        />
      </Space>
      <ListPermissions permissions={roleName} refetch={refetch} />
      <AddPermission refetch={refetch} />
    </div>
  );
};

export default RolePermissions;
