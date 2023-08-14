import { TPrivateRole } from "../types/roles";
import { StandartSelector } from "../types/selectors";

export const RoleList: StandartSelector[] = [
  {
    label: "Available Roles",
    value: "",
  },
  {
    label: "Private Roles",
    value: "private",
  },
  {
    label: "Public Roles",
    value: "public",
  },
];

export const PrivateRole: TPrivateRole[] = [
  {
    label: "true",
    value: true,
  },
  {
    label: "false",
    value: false,
  },
];
