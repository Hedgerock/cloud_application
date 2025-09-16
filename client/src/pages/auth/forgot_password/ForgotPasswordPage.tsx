import {type FormEvent, useState} from "react";
import {FormLabel} from "../../../components/auth/form_label/FormLabel.tsx";
import {useRestorePasswordMutation} from "../../../redux/api/authApi.ts";
import {ErrorList} from "../../../components/error/ErrorList.tsx";
import type {ErrorResponse} from "../../../hooks/auth/useRegister.ts";
import '../Auth.css';

const ForgotPasswordPage = () => {
    const [email, setEmail] = useState<string>("");
    const [isSend, setIsSend] = useState<boolean>(false);
    const [restorePassword, { isError, isLoading, error }] = useRestorePasswordMutation();

    const handleSubmit = async(e: FormEvent) => {
        if (!email.length) return;
        e.preventDefault();

        try {
            await restorePassword({ email }).unwrap();
            setIsSend(true);
        } catch (e) {
            console.error("Failed to send email", e);
        }
    }

    return (
        <>
            {!isSend
                    ? (
                        <form onSubmit={handleSubmit} className="auth-form">
                            <h2>Getting back into your Cloud account</h2>
                            <p>Tell us some information about your account.</p>

                            <FormLabel
                                inputType="email"
                                idTitle="forgotPasswordEmail"
                                setValue={setEmail}
                                labelTitle={"Email"}
                            />

                            <button type={"submit"} disabled={isLoading}>Send email</button>

                            { isError && <ErrorList error={error as ErrorResponse} />}
                        </form>
                    )

                    : (
                        <p>
                            Your email has been successfully dispatched.
                            Please verify your inbox to proceed.
                            If you do not receive it within a few minutes, kindly check your spam or junk folder.
                        </p>
                    )
            }
        </>
    )
}

export default ForgotPasswordPage;