import { Form, Space } from "antd";
import Title from "antd/es/typography/Title";
import { SaveIcon } from "lucide-react";
import { Button, Password } from "../../forms/style.d";
import { useAppDispatch } from "../../../hooks/redux";
import UseNotification from "../../../hooks/notification";
import { UpdatePasswordAction } from "../../../redux/actions/user";
import { User } from "../../../types/user";

type PropsPassword = Partial<Pick<User, "id">>;

const PasswordForm = ({ id }: PropsPassword) => {
  const [PasswordInstance] = Form.useForm();
  const passwordPattern =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[.@$!%*?&])[A-Za-z\d.@$!%*?&]{8,}$/;

  const dispatch = useAppDispatch();
  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();

  const UpdatePassword = (payload: any) => {
    dispatch(UpdatePasswordAction({ id: id!, payload: payload }))
      .unwrap()
      .then(() => openSuccessNotification(`Password successfully updated`))
      .catch(err => openErrorNotification(err));
  };
  return (
    <>
      {contextHolder}
      <Form
        name="password-form"
        layout="vertical"
        form={PasswordInstance}
        onFinish={values => {
          UpdatePassword(values);
        }}
      >
        <Space direction="vertical" size="middle" style={{ display: "flex" }}>
          <Form.Item
            label="Old Password"
            name={"oldPassword"}
            rules={[
              {
                required: true,
                message: "Required Password",
              },
            ]}
          >
            <Password />
          </Form.Item>
          <Form.Item
            label="New Password"
            name={"newPassword"}
            rules={[
              {
                required: true,
                message: "Required Password",
              },
              {
                pattern: passwordPattern,
                message: "Required Password does not meet the requirements",
              },
            ]}
          >
            <Password />
          </Form.Item>
          <Button htmlType="submit" type="primary" icon={<SaveIcon />}>
            Change Password
          </Button>
        </Space>
      </Form>
    </>
  );
};

export default PasswordForm;
