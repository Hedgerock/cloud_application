import type {IUser} from "./IUser.ts";

export interface ISuccessPageProps {
    currentData: IUser | undefined;
    data: IUser | undefined;
}