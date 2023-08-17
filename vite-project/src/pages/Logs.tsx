import { useQuery } from "@tanstack/react-query";
import {
  Alert,
  Button,
  Card,
  DatePicker,
  DatePickerProps,
  Input,
  Popconfirm,
  Space,
  Table,
} from "antd";
import moment from "moment";
import { useMemo, useState } from "react";
import { Helmet } from "react-helmet";
import {_DELETE, _GET} from "../api/config";
import { DeleteLogAction } from "../redux/actions/permissions";
import { Trash } from "lucide-react";
import { useAppDispatch } from "../redux/store";
import UseNotification from "../hooks/notification";
import axios from "axios";
type Logs = {
  action: string;
  clientIpAddress: string;
  date: string;
  id: number;
  isVerifiedEmail: boolean;
  osName: string;
  osVersion: string;
  userAgent: string;
  userEmail: string;
  userId: string;
};

const Logs = () => {
  const [oFrom, setOFrom] = useState("");
  const [oTo, setOTo] = useState("");
  const [action, setAction] = useState("");
  const [userId, setUserId] = useState("");
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");
  const dispatch = useAppDispatch();
  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();
  const { data, isLoading, refetch } = useQuery(["logs"], async () => {
    const response = await _GET("/logs");
    return response.data as Logs[];
  });
  const columns = [
    {
      title: "User Id",
      dataIndex: "userId",
      key: "userId",
    },
    {
      title: "User Email",
      dataIndex: "userEmail",
      key: "userEmail",
    },
    {
      title: "User Agent",
      dataIndex: "userAgent",
      key: "userAgent",
    },
    {
      title: "Os Version",
      dataIndex: "osVersion",
      key: "osVersion",
    },
    {
      title: "Os Name",
      dataIndex: "osName",
      key: "osName",
    },
    {
      title: "Date",
      dataIndex: "date",
      key: "date",
    },
    {
      title: "Client Ip Address",
      dataIndex: "clientIpAddress",
      key: "clientIpAddress",
    },
    {
      title: "Action",
      dataIndex: "action",
      key: "action",
    },
    {
      title: "Delete",
      key: "Delete",
      render: (_: any, record: Logs) => {
        return (
          <Popconfirm
            title="Delete the permission"
            description="Do you want to delete this log? this action is irreversible"
            onConfirm={() =>
              dispatch(DeleteLogAction({ id: record.id }))
                .unwrap()
                .then(() => {
                  refetch();
                  openSuccessNotification("Log deleted successfully");
                })
                .catch(err => openErrorNotification(err))
            }
            okText="Yes"
            cancelText="No"
          >
            <Button
              type="primary"
              icon={<Trash />}
              style={{
                display: "flex",
                alignItems: "center",
                gap: "2",
                justifyContent: "center",
              }}
              danger
            />
          </Popconfirm>
        );
      },
    },
  ];

  const filteredData = useMemo(() => {
    if (!action && !from && !to && !userId) {
      return data;
    }

    return data?.filter(log => {
      const date = moment(log.date, "DD-MM-YYYY");

      const isMatchAction =
        !action || log.action.toLowerCase().includes(action.toLowerCase());

      const isMatchUserId =
        !userId || log.userId == userId;

      const isMatchFrom =
        !from ||
        date.isAfter(moment(from, "DD-MM-YYYY"), "day") ||
        date.isSame(moment(from, "DD-MM-YYYY"), "day");


      const isMatchTo =
        !to ||
        date.isBefore(moment(to, "DD-MM-YYYY"), "day") ||
        date.isSame(moment(to, "DD-MM-YYYY"), "day");

      return isMatchAction && isMatchFrom && isMatchTo && isMatchUserId;
    });
  }, [data, action, from, to,userId]);

  const datasource: Logs[] = filteredData ?? [];
  const onChangeFrom: DatePickerProps["onChange"] = (date, dateString) => {
    setFrom(moment(dateString).format("DDMMYYYY"));
    setOFrom(moment(dateString).format("DD-MM-YYYY"));

  };

  const onChangeTo: DatePickerProps["onChange"] = (date, dateString) => {
    setTo(moment(dateString).format("DDMMYYYY"));
    setOTo(moment(dateString).format("DD-MM-YYYY"));
  };
  const DeleteAll = async (
      from: string,
      to: string,
      action: string,
      userId: string
  ) => {
    try {
      await _DELETE(
          `/logs?from=${from}&to=${to}&action=${action}&userId=${userId}`
      );
      setFrom("");
      setTo("");
      setAction("");
      setUserId("");
      setOTo("");
      setOFrom("");
      refetch();
      openSuccessNotification("Logs Successfully Deleted");
    } catch (error) {
      openErrorNotification("Error logs problem");
    }
  };
  return (
    <div style={{ width: "100%", padding: "5px" }}>
      <Helmet>
        <title>Permissions administration</title>
      </Helmet>
      {contextHolder}
      <Space direction="vertical" size="middle" style={{ display: "flex" }}>
        <Alert message={`Logs`} type="success" style={{ fontWeight: "bold" }} />
      </Space>
      <div
        style={{
          marginTop: "20px",
          display: "flex",
          alignItems: "center",
          gap: "4px",
        }}
      >
        <Input
          style={{ width: "30%" }}
          placeholder="Search By User ID"
          onChange={e => setUserId(e.target.value)}
      />
        <Input
          style={{ width: "30%" }}
          placeholder="Search By Action"
          onChange={e => setAction(e.target.value)}
        />
        <DatePicker onChange={onChangeFrom} />
        <DatePicker onChange={onChangeTo} />
        {(oFrom != "" || oTo != "" || action != "" || userId != "") && (
            <Button danger onClick={() => DeleteAll(oFrom, oTo, action, userId)}>
              Delete All
            </Button>
        )}
      </div>
      <Card style={{ marginTop: "10px" }}>
        <Table dataSource={datasource} columns={columns} loading={isLoading} />
      </Card>
    </div>
  );
};

export default Logs;
