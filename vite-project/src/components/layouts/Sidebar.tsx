import type { MenuProps } from "antd";
import { Button, Menu } from "antd";
import {
  Check,
  DivideCircleIcon,
  Lock,
  LogOutIcon,
  PieChart,
  Settings2,
  SidebarClose,
  SidebarOpen,
  User2Icon,
} from "lucide-react";
import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { LogoutAction, LogoutAllAction } from "../../redux/actions/auth";
import { useAppDispatch } from "../../redux/store";
import UseNotification from "../../hooks/notification";

type MenuItem = Required<MenuProps>["items"][number];

function getItem(
  label: React.ReactNode,
  key: React.Key,
  icon?: React.ReactNode,
  children?: MenuItem[],
  type?: "group"
): MenuItem {
  return {
    key,
    icon,
    children,
    label,
    type,
  } as MenuItem;
}

const items: MenuItem[] = [
  getItem(
    "Profile",
    "1",
    <Link to={"/"}>
      <User2Icon />
    </Link>
  ),
  getItem(
    "Users Administration",
    "2",
    <Link to={"/administration"}>
      <Settings2 />
    </Link>
  ),
  getItem(
    "Roles Administration",
    "3",
    <Link to={"/roles"}>
      <Check />
    </Link>
  ),
  getItem(
    "Permissions Administration",
    "4",
    <Link to={"/permissions"}>
      <Lock />
    </Link>
  ),
  getItem(
    "Logs",
    "5",
    <Link to={"/logs"}>
      <PieChart />
    </Link>
  ),
];

const Sidebar: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false);
  const { contextHolder, openErrorNotification, openSuccessNotification } =
    UseNotification();
  const dispatch = useAppDispatch();
  const toggleCollapsed = () => {
    setCollapsed(!collapsed);
  };
  const { pathname } = useLocation();
  const activeMenu = {
    "/": ["1"],
    "/administration": ["2"],
    "/roles": ["3"],
    "/permissions": ["4"],
    "/logs": ["5"],
  };
  return (
    <div
      style={{
        height: "100vh",
      }}
    >
      {contextHolder}

      <Button
        type="default"
        onClick={toggleCollapsed}
        style={{ marginBottom: 16, marginLeft: "10px" }}
      >
        {collapsed ? <SidebarOpen /> : <SidebarClose />}
      </Button>
      <Menu
        defaultSelectedKeys={activeMenu[String(pathname)]}
        defaultOpenKeys={["sub1"]}
        mode="inline"
        theme="light"
        inlineCollapsed={collapsed}
        items={items}
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "start",
        }}
      />
      <Button
        type="default"
        style={{
          position: "absolute",
          bottom: 0,
          marginBottom: 70,
          marginLeft: "10px",
        }}
        title="Logout ALL"
        onClick={() =>
          dispatch(LogoutAllAction({ value: "LoggedOut" }))
            .unwrap()
            .then(() => openSuccessNotification("All devices disconnected now"))
              .then(()=> window.location.href="/login")
            .catch(err => openErrorNotification(err))
        }
      >
        <span style={{ display: "flex", alignItems: "center", gap: "5px" }}>
          <DivideCircleIcon />
        </span>
      </Button>
      <Button
        type="default"
        style={{
          position: "absolute",
          bottom: 0,
          marginBottom: 10,
          marginLeft: "10px",
        }}
        title="Logout"
        onClick={() =>
          dispatch(LogoutAction())
            .unwrap()
            .then(() => (window.location.href = "/login"))
        }
      >
        <span style={{ display: "flex", alignItems: "center", gap: "5px" }}>
          <LogOutIcon />
        </span>
      </Button>
    </div>
  );
};

export default Sidebar;
