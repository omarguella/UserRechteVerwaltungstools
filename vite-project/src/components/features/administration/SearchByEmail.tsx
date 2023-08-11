import { Col, Form } from "antd";
import { Search } from "../../forms/style.d";

interface Props {
  setEmail: (value: string) => void;
  email: string;
}
const SearchByEmail = ({ setEmail }: Props) => {
  const [EmailForm] = Form.useForm();
  const onChangeHandler = async (e: any) => {
    setEmail(e.target.value);
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
