import * as React from "react";
import {useCallback, useMemo, useState} from "react";
import {validatePasswords} from "../../../utils/constants";
import {FormLabel} from "../../../components/auth/form_label/FormLabel.tsx";
import type {IRegisterCredentials} from "../../../redux/api/authApi.ts";

export const useGetRegistrationData = (register: (credentials: IRegisterCredentials) => Promise<void>) => {
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [repeatPassword, setRepeatPassword] = useState<string>("");

    const isValidPasswords = useMemo(() => validatePasswords(password, repeatPassword), [password, repeatPassword]);

    const handleRegister = useCallback(async(e: React.FormEvent) => {
        e.preventDefault();
        if (!isValidPasswords) return;
        await register({ email, password });
    }, [email, isValidPasswords, password, register])

    const Fields = useMemo(() => {
        return (
            <>
                <FormLabel
                    labelTitle={"Email"}
                    inputType={"email"}
                    idTitle={"registerEmail"}
                    setValue={setEmail}
                    />
            
                    <FormLabel
                    labelTitle={"Password"}
                    inputType={"password"}
                    idTitle={"registerPassword"}
                    setValue={setPassword}
                    />
            
                    <FormLabel
                    labelTitle={"Repeat password"}
                    inputType={"password"}
                    idTitle={"registerRepeatPassword"}
                    setValue={setRepeatPassword}
                    />
            </>
        )
    }, [] )
    
    return { Fields, handleRegister, isValidPasswords}
}