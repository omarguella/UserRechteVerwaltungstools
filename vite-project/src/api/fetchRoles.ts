import { useQuery } from "@tanstack/react-query";

export const FetchRole = () => {
  const { data, isLoading, error } = useQuery(["publicRoles"], async () => {
    const response = await fetch("http://localhost:9000/roles/publicRoles");
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    return response.json();
  });
  return { data, isLoading, error };
};
