import {type FormEvent, useState} from "react";
import {FormLabel} from "../../../components/auth/form_label/FormLabel.tsx";
import {TOKEN_SEARCH_PARAM_KEY, validatePasswords} from "../../../utils/constants";
import '../Auth.css';
import {useGetSearchParams} from "../../../hooks/useGetSearchParams.ts";
import {useConfirmPasswordMutation} from "../../../redux/api/authApi.ts";
import {ErrorList} from "../../../components/error/ErrorList.tsx";
import type {ErrorResponse} from "../../../hooks/auth/useRegister.ts";
import {useNavigate} from "react-router-dom";

const RestorePasswordPage = () => {
    const [newPassword, setNewPassword] = useState<string>("");
    const [repeatNewPassword, setRepeatNewPassword] = useState<string>("");
    const token = useGetSearchParams(TOKEN_SEARCH_PARAM_KEY);
    const [confirmPassword, { isLoading, isError, error }] = useConfirmPasswordMutation();
    const navigate = useNavigate();

    const comparePasswords = validatePasswords(newPassword, repeatNewPassword);

    if (!token) {
        navigate("/not-found-page");
        return;
    }

    const handleSubmit = async(e: FormEvent) => {
        e.preventDefault();

        try {
            await confirmPassword({ token, password: newPassword  }).unwrap();
            navigate("/auth/login")
        } catch (e) {
            console.error("Bad credentials", e);
        }
    }

    return (
        <>
            <form className="auth-form" onSubmit={handleSubmit}>
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

                <button type={"submit"} disabled={ !comparePasswords || isLoading }>Confirm new password</button>
            </form>

            { isError && <ErrorList error={error as ErrorResponse} /> }
        </>
    )
}

export default RestorePasswordPage;