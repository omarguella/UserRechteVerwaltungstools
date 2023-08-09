import { Col, Form } from "antd";
import { Search } from "../../forms/style.d";
import { User } from "../../../types/user";
import UseNotification from "../../../hooks/notification";
import { _GET } from "../../../api/config";

interface Props {
  setUsers: (value: User[]) => void;
}
const SearchByUsername = ({ setUsers }: Props) => {
  const [UsernameForm] = Form.useForm();
  const onChangeHandler = async (e: any) => {
    try {
      const response = await _GET(`/users/username/${e.target.value}`);
      setUsers(response?.data);
    } catch (error: any) {
      UseNotification().openErrorNotification(error?.message);
    }
  };
  return (
    <Col xs={24} sm={24} md={12} lg={8} xl={8}>
      <Form name="username-form" form={UsernameForm}>
        <Form.Item name="email">
          <Search
            placeholder="Search user by username"
            onChange={onChangeHandler}
          />
        </Form.Item>
      </Form>
    </Col>
  );
};

export default SearchByUsername;
