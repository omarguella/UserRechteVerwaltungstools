import { Col, Form } from "antd";
import { Search } from "../../forms/style.d";

interface Props {
  setUsername: (value: string) => void;
  username: string;
}
const SearchByUsername = ({ setUsername }: Props) => {
  const [UsernameForm] = Form.useForm();
  const onChangeHandler = async (e: any) => {
    setUsername(e.target.value);
  };
  return (
    <Col xs={24} sm={24} md={12} lg={8} xl={8}>
      <Form name="username-form" form={UsernameForm}>
        <Form.Item name="username">
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
