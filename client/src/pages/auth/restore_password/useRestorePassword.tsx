import {type FormEvent, useCallback, useMemo, useState} from "react";
import {useGetSearchParams} from "../../../hooks/useGetSearchParams.ts";
import {TOKEN_SEARCH_PARAM_KEY, validatePasswords} from "../../../utils/constants";
import {useConfirmPasswordMutation} from "../../../redux/api/authApi.ts";
import {useNavigate} from "react-router-dom";
import {FormLabel} from "../../../components/auth/form_label/FormLabel.tsx";

export const useRestorePassword = () => {
    const [newPassword, setNewPassword] = useState<string>("");
    const [repeatNewPassword, setRepeatNewPassword] = useState<string>("");
    const token = useGetSearchParams(TOKEN_SEARCH_PARAM_KEY) || "";
    const [confirmPassword, { isLoading, isError, error }] = useConfirmPasswordMutation();
    const navigate = useNavigate();

    const comparePasswords = useMemo(() =>
        validatePasswords(newPassword, repeatNewPassword), [newPassword, repeatNewPassword]);

    const handleSubmit = useCallback(async(e: FormEvent) => {
        e.preventDefault();

        try {
            await confirmPassword({ token, password: newPassword  }).unwrap();
            navigate("/auth/login")
        } catch (e) {
            console.error("Bad credentials", e);
        }
    }, [confirmPassword, navigate, newPassword, token])

    const Fields = useMemo(() => {
        return (
            <>
                <FormLabel
                    inputType={"password"}
                    idTitle={"newPasswordValue"}
                    setValue={setNewPassword}
                    labelTitle={"New password"}
                />
                <FormLabel
                    inputType={"password"}
                    idTitle={"repeatNewPasswordValue"}
                    setValue={setRepeatNewPassword}
                    labelTitle={"Repeat new password"}
                />
            </>
        )
    }, [])
    
    return {isLoading, isError, error, handleSubmit, comparePasswords, Fields, token, navigate}
}