import { Role } from "./roles";

export type User = {
  email: string;
  id: number;
  isVerifiedEmail: true;
  lastname: string;
  name: string;
  password: string;
  phoneNumber: string;
  roles: Role[];
  isMailToVerify: boolean;
  isPrivate: boolean;
  level: number;
  sessionTimer: number;
  username: string;
};
