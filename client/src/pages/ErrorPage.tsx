import type {FC} from "react";

export const ErrorPage: FC<{ message: string }> = ({ message }) => {

    return <h1>{ message }</h1>
}