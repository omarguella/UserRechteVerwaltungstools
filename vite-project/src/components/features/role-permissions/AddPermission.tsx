import { Alert, Card, Form, Space, Table } from "antd";
import { FC, useState } from "react";
import { Permissions } from "../../../types/permissions";
import { useQuery } from "@tanstack/react-query";
import { _GET } from "../../../api/config";
import Loading from "../../common/Loading";
import { Button, Input, Select } from "../../forms/style.d";
import { Check, PlusCircle } from "lucide-react";
import { useAppDispatch } from "../../../redux/store";
import { CreatePermissionAction } from "../../../redux/actions/permissions";
import UseNotification from "../../../hooks/notification";

interface AddPermissionProps {
  refetch: () => void;
}

const AddPermission: FC<AddPermissionProps> = ({ refetch }) => {
  const [form] = Form.useForm();
  const dispatch = useAppDispatch();
  const [disabled, setDisabled] = useState<boolean>(true);
  const [disabledSelect, setDisabledSelect] = useState<boolean>(true);
  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();
  const { data, isError, isLoading } = useQuery(["permissions"], async () => {
    const response = await _GET(`/auto/allPermissions`);
    return response.data;
  });

  if (isLoading) {
    return <Loading />;
  }

  const OnCheck = (data: Omit<Permissions, "id">) => {
    setDisabled(false);
    if (data.type == "ALL") {
      setDisabledSelect(false);
    }
    form.setFieldsValue(data);
  };

  const CreatePermission = (data: Omit<Permissions, "id">) => {
    dispatch(CreatePermissionAction({ data }))
      .unwrap()
      .then(() => {
        refetch();
        openSuccessNotification("Permission Added with success");
      })
      .catch(err => openErrorNotification(err));
  };

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
      title: "Role name",
      dataIndex: "roleName",
      key: "roleName",
    },
    {
      title: "Type",
      dataIndex: "type",
      key: "type",
    },
    {
      title: "Check To Add",
      key: "check",
      render: (_: any, record: Permissions) => {
        const data: Omit<Permissions, "id"> = {
          permissionKey: record.permissionKey,
          roleName: record.roleName,
          type: record.type,
        };
        return (
          <Button
            icon={<Check />}
            onClick={() => {
              setDisabled(true);
              setDisabledSelect(true);
              OnCheck(data);
            }}
          />
        );
      },
    },
  ];
  const datasource: Permissions[] = data ?? [];
  return (
    <div style={{ marginTop: "20px" }}>
      {contextHolder}
      <Form
        name="permission"
        form={form}
        layout="vertical"
        onFinish={values => CreatePermission(values)}
      >
        <div
          style={{
            width: "100%",
            display: "flex",
            alignItems: "center",
            gap: "5px",
          }}
        >
          <Form.Item
            name={"permissionKey"}
            label="Permission key"
            style={{ width: "50%" }}
          >
            <Input disabled={true} placeholder="permissionKey (not editable)" />
          </Form.Item>
          <Form.Item
            name={"roleName"}
            label="Role Name"
            style={{ width: "50%" }}
          >
            <Input disabled={true} placeholder="roleName (not editable)" />
          </Form.Item>

          <Form.Item
            name={"type"}
            label="Select the type"
            style={{ width: "50%" }}
            rules={[
              {
                required: form.getFieldValue("type") === "ALL",
                message: "Required field",
              },
            ]}
          >
            <Select
              disabled={disabledSelect}
              options={[
                {
                  lable: "ALL",
                  value: "ALL",
                },
                {
                  lable: "ONE",
                  value: "ONE",
                },
              ]}
            />
          </Form.Item>

          <Button
            htmlType="submit"
            type="primary"
            style={{ marginBottom: "10px" }}
            icon={<PlusCircle />}
            disabled={disabled}
          >
            Add Permission
          </Button>
        </div>
      </Form>
      <Card title="Add Permission">
        <Table dataSource={datasource} columns={columns} />
      </Card>
    </div>
  );
};

export default AddPermission;
