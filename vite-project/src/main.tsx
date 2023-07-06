import React from "react";
import ReactDOM from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import routes from "./components/Routes.tsx";
import "./index.css";
import { ConfigProvider } from "antd";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Provider } from "react-redux";
import { store } from "./redux/store.ts";
const queryClient = new QueryClient();
ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <React.StrictMode>
    <ConfigProvider
      theme={{
        token: {
          colorPrimary: "#071108",
          colorBorder: "#B5BEC6",
          colorBorderBg: "red",
          borderRadius: 5,
        },
      }}
    >
      <QueryClientProvider client={queryClient}>
        <Provider store={store}>
          <RouterProvider router={routes} />
        </Provider>
      </QueryClientProvider>
    </ConfigProvider>
  </React.StrictMode>
);
