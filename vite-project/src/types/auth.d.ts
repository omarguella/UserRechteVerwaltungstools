export type TLogin = {
  emailOrUsername: string;
  password: string;
};

export type TRegistration = {
  email: string;
  username: string;
  password: string;
  name: string;
  lastname: string;
  phoneNumber: string;
  roles: string;
};

export type TTokens = {
  accessToken: string;
  refreshToken: string;
};
