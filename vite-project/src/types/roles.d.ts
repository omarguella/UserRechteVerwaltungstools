export type Role = {
  id: number;
  isMailToVerify: boolean;
  isPrivate: boolean;
  level: number;
  name: string;
  sessionTimer: number;
};

export type Builder = {
  value: string;
  path: any;
};
