import {ErrorBlock} from "./ErrorBlock.tsx";
import type {FC} from "react";
import type {ErrorResponse} from "../../hooks/auth/useRegister.ts";

export const ErrorList: FC<{ error: ErrorResponse }> = ({ error }) => {
    return (
        error.data?.errors.map((el: string, index: number) => {
            return (
                <ErrorBlock message={el} key = { index } />
            )
        })
    )
}