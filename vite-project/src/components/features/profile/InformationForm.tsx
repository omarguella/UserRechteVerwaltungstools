import { Form, Space } from "antd";
import { SaveIcon } from "lucide-react";
import { FC } from "react";
import UseNotification from "../../../hooks/notification";
import { useAppDispatch } from "../../../hooks/redux";
import { UpdateProfileAction } from "../../../redux/actions/user";
import { User } from "../../../types/user";
import Input from "../../forms/Input";
import { Button } from "../../forms/style.d";

type InformationFormProps = Pick<
  User,
  "username" | "name" | "lastname" | "phoneNumber" | "id"
>;

const InformationForm: FC<InformationFormProps> = ({
  username,
  name,
  lastname,
  phoneNumber,
  id,
}) => {
  const [InfoForm] = Form.useForm();
  const dispatch = useAppDispatch();
  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();

  const UpdateProfile = (values: User) => {
    dispatch(UpdateProfileAction({ data: values, id: id }))
      .unwrap()
      .then(() => openSuccessNotification(`Profile successfully updated`))
      .catch(err => openErrorNotification(err));
  };

  const phonePattern = /^(\\+[0-9]{1,3})?[0-9]{9,15}$/;
  return (
    <>
      {contextHolder}
      <Form
        name="info-form"
        layout="vertical"
        form={InfoForm}
        onFinish={values => {
          UpdateProfile(values);
        }}
      >
        <Space direction="vertical" size="middle" style={{ display: "flex" }}>
          <Form.Item
            label="Username"
            name={"username"}
            initialValue={username}
            rules={[{ required: true, message: "Required field" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="Name"
            name={"name"}
            initialValue={name}
            rules={[{ required: true, message: "Required field" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="LastName"
            name={"lastname"}
            initialValue={lastname}
            rules={[{ required: true, message: "Required field" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            label="Phone Number"
            name={"phoneNumber"}
            initialValue={phoneNumber}
            rules={[
              { required: true, message: "Required field" },
              {
                pattern: phonePattern,
                message: "Invalid phone format",
              },
            ]}
          >
            <Input />
          </Form.Item>
          <Button htmlType="submit" type="primary" icon={<SaveIcon />}>
            Change Information
          </Button>
        </Space>
      </Form>
    </>
  );
};

export default InformationForm;
