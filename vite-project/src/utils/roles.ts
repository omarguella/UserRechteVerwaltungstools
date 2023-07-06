import { Role } from "../types/roles";
import { StandartSelector } from "../types/selectors";

/**
 *
 * @param {Role[]} roles
 * @returns
 */
export const ReformatRole = (roles: Role[]) => {
  const _formatted = roles?.map(role => {
    return {
      label: role.name,
      value: role.name,
    };
  });
  return _formatted as StandartSelector[];
};
