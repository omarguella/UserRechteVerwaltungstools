import { Alert, Drawer, Form, Space } from "antd";
import { Plus } from "lucide-react";
import { useState } from "react";
import { FetchAvailableRole } from "../api/fetchRoles";
import Loading from "../components/common/Loading";
import { ListRoles } from "../components/features/roles/ListRoles";
import TableRole from "../components/features/roles/TableRole";
import RoleForm from "../components/forms/role/RoleForm";
import { Button } from "../components/forms/style.d";
import { useAppSelector } from "../hooks/redux";
import { FindCookies } from "../lib/cookies";
import { RolesSlice } from "../redux/reducers/roles";
import { useAppDispatch } from "../redux/store";
import { Helmet } from "react-helmet";
import { Role } from "../types/roles";
import { CreateRoleAction } from "../redux/actions/roles";
import UseNotification from "../hooks/notification";

const RolesAdministration = () => {
  const cookiesPermission: [] = FindCookies("current-role");
  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();
  const [form] = Form.useForm();
  const [selectedRole, setSelectedRole] = useState<string>();
  const dispatch = useAppDispatch();

  const { modal: modalCreate } = useAppSelector(state => state.roles.create);
  const { data, isLoading, error, refetch } = FetchAvailableRole();

  if (isLoading) {
    return <Loading />;
  }

  if (error) {
    return <div>{JSON.stringify(error)}</div>;
  }

  const ClearFormOpenModal = () => {
    form.resetFields();
    dispatch(RolesSlice.actions.OpenCreateModal());
  };

  const ClearFormCloseModal = () => {
    form.resetFields();
    dispatch(RolesSlice.actions.CloseCreateModal());
  };

  const CreateRole = (data: Omit<Role, "id">) => {
    dispatch(CreateRoleAction({ data }))
      .unwrap()
      .then(() => {
        refetch();
        openSuccessNotification("Role added successfully");
      })
      .catch(err => openErrorNotification(err));
  };

  return (
    <div style={{ width: "100%", padding: "5px" }}>
      <Helmet>
        <title>Roles administration</title>
      </Helmet>
      {contextHolder}
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
        onClick={ClearFormOpenModal}
        icon={<Plus />}
        style={{ marginBottom: "10px" }}
        disabled={!cookiesPermission.some(p => p === "ROLE_MANAGER_POST_ALL")}
      >
        Create Role
      </Button>
      <TableRole roles={data!} selectedRole={selectedRole!} refetch={refetch} />

      <Drawer
        title="Create Role"
        open={modalCreate}
        onClose={ClearFormCloseModal}
        placement="right"
      >
        <RoleForm form={form} action={CreateRole} type="create" />
      </Drawer>
    </div>
  );
};

export default RolesAdministration;
