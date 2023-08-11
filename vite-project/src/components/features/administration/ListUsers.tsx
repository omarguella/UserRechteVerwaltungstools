import { QueryClient } from "@tanstack/react-query";
import { Button, Form, Popconfirm, Skeleton, Space, Table } from "antd";
import { Edit, Plus, Trash } from "lucide-react";
import { FC } from "react";
import UseNotification from "../../../hooks/notification";
import { useAppSelector } from "../../../hooks/redux";
import { FindCookies } from "../../../lib/cookies";
import {
  CreatePrivateUserAction,
  DeleteUserAction,
  GetUserAction,
  UpdateProfileAction,
} from "../../../redux/actions/user";
import { UsersSlice } from "../../../redux/reducers/user";
import { useAppDispatch } from "../../../redux/store";
import { TRegistration } from "../../../types/auth";
import { User } from "../../../types/user";
import { ReformatRole } from "../../../utils/roles";
import Drawer from "../../common/Drawer";
import UserForm from "../../forms/user/UserForm";

interface ListUsersProps {
  users: User[];
  refetch: () => void;
}

const ListUsers: FC<ListUsersProps> = ({ users, refetch }) => {
  const dataSource: User[] = users;
  const { modal: modalUpdate, loading: loadingUpdate } = useAppSelector(
    state => state.users.update
  );
  const { modal: modalCreate } = useAppSelector(state => state.users.create);
  const dispatch = useAppDispatch();
  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();
  const [InfoForm] = Form.useForm();
  const queryClient = new QueryClient();
  const cookiesPermission: [] = FindCookies("current-role");

  /**
   * Fetch User
   * @param id
   */
  const fetchUser = (id: number) => {
    dispatch(GetUserAction(id))
      .unwrap()
      .then((res: any) => {
        InfoForm.setFieldsValue(res);
        InfoForm.setFieldValue("roles", ReformatRole(res.roles));
      })
      .catch(err => openErrorNotification(err));
  };

  /**
   * Create User
   * @param values
   */
  const CreateUser = (values: TRegistration) => {
    dispatch(CreatePrivateUserAction({ data: values }))
      .unwrap()
      .then(() => {
        openSuccessNotification(`Profile successfully updated`);
        refetch();
      })
      .catch(err => openErrorNotification(err));
  };

  const DeleteUser = (id: number) => {
    dispatch(DeleteUserAction({ id: id }))
      .unwrap()
      .then(() => {
        openSuccessNotification(`User successfully deleted`);
        refetch();
      })
      .catch(err => openErrorNotification(err));
  };

  const UpdateUser = (values: any, id: number) => {
    dispatch(UpdateProfileAction({ data: values, id: id }))
      .unwrap()
      .then(() => {
        openSuccessNotification(`User successfully deleted`);
        refetch();
      })
      .catch(err => openErrorNotification(err));
  };

  const ClearFormOpenModal = () => {
    InfoForm.resetFields();
    dispatch(UsersSlice.actions.OpenCreateModal());
  };

  const columns = [
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
    },
    {
      title: "Username",
      dataIndex: "username",
      key: "username",
    },
    {
      title: "Actions",
      key: "action",
      render: (_: any, record: User) => {
        return (
          <Space size={"middle"}>
            <Button
              type="primary"
              icon={<Edit />}
              style={{ display: "flex", alignItems: "center", gap: "2" }}
              ghost
              loading={loadingUpdate}
              onClick={() => fetchUser(record.id)}
              disabled={
                !cookiesPermission.some(p => p === "USER_MANAGER_PUT_ALL")
              }
            >
              Update User
            </Button>
            <Drawer
              title="Update User"
              open={modalUpdate}
              onClose={() => dispatch(UsersSlice.actions.CloseUpdateModal())}
              placement="right"
            >
              {loadingUpdate ? (
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
              )}
            </Drawer>

            <Popconfirm
              title="Delete the User"
              description="Do you want to delete this user? this action is irreversible"
              onConfirm={() => DeleteUser(record?.id)}
              okText="Yes"
              cancelText="No"
            >
              <Button
                type="primary"
                icon={<Trash />}
                style={{ display: "flex", alignItems: "center", gap: "2" }}
                danger
                disabled={
                  !cookiesPermission.some(p => p === "USER_MANAGER_DELETE_ALL")
                }
              >
                Delete User
              </Button>
            </Popconfirm>
          </Space>
        );
      },
    },
  ];

  return (
    <>
      {contextHolder}
      <Button
        type="primary"
        icon={<Plus />}
        style={{
          display: "flex",
          alignItems: "center",
          gap: "2",
          marginBottom: "10px",
        }}
        disabled={!cookiesPermission.some(p => p === "USER_MANAGER_POST_ALL")}
        ghost
        loading={loadingUpdate}
        onClick={() => ClearFormOpenModal()}
      >
        Create Private User
      </Button>
      <Table
        dataSource={dataSource}
        columns={columns}
        style={{ width: "100%" }}
      />

      <Drawer
        title="Create private User"
        open={modalCreate}
        onClose={() => dispatch(UsersSlice.actions.CloseCreateModal())}
        placement="right"
      >
        <UserForm
          InfoForm={InfoForm}
          action={CreateUser}
          role="private"
          form="create"
        />
      </Drawer>
    </>
  );
};

export default ListUsers;
