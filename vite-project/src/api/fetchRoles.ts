import { useQuery } from "@tanstack/react-query";
import { _GET } from "./config";
import { Role } from "../types/roles";

export const FetchRole = () => {
  const { data, isLoading, error, refetch } = useQuery(
    ["publicRoles"],
    async () => {
      const response = await fetch(
        `${import.meta.env.VITE_URL}/roles/publicRoles`
      );
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    }
  );
  return { data, isLoading, error, refetch };
};

export const FetchPrivateRole = () => {
  const { data, isLoading, error, refetch } = useQuery(
    ["privateRoles"],
    async () => {
      const response = await _GET(`/roles/privatRoles/`);
      return response.data;
    }
  );
  return { data, isLoading, error, refetch };
};

export const FetchAvailableRole = () => {
  const { data, isLoading, error, refetch } = useQuery(
    ["availableRoles"],
    async () => {
      const response = await _GET(`/roles/edit/`);
      return response.data as Role[];
    }
  );
  return { data, isLoading, error, refetch };
};
