import { notification as Notification } from "antd";

const UseNotification = () => {
  const [api, contextHolder] = Notification.useNotification();

  const openErrorNotification = (message: string) => {
    api.error({
      message,
      placement: "topRight",
    });
  };
  const openSuccessNotification = (message: string) => {
    api.success({
      message,
      placement: "topRight",
    });
  };
  return { contextHolder, openErrorNotification, openSuccessNotification };
};

export default UseNotification;
