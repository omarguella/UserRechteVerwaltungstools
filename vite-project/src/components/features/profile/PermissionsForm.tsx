import { useQuery } from "@tanstack/react-query";
import { Table } from "antd";
import { _GET } from "../../../api/config";
import Loading from "../../common/Loading";
import { CreateCookies } from "../../../lib/cookies";

const PermissionsForm = () => {
  const { data, isLoading } = useQuery(["permissions"], async () => {
    const response = await _GET("/auto/allPermissions/text");
    CreateCookies("current-role", response.data);
    return response.data;
  });

  const dataSource = data?.map((permission: string) => {
    return {
      permission,
    };
  });
  const columns = [
    {
      title: "Name",
      dataIndex: "permission",
      key: "permission",
    },
  ];

  if (isLoading) {
    return <Loading />;
  }

  return <Table dataSource={dataSource} columns={columns} />;
};

export default PermissionsForm;
