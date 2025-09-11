import * as React from "react";
import {memo, useState} from "react";
import {Link} from "react-router-dom";
import {FormLabel} from "../../../components/auth/form_label/FormLabel.tsx";
import '../Auth.css';
import {useRegister} from "../../../hooks/auth/useRegister.ts";
import {ErrorBlock} from "../../../components/error/ErrorBlock.tsx";
import {validatePasswords} from "../../../utils/constants";

export const RegistrationPage = memo(() => {
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [repeatPassword, setRepeatPassword] = useState<string>("");

    const {register, isError, isLoading, error} = useRegister();

    const isValidPasswords = validatePasswords(password, repeatPassword);

    const handleRegister = async(e: React.FormEvent) => {
        e.preventDefault();
        if (!isValidPasswords) return;
        await register({ email, password });
    }

    return (
        <form className="auth-form" onSubmit={ handleRegister }>
            <fieldset className="auth-form-fieldset">
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
            </fieldset>

            <div className="login-buttons">
                <Link
                    to={"/auth/login"}
                    className="login-buttons__button"
                >
                    Login page
                </Link>
                <button
                    type="submit"
                    disabled={!isValidPasswords || isLoading}
                    className="login-buttons__button"
                >Register</button>
            </div>

            { isError &&
                error.data.errors.map((el: string, index: number) => {
                    return (
                        <ErrorBlock message={el} key = { index } />
                    )
                })
            }
        </form>
    )
})