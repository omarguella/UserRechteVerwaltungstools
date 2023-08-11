import { Form, Space } from "antd";
import { FC } from "react";
import Input from "../../forms/Input";
import Title from "antd/es/typography/Title";
import { Button } from "../../forms/style.d";
import { SaveIcon } from "lucide-react";
import { User } from "../../../types/user";
import { useAppDispatch } from "../../../redux/store";
import { UpdateEmailAction } from "../../../redux/actions/user";
import UseNotification from "../../../hooks/notification";

type EmailFormProps = Partial<Pick<User, "id" | "email">>;

const EmailForm: FC<EmailFormProps> = ({ id, email }) => {
  const [EmailInstance] = Form.useForm();
  const dispatch = useAppDispatch();
  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();

  const UpdateEmail = (email: string) => {
    dispatch(UpdateEmailAction({ id: id!, email: email! }))
      .unwrap()
      .then(() => openSuccessNotification(`Email successfully updated`))
      .catch(err => openErrorNotification(err));
  };
  return (
    <>
      {contextHolder}
      <Form
        name="email-form"
        layout="vertical"
        form={EmailInstance}
        onFinish={values => {
          UpdateEmail(values);
        }}
      >
        <Space direction="vertical" size="middle" style={{ display: "flex" }}>
          <Form.Item
            label="Your Email"
            name={"email"}
            rules={[
              { required: true, message: "Required field" },
              {
                type: "email",
                message: "The input is not valid E-mail!",
              },
            ]}
            initialValue={email}
          >
            <Input />
          </Form.Item>
          <Button htmlType="submit" type="primary" icon={<SaveIcon />}>
            Change Email
          </Button>
        </Space>
      </Form>
    </>
  );
};

export default EmailForm;
