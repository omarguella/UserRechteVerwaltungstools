import React, { FC } from "react";
import { Drawer as DrawerComponent } from "antd";
interface DrawerProps {
  title: string;
  open: boolean;
  onClose: () => void;
  children: React.ReactNode;
  placement: "right" | "left";
}

const Drawer: FC<DrawerProps> = ({
  title,
  open,
  onClose,
  children,
  placement,
}) => {
  return (
    <DrawerComponent
      title={title}
      width={500}
      placement={placement}
      onClose={onClose}
      open={open}
    >
      {children}
    </DrawerComponent>
  );
};

export default Drawer;
