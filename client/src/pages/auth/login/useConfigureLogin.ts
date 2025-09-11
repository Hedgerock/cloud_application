import {useLogin} from "../../../hooks/auth/useLogin.ts";
import {useCallback, useState} from "react";
import {useSelector} from "react-redux";
import type {RootState} from "../../../redux/store/store.ts";
import * as React from "react";

export const useConfigureLogin = () => {
    const { login, isLoading, isError } = useLogin();
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const prevPage = useSelector((state: RootState) => state.navigation.prevPath);

    const handleSubmit = useCallback(async(e: React.FormEvent) => {
        e.preventDefault();
        await login(email, password).then();
    }, [email, login, password])

    return { prevPage, handleSubmit, setEmail, setPassword, isLoading, isError };
}