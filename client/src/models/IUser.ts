import type {IFiles} from "./IFiles.ts";

export interface IUser {
    id: number;
    email: string;
    diskSpace: number;
    usedSpace: number;
    files: IFiles[];
}