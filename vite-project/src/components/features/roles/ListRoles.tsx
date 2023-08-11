import { Col, Form, Row } from "antd";
import { RoleList } from "../../../constants/roles";
import { Select } from "../../forms/style.d";
import { ChangeEvent } from "react";

interface Props {
  selectedRole: string;
  setSelectedRole: (value: string) => void;
}

export const ListRoles = ({ setSelectedRole }: Props) => {
  const onChangeHandler = (e: any) => {
    setSelectedRole(e);
  };

  return (
    <div style={{ marginTop: "20px" }}>
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={24} md={8} lg={8} xl={8}>
          <Form layout="vertical">
            <Form.Item label="Roles list" name={"role"}>
              <Select options={RoleList} onChange={onChangeHandler} />
            </Form.Item>
          </Form>
        </Col>
      </Row>
    </div>
  );
};
