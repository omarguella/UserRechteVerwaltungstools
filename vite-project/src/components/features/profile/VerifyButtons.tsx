import { Divider, Form, Row, Space } from "antd";
import { FC, useState } from "react";
import { Button } from "../../forms/style.d";
import {
  ActivityIcon,
  CheckSquare,
  Code,
  SendIcon,
  Verified,
} from "lucide-react";
import Drawer from "../../common/Drawer";
import Input from "../../forms/Input";
import { _POST } from "../../../api/config";
import UseNotification from "../../../hooks/notification";

interface VerifyButtonsProps {
  isVerifiedEmail: boolean;
}

const VerifyButtons: FC<VerifyButtonsProps> = ({ isVerifiedEmail }) => {
  const [open, setOpen] = useState(false);
  const [form] = Form.useForm();
  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();
  const showDrawer = () => {
    setOpen(true);
  };

  const onClose = () => {
    setOpen(false);
  };

  const SendConfirmation = async () => {
    try {
      await _POST("/users/mail/sendPin/", {});
      return openSuccessNotification(
        `We have send a code of confirmation, check your email please`
      );
    } catch (error: any) {
      return openErrorNotification(error.status);
    }
  };
  /**
   * Verification code
   * @returns
   */
  const VerificationPin = async (values: any) => {
    try {
      await _POST("/users/mail/verify/", values);
      window.location.href = "/";
    } catch (error: any) {
      console.log(error);
      return openErrorNotification(error.message);
    }
  };
  return (
    <Row justify={"center"}>
      {contextHolder}
      {!isVerifiedEmail ? (
        <Button
          onClick={showDrawer}
          type="primary"
          icon={<ActivityIcon />}
          size="large"
          danger
        >
          Verify your email
        </Button>
      ) : (
        <Button type="primary" icon={<Verified />} success>
          Verified Email
        </Button>
      )}
      <Drawer
        open={open}
        onClose={onClose}
        title={"Verify you email"}
        placement="right"
      >
        <Space direction="vertical" size="middle" style={{ display: "flex" }}>
          <Button onClick={SendConfirmation} icon={<Code />}>
            Send Verification Code
          </Button>
          <Divider />
          <Form
            layout="vertical"
            name="pin"
            form={form}
            onFinish={values => VerificationPin(values)}
          >
            <Form.Item
              label="Enter Your Code Here"
              name={"pin"}
              rules={[
                { required: true, message: "Required field" },
                {
                  pattern: /^.{5,5}$/,
                  message: "Code composed with 5 characters",
                },
              ]}
            >
              <Input />
            </Form.Item>
            <Button
              htmlType="submit"
              type="primary"
              style={{ marginTop: "20px" }}
              icon={<SendIcon />}
            >
              Send
            </Button>
          </Form>
        </Space>
      </Drawer>
    </Row>
  );
};

export default VerifyButtons;
