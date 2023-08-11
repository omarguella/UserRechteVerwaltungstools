import { Alert, Drawer, Space } from "antd";
import { ListRoles } from "../components/features/roles/ListRoles";
import { FetchAvailableRole } from "../api/fetchRoles";
import Loading from "../components/common/Loading";
import TableRole from "../components/features/roles/TableRole";
import { useState } from "react";
import UseToggle from "../hooks/toggle";
import { Button } from "../components/forms/style.d";
import { Plus } from "lucide-react";
import { FindCookies } from "../lib/cookies";

const RolesAdministration = () => {
  const [open, onClose, showDrawer] = UseToggle();
  const { data, isLoading, error, refetch } = FetchAvailableRole();
  const cookiesPermission: [] = FindCookies("current-role");

  const [selectedRole, setSelectedRole] = useState<string>();
  if (isLoading) {
    return <Loading />;
  }

  if (error) {
    return <div>{JSON.stringify(error)}</div>;
  }

  return (
    <div style={{ width: "100%", padding: "5px" }}>
      <Space direction="vertical" size="middle" style={{ display: "flex" }}>
        <Alert
          message="Roles Administration"
          type="success"
          style={{ fontWeight: "bold" }}
        />
      </Space>
      <ListRoles
        selectedRole={selectedRole!}
        setSelectedRole={setSelectedRole}
      />

      <Button
        onClick={showDrawer}
        icon={<Plus />}
        style={{ marginBottom: "10px" }}
        disabled={!cookiesPermission.some(p => p === "ROLE_MANAGER_POST_ALL")}
      >
        Create Role
      </Button>
      <TableRole roles={data!} selectedRole={selectedRole!} />
      <Drawer
        title="Create Role"
        open={open}
        onClose={onClose}
        placement="right"
      >
        <p>hello</p>
      </Drawer>
    </div>
  );
};

export default RolesAdministration;
