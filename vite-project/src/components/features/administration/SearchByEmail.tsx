import { Col, Form } from "antd";
import { Button, Search } from "../../forms/style.d";
import { User } from "../../../types/user";
import UseNotification from "../../../hooks/notification";
import { _GET } from "../../../api/config";

interface Props {
  setUsers: (value: User[]) => void;
}
const SearchByEmail = ({ setUsers }: Props) => {
  const [EmailForm] = Form.useForm();
  const onChangeHandler = async (e: any) => {
    try {
      const response = await _GET(`/users/email/${e.target.value}`);
      setUsers(response?.data);
    } catch (error: any) {
      UseNotification().openErrorNotification(error?.message);
    }
  };
  return (
    <Col xs={24} sm={24} md={12} lg={8} xl={8}>
      <Form name="email-form" form={EmailForm}>
        <Form.Item name="email">
          <Search
            placeholder="Search user by email"
            onChange={onChangeHandler}
          />
        </Form.Item>
      </Form>
    </Col>
  );
};

export default SearchByEmail;
