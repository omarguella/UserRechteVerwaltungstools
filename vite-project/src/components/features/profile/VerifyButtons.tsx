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

interface VerifyButtonsProps {
  isVerifiedEmail: boolean;
}

const VerifyButtons: FC<VerifyButtonsProps> = ({ isVerifiedEmail }) => {
  const [open, setOpen] = useState(false);

  const showDrawer = () => {
    setOpen(true);
  };

  const onClose = () => {
    setOpen(false);
  };

  const SendConfirmation = () => {
    console.log("worl ....");
  };
  return (
    <Row justify={"center"}>
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
          <Form layout="vertical">
            <Form.Item label="Enter Your Code Here" name={"code"}>
              <Input />
              <Button
                htmlType="submit"
                type="primary"
                style={{ marginTop: "20px" }}
                icon={<SendIcon />}
              >
                Send
              </Button>
            </Form.Item>
          </Form>
        </Space>
      </Drawer>
    </Row>
  );
};

export default VerifyButtons;
