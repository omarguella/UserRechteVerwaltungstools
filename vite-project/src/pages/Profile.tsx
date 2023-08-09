import { useQuery } from "@tanstack/react-query";
import { Alert, Card, Col, Form, Row, Space, Typography } from "antd";
import { _GET } from "../api/config";
import EmailForm from "../components/features/profile/EmailForm";
import InformationForm from "../components/features/profile/InformationForm";
import PasswordForm from "../components/features/profile/PasswordForm";
import VerifyButtons from "../components/features/profile/VerifyButtons";
import { User } from "../types/user";
import RoleList from "../components/features/profile/RoleList";
import PermissionsForm from "../components/features/profile/PermissionsForm";

const Profile = () => {
  const { data, isLoading, isError } = useQuery({
    queryKey: ["profile"],
    queryFn: async () => {
      const response = await _GET("/auth/me");
      const profile = response.data as User;
      return profile;
    },
  });

  if (isLoading) {
    return <div>loading ......</div>;
  }

  if (isError) {
    return (
      <Alert
        message="Error Ocurred"
        description="Something wrong try again"
        type="error"
      />
    );
  }

  return (
    <div style={{ width: "100%" }}>
      <Space direction="vertical" size="middle" style={{ display: "flex" }}>
        <Alert
          message="My Profile"
          type="success"
          style={{ fontWeight: "bold" }}
        />

        <VerifyButtons isVerifiedEmail={data.isVerifiedEmail} />
      </Space>

      <Row gutter={[16, 16]} style={{ marginTop: "20px" }}>
        {/* Form email */}
        <Col xs={24} sm={24} md={24} lg={12} xl={12}>
          <Card title="Update Email">
            <EmailForm email={data.email} />
          </Card>

          <Card
            style={{ marginTop: "20px" }}
            title="Update Profile Information"
          >
            <InformationForm
              username={data?.username}
              name={data?.name}
              lastname={data?.lastname}
              phoneNumber={data?.phoneNumber}
            />
          </Card>
        </Col>
        {/* Form password */}
        <Col xs={24} sm={24} md={24} lg={12} xl={12}>
          <Card title="Update Password">
            <PasswordForm />
          </Card>
          <Card title="My Roles" style={{ marginTop: "20px" }}>
            <RoleList roles={data?.roles} />
          </Card>
          <Card title="My Permissions" style={{ marginTop: "20px" }}>
            <PermissionsForm />
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Profile;
