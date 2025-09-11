import * as React from "react";
import {type MouseEventHandler, useCallback, useMemo, useState} from "react";
import {capitalize} from "../../../utils/capitalize.ts";
import type {ILoginLabelProps, InputTypes} from "../../../models/ILogin.ts";

type useCredentials = Omit<ILoginLabelProps, "labelTitle">;

export const useGetLabelCredentials = ({ inputType, setValue, idTitle }: useCredentials) => {
    const [type, setType] = useState<InputTypes>(inputType);

    const handleChangeValue = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
        setValue(e.target.value);
    }, [setValue])

    const inputTitle = useMemo(() => {
        return capitalize(idTitle);
    }, [idTitle])

    const handleType = useCallback<MouseEventHandler<HTMLButtonElement>>((e) => {
        e.preventDefault();
        setType(prev => prev === "password" ? "text" : "password");
    }, [])

    const reset = useCallback(() => {
        setType("password");
    }, [])

    const textContent = useMemo(() => {
        return `${type === "password" ? "Show" : "Hide"} password`
    }, [type])

    return { textContent, type, inputTitle, handleType, reset, handleChangeValue };
}