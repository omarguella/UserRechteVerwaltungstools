import { Checkbox, Form, Radio } from "antd";
import { FC } from "react";
import { Button, Input } from "../style.d";
import { Save } from "lucide-react";
import { StandartSelector } from "../../../types/selectors";
import { useAppSelector } from "../../../hooks/redux";
import { useAppDispatch } from "../../../redux/store";
import { CreatePermissionByMethodAction } from "../../../redux/actions/permissions";
import UseNotification from "../../../hooks/notification";

interface PermissionsFormProps {
  refetch: () => void;
}

const PermissionsForm: FC<PermissionsFormProps> = ({ refetch }) => {
  const [form] = Form.useForm();
  const { loading } = useAppSelector(state => state.permissions.create);
  const dispatch = useAppDispatch();
  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();
  const METHODS: StandartSelector[] = [
    {
      label: "POST",
      value: "POST",
    },
    {
      label: "GET",
      value: "GET",
    },
    {
      label: "PUT",
      value: "PUT",
    },
    {
      label: "DELETE",
      value: "DELETE",
    },
  ];
  return (
    <div>
      {contextHolder}
      <Form
        name="permission"
        form={form}
        onFinish={values => {
          dispatch(CreatePermissionByMethodAction({ data: values }))
            .unwrap()
            .then(() => {
              refetch();
              openSuccessNotification("Permission Added Successfully");
            })
            .catch(err => openErrorNotification(err));
        }}
      >
        <Form.Item
          name={"name"}
          rules={[{ required: true, message: "Required Message" }]}
        >
          <Input placeholder="Name of Permission" />
        </Form.Item>
        <Form.Item
          name={"listOfAction"}
          rules={[{ required: true, message: "Required field" }]}
        >
          <Checkbox.Group options={METHODS} />
        </Form.Item>
        <Button
          icon={<Save />}
          type="primary"
          htmlType="submit"
          loading={loading}
        >
          Save
        </Button>
      </Form>
    </div>
  );
};

export default PermissionsForm;
