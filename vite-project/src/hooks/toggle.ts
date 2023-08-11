import React, { useState } from "react";

const UseToggle = () => {
  const [open, setOpen] = useState<boolean>(false);

  const showDrawer = () => {
    setOpen(true);
  };

  const onClose = () => {
    setOpen(false);
  };

  return [open, onClose, showDrawer];
};

export default UseToggle;
