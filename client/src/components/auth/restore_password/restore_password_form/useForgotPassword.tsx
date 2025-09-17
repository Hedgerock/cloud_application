import React, {type FormEvent, useCallback, useMemo, useState} from "react";
import {useRestorePasswordMutation} from "../../../../redux/api/authApi.ts";
import {FormLabel} from "../../form_label/FormLabel.tsx";

export const useForgotPassword = (setIsSend: React.Dispatch<boolean>) => {
    const [email, setEmail] = useState<string>("");
    const [restorePassword, { isError, isLoading, error }] = useRestorePasswordMutation();

    const handleSubmit = useCallback(async(e: FormEvent) => {
        if (!email.length) return;
        e.preventDefault();

        try {
            await restorePassword({ email }).unwrap();
            setIsSend(true);
        } catch (e) {
            console.error("Failed to send email", e);
        }
    }, [email, restorePassword, setIsSend])

    const Fields = useMemo(() => {
        return (
            <>
                <FormLabel
                    inputType="email"
                    idTitle="forgotPasswordEmail"
                    setValue={setEmail}
                    labelTitle={"Email"}
                />
            </>
        )
    }, [])

    return { Fields, handleSubmit, isError, isLoading, error }
}