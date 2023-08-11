import Cookies from "js-cookie";

/**
 * Create Cookies
 * @param name
 * @param data
 */
export const CreateCookies = (name: string, data: any) => {
  Cookies.set(name, JSON.stringify(data));
};

/**
 * Get Cookies
 * @param name
 */
export const FindCookies = (name: string) => {
  const result = Cookies.get(name);
  return JSON.parse(result!) ?? "";
};

/**
 * Remove Cookies
 * @param name
 */
export const DeleteCookies = (name: string) => {
  Cookies.remove(name);
};
