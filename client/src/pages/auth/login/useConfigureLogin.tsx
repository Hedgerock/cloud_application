import {useLogin} from "../../../hooks/auth/useLogin.ts";
import {useCallback, useMemo, useState} from "react";
import {useSelector} from "react-redux";
import type {RootState} from "../../../redux/store/store.ts";
import * as React from "react";
import {FormLabel} from "../../../components/auth/form_label/FormLabel.tsx";

export const useConfigureLogin = () => {
    const { login, isLoading, isError, error } = useLogin();
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const prevPage = useSelector((state: RootState) => state.navigation.prevPath);

    const handleSubmit = useCallback(async(e: React.FormEvent) => {
        e.preventDefault();
        await login(email, password).then();
    }, [email, login, password])

    const Fields = useMemo(() => {
        return (
            <>
                <FormLabel
                    labelTitle={"Login"}
                    inputType={"email"}
                    idTitle={"authEmail"}
                    setValue={setEmail}
                />

                <FormLabel
                    labelTitle={"Password"}
                    inputType={"password"}
                    idTitle={"authPassword"}
                    setValue={setPassword}
                />
            </>
        )
    }, [setEmail, setPassword])

    return { prevPage, handleSubmit, setEmail, setPassword, isLoading, isError, Fields, error };
}